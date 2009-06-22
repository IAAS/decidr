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

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import de.decidr.model.exceptions.IncompleteConfigurationException;
import de.decidr.model.exceptions.StorageException;

/**
 * DecidR storage providers that allow storing and retrieving files.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @author Reinhold
 * 
 * @version 0.1
 */
public interface StorageProvider {

    /**
     * Creates or replaces the file that is identified by the given id on the
     * storage backend.
     */
    public void putFile(FileInputStream data, Long fileId) throws StorageException;

    /**
     * Retrieves the file that is identified by the given id from the storage
     * backend.
     * 
     * @param fileId
     *            RR
     * @return the file data
     */
    public InputStream getFile(Long fileId) throws StorageException;

    /**
     * Permanently removes the file that is identified by the given id from the
     * storage backend.
     * 
     * @param fileId
     *            RR
     */
    public void removeFile(Long fileId) throws StorageException;

    /**
     * TODO: add comment
     * 
     * @param config
     * @return
     */
    public boolean isApplicable(Properties config);

    /**
     * TODO: add comment <br>
     * The method <code>{@link #isApplicable(Properties)}</code> can be used to
     * make sure that the configuration is complete.
     * 
     * @param config
     * @throws IncompleteConfigurationException
     *             thrown if vital parts of the configuration are missing (e.g.
     *             authentification data).<br>
     *             This exception is usually thrown when calling this
     *             <code>{@link StorageProvider}</code> manually or when the
     *             configuration of the
     *             <code>{@link StorageProviderFactory}</code> is changed after
     *             its instantiation or a call to
     *             <code>{@link StorageProviderFactory#configure()}</code>.<br>
     * <br>
     *             The recommended course of action is to either manually choose
     *             a different <code>{@link StorageProvider}</code> or to run
     *             <code>{@link StorageProviderFactory#configure()}</code>.
     */
    public void applyConfig(Properties config)
            throws IncompleteConfigurationException;
}
