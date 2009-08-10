package de.decidr.model.commands.user;

import de.decidr.model.notifications.NotificationEvents;
import de.decidr.model.acl.access.InvitationAccess;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.Invitation;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Refuses the given invitation and sends an information email to the inviting
 * user.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class RefuseInviationCommand extends UserCommand implements InvitationAccess{

    private Long invitationId;

    /**
     * Creates a new RefuseInviationCommand. The command refuses the given
     * invitation and sends an information email to the inviting user.<br>
     * 
     * @param role
     *            user which executes the command
     * @param invitationId
     *            the ID of the invitation which should be refused
     */
    public RefuseInviationCommand(Role role, Long invitationId) {
        super(role, null);

        this.invitationId = invitationId;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        Invitation invitation = (Invitation) evt.getSession().load(
                Invitation.class, invitationId);
        evt.getSession().delete(invitationId);

        NotificationEvents.refusedInvitation(invitation);

    }

    @Override
    public Long[] getInvitationIds() {
        Long[] res={invitationId};
        return res;
    }
}
