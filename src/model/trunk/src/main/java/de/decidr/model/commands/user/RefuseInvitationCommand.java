/*
 * The DecidR Development Team licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.decidr.model.commands.user;

import de.decidr.model.acl.access.InvitationAccess;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.Invitation;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.notifications.NotificationEvents;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Refuses the given invitation and sends an information email to the inviting
 * user.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class RefuseInvitationCommand extends UserCommand implements
        InvitationAccess {

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
    public RefuseInvitationCommand(Role role, Long invitationId) {
        super(role, null);
        if (invitationId == null) {
            throw new IllegalArgumentException(
                    "Invitation ID must not be null.");
        }
        this.invitationId = invitationId;
    }

    @Override
    public Long[] getInvitationIds() {
        Long[] res = { invitationId };
        return res;
    }

    @Override
    public void transactionAllowed(TransactionStartedEvent evt)
            throws TransactionException {

        Invitation invitation = (Invitation) evt.getSession().get(
                Invitation.class, invitationId);

        if (invitation == null) {
            throw new EntityNotFoundException(Invitation.class, invitationId);
        }

        evt.getSession().delete(invitationId);

        NotificationEvents.refusedInvitation(invitation);
    }
}
