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

package de.decidr.model.storage;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionException;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.IncompleteConfigurationException;
import de.decidr.model.exceptions.StorageException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;

/**
 * {@link StorageProvider} that uses a Hibernate entity to store files in a
 * database.
 * <p>
 * You can specify the name of the entity and the name of the property that
 * holds the file data via the configuration options "entityTypeName",
 * "idPropertyName" and "dataPropertyName".<br>
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
     * should be deleted when calling the deleteFile() method.
     */
    public static final String CONFIG_KEY_DELETE_ENTITY = "deleteEntity";

    private String entityTypeName = null;
    private String dataPropertyName = null;
    private String idPropertyName = null;
    /**
     * Whether removing a file should also remove the entity. If set to false,
     * the data property is set to null when deleting a file.
     */
    private Boolean deleteEntity = false;

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
     * Throws an {@link IllegalArgumentException} if the given file ID is null.
     * 
     * @param fileId
     *            file id to check
     */
    private void checkFileId(Long fileId) {
        if (fileId == null) {
            throw new IllegalArgumentException("Invalid file ID.");
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

        if (entityTypeName == null || "".equals(entityTypeName)) {
            throw new IncompleteConfigurationException(
                    CONFIG_KEY_ENTITY_TYPE_NAME
                            + " is a required configuration option.");
        }

        if (idPropertyName == null || "".equals(idPropertyName)) {
            throw new IncompleteConfigurationException(
                    CONFIG_KEY_ID_PROPERTY_NAME
                            + " is a required configuration option.");
        }

        if (dataPropertyName == null | "".equals(dataPropertyName)) {
            throw new IncompleteConfigurationException(
                    CONFIG_KEY_DATA_PROPERTY_NAME
                            + " is a required configuration option.");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public InputStream getFile(Long fileId) throws StorageException {
        checkFileId(fileId);
        Session session = getCurrentSession();

        String hql = "select f." + dataPropertyName + " from " + entityTypeName
                + " f where f." + idPropertyName + "=" + fileId.toString();

        List data = session.createQuery(hql).setMaxResults(1).list();
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
            String message = String
                    .format(
                            "The property %1$s has type %2$s, which cannot be mapped to a byte array.",
                            dataPropertyName, dataObject.getClass().getName());
            throw new StorageException(message);
        }

        return stream;
    }

    @Override
    // DH please complete according to (updated) documentation ~rr
    public boolean isApplicable(Properties config) {
        return config != null
                && config.containsKey(CONFIG_KEY_DATA_PROPERTY_NAME)
                && config.containsKey(CONFIG_KEY_ENTITY_TYPE_NAME)
                && config.containsKey(CONFIG_KEY_ID_PROPERTY_NAME);
    }

    @Override
    public void putFile(FileInputStream data, Long fileId)
            throws StorageException {
        checkFileId(fileId);
        if (data == null) {
            throw new IllegalArgumentException("Data must not be null.");
        }

        try {
            // reading the entire file into a byte array may not be the most
            // elegant solution, but for small files
            // this should work well.

            Long size = data.getChannel().size();
            if (size > Integer.MAX_VALUE) {
                throw new StorageException("File is too large.");
            }

            byte[] bytes = new byte[size.intValue()];
            data.read(bytes);

            Session session = getCurrentSession();
            int updatedRows = session.createQuery(
                    "update " + entityTypeName + " f set f." + dataPropertyName
                            + "=:bytes where f." + idPropertyName + "="
                            + fileId.toString()).setBinary("bytes", bytes)
                    .executeUpdate();

            // a correpsonding table row should have already been created,
            // otherwise the caller wouldn't know the fileId.
            if (updatedRows == 0) {
                String message = String
                        .format(
                                "Cannot put file since there is no existing table row for ID %1$s",
                                fileId);
                throw new StorageException(message);
            }
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void removeFile(Long fileId) throws StorageException {
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

        getCurrentSession().createQuery(hql).executeUpdate();
    }
}
