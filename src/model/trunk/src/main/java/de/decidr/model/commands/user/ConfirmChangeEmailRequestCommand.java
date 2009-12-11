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

import de.decidr.model.LifetimeValidator;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.ChangeEmailRequest;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.AuthKeyException;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Changes the email address of the given user iff the given auth key matches
 * the auth key in a previously created {@link ChangeEmailRequest}. Otherwise an
 * {@link AuthKeyException} is thrown. Successfully confirmed requests as well
 * as expired requests are removed from the database.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class ConfirmChangeEmailRequestCommand extends UserCommand {

    private String requestAuthKey = null;
    private Long userId = null;
    private boolean requestExpired = false;

    /**
     * 
     * Creates a new ConfirmChangeEmailRequestCommand that changes the email
     * address of the given user iff the given auth key matches the auth key in
     * a previously created {@link ChangeEmailRequest}. Otherwise an
     * {@link AuthKeyException} is thrown. Successfully confirmed requests as
     * well as expired requests are removed from the database.
     * 
     * @param actor
     *            user / system executing the command
     * @param userId
     *            user whose email address is being changed
     * @param requestAuthKey
     *            authentication key associated with the
     *            {@link ChangeEmailRequest}
     * @throws IllegalArgumentException
     *             if userId is <code>null</code> or if requestAuthKey is
     *             <code>null</code> or empty.
     */
    public ConfirmChangeEmailRequestCommand(Role actor, Long userId,
            String requestAuthKey) {
        super(actor, userId);
        requireUserId();
        if (requestAuthKey == null || requestAuthKey.isEmpty()) {
            throw new IllegalArgumentException(
                    "Authentication key must not be null or empty.");
        }
        this.requestAuthKey = requestAuthKey;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        requestExpired = false;
        User user = fetchUser(evt.getSession());

        ChangeEmailRequest request = user.getChangeEmailRequest();
        if (request == null) {
            throw new EntityNotFoundException(ChangeEmailRequest.class, userId);
        }

        if (!requestAuthKey.equals(request.getAuthKey())) {
            throw new AuthKeyException();
        }

        if (LifetimeValidator.isChangeEmailRequestValid(request)) {

            user.setEmail(request.getNewEmail());
            // due to a consistency trigger we must delete the request BEFORE
            // updating the user.
            evt.getSession().delete(request);
            evt.getSession().update(user);
        } else {
            // the request has expired
            requestExpired = true;
            evt.getSession().delete(request);
        }
    }

    /**
     * @return whether the change email request has expired
     */
    public boolean isRequestExpired() {
        return requestExpired;
    }
}