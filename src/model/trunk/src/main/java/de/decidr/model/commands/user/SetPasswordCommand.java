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

import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Password;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Sets the password of a user if the current password is known to the invoker.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class SetPasswordCommand extends UserCommand {

    private Boolean passwordWasChanged = false;
    private String oldPassword;
    private String newPassword;

    public SetPasswordCommand(Role role, Long userId, String oldPassword,
            String newPassword) {
        super(role, userId);
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        passwordWasChanged = false;

        UserProfile profile = (UserProfile) evt.getSession().get(
                UserProfile.class, getUserId());

        if (profile == null) {
            // the user does not have a password and is identified via
            // authentication key.
            throw new EntityNotFoundException(UserProfile.class, getUserId());
        }

        try {
            String hash = Password.getHash(oldPassword, profile
                    .getPasswordSalt());

            if (hash.equals(profile.getPasswordHash())) {
                // the given oldPassword is correct
                profile.setPasswordHash(Password.getHash(newPassword, Password
                        .getRandomSalt()));
            }

            evt.getSession().update(profile);
        } catch (NoSuchAlgorithmException e) {
            throw new TransactionException(e);
        } catch (UnsupportedEncodingException e) {
            throw new TransactionException(e);
        }

        passwordWasChanged = true;
    }

    /**
     * @return true iff oldPassword matched the current password and the
     *         password has been changed.
     */
    public Boolean getPasswordWasChanged() {
        return passwordWasChanged;
    }
}
