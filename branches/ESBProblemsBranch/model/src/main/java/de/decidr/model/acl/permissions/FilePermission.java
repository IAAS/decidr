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
 * Represents the permission to read from or write to a file.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public abstract class FilePermission extends EntityPermission {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new FilePermission.
     * 
     * @param fileId
     *            the ID of the accessed file.
     */
    public FilePermission(String prefix, Long fileId) {
        super(
                File.class.getName()
                        + ((prefix != null && !prefix.isEmpty()) ? ("." + prefix)
                                : ""), fileId);
    }

}
