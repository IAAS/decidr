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

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.activation.MimetypesFileTypeMap;

import org.junit.BeforeClass;
import org.junit.Test;
import de.decidr.model.entities.File;
import de.decidr.model.exceptions.IncompleteConfigurationException;
import de.decidr.model.exceptions.StorageException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.storage.commands.GetFileTestCommand;
import de.decidr.model.storage.commands.PutFileTestCommand;
import de.decidr.model.storage.commands.PutFileTestFaultyCommand;
import de.decidr.model.storage.commands.RemoveFileFaultyTestCommand;
import de.decidr.model.storage.commands.RemoveFileTestCommand;
import de.decidr.model.testing.LowLevelDatabaseTest;
import de.decidr.model.transactions.HibernateTransactionCoordinator;

/**
 * Test class for the {@link HibernateEntityStorageProvider}.
 * 
 * @author Markus Fischer
 */
public class HibernateEntityStorageProviderTest extends LowLevelDatabaseTest {

    static HibernateEntityStorageProvider storageProvider;
    static File DataFile;
    static java.io.File BasicFile;

    /*
     * converts file into byte[]
     */
    private static ByteArrayOutputStream readFile(java.io.File file)
            throws Exception {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        FileInputStream fileInputStream = new FileInputStream(file
                .getAbsolutePath());

        byte[] buffer = new byte[16384];

        for (int len = fileInputStream.read(buffer); len > 0; len = fileInputStream
                .read(buffer)) {
            byteArrayOutputStream.write(buffer, 0, len);
        }

        fileInputStream.close();

        return byteArrayOutputStream;
    }

    /*
     * converts InputStream into byte[]
     */
    private static byte[] readInputStream(FileInputStream stream)
            throws Exception {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        FileInputStream fileInputStream = stream;

        byte[] buffer = new byte[16384];

        for (int len = fileInputStream.read(buffer); len > 0; len = fileInputStream
                .read(buffer)) {
            byteArrayOutputStream.write(buffer, 0, len);
        }

        fileInputStream.close();

        return byteArrayOutputStream.toByteArray();
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        BasicFile = new java.io.File("./src/test/java/decidr.jpg");

        DataFile = new de.decidr.model.entities.File();

        DataFile.setFileName("decidr.jpg");
        DataFile.setMimeType(new MimetypesFileTypeMap()
                .getContentType(BasicFile));
        DataFile.setFileSizeBytes(BasicFile.length());
        DataFile.setData(readFile(BasicFile).toByteArray());
        DataFile.setId(123456l);

        storageProvider = new HibernateEntityStorageProvider();

    }

    /**
     * Test method for
     * {@link HibernateEntityStorageProvider#putFile(InputStream, Long, Long)},
     * {@link HibernateEntityStorageProvider#getFile(Long)}.
     * 
     * @throws Exception
     */
    @Test
    public void testPutFileGetFile() throws Exception {
        fail("Not yet fully implemented");// TODO finish

        /*
         * put file
         */
        PutFileTestCommand cmd = new PutFileTestCommand(123456l, BasicFile,
                storageProvider);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        FileInputStream stream = new FileInputStream(
                "./src/test/java/decidr.jpg");

        /*
         * get file
         */
        GetFileTestCommand cmd2 = new GetFileTestCommand(123456l,
                storageProvider);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd2);

        /*
         * check if equal
         */
        byte[] In = readInputStream(stream);
        byte[] Out = readInputStream(cmd2.getResultStrem());

        assertTrue(java.util.Arrays.equals(In, Out));

        /*
         * check some illegal calls
         */
        try {
            PutFileTestFaultyCommand cmd3 = new PutFileTestFaultyCommand(
                    123456l, BasicFile, storageProvider);
            HibernateTransactionCoordinator.getInstance().runTransaction(cmd3);

            if (cmd3.getResult() == false) {
                fail("IllegalArgumentExpected");
            }

        } catch (IllegalArgumentException e) {
            // nothing to do
        }
    }

    /**
     * Test method for {@link HibernateEntityStorageProvider#removeFile(Long)}.
     * 
     * @throws TransactionException .getNewWorkItemSubject
     *             (tenantName);
     * 
     * @throws StorageException
     */
    @Test
    public void testRemoveFile() throws TransactionException {
        fail("Not yet fully implemented");// TODO finish

        // removing test
        RemoveFileTestCommand cmd4 = new RemoveFileTestCommand(123456l,
                storageProvider);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd4);

        // check if file is deleted
        GetFileTestCommand cmd5 = new GetFileTestCommand(123456l,
                storageProvider);
        try {
            HibernateTransactionCoordinator.getInstance().runTransaction(cmd5);
            fail("TransactionException expected");
        } catch (TransactionException e1) {
            // nothing to to
        }

        // remove non existing file
        cmd4 = new RemoveFileTestCommand(123456l, storageProvider);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd4);

        // call with illegal params
        RemoveFileFaultyTestCommand cmd6 = new RemoveFileFaultyTestCommand(
                123456l, storageProvider);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd6);

        if (cmd6.getResult() == false) {
            fail("IllegalArgumentException expected");
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
     * {@link HibernateEntityStorageProvider#applyConfig(Properties)}.
     */
    @Test
    public void testApplyConfig() {

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

        try {
            storageProvider.applyConfig(props);
        } catch (IncompleteConfigurationException e1) {
            fail("This configuration should work");
        }

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
}
