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

package de.decidr.model.commands.user;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.acl.roles.TenantAdminRole;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.acl.roles.WorkflowAdminRole;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Saves the highest role of the user in the given tenant in the result
 * variable. If the user is not even a tenant member the result will be null.
 * <p>
 * <b>Special case:</b>
 * <p>
 * If tenantId references the default tenant, this method returns
 * <code>WorkflowAdminRole.class</code> unless userId references the superadmin:
 * in this case <code>SuperAdminRole.class</code> is returned.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class GetUserRoleForTenantCommand extends UserCommand {

    Long tenantId;
    Class<? extends UserRole> result;

    /**
     * Creates a new GetUserRoleForTenantCommand. This command saves the highest
     * role of the user in the given tenant in the result variable. If the user
     * is not even a tenant member the result will be null.
     * <p>
     * <b>Special case:</b>
     * <p>
     * If tenantId references the default tenant, this method returns
     * <code>WorkflowAdminRole.class</code> unless userId references the
     * superadmin: in this case <code>SuperAdminRole.class</code> is returned.
     * 
     * @param role
     *            the user which executes the command
     * @param userId
     *            the ID of the user whose role should be requested for the
     *            given tenant
     * @param tenantId
     *            the ID of the tenant for which the role should be appointed
     * @throws IllegalArgumentException
     *             if userId or tenantId is <code>null</code>
     */
    public GetUserRoleForTenantCommand(Role role, Long userId, Long tenantId) {
        super(role, userId);
        requireUserId();
        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID must not be null.");
        }
        this.tenantId = tenantId;
    }

    /**
     * @return the highest UserRole of the user for the given tenant
     */
    public Class<? extends UserRole> getResult() {
        return result;
    }

    @Override
    public void transactionAllowed(TransactionStartedEvent evt)
            throws TransactionException {
        result = null;

        // does the user exist?
        String hql = "select u.id from User u where u.id = :userId";
        Object found = evt.getSession().createQuery(hql).setLong("userId",
                getUserId()).setMaxResults(1).uniqueResult();

        if (found == null) {
            throw new EntityNotFoundException(User.class, getUserId());
        }

        // check if super admin
        SystemSettings settings = DecidrGlobals.getSettings();

        if (getUserId().equals(settings.getSuperAdmin().getId())) {
            result = SuperAdminRole.class;
            return;
        }

        // Everybody is a workflow admin in the default tenant (except the super
        // admin)
        if (tenantId == DecidrGlobals.DEFAULT_TENANT_ID) {
            result = WorkflowAdminRole.class;
            return;
        }

        // check if tenant admin
        Tenant tenant = (Tenant) evt.getSession().get(Tenant.class, tenantId);
        if (tenant == null) {
            throw new EntityNotFoundException(Tenant.class, tenantId);
        }

        if (getUserId().equals(tenant.getAdmin().getId())) {
            result = TenantAdminRole.class;
            return;
        }

        // check if workflow admin
        hql = "select u.id from User u "
                + "where u.id = :userId and "
                + "exists(from UserAdministratesWorkflowInstance relWi "
                + "where relWi.user = u and "
                + "relWi.workflowInstance.deployedWorkflowModel.tenant.id = :tenantId) or "
                + "exists(from UserAdministratesWorkflowModel relWm "
                + "where relWm.user = u and relWm.workflowModel.tenant.id = :tenantId)";

        found = evt.getSession().createQuery(hql).setLong("tenantId", tenantId)
                .setLong("userId", getUserId()).setMaxResults(1).uniqueResult();

        if (found != null) {
            result = WorkflowAdminRole.class;
            return;
        }

        // check if member
        hql = "select rel.user.id from UserIsMemberOfTenant rel where "
                + "rel.user.id = :userId and rel.tenant.id = :tenantId";

        found = evt.getSession().createQuery(hql)
                .setLong("userId", getUserId()).setLong("tenantId", tenantId)
                .setMaxResults(1).uniqueResult();

        if (found != null) {
            result = UserRole.class;
            return;
        }

        // user is not a tenant member
        result = null;
    }
}
