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

import de.decidr.model.acl.access.InvitationAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionStartedEvent;

public class UserIsInvitationReceiverAsserter extends CommandAsserter {

    private Long userId;
    private Long[] invitationIds = new Long[0];
    private boolean isReceiver = false;

    @Override
    public boolean assertRule(Role role, Permission permission)
            throws TransactionException {

        boolean result = false;

        if (role instanceof UserRole) {

            userId = role.getActorId();
            TransactionalCommand command = getCommandInstance(permission);

            if (command instanceof InvitationAccess) {
                invitationIds = ((InvitationAccess) command).getInvitationIds();
                HibernateTransactionCoordinator.getInstance().runTransaction(
                        this);

                result = isReceiver;
            }

        }
        return result;
    }

    @Override
    public void transactionStarted(TransactionStartedEvent evt)
            throws TransactionException {

        if ((invitationIds == null) || (invitationIds.length == 0)) {
            isReceiver = false;
        } else {
            String hql = "select count(*) from Invitation i where "
                    + "i.id in (:invitationIds) and "
                    + "i.receiver.id = :userId";

            Query q = evt.getSession().createQuery(hql).setLong("userId",
                    userId).setParameterList("invitationIds", invitationIds);

            isReceiver = ((Number) q.uniqueResult()).intValue() == invitationIds.length;
        }
    }
}
