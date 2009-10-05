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
package de.decidr.model.commands.file;

import org.hibernate.Session;

import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.File;
import de.decidr.model.exceptions.EntityNotFoundException;

/**
 * Abstract base class for commands that read from / write to the {@link File}
 * entity.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public abstract class FileCommand extends AclEnabledCommand {

    /**
     * ID of the file that is being accessed.
     */
    protected Long fileId;

    /**
     * Creates a new FileCommand that will access the file identified by fileId.
     * 
     * @param role
     *            user / system executing the command
     * @param fileId
     *            ID of the file that is accessed.
     */
    public FileCommand(Role role, Long fileId) {
        super(role, (Permission) null);
    }

    /**
     * Retrieves the file identified by the current file ID from the database.
     * 
     * @param session
     *            the current Hibernate session.
     * @return the file identified by the current file ID (never null)
     * @throws EntityNotFoundException
     *             iff the file with the current file ID cannot be found.
     */
    protected File fetchFile(Session session) throws EntityNotFoundException {
        File result = (File) session.get(File.class, fileId);

        if (result == null) {
            throw new EntityNotFoundException(File.class, fileId);
        }

        return result;
    }

    /**
     * @return the fileId
     */
    public Long getFileId() {
        return fileId;
    }

}
