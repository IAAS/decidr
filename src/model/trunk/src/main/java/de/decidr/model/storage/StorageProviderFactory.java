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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.apache.log4j.Logger;

import de.decidr.model.exceptions.IncompleteConfigurationException;
import de.decidr.model.logging.DefaultLogger;

/**
 * TODO add comments
 * <p>
 * Usage: new StorageProviderFactory().getStorageProvider();
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @author Reinhold
 * @version 0.1
 */
public class StorageProviderFactory {

    /**
     * The default properties file.
     */
    private static final File DEFAULT_CONFIG_FILE = new File(
            "decidr-storage.xml");

    Logger log = DefaultLogger.getLogger(StorageProviderFactory.class);

    /**
     * The configuration that needs to be applied to the
     * <code>{@link StorageProvider}</code>.
     * <p>
     * Contains a set of standard properties also settable through methods
     * provided by this class and some provider-specific properties, e.g.
     * authentification data.
     */
    private Properties config;

    private Class<? extends StorageProvider> selectedProvider;

    /**
     * Creates a new StorageProviderFactory using the given configuration file.
     * 
     * @param config
     *            configuration <code>{@link Properties}</code> to use
     */
    public StorageProviderFactory(Properties config) {
        configure(config);
    }

    /**
     * Creates a new StorageProviderFactory using the given configuration file.
     * 
     * @param configFile
     *            configuration file to use
     * @throws IOException
     *             see <code>{@link #configure(InputStream)}</code>
     * @throws InvalidPropertiesFormatException
     *             see <code>{@link #configure(InputStream)}</code>
     */
    public StorageProviderFactory(InputStream configFile)
            throws InvalidPropertiesFormatException, IOException {
        configure(configFile);
    }

    /**
     * Creates a new StorageProviderFactory using the given configuration file.
     * 
     * @param configFile
     *            configuration file to use
     * @throws IOException
     *             see <code>{@link #StorageProviderFactory(InputStream)}</code>
     * @throws FileNotFoundException
     *             thrown when the specified file can't be located/accessed on
     *             the file system.
     * @throws InvalidPropertiesFormatException
     *             see <code>{@link #StorageProviderFactory(InputStream)}</code>
     */
    public StorageProviderFactory(File configFile)
            throws InvalidPropertiesFormatException, FileNotFoundException,
            IOException {
        this(new FileInputStream(configFile));
    }

    /**
     * Creates a new StorageProviderFactory using the default configuration
     * file.
     * 
     * @throws IOException
     *             see <code>{@link #StorageProviderFactory(File)}</code>
     * @throws FileNotFoundException
     *             see <code>{@link #StorageProviderFactory(File)}</code>
     * @throws InvalidPropertiesFormatException
     *             see <code>{@link #StorageProviderFactory(File)}</code>
     */
    public StorageProviderFactory() throws InvalidPropertiesFormatException,
            FileNotFoundException, IOException {
        this(DEFAULT_CONFIG_FILE);
    }

    /**
     * (Re-)applies the current configuration.
     * 
     * @return this object for method chaining.
     */
    public StorageProviderFactory configure() {
        // TODO apply current configuration
        return this;
    }

    /**
     * Applies a new configuration.
     * 
     * @param config
     *            configuration <code>{@link Properties}</code> to use
     * @return this object for method chaining.
     */
    public StorageProviderFactory configure(Properties config) {
        this.config = config;
        return configure();
    }

    /**
     * Applies a new configuration.
     * 
     * @param configFile
     *            configuration file to use
     * @return this object for method chaining.
     * @throws IOException
     *             thrown when an error occurs while reading the config file.
     * @throws InvalidPropertiesFormatException
     *             thrown when the specified file does not conform to the Java
     *             properties specification.
     */
    public StorageProviderFactory configure(InputStream configFile)
            throws InvalidPropertiesFormatException, IOException {
        Properties config = new Properties();
        config.loadFromXML(configFile);
        return configure(config);
    }

    /**
     * Returns a configured storage provider.
     * 
     * @return <ul>
     *         <li value="-"><code>null</code>, if there is no
     *         <code>{@link StorageProvider}</code> available that is compatible
     *         with the specified configuration.</li> <li value="-">otherwise,
     *         an instance of a <code>{@link StorageProvider}</code> conforming
     *         to the specified configuration.</li>
     *         </ul>
     * @throws IncompleteConfigurationException
     *             see
     *             <code>{@link StorageProvider#applyConfig(Properties)}</code>
     */
    public StorageProvider getStorageProvider()
            throws IncompleteConfigurationException {
        log.trace("Entering " + StorageProviderFactory.class.getSimpleName()
                + ".getStorageProvider()");
        StorageProvider result = null;

        if (selectedProvider != null) {
            try {
                result = selectedProvider.newInstance();
                result.applyConfig(config);
            } catch (InstantiationException e) {
                selectedProvider = null;
            } catch (IllegalAccessException e) {
                selectedProvider = null;
            }
        }

        log.trace("Leaving " + StorageProviderFactory.class.getSimpleName()
                + ".getStorageProvider()");
        return result;
    }

    /**
     * TODO: add comment
     * 
     * @param local
     */
    public void setLocalOnly(Boolean local) {
        config.setProperty("local", local.toString());
    }

    /**
     * TODO: add comment
     * 
     * @param amazons3
     */
    public void setAmazonS3(Boolean amazons3) {
        config.setProperty("amazons3", amazons3.toString());
    }

    /**
     * TODO: add comment
     * 
     * @param protocol
     */
    public void setProtocol(String protocol) {
        config.setProperty("protocol", protocol);
    }
}
