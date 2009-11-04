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
 * Asserts that the given user's account hasn't been disabled by the superadmin.
 * <p>
 * The {@link Permission} passed to <code>assertRule</code> is ignored.
 * 
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class UserIsEnabledAsserter extends AbstractTransactionalCommand
        implements Asserter {

    private Long userId = null;
    private Boolean userIsEnabled = false;

    @Override
    public Boolean assertRule(Role role, Permission permission)
            throws TransactionException {
        Boolean result = false;

        if (role instanceof UserRole) {
            userId = role.getActorId();

            if (userId == null) {
                result = false;
            } else {
                HibernateTransactionCoordinator.getInstance().runTransaction(
                        this);
                result = userIsEnabled;
            }
        }

        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt) {
        User user = (User) evt.getSession().get(User.class, userId);
        // a user is enabled iff no "disabled since" date has been set
        userIsEnabled = user != null && user.getDisabledSince() == null;
    }
}
