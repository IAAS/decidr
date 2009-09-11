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
 * Sets the result variable true if the given user is registered, else false.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class IsRegisteredCommand extends UserCommand {

    private boolean result;

    /**
     * Creates a new IsRegisteredCommand. This command sets the result variable
     * <code>true</code> if the given user is registered, else
     * <code>false</code>.
     * 
     * @param role
     *            the user which executes the command
     * @param userId
     *            the id of the user which should be checked
     */
    public IsRegisteredCommand(Role role, Long userId) {
        super(role, userId);
        // nothing to do
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        User user = (User) evt.getSession().load(User.class, getUserId());

        if (user.getUserProfile() == null) {
            result = false;
        } else {
            result = true;
        }
    }

    /**
     * @return <code>true</code> if the given user is registered, else
     *         <code>false</code>.
     */
    public boolean getResult() {
        return result;
    }
}
