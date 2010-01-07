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
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;
import de.decidr.model.workflowmodel.instancemanagement.InstanceManagerImpl;

/**
 * Retrieves the properties of the given work item, including the following
 * properties which are normally not available due to lazy loading:
 * 
 * <ul>
 * <li>deployedWorkflowModel</li>
 * </ul>
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class GetWorkItemCommand extends WorkItemCommand {

    private WorkItem result = null;

    /**
     * Creates a new GetWorkItemCommand that retrieves the properties of the
     * given work item.
     * 
     * @param role
     *            user / system which executes the command
     * @param workItemId
     *            the ID of the workitem which should be returned
     * @throws IllegalArgumentException
     *             if workItemId is <code>null</code>.
     */
    public GetWorkItemCommand(Role role, Long workItemId) {
        super(role, workItemId);
        requireWorkItemId();
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        result = fetchWorkItem(evt.getSession());

        // make sure the required properties are loaded.
        result.getData();
        result.getWorkflowInstance().getDeployedWorkflowModel();
    }

    /**
     * @return the corresponding workitem to the given ID
     */
    public WorkItem getResult() {
        return result;
    }
}
