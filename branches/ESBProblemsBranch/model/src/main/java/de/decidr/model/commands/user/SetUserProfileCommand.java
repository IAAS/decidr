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

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Overwrites a part of the user's profile data iff the user already has a user
 * profile.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class SetUserProfileCommand extends UserCommand {

    private UserProfile newProfile = null;

    /**
     * Creates a new SetUserProfileCommand.
     * 
     * @param role
     *            user who executes the command
     * @param userId
     *            the ID of the user whose profile should be updated
     * @param newProfile
     *            the new user profile
     * @throws IllegalArgumentException
     *             if newProfile is <code>null</code>.
     */
    public SetUserProfileCommand(Role role, Long userId, UserProfile newProfile) {
        super(role, userId);
        if (newProfile == null || userId == null) {
            throw new IllegalArgumentException(
                    "User ID and new profile must not be null.");
        }
        this.newProfile = newProfile;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        UserProfile currentProfile = (UserProfile) evt.getSession().get(
                UserProfile.class, getUserId());

        if (currentProfile == null) {
            throw new EntityNotFoundException(UserProfile.class, getUserId());
        }

        // copying the properties from the new profile data for safety and
        // robustness. Disadvantage: must be updated if the user profile
        // receives new properties.
        currentProfile.setUsername(newProfile.getUsername());
        currentProfile.setCity(newProfile.getCity());
        currentProfile.setFirstName(newProfile.getFirstName());
        currentProfile.setLastName(newProfile.getLastName());
        currentProfile.setPostalCode(newProfile.getPostalCode());
        currentProfile.setStreet(newProfile.getStreet());

        evt.getSession().update(currentProfile);
    }
}
