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

import org.hibernate.Query;

import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Asserts that the given super admin is actually the super admin.
 * <p>
 * The {@link Permission} passed to <code>assertRule</code> is ignored.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class UserIsSuperAdminAsserter extends AbstractTransactionalCommand implements
        Asserter {

    private Long userid = null;
    private Boolean isSuperAdmin = false;

    @Override
    public Boolean assertRule(Role role, Permission permission)
            throws TransactionException {
        isSuperAdmin = false;

        if (role instanceof SuperAdminRole) {
            userid = role.getActorId();
            HibernateTransactionCoordinator.getInstance().runTransaction(this);
        }

        return isSuperAdmin;
    }

    @Override
    public void transactionStarted(TransactionEvent evt)
            throws TransactionException {
        isSuperAdmin = false;

        Query q = evt.getSession().createQuery(
                "select s.superAdmin.id from SystemSettings s")
                .setMaxResults(1);
        Long superAdminId = (Long) q.uniqueResult();

        isSuperAdmin = (superAdminId != null) && (superAdminId.equals(userid));
    }
}