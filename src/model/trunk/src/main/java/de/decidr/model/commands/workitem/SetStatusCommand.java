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
import de.decidr.model.transactions.TransactionEvent;

/**
 * Sets the status of a work item to the given value. If the status is set to
 * "done", the Human Task Web Service is invoked and the work item properties
 * are sent back to the ODE. After the status has been set to "done", you cannot
 * set it to any other value (if you do, a {@link TransactionException} is
 * thrown).
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class SetStatusCommand extends WorkItemCommand {

    private WorkItemStatus newStatus;

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
     *             if the new status is null or if the work item ID is null
     */
    public SetStatusCommand(Role role, Long workItemId, WorkItemStatus newStatus) {
        super(role, workItemId);
        if (workItemId == null || newStatus == null) {
            throw new IllegalArgumentException(
                    "Work item ID and status must not be null.");
        }
        this.newStatus = newStatus;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        WorkItem workItem = fetchWorkItem(evt.getSession());

        WorkItemStatus status = WorkItemStatus.valueOf(workItem.getStatus());

        if (status.equals(WorkItemStatus.Done)) {
            // We're not allowed to transition from "done" to any other status!
            if (!status.equals(newStatus)) {
                throw new TransactionException(
                        "Cannot mark work item as 'not done' after it has been marked as 'done.'");
            } else {
                // Do no notify human task web service more than once.
            }
        } else {
            workItem.setStatus(newStatus.toString());
            evt.getSession().update(workItem);

            if (newStatus.equals(WorkItemStatus.Done)) {
                // Notifiy human task web service
                // FIXME DH if the status is "done", invoke Human Task WS
            }
        }
    }
}
