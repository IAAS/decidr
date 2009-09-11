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

import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.acl.roles.TenantAdminRole;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.acl.roles.WorkflowAdminRole;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Saves the highest role of the given user in the result variable.
 * 
 * @author Markus Fischer
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
     */
    public GetHighestUserRoleCommand(Role role, Long userId) {
        super(role, userId);
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

        // check if user is tenant admin

        Query q = evt.getSession().createQuery(
                "select count(*) from Tenant a where a.admin.id = :userId");
        q.setLong("userId", getUserId());

        Number count = (Number) q.uniqueResult();

        if (count == null) {
            throw new TransactionException("Query didn't return a result.");
        } else if (count.intValue() > 0) {
            result = TenantAdminRole.class;
            return;
        }

        // check if user is workflow admin
        Query q2 = evt
                .getSession()
                .createQuery(
                        "select count(*) from UserAdministratesWorkflowInstance a where a.user.id = :userId");
        q.setLong("userId", getUserId());

        count = (Number) q2.uniqueResult();

        if (count == null) {
            throw new TransactionException("Query didn't return a result.");
        } else if (count.intValue() > 0) {
            result = WorkflowAdminRole.class;
            return;
        }

        // check if user is member
        Query q3 = evt
                .getSession()
                .createQuery(
                        "select count(*) from UserIsMemberOfTenant a where a.user.id = :userId");
        q.setLong("userId", getUserId());

        count = (Number) q3.uniqueResult();

        if (count == null) {
            throw new TransactionException("Query didn't return a result.");
        } else if (count.intValue() > 0) {
            result = UserRole.class;
            return;
        }

        // if user isn't member of any tenant
        result = null;
    }

    /**
     * @return highest role of the given user
     */
    public Class<? extends UserRole> getResult() {
        return result;
    }
}
