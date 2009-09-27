package de.decidr.model.commands.user;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.RegistrationRequest;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.AuthKeyException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * The command in this class creates the user profile for the given user and
 * removes the auth key. The request will be deleted at all.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class ConfirmRegistrationCommand extends UserCommand {

    private Long userId;
    private String authKey;

    /**
     * Creates a new ConfirmRegistrationCommand. This command creates the user
     * profile for the given user and removes the auth key. The request will be
     * deleted at all.
     * 
     * @param role
     *            user which executes the command
     * @param userId
     *            ID of the user whose registration should be treated
     * @param authKey
     *            secret key which allows the user to confirm the registration
     *            (was sent via email to the user)
     */
    public ConfirmRegistrationCommand(Role role, Long userId, String authKey) {
        super(role, userId);
        this.userId = userId;
        this.authKey = authKey;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        User user = (User) evt.getSession().load(User.class, userId);
        RegistrationRequest request = user.getRegistrationRequest();

        // DH: what if nor request exists because user already is registered?
        // ~rr
        if (authKey.equals(request.getAuthKey())) {

            // set user profile
            UserProfile newUserProfile = request.getUser().getUserProfile();
            user.setUserProfile(newUserProfile);

            // set registration date and set auth key to null
            user.setRegisteredSince(DecidrGlobals.getTime().getTime());
            user.setAuthKey(null);

            // update user
            evt.getSession().update(user);

            // delete request
            evt.getSession().delete(request);

        } else {
            throw new AuthKeyException("Auth key does not match");
        }

    }

}
