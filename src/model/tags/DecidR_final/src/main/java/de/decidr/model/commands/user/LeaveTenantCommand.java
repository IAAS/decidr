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

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Dissociates a user from a tenant.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class LeaveTenantCommand extends UserCommand {

    protected Long tenantId;

    protected Boolean leftTenant = false;

    protected Tenant tenant = null;

    protected User user = null;

    /**
     * Creates a new LeaveTenantCommand.
     * 
     * @param role
     *            user which executes the command
     * @param userId
     *            the ID of the user which wants to leave the tenant
     * @param tenantId
     *            tenant to leave
     * @throws IllegalArgumentException
     *             if userId or tenantId is <code>null</code>
     */
    public LeaveTenantCommand(Role role, Long userId, Long tenantId) {
        super(role, userId);
        requireUserId();
        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID must not be null.");
        }
        this.tenantId = tenantId;
    }

    /**
     * @return true iff the tenant was successfully left.
     */
    public Boolean getLeftTenant() {
        return leftTenant;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        leftTenant = false;

        user = fetchUser(evt.getSession());

        // does the tenant exist?
        tenant = (Tenant) evt.getSession().get(Tenant.class, tenantId);

        if (tenant == null) {
            throw new EntityNotFoundException(Tenant.class, tenantId);
        }

        // is the user the tenant admin? Tenant admins may not leave their
        // tenant (yet).
        if (user.getId().equals(tenant.getAdmin().getId())) {
            leftTenant = false;
            return;
        } else {
            // dissociate from workflow models of the tenant
            String hql = "delete from UserAdministratesWorkflowModel "
                    + "where user = :user and workflowModel.id in "
                    + "(select wfm.id from WorkflowModel wfm "
                    + "where wfm.tenant = :tenant)";
            evt.getSession().createQuery(hql).setEntity("user", user)
                    .setEntity("tenant", tenant).executeUpdate();

            // dissociate from tenant
            hql = "delete from UserIsMemberOfTenant "
                    + "where (user = :user) and (tenant = :tenant)";
            int affectedRows = evt.getSession().createQuery(hql).setEntity(
                    "user", user).setEntity("tenant", tenant).executeUpdate();

            leftTenant = affectedRows > 0;
        }
    }
}
