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
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Saves the highest role of the given user in the result variable.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class GetHighestUserRoleCommand extends UserCommand {

    Class<? extends UserRole> result;

    /**
     * Creates a new GetHighestUserRoleCommand. This command saves the highest
     * role of the given user in the result variable.
     * 
     * @param role
     *            user which executes the command
     * @param userId
     *            the ID of the user whose highest role should be requested
     * @throws IllegalArgumentException
     *             if userId is <code>null</code>.
     */
    public GetHighestUserRoleCommand(Role role, Long userId) {
        super(role, userId);
        requireUserId();
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        // check if super admin
        SystemSettings settings = DecidrGlobals.getSettings();
        if (getUserId().equals(settings.getSuperAdmin().getId())) {
            result = SuperAdminRole.class;
            return;
        }

        // check if user is tenant admin
        Object id = evt.getSession().createQuery(
                "select a.id from Tenant a where a.admin.id = :userId")
                .setLong("userId", getUserId()).setMaxResults(1).uniqueResult();

        if (id != null) {
            result = TenantAdminRole.class;
            return;
        }

        // check if user is workflow admin
        id = evt.getSession().createQuery(
                "select a.user.id from UserAdministratesWorkflowInstance a "
                        + "where a.user.id = :userId").setLong("userId",
                getUserId()).setMaxResults(1).uniqueResult();

        if (id != null) {
            result = WorkflowAdminRole.class;
            return;
        }

        // check if user is member of a tenant
        id = evt.getSession().createQuery(
                "select a.user.id from UserIsMemberOfTenant a "
                        + "where a.user.id = :userId").setLong("userId",
                getUserId()).setMaxResults(1).uniqueResult();

        if (id != null) {
            result = UserRole.class;
            return;
        }

        // if user isn't member of any tenant
        result = null;
    }

    /**
     * @return highest role of the given user or null if the user not even a
     *         tenant member (but can still be a registered user).
     */
    public Class<? extends UserRole> getResult() {
        return result;
    }
}
