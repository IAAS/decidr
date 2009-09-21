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

import java.util.HashSet;
import java.util.Set;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.WorkItem;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Writes all Workitems of the given WorkflowInstance in the result variable
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class GetAllWorkitemsCommand extends WorkflowInstanceCommand {

    @SuppressWarnings("unchecked")
    private Set<WorkItem> result = new HashSet();

    /**
     * 
     * Creates a new GetAllWorkitemsCommand.
     * 
     * @param role
     *            the user which want to execute the command
     * @param WorkflowInstanceId
     *            the id of the corresponding WorkflowInstance
     */
    public GetAllWorkitemsCommand(Role role, Long WorkflowInstanceId) {
        super(role, null, WorkflowInstanceId);
    }

    @Override
    public void transactionAllowed(TransactionEvent evt) {

        WorkflowInstance instance = (WorkflowInstance) evt.getSession().load(
                WorkflowInstance.class, this.getWorkflowInstanceIds()[0]);

        result = instance.getWorkItems();
    }

    public Set<WorkItem> getResult() {
        return result;
    }

}