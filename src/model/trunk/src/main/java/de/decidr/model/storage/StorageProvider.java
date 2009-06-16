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

import de.decidr.model.exceptions.StorageException;

/**
 * 
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public interface StorageProvider {

    /**
     * Creates or replaces the file that is identified by the given id on the
     * storage backend.
     */
    public void putFile(InputStream data, Long fileId) throws StorageException;
    
    /**
     * Retrieves the file that is identified by the given id from the storage
     * backend.
     * 
     * @param fileId
     * @return the file data
     */
    public InputStream getFile(Long fileId) throws StorageException;

    /**
     * Permanently removes the file that is identified by the given id from the
     * storage backend.
     * 
     * @param fileId
     */
    public void removeFile(Long fileId) throws StorageException;
}
