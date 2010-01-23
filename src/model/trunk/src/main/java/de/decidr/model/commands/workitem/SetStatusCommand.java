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

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.WorkItem;
import de.decidr.model.enums.WorkItemStatus;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Sets the status of a work item to the given value. This command does not
 * invoke the HumanTask WS, but can be used from within the HumanTask WS to set
 * the status of a {@link WorkItem}.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class SetStatusCommand extends WorkItemCommand {

    private WorkItemStatus newStatus;
    private WorkItem foundWorkItem;

    /**
     * Creates a new instance of the SetStatusCommand.
     * 
     * @param role
     *            user who executes the command
     * @param workItemId
     *            the ID of the workitem whose status should be set
     * @param newStatus
     *            the new status as {@link WorkItemStatus}
     * @throws IllegalArgumentException
     *             if the new status is <code>null</code> or if the work item ID
     *             is <code>null</code>
     */
    public SetStatusCommand(Role role, Long workItemId, WorkItemStatus newStatus) {
        super(role, workItemId);
        requireWorkItemId();
        if (newStatus == null) {
            throw new IllegalArgumentException(
                    "Work item status must not be null.");
        }
        this.newStatus = newStatus;
    }

    @Override
    public void transactionAllowed(TransactionStartedEvent evt)
            throws TransactionException {
        foundWorkItem = fetchWorkItem(evt.getSession());

        WorkItemStatus status = WorkItemStatus.valueOf(foundWorkItem
                .getStatus());

        if (status.equals(WorkItemStatus.Done) && !status.equals(newStatus)) {
            // We're not allowed to transition from "done" to any other status!
            throw new TransactionException(
                    "Cannot mark work item as 'not done' after it has been marked as 'done.'");
        } else {
            foundWorkItem.setStatus(newStatus.toString());
            evt.getSession().update(foundWorkItem);
        }
    }
}
