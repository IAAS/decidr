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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.junit.Test;

import de.decidr.model.exceptions.IncompleteConfigurationException;
import de.decidr.model.testing.DecidrOthersTest;

/**
 * Test case for <code>{@link StorageProviderFactoryTest}</code>.
 * 
 * @author Reinhold
 */
public class StorageProviderFactoryTest extends DecidrOthersTest {

    /**
     * Test method for {@link StorageProviderFactory#getStorageProvider()},
     * {@link StorageProviderFactory#configure(InputStream)},
     * {@link StorageProviderFactory#configure(Properties)},
     * {@link StorageProviderFactory#configure()},
     * {@link StorageProviderFactory#setAmazonS3(Boolean)},
     * {@link StorageProviderFactory#unsetAmazonS3()},
     * {@link StorageProviderFactory#setLocalOnly(Boolean)},
     * {@link StorageProviderFactory#unsetLocalOnly()},
     * {@link StorageProviderFactory#setPersistent(Boolean)},
     * {@link StorageProviderFactory#unsetPersistent()},
     * {@link StorageProviderFactory#setProtocol(String)} and
     * {@link StorageProviderFactory#unsetProtocol()}.
     */
    private static void runFactoryInstanceTests(StorageProviderFactory factory)
            throws IncompleteConfigurationException, InstantiationException,
            IllegalAccessException, InvalidPropertiesFormatException,
            IOException {
        factory.configure();
        assertNotNull(factory.getStorageProvider());
        assertFalse(factory.getStorageProvider() == factory
                .getStorageProvider());
        factory.configure();

        factory.unsetAmazonS3();
        factory.setAmazonS3(true);
        factory.setAmazonS3(null);
        factory.setAmazonS3(false);
        factory.unsetAmazonS3();

        factory.unsetLocalOnly();
        factory.setLocalOnly(true);
        factory.setLocalOnly(null);
        factory.setLocalOnly(false);
        factory.unsetLocalOnly();

        factory.unsetPersistent();
        factory.setPersistent(true);
        factory.setPersistent(null);
        factory.setPersistent(false);
        factory.unsetPersistent();

        factory.unsetProtocol();
        factory.setProtocol("");
        factory.setProtocol(null);
        factory.setProtocol("file");
        factory.setProtocol("http");
        factory.setProtocol("ftp");
        factory.setProtocol("asdfjhgsdfbj");
        factory.unsetProtocol();

        factory.configure();

        try {
            factory.configure((InputStream) null);
            fail("configure accepted null input stream");
        } catch (InvalidPropertiesFormatException e) {
            // supposed to happen
        }
        try {
            factory.configure(new ByteArrayInputStream(new byte[] {}));
            fail("configure accepted empty input stream");
        } catch (InvalidPropertiesFormatException e) {
            // supposed to happen
        }
        try {
            factory.configure((Properties) null);
            fail("configure accepted null properties");
        } catch (InvalidPropertiesFormatException e) {
            // supposed to happen
        }

        factory.configure(new Properties());
        Properties props = new Properties();
        props.loadFromXML(StorageProviderFactoryTest.class
                .getResourceAsStream("/decidr-storage.xml"));
        factory.configure(props);

        factory.configure(StorageProviderFactoryTest.class
                .getResourceAsStream("/decidr-storage.xml"));
    }

    /**
     * Test method for {@link StorageProviderFactory#getDefaultFactory()}.
     */
    @Test
    public void testGetDefaultFactory()
            throws IncompleteConfigurationException, InstantiationException,
            IllegalAccessException, InvalidPropertiesFormatException,
            IOException {
        StorageProviderFactory factory = StorageProviderFactory
                .getDefaultFactory();

        runFactoryInstanceTests(factory);
    }

    /**
     * Test method for {@link StorageProviderFactory#StorageProviderFactory()}.
     */
    @Test
    public void testStorageProviderFactory() throws InstantiationException,
            IllegalAccessException, IncompleteConfigurationException,
            InvalidPropertiesFormatException, IOException {
        StorageProviderFactory factory = new StorageProviderFactory();

        runFactoryInstanceTests(factory);
    }

    /**
     * Test method for
     * {@link StorageProviderFactory#StorageProviderFactory(File)}.
     */
    @Test
    public void testStorageProviderFactoryFile() throws InstantiationException,
            IllegalAccessException, IncompleteConfigurationException,
            InvalidPropertiesFormatException, IOException {
        File f = File.createTempFile("decidr_test", ".xml");

        try {
            new StorageProviderFactory((File) null);
            fail("StorageProviderFactory accepted null file");
        } catch (InvalidPropertiesFormatException e) {
            // supposed to happen
        }
        try {
            new StorageProviderFactory(f);
            fail("StorageProviderFactory accepted empty file");
        } catch (InvalidPropertiesFormatException e) {
            // supposed to happen
        }

        InputStream in = StorageProviderFactoryTest.class
                .getResourceAsStream("/decidr-storage.xml");
        FileOutputStream out = new FileOutputStream(f);
        int tmpByte;
        while ((tmpByte = in.read()) != -1) {
            out.write(tmpByte);
        }

        StorageProviderFactory factory = new StorageProviderFactory(f);

        runFactoryInstanceTests(factory);
    }

    /**
     * Test method for
     * {@link StorageProviderFactory#StorageProviderFactory(InputStream)}.
     */
    @Test
    public void testStorageProviderFactoryInputStream()
            throws InstantiationException, IllegalAccessException,
            IncompleteConfigurationException, InvalidPropertiesFormatException,
            IOException {
        try {
            new StorageProviderFactory((InputStream) null);
            fail("StorageProviderFactory accepted null input stream");
        } catch (InvalidPropertiesFormatException e) {
            // supposed to happen
        }
        try {
            new StorageProviderFactory(new ByteArrayInputStream(new byte[] {}));
            fail("StorageProviderFactory accepted empty input stream");
        } catch (InvalidPropertiesFormatException e) {
            // supposed to happen
        }

        StorageProviderFactory factory = new StorageProviderFactory(
                StorageProviderFactoryTest.class
                        .getResourceAsStream("/decidr-storage.xml"));

        runFactoryInstanceTests(factory);
    }

    /**
     * Test method for
     * {@link StorageProviderFactory#StorageProviderFactory(Properties)}.
     */
    @Test
    public void testStorageProviderFactoryProperties()
            throws InstantiationException, IllegalAccessException,
            IncompleteConfigurationException, InvalidPropertiesFormatException,
            IOException {
        try {
            new StorageProviderFactory((Properties) null);
            fail("StorageProviderFactory accepted null properties");
        } catch (InvalidPropertiesFormatException e) {
            // supposed to happen
        }

        Properties props = new Properties();
        props.loadFromXML(StorageProviderFactoryTest.class
                .getResourceAsStream("/decidr-storage.xml"));
        StorageProviderFactory factory = new StorageProviderFactory(props);

        runFactoryInstanceTests(factory);
    }
}
