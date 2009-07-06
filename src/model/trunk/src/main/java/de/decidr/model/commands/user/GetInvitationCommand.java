package de.decidr.model.commands.user;

import de.decidr.model.entities.InvitationView;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Saves the invitation which corresponds to the given id in the result
 * variable.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class GetInvitationCommand extends UserCommand {

    private InvitationView result;
    private Long invitationId;

    /**
     * 
     * Creates a new GetInvitationCommand. The command saves the invitation
     * which corresponds to the given id in the result variable.
     * 
     * @param role
     * @param userId
     * @param invitationId
     */
    public GetInvitationCommand(Role role, Long invitationId) {
        super(role, null);
        this.invitationId = invitationId;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        result = (InvitationView) evt.getSession().load(InvitationView.class,invitationId);

    }

    public InvitationView getResult() {
        return result;
    }

}
