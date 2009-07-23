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

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import de.decidr.model.LifetimeValidator;
import de.decidr.model.notifications.NotificationEvents;
import de.decidr.model.entities.PasswordResetRequest;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Password;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Generates a new password for a user if the given authentication key matches.
 * A notification mail containing the new password is sent to the user.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class ConfirmPasswordResetCommand extends UserCommand {

    private String authKey = null;

    private Boolean requestExpired = false;

    private String newPassword = null;

    /**
     * Creates a new ConfirmPasswordResetCommand.
     * 
     * @param role
     *            TODO document
     * @param userId
     *            TODO document
     */
    public ConfirmPasswordResetCommand(Role role, Long userId, String authKey) {
        super(role, userId);
        if (authKey == null) {
            throw new NullPointerException("authKey must not be null.");
        }
        this.authKey = authKey;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        requestExpired = false;
        newPassword = null;

        // is there a password reset request?

        PasswordResetRequest request = (PasswordResetRequest) evt.getSession()
                .get(PasswordResetRequest.class, getUserId());

        if ((request == null) || (!authKey.equals(request.getAuthKey()))) {
            throw new EntityNotFoundException(PasswordResetRequest.class,
                    getUserId());
        }

        // is the request still valid?
        Boolean isAlive = LifetimeValidator.isPasswordResetRequestValid(
                request, evt.getSession());

        if (isAlive) {
            try {
                // generate a new password
                UserProfile profile = (UserProfile) evt.getSession().get(
                        UserProfile.class, getUserId());

                if (profile == null) {
                    throw new EntityNotFoundException(UserProfile.class,
                            getUserId());
                }

                newPassword = Password.generateRandomPassword();
                profile.setPasswordHash(Password.getHash(newPassword, profile
                        .getPasswordSalt()));

                // save new password
                evt.getSession().save(profile);

                // notify the user
                NotificationEvents.generatedNewPassword(profile.getUser(),
                        newPassword);
            } catch (NoSuchAlgorithmException e) {
                throw new TransactionException(e);
            } catch (UnsupportedEncodingException e) {
                throw new TransactionException(e);
            }
        } else {
            // the request has expired
            requestExpired = true;
            // we cannot throw an exception because that would undo the delete.
            evt.getSession().delete(request);
        }
    }

    /**
     * @return whether the request has expired.
     */
    public Boolean getRequestExpired() {
        return requestExpired;
    }
}
