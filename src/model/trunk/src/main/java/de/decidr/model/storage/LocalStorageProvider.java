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
 * TODO: add comment
 * 
 * @author Reinhold
 */
public class LocalStorageProvider implements StorageProvider {

    boolean local = true, amazonS3 = false, persistent = false;
    String[] protocols = { "file" };

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.model.storage.StorageProvider#applyConfig(java.util.Properties)
     */
    @Override
    public void applyConfig(Properties config)
            throws IncompleteConfigurationException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.model.storage.StorageProvider#getFile(java.lang.Long)
     */
    @Override
    public InputStream getFile(Long fileId) throws StorageException {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return false;
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
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.model.storage.StorageProvider#removeFile(java.lang.Long)
     */
    @Override
    public void removeFile(Long fileId) throws StorageException {
        // TODO Auto-generated method stub

    }

}
