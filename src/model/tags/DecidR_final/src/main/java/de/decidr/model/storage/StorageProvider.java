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

    public static final String AMAZON_S3_CONFIG_KEY = "amazons3";
    public static final String PROTOCOL_CONFIG_KEY = "protocol";
    public static final String LOCAL_CONFIG_KEY = "local";
    public static final String PERSISTENT_CONFIG_KEY = "persistent";

    /**
     * Tries to apply a configuration to the provider. The caller needs to make
     * sure all required settings are present.<br>
     * The method <code>{@link #isApplicable(Properties)}</code> can be used to
     * make sure that the configuration is complete.
     * <p>
     * Implementations of this interface may need this method to be called
     * successfully before being able to use the
     * <code>{@link #getFile(Long)}</code>,
     * <code>{@link #putFile(InputStream, Long, Long)}</code> and
     * <code>{@link #removeFile(Long)}</code> methods. The
     * {@link StorageProviderFactory} will already do this if it is used to
     * retrieve an instance of a {@link StorageProvider}.
     * 
     * @param config
     *            A <code>{@link Properties}</code> containing settings to be
     *            applied to the provider.
     * @throws IncompleteConfigurationException
     *             thrown if vital parts of the configuration are missing (e.g.
     *             authentification data) or the configuration is not applicable
     *             to this provider.<br>
     *             This exception is usually thrown when calling this <code>
     *             {@link StorageProvider}</code> manually or when the
     *             configuration of the <code>{@link StorageProviderFactory}
     *             </code> is changed after its instantiation or a call to
     *             <code>{@link StorageProviderFactory#configure()}</code>.<br>
     * <br>
     *             The recommended course of action is to either manually choose
     *             a different <code>{@link StorageProvider}</code> or to run
     *             <code>{@link StorageProviderFactory#configure()}</code>.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>
     */
    public void applyConfig(Properties config)
            throws IncompleteConfigurationException;

    /**
     * Retrieves the file that is identified by the given ID from the storage
     * backend.
     * 
     * @param fileId
     *            The file identifier.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>
     * @return the file data
     */
    public InputStream getFile(Long fileId) throws StorageException;

    /**
     * Checks to see if a <code>{@link Properties}</code> can be applied to this
     * provider. The default properties (see below) have to be checked by every
     * <code>{@link StorageProvider}</code>. The default properties are
     * available as {@link String} constants from this interface.
     * <p>
     * Default properties:<br>
     * <ul>
     * <li>local (<code>boolean</code> - whether files are to be saved locally)</li>
     * <li>amazons3 (<code>boolean</code> - whether files are to be stored in
     * the Amazon S3 service)</li>
     * <li>persistent (<code>boolean</code> - whether files should survive
     * system reboot/failure)</li>
     * <li>protocol (<code>{@link String}</code> - what protocol should be used
     * to access files; e.g. &quot;file&quot;, &quot;http&quot;,
     * &quot;https&quot;)</li>
     * </ul>
     * 
     * @param config
     *            The configuration that should be checked.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>
     * @return - <code>true</code>, if the configuration can be applied<br>
     *         - <code>false</code>, if it can't
     */
    public boolean isApplicable(Properties config);

    /**
     * Creates or replaces the file that is identified by the given ID on the
     * storage backend.
     * 
     * @param data
     *            the contents of the file
     * @param fileId
     *            the file identifier
     * @param fileSize
     *            the size of the file
     * @throws StorageException
     *             if a problem occurs while storing
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>
     */
    public void putFile(InputStream data, Long fileId, Long fileSize)
            throws StorageException;

    /**
     * Permanently removes the file that is identified by the given ID from the
     * storage backend.
     * 
     * @param fileId
     *            The file identifier.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>
     */
    public void removeFile(Long fileId) throws StorageException;
}