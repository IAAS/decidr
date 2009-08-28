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

import java.util.List;

import de.decidr.model.notifications.NotificationEvents;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Rejects all tenants which corresponds to the given IDs (tenants will be
 * deleted).
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class RejectTenantsCommand extends AclEnabledCommand {

    private List<Long> tenantIds;

    /**
     * Creates a new RejectTenantsCommand. This Command will reject all
     * tenants which corresponds to the given IDs (tenants will be deleted).
     * 
     * @param role
     *            the user which executes the command
     * @param tenantIds
     *            list of tenantIds which should be rejected
     */
    public RejectTenantsCommand(Role role, List<Long> tenantIds) {
        super(role, (Permission) null);

        this.tenantIds = tenantIds;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        for (Long tenantid : tenantIds) {
            Tenant tenant = (Tenant) evt.getSession().get(Tenant.class,
                    tenantid);
            if (tenant != null) {
                if (tenant.getApprovedSince() == null) {
                    NotificationEvents.rejectedTenant(tenant.getAdmin(),
                            tenant.getName());
                    evt.getSession().delete(tenant);
                }
            }
        }
    }
}
