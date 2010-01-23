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

import de.decidr.model.acl.Password;
import de.decidr.model.acl.asserters.UserIsSuperAdminAsserter;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.AccessDeniedException;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Sets the password of a user. Unless invoked by a superadmin, the caller must
 * know the current password.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class SetPasswordCommand extends UserCommand {

    private Boolean passwordWasChanged = false;
    private String oldPassword = null;
    private String newPassword = null;

    /**
     * Creates a new SetPasswordCommand that sets the password of a given user.
     * Unless invoked by a superadmin, the caller must know the current
     * password.
     * 
     * @param role
     *            user / system executing the command.
     * @param userId
     *            user whose password should be changed
     * @param oldPassword
     *            old password (if executed by a superadmin, this parameter can
     *            be null or any value as it will be ignored).
     * @param newPassword
     *            New password to set.
     * @throws IllegalArgumentException
     *             if the user ID is <code>null</code> or the new password is
     *             <code>null</code> or the old password is <code>null</code> in
     *             case this command is not invoked by a superadmin.
     */
    public SetPasswordCommand(Role role, Long userId, String oldPassword,
            String newPassword) {
        super(role, userId);

        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null.");
        }

        if (newPassword == null) {
            throw new IllegalArgumentException("New password must not be null.");
        }

        if (!(role instanceof SuperAdminRole) && (oldPassword == null)) {
            throw new IllegalArgumentException(
                    "Must provide old password to set a new password when not invoked by a super admin.");
        }

        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    /**
     * @return true iff oldPassword matched the current password and the
     *         password has been changed.
     */
    public Boolean getPasswordWasChanged() {
        return passwordWasChanged;
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

        // superadmins don't have to know the old password.
        Boolean allowChange = true;
        if (!(role instanceof SuperAdminRole)) {
            String hash = Password.getHash(oldPassword, profile
                    .getPasswordSalt());
            allowChange = hash.equals(profile.getPasswordHash());
        } else if (!new UserIsSuperAdminAsserter().assertRule(role, null)) {
            // Superadmins must actually be superadmins.
            throw new AccessDeniedException(
                    "Declared superadmin is not actually a superadmin.");
        }

        if (allowChange) {
            String newSalt = Password.getRandomSalt();

            profile.setPasswordHash(Password.getHash(newPassword, newSalt));
            profile.setPasswordSalt(newSalt);

            evt.getSession().update(profile);
            passwordWasChanged = true;
        }
    }
}
