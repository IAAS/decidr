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

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

import org.hibernate.Query;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.IncompleteConfigurationException;
import de.decidr.model.exceptions.StorageException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.storage.StorageProviderFactory;
import de.decidr.model.transactions.TransactionEvent;
import de.decidr.model.workflowmodel.deployment.DeployerImpl;

/**
 * Deletes all tenants which correspond to the given IDs including all
 * corresponding workflow instances and work items.
 * 
 * @author Markus Fischer
 * @version 0.1
 */
public class DeleteTenantCommand extends TenantCommand {

    private Long tenantId;

    /**
     * Creates a new DeleteTenantsCommand. This Command will delete all tenants
     * which corresponds to the given IDs including all corresponding
     * WorkflowInstances and workitems. all .
     * 
     * @param role
     *            the user which executes the command
     * @param tenantId
     *            the id of the tenant which should be deleted
     */
    public DeleteTenantCommand(Role role, Long tenantId) {
        super(role, null);
        // DH check for null tenant ID ~rr

        this.tenantId = tenantId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        Tenant tenant = (Tenant) evt.getSession().get(Tenant.class, tenantId);
        DeployerImpl deployer = new de.decidr.model.workflowmodel.deployment.DeployerImpl();

        if (tenant != null) {

            StorageProviderFactory factory;

            try {
                factory = StorageProviderFactory.getDefaultFactory();
            } catch (InvalidPropertiesFormatException e) {
                throw new TransactionException(e);
            } catch (IncompleteConfigurationException e) {
                throw new TransactionException(e);
            } catch (IOException e) {
                throw new TransactionException(e);
            }

            try {
                // DH I added some null checks to be able to continue unit test
                // implementation. Please review these changes ~rr
                if (tenant.getLogo() != null) {
                    // delete File logo (permanent storage)
                    factory.getStorageProvider().removeFile(
                            tenant.getLogo().getId());

                    // delete File logo (db representation)
                    evt.getSession().delete(tenant.getLogo());
                }

                if (tenant.getSimpleColorScheme() != null) {
                    // delete File simpleColorScheme (permanent storage)
                    factory.getStorageProvider().removeFile(
                            tenant.getSimpleColorScheme().getId());

                    // delete File simplecolorScheme (db representation)
                    evt.getSession().delete(tenant.getSimpleColorScheme());
                }

                if (tenant.getAdvancedColorScheme() != null) {
                    // delete File advancedColorScheme (permanent storage)
                    factory.getStorageProvider().removeFile(
                            tenant.getAdvancedColorScheme().getId());

                    // delete File advancedColorScheme (db representation)
                    evt.getSession().delete(tenant.getAdvancedColorScheme());
                }

                if (tenant.getCurrentColorScheme() != null) {
                    // delete File currentColorScheme (permanent storage)
                    factory.getStorageProvider().removeFile(
                            tenant.getCurrentColorScheme().getId());

                    // delete File currentColorScheme (db representation)
                    evt.getSession().delete(tenant.getCurrentColorScheme());
                }
            } catch (StorageException e) {
                throw new TransactionException(e);
            } catch (IncompleteConfigurationException e) {
                throw new TransactionException(e);
            }

            for (DeployedWorkflowModel model : tenant
                    .getDeployedWorkflowModels()) {
                // Undeploy models from server
                String hql = "select s from Server s join WorkflowModelIsDeployedOnServer rel "
                        + "where rel.server = s and rel.deployedWorkflowModel = :toUndeploy";

                Query q = evt.getSession().createQuery(hql);
                q.setEntity("toUndeploy", model);
                List<Server> result = q.list();

                for (Server server : result) {
                    try {
                        deployer.undeploy(model, server);
                    } catch (Exception e) {
                        throw new TransactionException(e);
                    }
                }

                /*
                 * corresponding WorkflowInstances and Workitems will be
                 * automatically deleted by the dbms.
                 */
                evt.getSession().delete(model);
            }

            // finally delete tenant object
            evt.getSession().delete(tenant);
        }
    }
}
