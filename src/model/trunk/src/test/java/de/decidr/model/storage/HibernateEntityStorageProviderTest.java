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
 * Test class for the HibernateEntityStorageProvider
 * 
 * @author Markus Fischer
 */
public class HibernateEntityStorageProviderTest extends LowLevelDatabaseTest {

    static HibernateEntityStorageProvider StorageProvider;
    static File DataFile;
    static java.io.File BasicFile;

    /*
     * converts file into byte[]
     */
    private static byte[] readFile(java.io.File file) throws Exception {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        FileInputStream fileInputStream = new FileInputStream(file
                .getAbsolutePath());

        byte[] buffer = new byte[16384];

        for (int len = fileInputStream.read(buffer); len > 0; len = fileInputStream
                .read(buffer)) {
            byteArrayOutputStream.write(buffer, 0, len);
        }

        fileInputStream.close();

        return byteArrayOutputStream.toByteArray();
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
        DataFile.setData(readFile(BasicFile));
        DataFile.setId(123456l);

        StorageProvider = new HibernateEntityStorageProvider();

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

        /*
         * put file
         */
        PutFileTestCommand cmd = new PutFileTestCommand(123456l, BasicFile,
                StorageProvider);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        FileInputStream stream = new FileInputStream(
                "./src/test/java/decidr.jpg");

        /*
         * get file
         */
        GetFileTestCommand cmd2 = new GetFileTestCommand(123456l,
                StorageProvider);
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
                    123456l, BasicFile, StorageProvider);
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
     * @throws TransactionException 
     * 
     * @throws StorageException
     */
    @Test
    public void testRemoveFile() throws TransactionException {

        try {
            RemoveFileTestCommand cmd4 = new RemoveFileTestCommand(123456l,
                    StorageProvider);
            HibernateTransactionCoordinator.getInstance().runTransaction(cmd4);
        } catch (Exception e) {
            fail("Couldn't remove the file");
        }

        GetFileTestCommand cmd5 = new GetFileTestCommand(123456l,
                StorageProvider);
        try {
            HibernateTransactionCoordinator.getInstance().runTransaction(cmd5);
            fail("TransactionException expected");
        } catch (TransactionException e1) {
            // nothing to to
        }

        try {
            RemoveFileTestCommand cmd4 = new RemoveFileTestCommand(123456l,
                    StorageProvider);
            HibernateTransactionCoordinator.getInstance().runTransaction(cmd4);
        } catch (Exception e) {
            fail("No Exception should be thrown when an non existing file should be deleted");
        }

        
        
        RemoveFileFaultyTestCommand cmd6 = new RemoveFileFaultyTestCommand(123456l,
                    StorageProvider);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd6);
        
        if(cmd6.getResul() == false){
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

        props.setProperty("AMAZON_S3_CONFIG_KEY", "false");
        props.setProperty("PROTOCOL_CONFIG_KEY", "file");
        props.setProperty("LOCAL_CONFIG_KEY", "false");
        props.setProperty("PERSISTENT_CONFIG_KEY", "true");

        assertTrue(StorageProvider.isApplicable(props));

        props = new Properties();

        props.setProperty("AMAZON_S3_CONFIG_KEY", "true");
        props.setProperty("PROTOCOL_CONFIG_KEY", "http");
        props.setProperty("LOCAL_CONFIG_KEY", "true");
        props.setProperty("PERSISTENT_CONFIG_KEY", "true");

        assertFalse(StorageProvider.isApplicable(props));

        props = new Properties();
        props = (Properties) null;

        try {
            StorageProvider.isApplicable(props);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // nothing to do
        }

    }

    /**
     * Test method for
     * {@link HibernateEntityStorageProvider#applyConfig(Properties)}.
     */
    @Test
    public void testApplyConfig() {

        Properties props = new Properties();

        props.setProperty("AMAZON_S3_CONFIG_KEY", "false");
        props.setProperty("PROTOCOL_CONFIG_KEY", "file");
        props.setProperty("LOCAL_CONFIG_KEY", "false");
        props.setProperty("PERSISTENT_CONFIG_KEY", "true");

        try {
            StorageProvider.applyConfig(props);
        } catch (IncompleteConfigurationException e1) {
            // MF no it shouldn't - it doesn't use the file protocol ~rr
            fail("This configuration should work");
        }

        props = new Properties();

        props.setProperty("AMAZON_S3_CONFIG_KEY", "true");
        props.setProperty("PROTOCOL_CONFIG_KEY", "http");
        props.setProperty("LOCAL_CONFIG_KEY", "true");
        props.setProperty("PERSISTENT_CONFIG_KEY", "true");

        try {
            StorageProvider.applyConfig(props);
            fail("This conf shouldn't work.");
        } catch (IncompleteConfigurationException e1) {
            // nothing to do
        }

        props = new Properties();
        props = (Properties) null;

        try {
            StorageProvider.applyConfig(props);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // nothing to do
        } catch (IncompleteConfigurationException e) {
            fail("IllegalArgumentException expected");
        }

    }

}
