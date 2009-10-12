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
package de.decidr.model.acl.permissions;

import de.decidr.model.entities.File;

/**
 * Represents the permission to read a file.
 * 
 * @author Markus Fischer
 * @version 0.1
 */
public class FileReadPermission extends FilePermission {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new FileReadPermission.
     * 
     * @param fileId
     *            ID of file to read.
     */
    public FileReadPermission(Long fileId) {
        super(File.class.getName() + "read", fileId);
    }

}