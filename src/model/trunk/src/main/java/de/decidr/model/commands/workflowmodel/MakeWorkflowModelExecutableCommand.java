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

import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Marks a workflow model as executable or not executable. If necessary, the
 * workflow model is made executable by deploying it on the Apache ODE.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class MakeWorkflowModelExecutableCommand extends WorkflowModelCommand
        implements TransactionalCommand {

    private Boolean executable;

    /**
     * Constructor.
     * 
     * @param role
     * @param workflowModelId
     */
    public MakeWorkflowModelExecutableCommand(Role role, Long workflowModelId,
            Boolean executable) {
        super(role, workflowModelId);
        this.executable = executable;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        WorkflowModel workflowModel = fetchWorkflowModel(evt.getSession());

        if (executable) {
            /*
             * We have to make sure there is a deployed version of the workflow
             * model.
             */
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    new DeployWorkflowModelCommand(role, workflowModelId));

        }
        workflowModel.setExecutable(executable);
    }
}
