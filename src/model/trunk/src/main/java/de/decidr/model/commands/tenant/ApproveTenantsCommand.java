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

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Approves all tenants which corresponds to the given IDs.<br>
 * Non-existing tenants will we ignored (no exception is thrown).
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class ApproveTenantsCommand extends AclEnabledCommand {

    private List<Long> tenantIds;

    /**
     * Creates a new ApproveTenantsCommand. This Command will approve all
     * tenants which corresponds to the given IDs. Not existing tenants will we
     * ignored (no exception is thrown).
     * 
     * @param role
     *            the user/system which executes the command
     * @param tenantIds
     *            list of IDs of tenants which should be approved
     */
    public ApproveTenantsCommand(Role role, List<Long> tenantIds) {
        super(role, (Permission) null);

        if (tenantIds == null) {
            throw new IllegalArgumentException(
                    "List of tenant ids cannot be null");
        }

        this.tenantIds = tenantIds;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        if (!tenantIds.isEmpty()) {
            String hql = "update Tenant set approvedSince = :now where id in (:tenantIds)";

            evt.getSession().createQuery(hql).setDate("now",
                    DecidrGlobals.getTime().getTime()).setParameterList(
                    "tenantIds", tenantIds).executeUpdate();
        }
    }
}
