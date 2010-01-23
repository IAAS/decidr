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

import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Asserts that a user hasn't set his status to unavailable.
 * <p>
 * The {@link Permission} passed to <code>assertRule</code> is ignored.
 * 
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class UserIsAvailableAsserter extends AbstractTransactionalCommand
        implements Asserter {

    private Long userId = null;
    private boolean userIsAvailable = false;

    @Override
    public boolean assertRule(Role role, Permission permission)
            throws TransactionException {
        boolean result = false;

        if (role instanceof UserRole) {
            userId = role.getActorId();
            HibernateTransactionCoordinator.getInstance().runTransaction(this);
            result = userIsAvailable;
        }

        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt) {
        User user = (User) evt.getSession().get(User.class, userId);
        // a user is available iff no "unavailable since" date has been set
        userIsAvailable = (user != null)
                && (user.getUnavailableSince() == null);
    }
}
