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
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.EntityMode;
import org.hibernate.Session;
import org.hibernate.SessionException;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.IncompleteConfigurationException;
import de.decidr.model.exceptions.StorageException;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.storage.StorageProvider;
import de.decidr.model.transactions.HibernateTransactionCoordinator;

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
 * This storage provider uses the {@link HibernateTransactionCoordinator} to
 * retrieve the current Hibernate session.<br>
 * This means that you must invoke the storage methods from within a
 * {@link TransactionalCommand}, otherwise the methods will fail.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class HibernateEntityStorageProvider implements StorageProvider {

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

    /**
     * @return the current opened HibernateSession .
     * @throws SessionException
     *             if the current session is not open or if there is no current
     *             session.
     */
    private Session getCurrentSession() throws SessionException {
        Session result = HibernateTransactionCoordinator.getInstance()
                .getCurrentSession();
        if (result == null) {
            throw new SessionException("There is no current session.");
        }
        if (!result.isOpen()) {
            throw new SessionException("The current session is not open.");
        }

        return result;
    }

    /**
     * Throws an {@link IllegalArgumentException} if the given file ID is
     * <code>null</code>.
     * 
     * @param fileId
     *            file id to check
     */
    private void checkFileId(Long fileId) {
        if (fileId == null) {
            throw new IllegalArgumentException("Invalid file ID.");
        }
    }

    /**
     * Throws an {@link IllegalArgumentException} if the given file size is
     * <code>null</code> or less than zero.
     * 
     * @param fileSize
     *            file size to check
     */
    private void checkFileSize(Long fileSize) {
        if (fileSize == null || fileSize < 0) {
            throw new IllegalArgumentException("Invalid file size.");
        }
    }

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

        if (entityTypeName == null || entityTypeName.isEmpty()) {
            throw new IncompleteConfigurationException(
                    CONFIG_KEY_ENTITY_TYPE_NAME
                            + " is a required configuration option.");
        }

        if (idPropertyName == null || "".equals(idPropertyName)) {
            throw new IncompleteConfigurationException(
                    CONFIG_KEY_ID_PROPERTY_NAME
                            + " is a required configuration option.");
        }

        if (dataPropertyName == null || "".equals(dataPropertyName)) {
            throw new IncompleteConfigurationException(
                    CONFIG_KEY_DATA_PROPERTY_NAME
                            + " is a required configuration option.");
        }
    }

    /**
     * Internal class that represents the <code>null</code> value.
     */
    private class NullObject {
        // No content other than class definition intended
    }

    @Override
    public InputStream getFile(Long fileId) throws StorageException {
        logger.debug("Retrieving file with ID "
                + (fileId == null ? "null" : fileId.toString()));
        checkFileId(fileId);
        Session session = getCurrentSession();

        String hql = "select f." + dataPropertyName + " from " + entityTypeName
                + " f where f." + idPropertyName + "=" + fileId.toString();
        logger.debug("HQL query: " + hql);

        List<?> data = session.createQuery(hql).setMaxResults(1).list();
        if (data.isEmpty()) {
            throw new StorageException("File not found");
        }

        // we can only deal with BLOBS that are mapped to byte arrays and
        // strings.
        Object dataObject = data.get(0);
        InputStream stream;
        if (dataObject instanceof String) {
            stream = new ByteArrayInputStream(dataObject.toString().getBytes());
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
                            dataPropertyName, className);
            throw new StorageException(message);
        }

        return stream;
    }

    @Override
    public boolean isApplicable(Properties config) {
        return config != null
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
        checkFileId(fileId);
        checkFileSize(fileSize);
        if (data == null) {
            throw new IllegalArgumentException("Data must not be null.");
        }

        try {
            // reading the entire file into a byte array may not be the most
            // elegant solution, but for small files
            // this should work well.
            if (fileSize > Integer.MAX_VALUE) {
                throw new StorageException("File is too large.");
            }

            byte[] bytes = new byte[fileSize.intValue()];
            data.read(bytes, 0, fileSize.intValue());

            Session session = getCurrentSession();

            if (createEntity) {
                // Does a file entity already exist?
                Number existing = (Number) session.createQuery(
                        "select count(*) from " + entityTypeName
                                + " f where f." + idPropertyName + " = "
                                + fileId.toString()).uniqueResult();
                if (existing.intValue() < 1) {
                    Object newEntity = session.getSessionFactory()
                            .getClassMetadata(entityTypeName).instantiate(
                                    fileId, EntityMode.POJO);
                    logger.debug("Creating new file entity of type "
                            + entityTypeName);
                    initNewFileEntity(newEntity);
                    session.save(newEntity);
                }
            }

            // at this point we may assume that the entity identified by fileId
            // exists.
            String hql = "update " + entityTypeName + " f set f."
                    + dataPropertyName + " = :bytes where f." + idPropertyName
                    + " = " + fileId.toString();
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
                                fileId, entityTypeName);
                throw new StorageException(message);
            }
        } catch (Exception e) {
            if (e instanceof StorageException) {
                throw (StorageException) e;
            } else {
                throw new StorageException(e);
            }
        }
    }

    @Override
    public void removeFile(Long fileId) throws StorageException {
        logger.debug("Removing file with ID "
                + (fileId == null ? "null" : fileId.toString()));
        checkFileId(fileId);

        String hql;
        if (deleteEntity) {
            hql = "delete from " + entityTypeName + " f where f."
                    + idPropertyName + "=" + fileId.toString();
        } else {
            hql = "update " + entityTypeName + " f set f." + dataPropertyName
                    + "=null where f." + idPropertyName + "="
                    + fileId.toString();
        }

        logger.debug("HQL query: " + hql);

        getCurrentSession().createQuery(hql).executeUpdate();
    }

    /**
     * Initializes the given file entity with default values for all properties
     * that are not nullable.
     * 
     * @param entity
     *            entity to initialize
     * @throws ClassNotFoundException
     *             if the entity initializer class cannot be found.
     * @throws IllegalAccessException
     *             if the entity initalizer cannot be instantiated because its
     *             parameterless constructor is not visible.
     * @throws InstantiationException
     *             if the entity initializer cannot be instantiated.
     * @throws ClassCastException
     *             if the provided file entity initializer class does not
     *             actually implement {@link FileEntityInitializer}
     */
    private void initNewFileEntity(Object entity)
            throws ClassNotFoundException, StorageException,
            InstantiationException, IllegalAccessException {
        logger.debug("Initializing new file entity.");
        Class<?> clazz = Class.forName(entityInitializerClass);
        if (!FileEntityInitializer.class.isAssignableFrom(clazz)) {
            throw new ClassCastException("Given file entity initializer '"
                    + entityInitializerClass
                    + "' does not implement FileEntityInitializer interface.");
        }

        FileEntityInitializer instance = (FileEntityInitializer) clazz
                .newInstance();
        instance.initEntity(entity);
    }
}
