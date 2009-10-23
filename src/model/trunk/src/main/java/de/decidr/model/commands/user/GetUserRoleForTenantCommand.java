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

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.acl.roles.TenantAdminRole;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.acl.roles.WorkflowAdminRole;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserIsMemberOfTenant;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Saves the highest role of the user in the given tenant in the result
 * variable. If the user is not even a tenant member the result will be null.
 * 
 * @author Markus Fischer
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
     * 
     * @param role
     *            the user which executes the command
     * @param userId
     *            the ID of the user whose role should be requested for the
     *            given tenant
     * @param tenantId
     *            the ID of the tenant for which the role should be appointed
     */
    public GetUserRoleForTenantCommand(Role role, Long userId, Long tenantId) {
        super(role, userId);

        this.tenantId = tenantId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        // check if super admin
        Criteria c = evt.getSession().createCriteria(SystemSettings.class);
        List<SystemSettings> results = c.list();
        SystemSettings innerResult;

        if (results.size() > 0) {
            throw new TransactionException(
                    "More than one system settings found, but system settings should be unique");
        } else if (results.size() == 0) {
            throw new EntityNotFoundException(SystemSettings.class);
        } else {
            innerResult = results.get(0);
        }

        if (getUserId() == innerResult.getSuperAdmin().getId()) {
            result = SuperAdminRole.class;
            return;
        }

        // check if tenant admin

        Tenant tenant = (Tenant) evt.getSession().load(Tenant.class, tenantId);

        if (tenant.getAdmin().getId() == getUserId()) {
            result = TenantAdminRole.class;
            return;
        }

        // check if workflow admin

        User user = new User();
        user.setId(getUserId());

        Criteria c2 = evt.getSession().createCriteria(
                "UserAdministratesWorkflowInstance");
        c2.add(Restrictions.eq("user", user));

        Query q = evt
                .getSession()
                .createQuery(
                        "select count(*) from UserAdministratesWorkflowInstance a where a.deployedWorkflowModel.tenant.id = :tenantId");
        q.setLong("tenantId", tenantId);

        Number count = (Number) q.uniqueResult();

        if (count == null) {
            throw new TransactionException("Query didn't return a result.");
        } else if (count.intValue() > 0) {
            result = WorkflowAdminRole.class;
            return;
        }

        // check if member
        Criteria c3 = evt.getSession().createCriteria(
                UserIsMemberOfTenant.class).add(Restrictions.eq("user", user))
                .add(Restrictions.eq("tenant", tenant));

        if (!(c3.list().isEmpty())) {
            result = UserRole.class;
            return;
        }

        // if user has no role
        result = null;
    }

    /**
     * @return the highest UserRole of the user for the given tenant
     */
    public Class<? extends UserRole> getResult() {
        return result;
    }
}
