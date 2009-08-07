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
 * Checks whether a given auth key matches the user's auth key in the database.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class CheckAuthKeyCommand extends UserCommand {

    private String authKey = null;

    private Boolean authKeyMatches = false;

    /**
     * Creates a new CheckAuthKeyCommand.
     * 
     * @param role
     *            the user which executes the command
     * @param userId
     *            the id of the user
     * @param authKey
     *            the given auth key
     */
    public CheckAuthKeyCommand(Role role, Long userId, String authKey) {
        super(role, userId);
        if (null == authKey) {
            throw new NullPointerException();
        }
        this.authKey = authKey;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        User user = fetchUser(evt.getSession());

        authKeyMatches = authKey.equals(user.getAuthKey());
    }

    /**
     * @return <code>true</code> iff the auth key matched the auth key in the
     *         database.
     */
    public Boolean getAuthKeyMatches() {
        return authKeyMatches;
    }
}
