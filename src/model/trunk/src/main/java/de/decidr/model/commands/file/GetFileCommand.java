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

import de.decidr.model.acl.permissions.FileReadPermission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.File;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.storage.StorageProvider;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Retrieves file meta-information from the database. To retrieve the actual
 * file contents, see {@link StorageProvider}.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class GetFileCommand extends FileCommand {

    private File file = null;

    /**
     * Creates a new GetFileCommand
     * 
     * @param role
     *            the user / system executing the command
     * @param fileId
     *            the file to retrieve
     */
    public GetFileCommand(Role role, Long fileId) {
        super(role, fileId);
        this.additionalPermissions.add(new FileReadPermission(fileId));
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        file = fetchFile(evt.getSession());

    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }
}
