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

import org.apache.axis2.AxisFault;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;
import de.decidr.model.workflowmodel.instancemanagement.InstanceManager;
import de.decidr.model.workflowmodel.instancemanagement.InstanceManagerImpl;

/**
 * 
 * Stops a given workflow instance
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class StopWorkflowInstanceCommand extends WorkflowInstanceCommand {

    /**
     * Creates a new StopWorkflowInstanceCommand that stops the given workflow
     * instance.
     * 
     * @param role
     *            user / system executing the command
     * @param workflowInstanceId
     *            workflow instance to stop.
     */
    public StopWorkflowInstanceCommand(Role role, Long workflowInstanceId) {
        super(role, null, workflowInstanceId);
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        InstanceManager instanceManager = new InstanceManagerImpl();

        try {
            instanceManager
                    .stopInstance(fetchWorkflowInstance(evt.getSession()));
        } catch (AxisFault e) {
            throw new TransactionException(e);
        }
    }

}
