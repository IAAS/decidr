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
package de.decidr.model.commands.tenant;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.File;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.WorkflowModelIsDeployedOnServer;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.storage.StorageProvider;
import de.decidr.model.storage.StorageProviderFactory;
import de.decidr.model.transactions.TransactionEvent;
import de.decidr.model.workflowmodel.deployment.Deployer;
import de.decidr.model.workflowmodel.deployment.DeployerImpl;

/**
 * Deletes a single tenant including all corresponding workflow instances and
 * work items.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class DeleteTenantCommand extends TenantCommand {

    /**
     * Creates a new DeleteTenantsCommand. This command will delete all tenants
     * which corresponds to the given IDs including all corresponding workflow
     * instances and work items.
     * 
     * @param role
     *            the user which executes the command
     * @param tenantId
     *            the id of the tenant which should be deleted
     */
    public DeleteTenantCommand(Role role, Long tenantId) {
        super(role, tenantId);
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        Tenant toDelete = (Tenant) evt.getSession().get(Tenant.class,
                this.getTenantId());
        if (toDelete != null) {
            try {
                // delete all files the tenant owns
                StorageProvider storage = StorageProviderFactory
                        .getDefaultFactory().getStorageProvider();

                File[] colorSchemes = { toDelete.getSimpleColorScheme(),
                        toDelete.getAdvancedColorScheme() };
                for (File colorScheme : colorSchemes) {
                    if (colorScheme != null) {
                        storage.removeFile(colorScheme.getId());
                    }
                }

                // undeploy all workflow models the tenant owns
                Deployer deployer = new DeployerImpl();
                for (DeployedWorkflowModel deployedModel : toDelete
                        .getDeployedWorkflowModels()) {

                    for (WorkflowModelIsDeployedOnServer relation : deployedModel
                            .getWorkflowModelIsDeployedOnServers()) {
                        deployer.undeploy(deployedModel, relation.getServer());
                    }
                }

                // now we should be good to delete the tenant entity, the rest
                // will cascade
                evt.getSession().delete(toDelete);
            } catch (Exception e) {
                if (e instanceof TransactionException) {
                    throw (TransactionException) e;
                } else {
                    throw new TransactionException(e);
                }
            }
        }
    }
}
