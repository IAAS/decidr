package de.decidr.model.commands.user;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Fetches the user's properties including his profile (if he has one) from the
 * database
 * 
 * RR renamed the command, changed functionalitiy from "fetch user profile only"
 * to "fetch user data and user profile" ~dh
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class GetUserWithProfileCommand extends UserCommand {

    private User result;

    /**
     * Creates a new GetUserWithProfileCommand that fetches the user and his
     * profile from the database.
     * 
     * @param role
     *            user which executes the command
     * @param userId
     *            the ID of the user whose profile schould be requested
     */
    public GetUserWithProfileCommand(Role role, Long userId) {
        super(role, userId);
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        String hql = "from User u join fetch u.userProfile where u.id = :userId";
        result = (User) evt.getSession().createQuery(hql).setLong("userId",
                getUserId()).uniqueResult();
        if (result != null) {
            // make extra sure the data is retrieved from the db before lazy
            // loading becomes unavailable.
            result.getUserProfile();
        }
    }

    /**
     * @return the user profile of the given user or null if that user
     */
    public User getResult() {
        return result;
    }
}
