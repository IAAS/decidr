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
package de.decidr.model.commands.workitem;

import org.hibernate.Session;

import de.decidr.model.acl.access.WorkItemAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.WorkItem;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;

/**
 * Abstract base class for commands that modify work items.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public abstract class WorkItemCommand extends AclEnabledCommand implements
        WorkItemAccess {

    protected Long workItemId = null;

    /**
     * Creates a new {@link WorkItemCommand}
     * 
     * @param role
     *            user / system executing the command
     * @param workItemId
     */
    public WorkItemCommand(Role role, Long workItemId) {
        super(role, (Permission) null);
        this.workItemId = workItemId;
    }

    /**
     * Fetches the work item identified by this.workItemId from the database.
     * 
     * @param session
     *            current Hibernate session
     * @return the work item identified by this.workItemId
     * @throws EntityNotFoundException
     *             if the work item does not exist
     */
    public WorkItem fetchWorkItem(Session session) throws TransactionException {
        WorkItem result = (WorkItem) session.get(WorkItem.class, workItemId);
        if (result == null) {
            throw new EntityNotFoundException(WorkItem.class, workItemId);
        }
        return result;
    }

    /**
     * @return the workItemId
     */
    public Long getWorkItemId() {
        return workItemId;
    }

    /**
     * @return the workItemIds
     */
    public Long[] getWorkItemIds() {
        Long[] result = { workItemId };
        return result;
    }

    /**
     * Throws an {@link IllegalArgumentException} if the current work item ID is
     * <code>null</code>.
     * 
     * @throws IllegalArgumentException
     */
    protected void requireWorkItemId() {
        if (workItemId == null) {
            throw new IllegalArgumentException("Work item ID must not be null.");
        }
    }
}