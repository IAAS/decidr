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
import de.decidr.model.acl.Password;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.PasswordResetRequest;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.AuthKeyException;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.notifications.NotificationEvents;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Generates a new password for a user if the given authentication key matches.
 * A notification mail containing the new password is sent to the user.
 * Successfully confirmed requests as well as expired requests are removed from
 * the database.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class ConfirmPasswordResetCommand extends UserCommand {

    private String authKey = null;
    private boolean requestExpired = false;
    private String newPassword = null;

    /**
     * Creates a new ConfirmPasswordResetCommand that generates a new password
     * for a user if the given authentication key matches. A notification mail
     * containing the new password is sent to the user. Successfully confirmed
     * requests as well as expired requests are removed from the database.
     * 
     * @param role
     *            the user /system executing the command
     * @param userId
     *            the id of the user whose password reset request should be
     *            confirmed
     * @throws IllegalArgumentException
     *             if userId is <code>null</code> or if authKey is
     *             <code>null</code> or empty
     */
    public ConfirmPasswordResetCommand(Role role, Long userId, String authKey) {
        super(role, userId);
        requireUserId();
        if ((authKey == null) || authKey.isEmpty()) {
            throw new IllegalArgumentException(
                    "Authentication key must not be null or empty.");
        }
        this.authKey = authKey;
    }

    /**
     * @return whether the request has expired.
     */
    public boolean getRequestExpired() {
        return requestExpired;
    }

    @Override
    public void transactionAllowed(TransactionStartedEvent evt)
            throws TransactionException {

        requestExpired = false;
        newPassword = null;

        // is there a password reset request?
        PasswordResetRequest request = (PasswordResetRequest) evt.getSession()
                .get(PasswordResetRequest.class, getUserId());

        if (request == null) {
            throw new EntityNotFoundException(PasswordResetRequest.class,
                    getUserId());
        }

        if (!authKey.equals(request.getAuthKey())) {
            throw new AuthKeyException();
        }

        // is the request still valid?
        Boolean isAlive = LifetimeValidator
                .isPasswordResetRequestValid(request);

        if (isAlive) {
            // generate a new password, notify user and delete the request
            UserProfile profile = (UserProfile) evt.getSession().get(
                    UserProfile.class, getUserId());

            if (profile == null) {
                throw new EntityNotFoundException(UserProfile.class,
                        getUserId());
            }

            newPassword = Password.generateRandomPassword();
            profile.setPasswordHash(Password.getHash(newPassword, profile
                    .getPasswordSalt()));

            evt.getSession().save(profile);

            // it is important that we delete the request before sending the
            // email because we cannot roll back the latter
            evt.getSession().delete(request);

            NotificationEvents.generatedNewPassword(profile.getUser(),
                    newPassword);
        } else {
            // the request has expired
            requestExpired = true;
            evt.getSession().delete(request);
        }
    }
}
