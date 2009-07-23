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

import de.decidr.model.entities.InvitationView;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Saves the invitation which corresponds to the given ID in the result
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
     * Creates a new GetInvitationCommand. The command saves the invitation
     * which corresponds to the given ID in the result variable.
     * 
     * @param role
     *            TODO document
     * @param userId
     *            TODO document
     * @param invitationId
     *            TODO document
     */
    public GetInvitationCommand(Role role, Long invitationId) {
        super(role, null);
        this.invitationId = invitationId;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        result = (InvitationView) evt.getSession().load(InvitationView.class,
                invitationId);
    }

    /**
     * @return the invitation
     */
    public InvitationView getResult() {
        return result;
    }
}
