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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import de.decidr.model.exceptions.IncompleteConfigurationException;
import de.decidr.model.exceptions.StorageException;
import de.decidr.model.logging.DefaultLogger;

/**
 * <code>{@link StorageProvider}</code> that stores files locally. Persistence
 * cannot be guaranteed, as the temporary directory may reside in volatile
 * storage.
 * 
 * @author Reinhold
 */
public class LocalStorageProvider implements StorageProvider {

    Logger log = DefaultLogger.getLogger(LocalStorageProvider.class);

    boolean local = true, amazonS3 = false, persistent = false;
    String[] protocols = { "file", "" };

    private static File storageDirectory;

    /**
     * Default constructor initialising <code>{@link #storageDirectory}</code>,
     * if necessary.
     * 
     * @throws StorageException
     *             If the <code>{@link #storageDirectory}</code> can't be
     *             created.
     */
    public LocalStorageProvider() throws StorageException {
        log.trace("Entering " + LocalStorageProvider.class.getSimpleName()
                + "()");
        if (storageDirectory == null) {
            log.debug("setting the directory to store files in ...");
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                log.debug("... the windows way");
                storageDirectory = new File("C:\\DecidR\\");
            } else {
                log.debug("... the unix way");
                storageDirectory = new File("/tmp/decidr/");
            }
            if (!storageDirectory.exists()) {
                log.debug("creating directory");
                if (!storageDirectory.mkdirs()) {
                    log.error("Couldn't create storage directory");
                    throw new StorageException(
                            "Couldn't create storage directory");
                }
            }
        }
        log.trace("Leaving " + LocalStorageProvider.class.getSimpleName()
                + "()");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.model.storage.StorageProvider#applyConfig(java.util.Properties)
     */
    @Override
    public void applyConfig(Properties config)
            throws IncompleteConfigurationException {
        log.trace("Entering " + LocalStorageProvider.class.getSimpleName()
                + ".applyConfig(Properties)");
        if (!isApplicable(config)) {
            log.error("Cannot apply this configuration");
            throw new IncompleteConfigurationException(
                    "Configuration not applicable.");
        }

        // There's nothing that needs to be applied..
        log.trace("Leaving " + LocalStorageProvider.class.getSimpleName()
                + ".applyConfig(Properties)");
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.model.storage.StorageProvider#getFile(java.lang.Long)
     */
    @Override
    public InputStream getFile(Long fileId) throws StorageException {
        log.trace("Entering " + LocalStorageProvider.class.getSimpleName()
                + ".getFile(Long)");
        try {
            log.trace("Leaving " + LocalStorageProvider.class.getSimpleName()
                    + ".getFile(Long)");
            return new FileInputStream(new File(storageDirectory, "DecidR_"
                    + fileId + ".tmp"));
        } catch (FileNotFoundException e) {
            log.error("File couln't be found");
            throw new StorageException(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.model.storage.StorageProvider#isApplicable(java.util.Properties
     * )
     */
    @Override
    public boolean isApplicable(Properties config) {
        log.trace("Entering " + LocalStorageProvider.class.getSimpleName()
                + ".isApplicable(Properties)");
        boolean applicable = true;
        boolean configAmazon, configLocal, configPersistent;

        // check whether we implement the required protocol
        if (applicable && config.contains(PROTOCOL_CONFIG_KEY)) {
            log.debug("checking protocol compliance");
            boolean found = false;
            String required = config.getProperty(PROTOCOL_CONFIG_KEY);
            for (String protocol : protocols) {
                if (protocol.equalsIgnoreCase(required)) {
                    found = true;
                }
            }
            if (!found) {
                applicable = false;
            }
        }

        // check whether the config values exist and coincide with this
        // provider's config
        // if a value isn't contained in the config, it is assumed to be
        // applicable
        if (applicable) {
            if (config.contains(AMAZON_S3_CONFIG_KEY)) {
                configAmazon = new Boolean(config
                        .getProperty(AMAZON_S3_CONFIG_KEY));
            } else {
                configAmazon = amazonS3;
            }
            if (config.contains(LOCAL_CONFIG_KEY)) {
                configLocal = new Boolean(config.getProperty(LOCAL_CONFIG_KEY));
            } else {
                configLocal = local;
            }
            if (config.contains(PERSISTENT_CONFIG_KEY)) {
                configPersistent = new Boolean(config
                        .getProperty(PERSISTENT_CONFIG_KEY));
            } else {
                configPersistent = persistent;
            }

            log.debug("checking other default settings compliance");
            if (!(configAmazon == amazonS3 && configLocal == local && configPersistent == persistent)) {
                applicable = false;
            }
        }

        log.debug("applicability: " + applicable);
        log.trace("Leaving " + LocalStorageProvider.class.getSimpleName()
                + ".isApplicable(Properties)");
        return applicable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.model.storage.StorageProvider#putFile(java.io.FileInputStream,
     * java.lang.Long)
     */
    @Override
    public void putFile(FileInputStream data, Long fileId)
            throws StorageException {
        log.trace("Entering " + LocalStorageProvider.class.getSimpleName()
                + ".putFile(FileInputStream, Long)");
        File newFile = new File(storageDirectory, "DecidR_" + fileId + ".tmp");
        try {
            FileOutputStream fos = new FileOutputStream(newFile, false);
            int dataByte;

            log.debug("transferring data...");
            // XXX: this might block - bad??
            while ((dataByte = data.read()) != -1) {
                fos.write(dataByte);
            }
            log.debug("... done");
        } catch (IOException e) {
            log.error("IOException during access to either "
                    + "input or output file", e);
            throw new StorageException(e.getMessage(), e);
        }
        log.trace("Leaving " + LocalStorageProvider.class.getSimpleName()
                + ".putFile(FileInputStream, Long)");
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.model.storage.StorageProvider#removeFile(java.lang.Long)
     */
    @Override
    public void removeFile(Long fileId) throws StorageException {
        log.trace("Entering " + LocalStorageProvider.class.getSimpleName()
                + ".removeFile(Long)");
        File superfluous = new File(storageDirectory, "DecidR_" + fileId
                + ".tmp");
        if (superfluous.exists()) {
            log.debug("Deleting file...");
            superfluous.delete();
        } else {
            log.debug("File doesn't exist.");
        }
        log.trace("Leaving " + LocalStorageProvider.class.getSimpleName()
                + ".removeFile(Long)");
    }
}
