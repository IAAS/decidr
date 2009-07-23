package de.decidr.model.commands.user;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Saves the user profile of the given user in the result variable.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class GetUserProfileCommand extends UserCommand {

    private UserProfile result;

    /**
     * Creates a new GetUserProfileCommand. The command saves the user profile
     * of the given user in the result variable.
     * 
     * @param role
     *            user which executes the command
     * @param userId
     *            TODO document
     */
    public GetUserProfileCommand(Role role, Long userId) {
        super(role, userId);
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        Criteria c = evt.getSession().createCriteria(UserProfile.class);
        c.add(Restrictions.eq("userId", getUserId()));

        result = (UserProfile) c.uniqueResult();
    }

    /**
     * @return the result TODO document
     */
    public UserProfile getResult() {
        return result;
    }
}
