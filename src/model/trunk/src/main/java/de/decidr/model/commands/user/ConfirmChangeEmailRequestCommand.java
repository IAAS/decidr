package de.decidr.model.commands.user;

/**
 * This command changes the email address of the given user iff the given auth key is correct. 
 * If not an exception will be thrown.
 * 
 */
import de.decidr.model.entities.ChangeEmailRequest;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.AuthKeyException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * The function in this class changes the email address of the given user iff
 * the given auth key is correct. If not an exception will be thrown.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class ConfirmChangeEmailRequestCommand extends UserCommand {

    private String requestAuthKey;
    private Long userId;

    /**
     * 
     * Creates a new ConfirmChangeEmailRequestCommand. This command changes the
     * email address of the given user iff the given auth key is correct. If not
     * an exception will be thrown. The request object will be deleted as all.
     * 
     * @param actor
     * @param userId
     * @param requestAuthKey
     */
    public ConfirmChangeEmailRequestCommand(Role actor, Long userId,
            String requestAuthKey) {
        super(actor, userId);

        this.requestAuthKey = requestAuthKey;

    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        User user = (User) evt.getSession().load(User.class, userId);
        ChangeEmailRequest request = user.getChangeEmailRequest();

        if (requestAuthKey.equals(request.getAuthKey())) {

            // change email address
            user.setEmail(request.getNewEmail());
            evt.getSession().delete(request);

        } else {
            throw new AuthKeyException("AuthKey does not match");
        }

    }

}
