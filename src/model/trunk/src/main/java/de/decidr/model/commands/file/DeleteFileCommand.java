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

import de.decidr.model.acl.roles.Role;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Deletes a file. The file contents is also removed using the default storage
 * provider. If the file does not exist, no exception is thrown.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class DeleteFileCommand extends FileCommand {

    protected Boolean deleted = false;
    protected Integer deletedEntities = 0;

    /**
     * Creates a new DeleteFileCommand that deletes the file identified by
     * fileId if it exists. The file contents is also deleted using the default
     * storage provider.
     * 
     * @param role
     *            the user / system executing the command.
     * @param fileId
     *            the ID of the file to delete.
     */
    public DeleteFileCommand(Role role, Long fileId) {
        super(role, fileId);
        if (fileId == null) {
            throw new IllegalArgumentException("File ID must not be null.");
        }
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        deleted = false;
        deletedEntities = 0;

        String hql = "delete File f where f.id = :fileId";

        deletedEntities = evt.getSession().createQuery(hql).setLong("fileId",
                getFileId()).executeUpdate();
        deleted = true;
    }

    /**
     * @return the number of deleted file entities (0 or 1).
     */
    public Integer getDeletedEntities() {
        return deletedEntities;
    }

    /**
     * @return whether the file has been deleted. Does not indicate whether the
     *         file actually existed.
     */
    public Boolean isDeleted() {
        return deleted;
    }
}
