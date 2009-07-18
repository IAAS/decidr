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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.KnownWebService;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.entities.WorkflowModelIsDeployedOnServer;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionAbortedEvent;
import de.decidr.model.transactions.TransactionEvent;
import de.decidr.model.workflowmodel.deployment.DeployerImpl;
import de.decidr.model.workflowmodel.deployment.DeploymentResult;
import de.decidr.model.workflowmodel.deployment.StandardDeploymentStrategy;

/**
 * Deploys the given workflow model on the Apache ODE if it isn't already
 * deployed. The deployment result will be saved in the result variable.
 * 
 * @author Daniel Huss
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class DeployWorkflowModelCommand extends WorkflowModelCommand implements
        TransactionalCommand {

    private DeployedWorkflowModel newDeployedWorkflowModel;
    private DeploymentResult result;

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

    @SuppressWarnings("unchecked")
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
             */

            DeployerImpl dManager = new DeployerImpl();

            // Fill deployedWorkflowModel with data
            DeployedWorkflowModel dwfm = new DeployedWorkflowModel();
            dwfm.setOriginalWorkflowModel(workflowModel);
            dwfm.setTenant(workflowModel.getTenant());
            dwfm.setName(workflowModel.getName());
            dwfm.setDescription(workflowModel.getDescription());
            dwfm.setDwdl(workflowModel.getDwdl());
            dwfm.setVersion(workflowModel.getVersion());

            evt.getSession().save(dwfm);

            // Create ServerStatistics
            String hqlString = "from ServerLoadView";
            Query q2 = evt.getSession().createQuery(hqlString);
            List<ServerLoadView> serverStatistics = q2.list();

            // Create WebserviceInformation
            String hqlString2 = "from KnownWebService";
            Query q3 = evt.getSession().createQuery(hqlString2);
            List<KnownWebService> webservices = q3.list();

            // deploy it
            try {
                result = dManager.deploy(workflowModel.getDwdl(), webservices,
                        workflowModel.getTenant().getName(), serverStatistics,
                        new StandardDeploymentStrategy());

                // get servers on which it has been deployed

                List<Long> serverId = result.getServers();
                Set<WorkflowModelIsDeployedOnServer> dbEntry = new HashSet();

                for (Long sid : serverId) {

                    WorkflowModelIsDeployedOnServer entry = new WorkflowModelIsDeployedOnServer();
                    entry.setDeployedWorkflowModel(dwfm);
                    entry.setServer((Server) evt.getSession().load(
                            Server.class, sid));

                }

                dwfm.setDeployDate(result.getDoplementDate());

                dwfm.setSoapTemplate(result.getSOAPTemplate());

                // XXX stay empty at the moment
                // dwfm.setWsdl();

                dwfm.setWorkflowModelIsDeployedOnServers(dbEntry);

                newDeployedWorkflowModel = dwfm;

            } catch (Exception e) {
                throw new TransactionException(e);
            }

        }
    }

    @Override
    public void transactionAborted(TransactionAbortedEvent evt)
            throws TransactionException {

        DeployerImpl dManager = new DeployerImpl();

        for (Long serverId : result.getServers()) {

            try {
                dManager.undeploy(newDeployedWorkflowModel, (Server) evt
                        .getSession().load(Server.class, serverId));
            } catch (Exception e) {
                throw new TransactionException(e);
            }

        }

    }

    /**
     * @return the deployed workflow model
     */
    public DeployedWorkflowModel getDeployedWorkflowModel() {
        return deployedWorkflowModel;
    }

}
