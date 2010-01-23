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

import javax.xml.bind.JAXBException;

import org.hibernate.Query;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.KnownWebService;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.entities.WorkflowModelIsDeployedOnServer;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionAbortedEvent;
import de.decidr.model.transactions.TransactionStartedEvent;
import de.decidr.model.workflowmodel.deployment.Deployer;
import de.decidr.model.workflowmodel.deployment.DeployerImpl;
import de.decidr.model.workflowmodel.deployment.DeploymentResult;
import de.decidr.model.workflowmodel.deployment.StandardDeploymentStrategy;
import de.decidr.model.workflowmodel.dwdl.Workflow;
import de.decidr.model.workflowmodel.dwdl.transformation.TransformUtil;

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

    private DeploymentResult result;

    private DeployedWorkflowModel deployedWorkflowModel = null;

    /**
     * @param role
     *            user / system executing the command
     * @param workflowModelId
     *            the workflow model to deploy.
     */
    public DeployWorkflowModelCommand(Role role, Long workflowModelId) {
        super(role, workflowModelId);
    }

    /**
     * @return the deployed workflow model
     */
    public DeployedWorkflowModel getDeployedWorkflowModel() {
        return deployedWorkflowModel;
    }

    @Override
    public void transactionAborted(TransactionAbortedEvent evt)
            throws TransactionException {
        // FIXME compensate for already deployed model by undeploying ~dh
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionStartedEvent evt)
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
        q.setMaxResults(1);

        DeployedWorkflowModel existing = (DeployedWorkflowModel) q
                .uniqueResult();

        if (existing == null) {

            /*
             * There is no current deployed version of our workflow model, so we
             * have to deploy it now.
             */
            Deployer deployer = new DeployerImpl();

            // Fill deployedWorkflowModel with data
            DeployedWorkflowModel dwfm = new DeployedWorkflowModel();
            dwfm.setOriginalWorkflowModel(workflowModel);
            dwfm.setTenant(workflowModel.getTenant());
            dwfm.setName(workflowModel.getName());
            dwfm.setDescription(workflowModel.getDescription());
            dwfm.setDwdl(new byte[0]);
            dwfm.setVersion(workflowModel.getVersion());
            // XXX workflow model wsdl is not used, yet.
            dwfm.setWsdl(new byte[0]);
            dwfm.setSoapTemplate(new byte[0]);
            dwfm.setDeployDate(DecidrGlobals.getTime().getTime());

            evt.getSession().save(dwfm);
            try {
                // Update DWDL
                Workflow dwdl = TransformUtil.bytesToWorkflow(workflowModel
                        .getDwdl());
                dwdl.setId(dwfm.getId());
                dwdl.setTargetNamespace(DecidrGlobals
                        .getWorkflowTargetNamespace(dwfm.getId(), dwfm
                                .getTenant().getName()));
                dwfm.setDwdl(TransformUtil.workflowToBytes(dwdl));
            } catch (JAXBException e) {
                throw new TransactionException(e);
            }

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
                result = deployer.deploy(workflowModel.getDwdl(), webservices,
                        workflowModel.getTenant().getName(), serverStatistics,
                        new StandardDeploymentStrategy());

                // get servers on which it has been deployed
                List<Long> serverId = result.getServers();
                Set<WorkflowModelIsDeployedOnServer> dbEntry = new HashSet<WorkflowModelIsDeployedOnServer>();

                for (Long sid : serverId) {
                    WorkflowModelIsDeployedOnServer entry = new WorkflowModelIsDeployedOnServer();
                    entry.setDeployedWorkflowModel(dwfm);
                    entry.setServer((Server) evt.getSession().get(Server.class,
                            sid));
                }

                dwfm.setDeployDate(DecidrGlobals.getTime().getTime());
                dwfm.setSoapTemplate(result.getSOAPTemplate());
                if (dwfm.getSoapTemplate() == null) {
                    dwfm.setSoapTemplate(new byte[0]);
                }
                dwfm.setWorkflowModelIsDeployedOnServers(dbEntry);
            } catch (Exception e) {
                throw new TransactionException(e);
            }

            // write changes to DB
            evt.getSession().update(dwfm);
        }
    }
}