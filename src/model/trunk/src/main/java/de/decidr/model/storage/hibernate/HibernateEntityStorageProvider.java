/*
 * The DecidR Development Team licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.decidr.model.storage.hibernate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.EntityMode;
import org.hibernate.Session;

import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.exceptions.IncompleteConfigurationException;
import de.decidr.model.exceptions.StorageException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.storage.StorageProvider;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * {@link StorageProvider} that uses a Hibernate entity to store files in a
 * database.
 * <p>
 * You can specify the name of the entity and the name of the property that
 * holds the file data via the configuration options "entityTypeName",
 * "idPropertyName" and "dataPropertyName".<br>
 * All of these properties must be present in your configuration, otherwise this
 * storage provider is not applicable.<br>
 * You may omit the default storage provider properties, but if you set them,
 * this storage provider will only accept the following properties:
 * <ul>
 * <li>amazons3=false</li>
 * <li>local=false</li>
 * <li>persistent=true</li>
 * </ul>
 * <ul>
 * <p>
 * If this storage provider is invoked from within a transaction, changes made
 * to the storage backend can be rolled back. Each storage operation implemented
 * as a (nested / nestable) transaction.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class HibernateEntityStorageProvider implements StorageProvider {

    /**
     * Retrieves file contents within a (nested) transaction.
     */
    static class GetFileCommand extends HibernateStorageProviderCommand {

        public InputStream stream = null;

        /**
         * Creates a new GetFileCommand
         * 
         * @param owner
         *            storage provider that holds config values.
         * @param fileId
         *            ID of file that is read
         */
        public GetFileCommand(HibernateEntityStorageProvider owner, long fileId) {
            super(owner, fileId);
        }

        @Override
        public void transactionStarted(TransactionStartedEvent evt)
                throws TransactionException {
            stream = null;

            Session session = evt.getSession();

            String hql = "select f." + owner.dataPropertyName + " from "
                    + owner.entityTypeName + " f where f."
                    + owner.idPropertyName + "=" + fileId;
            logger.debug("HQL query: " + hql);

            List<?> data = session.createQuery(hql).setMaxResults(1).list();
            if (data.isEmpty()) {
                throw new TransactionException("File not found");
            }

            // we can only deal with BLOBS that are mapped to byte arrays and
            // strings.
            Object dataObject = data.get(0);
            if (dataObject instanceof String) {
                stream = new ByteArrayInputStream(dataObject.toString()
                        .getBytes());
            } else if (dataObject instanceof byte[]) {
                stream = new ByteArrayInputStream((byte[]) dataObject);
            } else {
                // we cannot deal with this kind of content
                if (dataObject == null) {
                    dataObject = new NullObject();
                }
                String className;
                Class<? extends Object> clazz = dataObject.getClass();
                if (clazz == null) {
                    className = "!unknown class!";
                } else {
                    className = clazz.getName();
                }

                String message = String
                        .format(
                                "The property %1$s has type %2$s, which cannot be mapped to a byte array.",
                                owner.dataPropertyName, className);
                throw new TransactionException(message);
            }
        }
    }

    /**
     * Abstract base class for commands that require the
     * {@link HibernateEntityStorageProvider} configuration.
     */
    static abstract class HibernateStorageProviderCommand extends
            AbstractTransactionalCommand {

        /**
         * Storage provider that holds the config values.
         */
        protected HibernateEntityStorageProvider owner = null;

        /**
         * ID of file that is read / modified
         */
        protected long fileId;

        /**
         * Creates a new HibernateStorageProviderCommand
         * 
         * @param owner
         *            storage provider that holds config values.
         * @param fileId
         *            ID of file that is read / modified
         */
        public HibernateStorageProviderCommand(
                HibernateEntityStorageProvider owner, long fileId) {
            if (owner == null) {
                throw new IllegalArgumentException("Owner must not be null.");
            }
            this.owner = owner;
            this.fileId = fileId;
        }
    }

    /**
     * Internal class that represents the <code>null</code> value.
     */
    static class NullObject {
        // No content other than class definition intended
    }

    /**
     * Creates or replaces file contents within a (nested) transaction.
     */
    static class PutFileCommand extends HibernateStorageProviderCommand {

        private long fileSize;
        private InputStream data;

        /**
         * Creates a new PutFileCommand
         * 
         * @param owner
         *            storage provider that holds config values.
         * @param fileId
         *            ID of file that is created / modified
         * @param fileSize
         *            number of bytes to read from data
         * @param data
         *            file contents to put
         * @throws IllegalArgumentException
         *             if data is <code>null</code>
         * @throws StorageException
         *             if the file is larger than Integer.MAX_VALUE
         */
        public PutFileCommand(HibernateEntityStorageProvider owner,
                long fileId, long fileSize, InputStream data)
                throws StorageException {
            super(owner, fileId);
            if (fileSize > Integer.MAX_VALUE) {
                throw new StorageException("File is too large.");
            }
            if (data == null) {
                throw new IllegalArgumentException("Data must not be null.");
            }
            this.fileSize = fileSize;
            this.data = data;
        }

        /**
         * Initializes the given file entity with default values for all
         * properties that are not nullable.
         * 
         * @param entity
         *            entity to initialize
         * @throws TransactionException
         *             <ul>
         *             <li>if the entity initializer class cannot be found.</li>
         *             <li>if the entity initalizer cannot be instantiated
         *             because its parameterless constructor is not visible.</li>
         *             <li>if the entity initializer cannot be instantiated.</li>
         *             <li>if the provided file entity initializer class does
         *             not actually implement {@link FileEntityInitializer}</li>
         *             </ul>
         */
        private void initNewFileEntity(Object entity)
                throws TransactionException {
            logger.debug("Initializing new file entity.");
            try {
                Class<?> clazz = Class.forName(owner.entityInitializerClass);
                if (!FileEntityInitializer.class.isAssignableFrom(clazz)) {
                    throw new ClassCastException(
                            "Given file entity initializer '"
                                    + owner.entityInitializerClass
                                    + "' does not implement FileEntityInitializer interface.");
                }

                FileEntityInitializer instance = (FileEntityInitializer) clazz
                        .newInstance();
                instance.initEntity(entity);
            } catch (ClassCastException e) {
                throw new TransactionException(e);
            } catch (InstantiationException e) {
                throw new TransactionException(e);
            } catch (IllegalAccessException e) {
                throw new TransactionException(e);
            } catch (ClassNotFoundException e) {
                throw new TransactionException(e);
            }
        }

        @Override
        public void transactionStarted(TransactionStartedEvent evt)
                throws TransactionException {
            // reading the entire file into a byte array may not be the most
            // elegant solution, but for small files
            // this should work well.
            byte[] bytes = new byte[(int) fileSize];
            try {
                data.read(bytes, 0, (int) fileSize);
            } catch (IOException e) {
                throw new TransactionException(e);
            }

            Session session = evt.getSession();

            if (owner.createEntity) {
                // Does a file entity already exist?
                Number existing = (Number) session.createQuery(
                        "select count(*) from " + owner.entityTypeName
                                + " f where f." + owner.idPropertyName + " = "
                                + fileId).uniqueResult();
                if (existing.intValue() < 1) {
                    Object newEntity = session.getSessionFactory()
                            .getClassMetadata(owner.entityTypeName)
                            .instantiate(fileId, EntityMode.POJO);
                    logger.debug("Creating new file entity of type "
                            + owner.entityTypeName);
                    initNewFileEntity(newEntity);
                    session.save(newEntity);
                }
            }

            // at this point we may assume that the entity identified by fileId
            // exists.
            String hql = "update " + owner.entityTypeName + " f set f."
                    + owner.dataPropertyName + " = :bytes where f."
                    + owner.idPropertyName + " = " + fileId;
            logger.debug("HQL query: " + hql);
            int updatedRows = session.createQuery(hql)
                    .setBinary("bytes", bytes).executeUpdate();

            if (updatedRows == 0) {
                // The user has set createEntity to false, but forgot to create
                // an entity himself.
                String message = String
                        .format(
                                "Cannot put file since there is "
                                        + "no existing entity for ID %1$s.\n"
                                        + "Please create a corresponding %2$s entity first.",
                                fileId, owner.entityTypeName);
                throw new TransactionException(message);
            }
        }
    }

    /**
     * Removes file contents within a (nested) transaction.
     */
    static class RemoveFileCommand extends HibernateStorageProviderCommand {

        /**
         * Creates a new RemoveFileCommand
         * 
         * @param owner
         *            storage provider that holds config values.
         * @param fileId
         *            ID of file that is read
         */
        public RemoveFileCommand(HibernateEntityStorageProvider owner,
                long fileId) {
            super(owner, fileId);
        }

        @Override
        public void transactionStarted(TransactionStartedEvent evt)
                throws TransactionException {
            String hql;
            if (owner.deleteEntity) {
                hql = "delete from " + owner.entityTypeName + " f where f."
                        + owner.idPropertyName + "=" + fileId;
            } else {
                hql = "update " + owner.entityTypeName + " f set f."
                        + owner.dataPropertyName + " = null where f."
                        + owner.idPropertyName + "=" + fileId;
            }

            logger.debug("HQL query: " + hql);
            evt.getSession().createQuery(hql).executeUpdate();
        }
    }

    /**
     * The config property that holds the name of the Hibernate entity which is
     * used to store file data.
     */
    public static final String CONFIG_KEY_ENTITY_TYPE_NAME = "entityTypeName";

    /**
     * The config property that holds the name of the property within the
     * Hibernate entity which holds the file data.
     */
    public static final String CONFIG_KEY_DATA_PROPERTY_NAME = "dataPropertyName";

    /**
     * The config property that holds the name of the ID property within the
     * Hibernate entity.
     */
    public static final String CONFIG_KEY_ID_PROPERTY_NAME = "idPropertyName";

    /**
     * The config property that specifies whether the entire hibernate entity
     * should be deleted when calling the deleteFile() method. Defaults to
     * false.
     */
    public static final String CONFIG_KEY_DELETE_ENTITY = "deleteEntity";
    /**
     * The config property that specifies whether the storage provider should
     * attempt to create a new entity when "putting" a file. Defaults to false.
     */
    public static final String CONFIG_KEY_CREATE_ENTITY = "createEntity";
    /**
     * Fully qualified name of the class that is used to create new file
     * entities. This setting is optional, if not set the default is used (see
     * below).
     */
    public static final String CONFIG_KEY_ENTITY_INITIALIZER = "entityInitializer";
    /**
     * Default file entity initializer.
     */
    public static final String DEFAULT_ENTITY_INITIALIZER = "de.decidr.model.storage.hibernate.DefaultFileEntityInitializer";

    private static Logger logger = DefaultLogger
            .getLogger(HibernateEntityStorageProvider.class);
    private String entityTypeName = null;

    private String dataPropertyName = null;

    private String idPropertyName = null;

    private String entityInitializerClass = null;

    /**
     * Whether removing a file should also remove the entity. If set to false,
     * the data property is set to null when deleting a file.
     */
    private Boolean deleteEntity = false;

    /**
     * Whether this storage provider should attempt to create a new entity using
     * the default parameterless constructor when "putting" files.
     */
    private Boolean createEntity = false;

    @Override
    public void applyConfig(Properties config)
            throws IncompleteConfigurationException {
        if (config == null) {
            throw new IncompleteConfigurationException(
                    "Configuration must not be null.");
        }

        entityTypeName = config.getProperty(CONFIG_KEY_ENTITY_TYPE_NAME, null);
        dataPropertyName = config.getProperty(CONFIG_KEY_DATA_PROPERTY_NAME,
                null);
        idPropertyName = config.getProperty(CONFIG_KEY_ID_PROPERTY_NAME, null);
        deleteEntity = Boolean.valueOf(config.getProperty(
                CONFIG_KEY_DELETE_ENTITY, "false"));
        createEntity = Boolean.valueOf(config.getProperty(
                CONFIG_KEY_CREATE_ENTITY, "false"));
        entityInitializerClass = config.getProperty(
                CONFIG_KEY_ENTITY_INITIALIZER, DEFAULT_ENTITY_INITIALIZER);

        if ((entityTypeName == null) || entityTypeName.isEmpty()) {
            throw new IncompleteConfigurationException(
                    CONFIG_KEY_ENTITY_TYPE_NAME
                            + " is a required configuration option.");
        }

        if ((idPropertyName == null) || "".equals(idPropertyName)) {
            throw new IncompleteConfigurationException(
                    CONFIG_KEY_ID_PROPERTY_NAME
                            + " is a required configuration option.");
        }

        if ((dataPropertyName == null) || "".equals(dataPropertyName)) {
            throw new IncompleteConfigurationException(
                    CONFIG_KEY_DATA_PROPERTY_NAME
                            + " is a required configuration option.");
        }
    }

    /**
     * Throws an {@link IllegalArgumentException} if the given file ID is
     * <code>null</code>.
     * 
     * @param fileId
     *            file id to check
     * @return the given file ID
     */
    private long checkFileId(Long fileId) {
        if (fileId == null) {
            throw new IllegalArgumentException("Invalid file ID.");
        }
        return fileId;
    }

    /**
     * Throws an {@link IllegalArgumentException} if the given file size is
     * <code>null</code> or less than zero.
     * 
     * @param fileSize
     *            file size to check
     * @return checked file size
     */
    private long checkFileSize(Long fileSize) {
        if ((fileSize == null) || (fileSize < 0)) {
            throw new IllegalArgumentException("Invalid file size.");
        }
        return fileSize;
    }

    /**
     * @return the createEntity
     */
    public Boolean getCreateEntity() {
        return createEntity;
    }

    /**
     * @return the dataPropertyName
     */
    public String getDataPropertyName() {
        return dataPropertyName;
    }

    /**
     * @return the deleteEntity
     */
    public Boolean getDeleteEntity() {
        return deleteEntity;
    }

    /**
     * @return the entityInitializerClass
     */
    public String getEntityInitializerClass() {
        return entityInitializerClass;
    }

    /**
     * @return the entityTypeName
     */
    public String getEntityTypeName() {
        return entityTypeName;
    }

    @Override
    public InputStream getFile(Long fileId) throws StorageException {
        logger.debug("Getting contents of file with ID "
                + (fileId == null ? "null" : fileId.toString()));
        GetFileCommand cmd = new GetFileCommand(this, checkFileId(fileId));
        try {
            HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        } catch (TransactionException e) {
            throw new StorageException(e);
        }
        return cmd.stream;
    }

    /**
     * @return the idPropertyName
     */
    public String getIdPropertyName() {
        return idPropertyName;
    }

    @Override
    public boolean isApplicable(Properties config) {
        return (config != null)
                && config.containsKey(CONFIG_KEY_DATA_PROPERTY_NAME)
                && config.containsKey(CONFIG_KEY_ENTITY_TYPE_NAME)
                && config.containsKey(CONFIG_KEY_ID_PROPERTY_NAME)
                // Amazons3 property can be missing or explicitly false
                && !Boolean.parseBoolean(config.getProperty(
                        AMAZON_S3_CONFIG_KEY, Boolean.toString(false)))
                // Persistent property can be missing or explicitly true
                && Boolean.parseBoolean(config.getProperty(
                        PERSISTENT_CONFIG_KEY, Boolean.toString(true)))
                // Local property can be missing or explicitly false
                && !Boolean.parseBoolean(config.getProperty(LOCAL_CONFIG_KEY,
                        Boolean.toString(false)));
    }

    /**
     * Creates or replaces the file that is identified by the given id on the
     * storage backend. Please note that if the "createEntity" property is set
     * to false, the corresponding Hibernate entity must exist before invoking
     * this method.
     * 
     * @param data
     *            the contents of the file
     * @param fileId
     *            the file identifier
     * @param fileSize
     *            the size of the file
     * @throws StorageException
     *             if a problem occurs while storing
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>
     */
    @Override
    public void putFile(InputStream data, Long fileId, Long fileSize)
            throws StorageException {
        logger.debug("Putting file with ID "
                + (fileId == null ? "null" : fileId.toString()));
        PutFileCommand cmd = new PutFileCommand(this, checkFileId(fileId),
                checkFileSize(fileSize), data);
        try {
            HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        } catch (TransactionException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void removeFile(Long fileId) throws StorageException {
        logger.debug("Removing file with ID "
                + (fileId == null ? "null" : fileId.toString()));

        RemoveFileCommand cmd = new RemoveFileCommand(this, checkFileId(fileId));
        try {
            HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        } catch (TransactionException e) {
            throw new StorageException(e);
        }
    }

    /**
     * @param createEntity
     *            the createEntity to set
     */
    public void setCreateEntity(Boolean createEntity) {
        this.createEntity = createEntity;
    }

    /**
     * @param dataPropertyName
     *            the dataPropertyName to set
     */
    public void setDataPropertyName(String dataPropertyName) {
        this.dataPropertyName = dataPropertyName;
    }

    /**
     * @param deleteEntity
     *            the deleteEntity to set
     */
    public void setDeleteEntity(Boolean deleteEntity) {
        this.deleteEntity = deleteEntity;
    }

    /**
     * @param entityInitializerClass
     *            the entityInitializerClass to set
     */
    public void setEntityInitializerClass(String entityInitializerClass) {
        this.entityInitializerClass = entityInitializerClass;
    }

    /**
     * @param entityTypeName
     *            the entityTypeName to set
     */
    public void setEntityTypeName(String entityTypeName) {
        this.entityTypeName = entityTypeName;
    }

    /**
     * @param idPropertyName
     *            the idPropertyName to set
     */
    public void setIdPropertyName(String idPropertyName) {
        this.idPropertyName = idPropertyName;
    }
}
