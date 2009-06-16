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

/**
 * TODO add comments
 * <p>
 * Usage: new StorageProviderFactory().configure().getStorageProvider();
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class StorageProviderFactory {

    private static final String DEFAULT_CONFIG_FILENAME = "decidr-storage.xml";

    /**
     * Creates a new StorageProviderFactory using the given configuration file.
     * 
     * @param configFileName
     *            configuration file to use
     */
    public StorageProviderFactory(String configFileName) {
        // TODO imeplement me - use given config file
    }

    /**
     * Creates a new StorageProviderFactory using the default configuration
     * file.
     */
    public StorageProviderFactory() {
        this(DEFAULT_CONFIG_FILENAME);
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
     * Returns a configured storage provider.
     * 
     * @return
     */
    public StorageProvider getStorageProvider() {
        // TODO return proper storage provider according to configuration
        throw new UnsupportedOperationException();
    }

}
