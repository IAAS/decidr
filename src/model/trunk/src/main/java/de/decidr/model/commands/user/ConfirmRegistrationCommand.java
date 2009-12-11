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
import de.decidr.model.LifetimeValidator;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.RegistrationRequest;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.AuthKeyException;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Creates the user profile for the given user and removes the auth key.
 * Successfully confirmed requests as well as expired requests are removed from
 * the database.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class ConfirmRegistrationCommand extends UserCommand {

    private String authKey = null;
    private boolean requestExpired = false;

    /**
     * Creates a new ConfirmRegistrationCommand. This command creates the user
     * profile for the given user and removes the auth key. The request will be
     * deleted at all.
     * 
     * @param role
     *            user which executes the command
     * @param userId
     *            ID of the user whose registration should be treated
     * @param authKey
     *            secret key which allows the user to confirm the registration
     *            (was sent via email to the user)
     * @throws IllegalArgumentException
     *             if the authentication key is <code>null</code> or empty or if
     *             userId is <code>null</code>
     */
    public ConfirmRegistrationCommand(Role role, Long userId, String authKey) {
        super(role, userId);

        requireUserId();
        if (authKey == null || authKey.isEmpty()) {
            throw new IllegalArgumentException(
                    "Authentication key must not be null or empty.");
        }

        this.authKey = authKey;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        requestExpired = false;

        User user = fetchUser(evt.getSession());

        RegistrationRequest request = user.getRegistrationRequest();
        if (request == null) {
            throw new EntityNotFoundException(RegistrationRequest.class,
                    getUserId());
        }

        if (!authKey.equals(request.getAuthKey())) {
            throw new AuthKeyException();
        }

        if (LifetimeValidator.isRegistrationRequestValid(request)) {
            // make sure the user has a profile
            if (user.getUserProfile() == null) {
                throw new EntityNotFoundException(UserProfile.class);
            }

            // registered users have no authentication key but have a registered
            // date.
            user.setRegisteredSince(DecidrGlobals.getTime().getTime());
            user.setAuthKey(null);

            // delete registration request
            evt.getSession().delete(request);

            // update user
            evt.getSession().update(user);
        } else {
            // request has expired
            evt.getSession().delete(request);
            requestExpired = true;
        }
    }

    /**
     * @return whether the request has expired.
     */
    public boolean isRequestExpired() {
        return requestExpired;
    }
}