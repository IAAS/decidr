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

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.Password;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.ChangeEmailRequest;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.notifications.NotificationEvents;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Creates a change email request for a given user and sends a confirmation
 * email to the new email address.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class RequestChangeEmailCommand extends UserCommand {

    private String newEmail;

    /**
     * Creates a new RequestChangeEmailCommand that will create or replace the
     * current change email request for the given user.
     * 
     * @param role
     *            user / system executing the command.
     * @param userId
     *            user whose email address should be changed
     * @param newEmail
     *            new email address
     * @throws IllegalArgumentException
     *             if userId is <code>null</code> or if newEmail is
     *             <code>null</code> or empty.
     */
    public RequestChangeEmailCommand(Role role, Long userId, String newEmail) {
        super(role, userId);
        requireUserId();

        if (newEmail == null || newEmail.isEmpty()) {
            throw new IllegalArgumentException(
                    "New email address cannot be null or empty.");
        }
        this.newEmail = newEmail;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        User user = fetchUser(evt.getSession());
        ChangeEmailRequest currentRequest = user.getChangeEmailRequest();
        if (currentRequest != null) {
            evt.getSession().delete(currentRequest);
        }

        ChangeEmailRequest newRequest = new ChangeEmailRequest();
        newRequest.setAuthKey(Password.getRandomAuthKey());
        newRequest.setCreationDate(DecidrGlobals.getTime().getTime());
        newRequest.setNewEmail(newEmail);
        newRequest.setUser(user);
        newRequest.setId(user.getId());
        evt.getSession().save(newRequest);

        user.setChangeEmailRequest(newRequest);
        // no need to update the user entity.

        NotificationEvents.createdChangeEmailRequest(newRequest);
    }

}
