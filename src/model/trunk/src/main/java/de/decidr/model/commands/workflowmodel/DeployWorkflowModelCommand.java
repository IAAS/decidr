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

import org.hibernate.Query;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionAbortedEvent;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Deploys the given workflow model on the Apache ODE if it isn't already
 * deployed.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class DeployWorkflowModelCommand extends WorkflowModelCommand implements
        TransactionalCommand {

    private DeployedWorkflowModel deployedWorkflowModel = null;

    /**
     * 
     * @param role
     * @param workflowModelId
     *            the workflow model to deploy.
     */
    public DeployWorkflowModelCommand(Role role, Long workflowModelId) {
        super(role, workflowModelId);
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        /*
         * Find the existing deployed workflow model.
         */
        WorkflowModel workflowModel = fetchWorkflowModel(evt.getSession());

        Query q = evt.getSession().createQuery(
                "from DeployedWorkflowModel where (version = :version)"
                        + "and (originalWorkflowModel = :original)");

        q.setLong("version", workflowModel.getVersion());
        q.setEntity("original", workflowModel);

        DeployedWorkflowModel existing = (DeployedWorkflowModel) q
                .uniqueResult();

        if (existing == null) {
            /*
             * There is no current deployed version of our workflow model, so we
             * have to deploy it now.
             * 
             * FIXME need deployment component to continue
             */
            existing = null;
        }

        deployedWorkflowModel = existing;
    }

    @Override
    public void transactionAborted(TransactionAbortedEvent evt)
            throws TransactionException {
        /*
         * TODO (low priority) add compensation action -> try to undeploy any
         * workflow models that have been deployed
         */
    }

    /**
     * @return the deployed workflow model
     */
    public DeployedWorkflowModel getDeployedWorkflowModel() {
        return deployedWorkflowModel;
    }

}
