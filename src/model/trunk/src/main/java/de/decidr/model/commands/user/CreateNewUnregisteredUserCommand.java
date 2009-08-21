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

import de.decidr.model.acl.Password;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Creates a new user in the database that has no user profile but may login
 * using an auto-generated authentication key.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class CreateNewUnregisteredUserCommand extends AclEnabledCommand {

    private User newUser = null;

    /**
     * Creates a new CreateNewUnregisteredUserCommand that will create a new
     * user in the database without creating a user profile.
     * <p>
     * <b>Note that this command will modify the given user object directly,
     * making it a persisted entity.</b>
     * 
     * @param role
     *            user / system executing the command
     * @param newUser
     *            must contain an email address.
     */
    public CreateNewUnregisteredUserCommand(Role role, User newUser) {
        super(role, (Permission) null);
        if ((newUser == null) || (newUser.getEmail() == null)) {
            throw new IllegalArgumentException(
                    "User object with email address is required.");
        }
        this.newUser = newUser;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        newUser.setId(null);
        try {
            newUser.setAuthKey(Password.getRandomAuthKey());
        } catch (UnsupportedEncodingException e) {
            throw new TransactionException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new TransactionException(e);
        }
        newUser.setRegisteredSince(null);
        newUser.setDisabledSince(null);
        newUser.setUnavailableSince(null);
        newUser.setUserProfile(null);

        evt.getSession().save(newUser);
    }
}
