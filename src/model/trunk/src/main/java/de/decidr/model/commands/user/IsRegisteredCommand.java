package de.decidr.model.commands.user;

import de.decidr.model.entities.User;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Sets the result variable true if the given user is registered, else false.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class IsRegisteredCommand extends UserCommand {

    private boolean result;

    /**
     * Creates a new IsRegisteredCommand. This command sets the result variable
     * <code>true</code> if the given user is registered, else
     * <code>false</code>.
     * 
     * @param role
     *            TODO document
     * @param userId
     *            TODO document
     */
    public IsRegisteredCommand(Role role, Long userId) {
        super(role, userId);
        // nothing to do
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        User user = (User) evt.getSession().load(User.class, getUserId());

        if (user.getUserProfile() == null) {
            result = false;
        } else {
            result = true;
        }
    }

    /**
     * @return the result TODO document
     */
    public boolean getResult() {
        return result;
    }
}
