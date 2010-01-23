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
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Asserts that the given user is not participating in any worklfow instance.
 * <p>
 * The {@link Permission} passed to <code>assertRule</code> is ignored.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class UserNotParticipatingInAnyWorkflowAsserter extends
        AbstractTransactionalCommand implements Asserter {

    private Long userid = null;
    private boolean notParticipating = false;

    @Override
    public boolean assertRule(Role role, Permission permission)
            throws TransactionException {

        boolean result = false;

        if (role instanceof UserRole) {
            userid = role.getActorId();
            HibernateTransactionCoordinator.getInstance().runTransaction(this);
            result = notParticipating;
        }
        return result;
    }

    @Override
    public void transactionStarted(TransactionStartedEvent evt) {
        notParticipating = false;
        Query q = evt.getSession().createQuery(
                "select rel.user.id from UserParticipatesInWorkflow rel "
                        + "where rel.user.id = :userId");
        q.setLong("userId", userid).setMaxResults(1);

        notParticipating = q.uniqueResult() == null;
    }
}
