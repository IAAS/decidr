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

import de.decidr.model.acl.permissions.FileReplacePermission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.File;
import de.decidr.model.entities.WorkItemContainsFile;
import de.decidr.model.entities.WorkItemContainsFileId;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Associates a file with a work item, establishing a "work item contains file"
 * relation. Also removes the temporary flag of the file, making the file
 * persistent.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class AssociateFileWithWorkItemCommand extends FileCommand {

    private Long workItemId;

    /**
     * Creates a new AssociateFileWithWorkItemCommand that associates and
     * persists the given file.
     * 
     * @param role
     *            user / system executing the command.
     * @param fileId
     *            file to persist.
     * @param workItemId
     *            work item to associate with.
     * @throws IllegalArgumentException
     *             if fileId or workItemId is <code>null</code>
     */
    public AssociateFileWithWorkItemCommand(Role role, Long fileId,
            Long workItemId) {
        super(role, fileId);
        if ((fileId == null) || (workItemId == null)) {
            throw new IllegalArgumentException(
                    "File ID and work item ID must not be null.");
        }
        this.workItemId = workItemId;
        additionalPermissions.add(new FileReplacePermission(fileId));
    }

    @Override
    public void transactionAllowed(TransactionStartedEvent evt)
            throws TransactionException {

        // persist file
        String hql = "update File set temporary = false where id = :fileId";
        if (evt.getSession().createQuery(hql).setLong("fileId", getFileId())
                .executeUpdate() == 0) {
            throw new EntityNotFoundException(File.class, getFileId());
        }

        // associate with work item
        WorkItemContainsFile rel = new WorkItemContainsFile();
        rel.setId(new WorkItemContainsFileId(workItemId, getFileId()));

        evt.getSession().saveOrUpdate(rel);
    }

}
