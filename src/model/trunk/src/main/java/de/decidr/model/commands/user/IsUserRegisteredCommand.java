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
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Sets the result variable true if the given user is registered, false
 * otherwise. A user is considered to have registered if and only if:
 * <ul>
 * <li>The user has no pending registration request</li>
 * <li>The user's register date has been set</li>
 * <li>The user account hasn't been disabled by the superadmin</li>
 * </ul>
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class IsUserRegisteredCommand extends UserCommand {

    private boolean result;

    /**
     * Creates a new IsUserRegisteredCommand. This command sets the result
     * variable <code>true</code> if the given user is registered, else
     * <code>false</code>.
     * 
     * @param role
     *            the user which executes the command
     * @param userId
     *            the id of the user which should be checked
     * @throws IllegalArgumentException
     *             if userId is <code>null</code>
     */
    public IsUserRegisteredCommand(Role role, Long userId) {
        super(role, userId);
        requireUserId();
    }

    /**
     * @return <code>true</code> if the given user is registered,
     *         <code>false</code> otherwise.
     */
    public boolean getResult() {
        return result;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        // set to false in case fetchUser throws an exception.
        result = false;
        User user = fetchUser(evt.getSession());
        result = (user.getRegisteredSince() != null)
                && (user.getRegistrationRequest() == null)
                && (user.getDisabledSince() == null);
    }
}
