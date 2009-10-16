package de.decidr.model.acl.asserters;

import org.hibernate.Query;

import de.decidr.model.acl.access.InvitationAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionEvent;

public class UserIsInvitationReceiverAsserter extends CommandAsserter {

    private Long userId;
    private Long[] invitationIds = new Long[0];
    private Boolean isReceiver = false;

    @Override
    public Boolean assertRule(Role role, Permission permission)
            throws TransactionException {

        Boolean result = false;

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
    public void transactionStarted(TransactionEvent evt)
            throws TransactionException {

        if (invitationIds == null || invitationIds.length ==0) {
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
