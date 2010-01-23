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
import de.decidr.model.entities.InvitationView;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Saves the invitation which corresponds to the given ID in the result
 * variable.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class GetInvitationCommand extends UserCommand implements
        InvitationAccess {

    private InvitationView result;
    private Long invitationId;

    /**
     * Creates a new GetInvitationCommand. The command saves the invitation
     * which corresponds to the given ID in the result variable.
     * 
     * @param role
     *            the user which executes the command
     * @param invitationId
     *            the id of the invitation which should be returned
     */
    public GetInvitationCommand(Role role, Long invitationId) {
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

    /**
     * @return the invitation
     */
    public InvitationView getResult() {
        return result;
    }

    @Override
    public void transactionAllowed(TransactionStartedEvent evt)
            throws TransactionException {
        result = (InvitationView) evt.getSession().get(InvitationView.class,
                invitationId);
    }
}
