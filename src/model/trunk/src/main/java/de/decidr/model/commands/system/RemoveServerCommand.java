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

package de.decidr.model.commands.system;

import java.util.List;
import java.util.Set;

import org.apache.axis2.AxisFault;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.stubs.DeployerImplStub;
import de.decidr.model.transactions.TransactionEvent;
import de.decidr.model.workflowmodel.deployment.Deployer;
import de.decidr.model.workflowmodel.instancemanagement.InstanceManager;
import de.decidr.model.workflowmodel.instancemanagement.InstanceManagerImpl;

/**
 * Removes the server from the database. Removing a server causes all workflow
 * instances that have been deployed on this server to be terminated and
 * removed. Workflow models that have been deployed on the affected server are
 * undeployed. The corresponding real server will not be shut down by this
 * command. If the server doesn't exist no exception is thrown.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class RemoveServerCommand extends SystemCommand {

    private Long serverId = null;

    /**
     * Creates a new RemoveServerCommand. The command removes the server from
     * the database. Removing a server causes all workflow instances that have
     * been deployed on this server to be terminated and removed. Workflow
     * models that have been deployed on the affected server are undeployed. The
     * corresponding real server will not be shut down by this command. If the
     * server doesn't exist no exception is thrown.
     * 
     * @param role
     *            the user/system that executes the command
     * @param serverId
     *            the id of the server to remove from the database
     */
    public RemoveServerCommand(Role role, Long serverId) {
        super(role, null);
        if (serverId == null) {
            throw new IllegalArgumentException("Server ID must not be null.");
        }
        this.serverId = serverId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        Server toDelete = (Server) evt.getSession().get(Server.class, serverId);
        if (toDelete == null) {
            // server not found, nothing to do
            return;
        }
        
        // terminate workflow instances
        InstanceManager instanceManager = new InstanceManagerImpl();
        Set<WorkflowInstance> instances = toDelete.getWorkflowInstances();
        for (WorkflowInstance toTerminate : instances) {
            try {
                instanceManager.stopInstance(toTerminate);
            } catch (AxisFault e) {
                throw new TransactionException(e);
            }
        }

        // undeploy workflow models
        Deployer modelDeployer = new DeployerImplStub();
        String hql = "from DeployedWorkflowModel dwm where "
                + "exists(from WorkflowModelIsDeployedOnServer rel "
                + "where rel.server = :toDelete)";
        List<DeployedWorkflowModel> models = evt.getSession().createQuery(hql)
                .setEntity("toDelete", toDelete).list();

        for (DeployedWorkflowModel toUndeploy : models) {
            try {
                modelDeployer.undeploy(toUndeploy, toDelete);
            } catch (AxisFault e) {
                throw new TransactionException(e);

            }
        }

        // finally remove the server (workflow instances and deployed model
        // relations are deleted by cascade)
        evt.getSession().delete(toDelete);
    }
}
