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

package de.decidr.model.acl.asserters;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.access.TenantAccess;
import de.decidr.model.acl.permissions.CommandPermission;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.WorkflowAdminRole;
import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Asserts that a user is a workflow admin within the given tenant(s).
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class UserIsWorkflowAdminWithinTenantAsserter extends
        AbstractTransactionalCommand implements Asserter {

    WorkflowAdminRole role = null;
    Long[] accessedTenantIds = null;
    boolean isWorkflowAdmin = false;

    @Override
    public boolean assertRule(Role role, Permission permission)
            throws TransactionException {
        boolean result = false;

        if (permission instanceof CommandPermission
                && role instanceof WorkflowAdminRole
                && role.getActorId() != null
                && ((CommandPermission) permission).getCommand() instanceof TenantAccess) {
            this.role = (WorkflowAdminRole) role;
            this.accessedTenantIds = ((TenantAccess) ((CommandPermission) permission)
                    .getCommand()).getTenantIds();
            HibernateTransactionCoordinator.getInstance().runTransaction(this);
            result = isWorkflowAdmin;
        }

        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt)
            throws TransactionException {
        isWorkflowAdmin = false;
        if (accessedTenantIds == null || accessedTenantIds.length == 0) {
            /*
             * Since there are no tenants to check against we can leave early.
             */
            return;
        }

        /*
         * A user is a workflow admin if he administers at least one workflow
         * model within the tenant(s) or if he is the tenant admin.
         * 
         * A special case is the default tenant: all users are workflow
         * administrators within the default tenant.
         */
        String hql = "select count(*) from Tenant t "
                + "where (t.id in (:tenantIds)) and "
                + "((t.id = :defaultTenantId) or (t.admin.id = :userId) or "
                + "exists(from UserAdministratesWorkflowModel rel "
                + "where rel.user = :userId and rel.workflowModel.tenant = t) )";

        Number count = (Number) evt.getSession().createQuery(hql).setLong(
                "userId", role.getActorId()).setParameterList("tenantIds",
                accessedTenantIds).setLong("defaultTenantId",
                DecidrGlobals.DEFAULT_TENANT_ID).uniqueResult();

        isWorkflowAdmin = count.intValue() == accessedTenantIds.length;
    }

}
