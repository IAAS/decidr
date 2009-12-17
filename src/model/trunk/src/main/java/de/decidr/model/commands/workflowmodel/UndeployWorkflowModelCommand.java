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

import java.util.List;
import java.util.Set;

import org.apache.axis2.AxisFault;
import org.hibernate.Session;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.stubs.DeployerImplStub;
import de.decidr.model.transactions.TransactionEvent;
import de.decidr.model.workflowmodel.deployment.Deployer;

/**
 * Removes all deployed versions of a workflow model from the Apache ODE except
 * those that still have running instances.
 * 
 * @author Daniel Huss
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class UndeployWorkflowModelCommand extends WorkflowModelCommand {

    /**
     * 
     * Creates a new UndeployWorkflowModelCommand. This command removes all
     * deployed versions of a workflow model from the Apache ODE except those
     * that still have running instances.
     * 
     * @param role
     *            user /system executing the command
     * @param workflowModelId
     *            ID of workflow model to undeploy
     * @throws IllegalArgumentException
     *             if workflowModelId is <code>null</code>.
     */
    public UndeployWorkflowModelCommand(Role role, Long workflowModelId) {
        super(role, workflowModelId);
        requireWorkflowModelId();
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        WorkflowModel model = fetchWorkflowModel(evt.getSession());

        if (model != null) {
            Set<DeployedWorkflowModel> models = model
                    .getDeployedWorkflowModels();

            for (DeployedWorkflowModel m : models) {
                boolean hasInstances = evt.getSession().createQuery(
                        "select wfi.id from WorkflowInstance wfi "
                                + "where wfi.deployedWorkflowModel = :model")
                        .setEntity("model", m).setMaxResults(1).uniqueResult() != null;

                if (!hasInstances) {
                    undeploy(m, evt.getSession());
                }
            }
        }
    }

    /**
     * Undeploys a workflow model from all servers it has been deployed on.
     * 
     * @param model
     *            to undeploy
     * @param session
     *            current Hibernate session
     * @throws TransactionException
     *             if undeploying the workflow model fails.
     */
    @SuppressWarnings("unchecked")
    private void undeploy(DeployedWorkflowModel model, Session session)
            throws TransactionException {
        Deployer deployer = new DeployerImplStub();
        List<Server> servers = session.createQuery(
                "select rel.server "
                        + "from WorkflowModelIsDeployedOnServer rel "
                        + "where rel.workflowModel = :model").setEntity(
                "model", model).list();

        for (Server server : servers) {
            try {
                deployer.undeploy(model, server);
            } catch (AxisFault e) {
                throw new TransactionException(e);
            }
        }
    }
}
