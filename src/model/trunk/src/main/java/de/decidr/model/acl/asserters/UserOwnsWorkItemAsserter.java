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

import de.decidr.model.acl.access.WorkItemAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Asserts that the given user owns the given work items(s).
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class UserOwnsWorkItemAsserter extends CommandAsserter {

    private Long userId = null;
    private Long[] workItemIds = null;
    private boolean isOwner = false;

    @Override
    public boolean assertRule(Role role, Permission permission)
            throws TransactionException {

        boolean result = false;

        if (role instanceof UserRole) {
            userId = role.getActorId();
            TransactionalCommand command = getCommandInstance(permission);

            if (command instanceof WorkItemAccess) {
                workItemIds = ((WorkItemAccess) command).getWorkItemIds();
                HibernateTransactionCoordinator.getInstance().runTransaction(
                        this);
                result = isOwner;
            } else {
                // the user is not accessing any work items, so we're not
                // denying access.
                result = true;
            }
        }
        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt) {
        if ((workItemIds == null) || (workItemIds.length == 0)) {
            // no work items to check against
            isOwner = false;
        } else {
            // we can assert the ownership for all work items with a single
            // query
            String hql = "select count(*) from WorkItem w where "
                    + "w.user.id = :userId and " + "w.id in (:workItemIds)";
            Query q = evt.getSession().createQuery(hql).setLong("userId",
                    userId).setParameterList("workItemIds", workItemIds);

            isOwner = ((Number) q.uniqueResult()).intValue() == workItemIds.length;
        }
    }
}
