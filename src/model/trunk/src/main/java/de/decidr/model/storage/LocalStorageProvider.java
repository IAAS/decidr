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

import de.decidr.model.exceptions.IncompleteConfigurationException;
import de.decidr.model.exceptions.StorageException;

/**
 * <code>{@link StorageProvider}</code> that stores files locally. Persistence
 * cannot be guaranteed, as the temporary directory may reside in volatile
 * storage.
 * 
 * @author Reinhold
 */
public class LocalStorageProvider implements StorageProvider {

    boolean local = true, amazonS3 = false, persistent = false;
    String[] protocols = { "file" };

    private static File storagePath;

    /**
     * Default constructor initialising <code>{@link #storagePath}</code>, if
     * necessary.
     */
    public LocalStorageProvider() {
        if (storagePath == null) {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                storagePath = new File("C:\\DecidR\\");
            } else {
                storagePath = new File("/tmp/decidr/");
            }
            if (!storagePath.exists()) {
                try {
                    storagePath.createNewFile();
                } catch (IOException e1) {
                    try {
                        storagePath.createNewFile();
                    } catch (IOException e2) {
                        try {
                            storagePath.createNewFile();
                        } catch (IOException e3) {
                            // XXX there's *probably* something wrong - now,
                            // what should we do?
                        }
                    }
                }
            }
        }
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
        if (!isApplicable(config)) {
            throw new IncompleteConfigurationException(
                    "Configuration not applicable.");
        }

        // There's nothing that needs to be applied..
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.model.storage.StorageProvider#getFile(java.lang.Long)
     */
    @Override
    public InputStream getFile(Long fileId) throws StorageException {
        try {
            return new FileInputStream(new File(storagePath, "DecidR_" + fileId
                    + ".tmp"));
        } catch (FileNotFoundException e) {
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
        boolean applicable = true;
        boolean configAmazon, configLocal, configPersistent;

        // check whether we implement the required protocol
        if (applicable && config.contains(PROTOCOL_CONFIG_KEY)) {
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

            if (!(configAmazon == amazonS3 && configLocal == local && configPersistent == persistent)) {
                applicable = false;
            }
        }

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
        File newFile = new File(storagePath, "DecidR_" + fileId + ".tmp");
        try {
            FileOutputStream fos = new FileOutputStream(newFile, false);
            int dataByte;

            // XXX: this might block - bad??
            while ((dataByte = data.read()) != -1) {
                fos.write(dataByte);
            }
        } catch (IOException e) {
            throw new StorageException(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.model.storage.StorageProvider#removeFile(java.lang.Long)
     */
    @Override
    public void removeFile(Long fileId) throws StorageException {
        File superfluous = new File(storagePath, "DecidR_" + fileId + ".tmp");
        if (superfluous.exists()) {
            superfluous.delete();
        }
    }
}
