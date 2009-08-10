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
public class IsUserAvailable extends AbstractTransactionalCommand implements
        Asserter {

    private Long userId = null;
    private Boolean userIsAvailable = false;

    @Override
    public Boolean assertRule(Role role, Permission permission)
            throws TransactionException {
        Boolean result = null;

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
        userIsAvailable = user.getUnavailableSince() == null;
    }
}
