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
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Removes the given workflow model, but retains any deployed version of the
 * model as long as there are still running instances of that deployed model.
 * <p>
 * <b>This command assumes that the workflow model to delete has already been
 * undeployed from the Apache ODE.</b>
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class DeleteWorkflowModelCommand extends WorkflowModelCommand {

    /**
     * Constructor
     * 
     * @param role
     * @param workflowModelId
     *            id of the workflow model to delete.
     * @throws IllegalArgumentException
     *             if workflowModelId is <code>null</code>
     */
    public DeleteWorkflowModelCommand(Role role, Long workflowModelId) {
        super(role, workflowModelId);
        requireWorkflowModelId();
    }

    @Override
    public void transactionAllowed(TransactionStartedEvent evt)
            throws TransactionException {
        /*
         * Make sure the workflow model has been undeployed from the ODE. There
         * should be no deployed versions of this workflow model.
         */
        String hql = "select dwm.id from DeployedWorkflowModel dwm "
                + "where (dwm.originalWorkflowModel.id = :workflowModelId)";

        boolean deployedModelFound = evt.getSession().createQuery(hql).setLong(
                "workflowModelId", getWorkflowModelId()).setMaxResults(1)
                .uniqueResult() != null;

        if (deployedModelFound) {
            throw new TransactionException(
                    "You must undeploy a workflow model before deleting it.");
        }

        hql = "delete from WorkflowModel where id = :workflowModelId";

        evt.getSession().createQuery(hql).setLong("workflowModelId",
                getWorkflowModelId()).executeUpdate();
    }
}
