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
package de.decidr.model.commands.workflowinstance;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Deletes a workflow instance including all work items from the database. No
 * communication with the ODE takes place within this command.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class DeleteWorkflowInstanceCommand extends WorkflowInstanceCommand {

    /**
     * Creates a new {@link DeleteWorkflowInstanceCommand} that deletes a
     * workflow instance including all work items from the database. No
     * communication with the ODE takes place within this command.
     * 
     * @param role
     *            user / system executing the command
     * @param workflowInstanceId
     *            ID of workflow instance to delete
     */
    public DeleteWorkflowInstanceCommand(Role role, Long workflowInstanceId) {
        super(role, null, workflowInstanceId);
    }

    @Override
    public void transactionAllowed(TransactionStartedEvent evt)
            throws EntityNotFoundException {
        WorkflowInstance instance = fetchWorkflowInstance(evt.getSession());
        evt.getSession().delete(instance);
    }
}
