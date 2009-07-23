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

import de.decidr.model.entities.WorkItem;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Retrieves the properties of the given work item.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class GetWorkItemCommand extends WorkItemCommand {

    private WorkItem result = null;

    /**
     * Constructor TODO document
     * 
     * @param role
     *            TODO document
     * @param workItemId
     *            TODO document
     */
    public GetWorkItemCommand(Role role, Long workItemId) {
        super(role, workItemId);
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        result = (WorkItem) evt.getSession().get(WorkItem.class, workItemId);

        if (result != null) {
            // make sure the required properties are loaded.
            result.getData();
            result.getWorkflowInstance();
        } else {
            throw new EntityNotFoundException(WorkItem.class, workItemId);
        }
    }

    /**
     * @return the result TODO document
     */
    public WorkItem getResult() {
        return result;
    }
}
