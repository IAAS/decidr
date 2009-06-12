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

package de.decidr.model.commands.workflowmodel;

import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Removes all deployed versions of a workflow model from the Apache ODE except
 * those that still have running instances. This command will not fail if the
 * given workflow model does not exist.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class UndeployWorkflowModelCommand extends WorkflowModelCommand {

    public UndeployWorkflowModelCommand(Role role, Long workflowModelId) {
        super(role, workflowModelId);
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        WorkflowModel model = (WorkflowModel) evt.getSession().get(
                WorkflowModel.class, getWorkflowModelId());

        if (model != null) {
            model.getDeployedWorkflowModels();
            // DH undeploy them!
        } else {
            // there is nothing to undeploy
        }
    }

}
