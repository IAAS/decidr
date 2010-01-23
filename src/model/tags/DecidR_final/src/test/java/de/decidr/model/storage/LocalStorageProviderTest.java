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
import de.decidr.model.testing.DecidrOthersTest;

/**
 * Test class for the LocalStorageProvider
 * 
 * @author Markus Fischer
 */
public class LocalStorageProviderTest extends DecidrOthersTest {

    static LocalStorageProvider storageProvider;
    static File DataFile;
    static java.io.File BasicFile;

    /**
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

    /**
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

        BasicFile = new java.io.File("./src/test/resources/decidr.jpg");

        DataFile = new de.decidr.model.entities.File();

        DataFile.setFileName("decidr.jpg");
        DataFile.setMimeType(new MimetypesFileTypeMap()
                .getContentType(BasicFile));
        DataFile.setFileSizeBytes(BasicFile.length());
        DataFile.setData(readFile(BasicFile));
        DataFile.setId(123456l);

        storageProvider = new LocalStorageProvider();

    }

    /**
     * Test method for {@link LocalStorageProvider#applyConfig(Properties)}.
     */
    @Test
    public void testApplyConfig() throws IncompleteConfigurationException {

        // set as it should

        Properties props = new Properties();

        props.setProperty(StorageProvider.AMAZON_S3_CONFIG_KEY, "false");
        props.setProperty(StorageProvider.PROTOCOL_CONFIG_KEY, "file");
        props.setProperty(StorageProvider.LOCAL_CONFIG_KEY, "true");
        props.setProperty(StorageProvider.PERSISTENT_CONFIG_KEY, "false");

        storageProvider.applyConfig(props);

        // set s3

        props = new Properties();

        props.setProperty(StorageProvider.AMAZON_S3_CONFIG_KEY, "true");
        props.setProperty(StorageProvider.PROTOCOL_CONFIG_KEY, "http");
        props.setProperty(StorageProvider.LOCAL_CONFIG_KEY, "true");
        props.setProperty(StorageProvider.PERSISTENT_CONFIG_KEY, "true");

        try {
            storageProvider.applyConfig(props);
            fail("This configuration shouldn't work");
        } catch (IncompleteConfigurationException e1) {
            // nothing to do
        }

        // set all false

        props = new Properties();

        props.setProperty(StorageProvider.AMAZON_S3_CONFIG_KEY, "false");
        props.setProperty(StorageProvider.PROTOCOL_CONFIG_KEY, "file");
        props.setProperty(StorageProvider.LOCAL_CONFIG_KEY, "false");
        props.setProperty(StorageProvider.PERSISTENT_CONFIG_KEY, "false");

        try {
            storageProvider.applyConfig(props);
            fail("This configuration shouldn't work");
        } catch (IncompleteConfigurationException e1) {
            // nothing to do
        }

        // give null

        props = null;

        try {
            storageProvider.applyConfig(props);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // nothing to do
        }

        // one missing key

        props = new Properties();

        props.setProperty(StorageProvider.PROTOCOL_CONFIG_KEY, "file");
        props.setProperty(StorageProvider.LOCAL_CONFIG_KEY, "true");
        props.setProperty(StorageProvider.PERSISTENT_CONFIG_KEY, "false");

        storageProvider.applyConfig(props);
    }

    /**
     * Test method for {@link LocalStorageProvider#isApplicable(Properties)}.
     */
    @Test
    public void testIsApplicable() {

        // set as it should be

        Properties props = new Properties();

        props.setProperty(StorageProvider.AMAZON_S3_CONFIG_KEY, "false");
        props.setProperty(StorageProvider.PROTOCOL_CONFIG_KEY, "file");
        props.setProperty(StorageProvider.LOCAL_CONFIG_KEY, "true");
        props.setProperty(StorageProvider.PERSISTENT_CONFIG_KEY, "false");

        assertTrue(storageProvider.isApplicable(props));

        // set s3

        props = new Properties();

        props.setProperty(StorageProvider.AMAZON_S3_CONFIG_KEY, "true");
        props.setProperty(StorageProvider.PROTOCOL_CONFIG_KEY, "http");
        props.setProperty(StorageProvider.LOCAL_CONFIG_KEY, "true");
        props.setProperty(StorageProvider.PERSISTENT_CONFIG_KEY, "true");

        assertFalse(storageProvider.isApplicable(props));

        // set all false

        props = new Properties();

        props.setProperty(StorageProvider.AMAZON_S3_CONFIG_KEY, "false");
        props.setProperty(StorageProvider.PROTOCOL_CONFIG_KEY, "file");
        props.setProperty(StorageProvider.LOCAL_CONFIG_KEY, "false");
        props.setProperty(StorageProvider.PERSISTENT_CONFIG_KEY, "false");

        assertFalse(storageProvider.isApplicable(props));

        // give null

        props = null;

        try {
            storageProvider.isApplicable(props);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // nthing to do
        }

        // one missing key

        props = new Properties();

        props.setProperty(StorageProvider.PROTOCOL_CONFIG_KEY, "file");
        props.setProperty(StorageProvider.LOCAL_CONFIG_KEY, "true");
        props.setProperty(StorageProvider.PERSISTENT_CONFIG_KEY, "false");

        assertTrue(storageProvider.isApplicable(props));

    }

    /**
     * Test method for
     * {@link LocalStorageProvider#putFile(InputStream, Long, Long)},
     * {@link LocalStorageProvider#getFile(Long)}.
     * 
     * @throws Exception
     */
    @Test
    public void testPutFileGetFile() throws Exception {

        storageProvider.putFile(
                new FileInputStream(BasicFile.getAbsolutePath()), 123456l,
                BasicFile.length());

        FileInputStream stream = new FileInputStream(
                "./src/test/resources/decidr.jpg");

        byte[] In = readInputStream(stream);
        byte[] Out = readInputStream((FileInputStream) storageProvider
                .getFile(123456l));

        assertTrue(java.util.Arrays.equals(In, Out));

        try {
            storageProvider.putFile((FileInputStream) null, 123l, BasicFile
                    .length());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // nothing to do
        }

        try {
            storageProvider.putFile(stream, (Long) null, BasicFile.length());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // nothing to do
        }

        try {
            storageProvider.putFile(stream, 123l, (Long) null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // nothing to do
        }

    }

    /**
     * Test method for {@link LocalStorageProvider#removeFile(Long)}.
     * 
     * @throws StorageException
     */
    @Test
    public void testRemoveFile() throws StorageException {

        storageProvider.removeFile(123456l);

        try {
            storageProvider.getFile(123456l);
            fail("StorageException expected");
        } catch (StorageException e) {
            // nothing to do
        }

        try {
            storageProvider.removeFile(999999l); // this file doesn't exist
        } catch (Exception e) {
            fail("No Exception should be thrown when an non existing file should be deleted");
        }

        try {
            storageProvider.removeFile((Long) null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // nothing to do
        }

    }

}
