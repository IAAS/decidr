package de.decidr.model.permissions.asserters;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Asserter;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.UserRole;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionAbortedEvent;
import de.decidr.model.transactions.TransactionEvent;
/**
 * Asserts that a user hasn't set his status to unavailable.
 * 
 * @author Daniel Huss
 *
 * @version 0.1
 */
public class IsUserAvailable implements Asserter, TransactionalCommand {

    private Long userId = null;

    private Boolean userIsEnabled = false;

    @Override
    public Boolean assertRule(Role role, Permission permission) throws TransactionException{
        Boolean result = null;

        if (role instanceof UserRole) {
            userId = role.getActorId();
            HibernateTransactionCoordinator.getInstance().runTransaction(this);
            result = userIsEnabled;
        }

        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt) {
        User user = (User) evt.getSession().get(User.class, userId);

        userIsEnabled = user.getUnavailableSince() == null;
    }

    @Override
    public void transactionAborted(TransactionAbortedEvent evt) {
    }

    @Override
    public void transactionCommitted(TransactionEvent evt) {
    }
}
