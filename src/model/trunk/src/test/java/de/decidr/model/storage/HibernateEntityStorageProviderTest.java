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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.activation.MimetypesFileTypeMap;

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.LowLevelDatabaseTest;
import de.decidr.model.entities.File;
import de.decidr.model.exceptions.IncompleteConfigurationException;
import de.decidr.model.exceptions.StorageException;

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

        StorageProvider.putFile(
                new FileInputStream(BasicFile.getAbsolutePath()), 123456l,
                BasicFile.length());

        FileInputStream stream = new FileInputStream(
                "./src/test/java/decidr.jpg");

        byte[] In = readInputStream(stream);
        byte[] Out = readInputStream((FileInputStream) StorageProvider
                .getFile(123456l));

        assertTrue(java.util.Arrays.equals(In, Out));

        try {
            StorageProvider.putFile((FileInputStream) null, 123l, BasicFile
                    .length());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // nothing to do
        }

        try {
            StorageProvider.putFile(stream, (Long) null, BasicFile.length());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // nothing to do
        }

        try {
            StorageProvider.putFile(stream, 123l, (Long) null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // nothing to do
        }

    }

    /**
     * Test method for {@link HibernateEntityStorageProvider#removeFile(Long)}.
     * 
     * @throws StorageException
     */
    @Test
    public void testRemoveFile() throws StorageException {

        StorageProvider.removeFile(123456l);

        try {
            StorageProvider.getFile(123456l);
            fail("StorageException expected");
        } catch (StorageException e) {
            // nothing to do
        }

        try {
            StorageProvider.removeFile(999999l); // this file doesn't exist
        } catch (Exception e) {
            fail("No Exception should be thrown when an non existing file should be deleted");
        }

        try {
            StorageProvider.removeFile((Long) null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // nothing to do
        }

    }

    /**
     * Test method for {@link HibernateEntityStorageProvider#isApplicable(Properties)}.
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
     * Test method for {@link HibernateEntityStorageProvider#applyConfig(Properties)}.
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
            //nothing to do
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
