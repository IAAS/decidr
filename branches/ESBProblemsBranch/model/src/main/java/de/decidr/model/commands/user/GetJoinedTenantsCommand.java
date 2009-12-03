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

import org.hibernate.Query;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Fetches all approved tenants of which the given user is a member from the
 * database.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class GetJoinedTenantsCommand extends UserCommand {

    private List<Tenant> result;

    /**
     * Creates a new getJoinedTenantsCommand. This command writes all tenants
     * the given user is member of in the result variable.
     * 
     * @param role
     *            the user which executes the command
     * @param userId
     *            the ID of the user whose joined tenants should be returned
     */
    public GetJoinedTenantsCommand(Role role, Long userId) {
        super(role, userId);
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null.");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        String hql = "select u.id from User u where u.id = :userId";

        Object found = evt.getSession().createQuery(hql).setLong("userId",
                getUserId()).setMaxResults(1).uniqueResult();

        if (found == null) {
            throw new EntityNotFoundException(User.class, getUserId());
        }

        hql = "select rel.tenant from UserIsMemberOfTenant as rel "
                + "where (rel.user.id = :userId) and "
                + "(rel.tenant.approvedSince is not null)";

        Query q = evt.getSession().createQuery(hql).setLong("userId",
                getUserId());

        result = q.list();
    }

    /**
     * @return the joined tenants.
     */
    public List<Tenant> getResult() {
        return result;
    }
}
