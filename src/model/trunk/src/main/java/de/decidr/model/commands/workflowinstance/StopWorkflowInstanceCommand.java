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
import de.decidr.model.transactions.TransactionEvent;
import de.decidr.model.workflowmodel.instancemanagement.InstanceManager;
import de.decidr.model.workflowmodel.instancemanagement.InstanceManagerImpl;

/**
 * 
 * Stops the workflow instance which corresponds to the given id.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class StopWorkflowInstanceCommand extends WorkflowInstanceCommand {

    public StopWorkflowInstanceCommand(Role role, Long workflowInstanceId) {
        super(role, null, workflowInstanceId);

    }

    @Override
    public void transactionAllowed(TransactionEvent evt) {

        InstanceManager iManager = new InstanceManagerImpl();

        iManager.stopInstance((WorkflowInstance) evt.getSession().load(
                WorkflowInstance.class, this.getWorkflowInstanceIds()[0]));

    }

}
