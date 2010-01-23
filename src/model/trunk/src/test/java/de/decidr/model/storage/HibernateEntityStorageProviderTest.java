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

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.entities.File;
import de.decidr.model.exceptions.IncompleteConfigurationException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.FileFacadeTest;
import de.decidr.model.storage.commands.BasicFileCreatorCommand;
import de.decidr.model.storage.commands.GetFileTestCommand;
import de.decidr.model.storage.commands.PutFileTestCommand;
import de.decidr.model.storage.commands.PutFileTestFaultyCommand;
import de.decidr.model.storage.commands.RemoveFileFaultyTestCommand;
import de.decidr.model.storage.commands.RemoveFileTestCommand;
import de.decidr.model.storage.hibernate.HibernateEntityStorageProvider;
import de.decidr.model.testing.LowLevelDatabaseTest;
import de.decidr.model.transactions.HibernateTransactionCoordinator;

/**
 * Test class for the {@link HibernateEntityStorageProvider}.
 * 
 * @author Markus Fischer
 */
public class HibernateEntityStorageProviderTest extends LowLevelDatabaseTest {

    static HibernateEntityStorageProvider storageProvider;
    static File dataFile;
    static java.io.File basicFile;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        BasicFileCreatorCommand cmd = new BasicFileCreatorCommand();

        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        basicFile = cmd.getBasicFile();
        dataFile = cmd.getDataFile();
    }

    @Before
    public void setUpBefore() throws Exception {
        storageProvider = new HibernateEntityStorageProvider();
    }

    /**
     * Test method for
     * {@link HibernateEntityStorageProvider#applyConfig(Properties)}.
     */
    @Test
    public void testApplyConfig() throws IncompleteConfigurationException {

        Properties props = new Properties();

        props.setProperty(StorageProvider.AMAZON_S3_CONFIG_KEY, "false");
        props.setProperty(StorageProvider.PROTOCOL_CONFIG_KEY, "");
        props.setProperty(StorageProvider.LOCAL_CONFIG_KEY, "false");
        props.setProperty(StorageProvider.PERSISTENT_CONFIG_KEY, "true");
        props.setProperty(
                HibernateEntityStorageProvider.CONFIG_KEY_ENTITY_TYPE_NAME,
                "File");
        props
                .setProperty(
                        HibernateEntityStorageProvider.CONFIG_KEY_DELETE_ENTITY,
                        "true");
        props.setProperty(
                HibernateEntityStorageProvider.CONFIG_KEY_ID_PROPERTY_NAME,
                "id");
        props.setProperty(
                HibernateEntityStorageProvider.CONFIG_KEY_DATA_PROPERTY_NAME,
                "data");

        storageProvider.applyConfig(props);

        props = new Properties();

        props.setProperty(
                HibernateEntityStorageProvider.CONFIG_KEY_ENTITY_TYPE_NAME,
                "File");
        props
                .setProperty(
                        HibernateEntityStorageProvider.CONFIG_KEY_DELETE_ENTITY,
                        "true");
        props.setProperty(
                HibernateEntityStorageProvider.CONFIG_KEY_DATA_PROPERTY_NAME,
                "data");

        try {
            storageProvider.applyConfig(props);
            fail("This conf shouldn't work.");
        } catch (IncompleteConfigurationException e1) {
            // nothing to do
        }

        props = null;

        try {
            storageProvider.applyConfig(props);
            fail("IncompleteConfigurationException expected");
        } catch (IncompleteConfigurationException e) {
            // nothing to do
        }
    }

    /**
     * Test method for
     * {@link HibernateEntityStorageProvider#isApplicable(Properties)}.
     */
    @Test
    public void testIsApplicable() {

        Properties props = new Properties();

        props.setProperty(StorageProvider.AMAZON_S3_CONFIG_KEY, "false");
        props.setProperty(StorageProvider.PROTOCOL_CONFIG_KEY, "");
        props.setProperty(StorageProvider.LOCAL_CONFIG_KEY, "false");
        props.setProperty(StorageProvider.PERSISTENT_CONFIG_KEY, "true");
        props.setProperty(
                HibernateEntityStorageProvider.CONFIG_KEY_ENTITY_TYPE_NAME,
                "File");
        props
                .setProperty(
                        HibernateEntityStorageProvider.CONFIG_KEY_DELETE_ENTITY,
                        "true");
        props.setProperty(
                HibernateEntityStorageProvider.CONFIG_KEY_ID_PROPERTY_NAME,
                "id");
        props.setProperty(
                HibernateEntityStorageProvider.CONFIG_KEY_DATA_PROPERTY_NAME,
                "data");

        assertTrue(storageProvider.isApplicable(props));

        props = new Properties();

        props.setProperty(StorageProvider.AMAZON_S3_CONFIG_KEY, "true");
        props.setProperty(StorageProvider.PROTOCOL_CONFIG_KEY, "http");
        props.setProperty(StorageProvider.LOCAL_CONFIG_KEY, "true");
        props.setProperty(StorageProvider.PERSISTENT_CONFIG_KEY, "true");
        props.setProperty(
                HibernateEntityStorageProvider.CONFIG_KEY_ENTITY_TYPE_NAME,
                "File");
        props
                .setProperty(
                        HibernateEntityStorageProvider.CONFIG_KEY_DELETE_ENTITY,
                        "true");
        props.setProperty(
                HibernateEntityStorageProvider.CONFIG_KEY_ID_PROPERTY_NAME,
                "id");
        props.setProperty(
                HibernateEntityStorageProvider.CONFIG_KEY_DATA_PROPERTY_NAME,
                "data");

        assertFalse(storageProvider.isApplicable(props));

        props = new Properties();
        props = null;

        assertFalse(storageProvider.isApplicable(props));
    }

    /**
     * Test method for
     * {@link HibernateEntityStorageProvider#putFile(InputStream, Long, Long)},
     * {@link HibernateEntityStorageProvider#getFile(Long)} and
     * {@link HibernateEntityStorageProvider#removeFile(Long)}.
     */
    @Test
    public void testPutFileGetFile() throws Exception {
        Properties props = new Properties();

        props.setProperty(StorageProvider.AMAZON_S3_CONFIG_KEY, "false");
        props.setProperty(StorageProvider.PROTOCOL_CONFIG_KEY, "");
        props.setProperty(StorageProvider.LOCAL_CONFIG_KEY, "false");
        props.setProperty(StorageProvider.PERSISTENT_CONFIG_KEY, "true");
        props.setProperty(
                HibernateEntityStorageProvider.CONFIG_KEY_ENTITY_TYPE_NAME,
                "File");
        props
                .setProperty(
                        HibernateEntityStorageProvider.CONFIG_KEY_DELETE_ENTITY,
                        "true");
        props.setProperty(
                HibernateEntityStorageProvider.CONFIG_KEY_ID_PROPERTY_NAME,
                "id");
        props.setProperty(
                HibernateEntityStorageProvider.CONFIG_KEY_DATA_PROPERTY_NAME,
                "data");
        storageProvider.applyConfig(props);

        /*
         * put file
         */
        PutFileTestCommand cmd = new PutFileTestCommand(dataFile.getId(),
                basicFile, storageProvider);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        FileInputStream stream = new FileInputStream(
                "./src/test/resources/decidr.jpg");

        /*
         * get file
         */
        GetFileTestCommand cmd2 = new GetFileTestCommand(dataFile.getId(),
                storageProvider);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd2);

        /*
         * check if equal
         */
        assertTrue(FileFacadeTest.compareInputStreams(stream, cmd2
                .getResultStrem()));

        /*
         * check some illegal calls
         */
        try {
            PutFileTestFaultyCommand cmd3 = new PutFileTestFaultyCommand(
                    dataFile.getId(), basicFile, storageProvider);
            HibernateTransactionCoordinator.getInstance().runTransaction(cmd3);

            if (cmd3.getResult() == false) {
                fail("IllegalArgumentExpected");
            }

        } catch (IllegalArgumentException e) {
            // nothing to do
        }

        // removing test
        RemoveFileTestCommand cmd4 = new RemoveFileTestCommand(
                dataFile.getId(), storageProvider);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd4);

        // check if file is deleted
        GetFileTestCommand cmd5 = new GetFileTestCommand(dataFile.getId(),
                storageProvider);
        try {
            HibernateTransactionCoordinator.getInstance().runTransaction(cmd5);
            fail("TransactionException expected");
        } catch (TransactionException e1) {
            // nothing to to
        }

        // remove non existing file
        cmd4 = new RemoveFileTestCommand(dataFile.getId(), storageProvider);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd4);

        // call with illegal params
        RemoveFileFaultyTestCommand cmd6 = new RemoveFileFaultyTestCommand(
                dataFile.getId(), storageProvider);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd6);

        if (cmd6.getResult() == false) {
            fail("IllegalArgumentException expected");
        }
    }
}
