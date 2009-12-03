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
 * Fetches the user's properties including his profile (if he has one) from the
 * database
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class GetUserWithProfileCommand extends UserCommand {

    private User result;

    /**
     * Creates a new GetUserWithProfileCommand that fetches the user and his
     * profile from the database.
     * 
     * @param role
     *            user which executes the command
     * @param userId
     *            the ID of the user whose profile schould be requested
     * @throws IllegalArgumentException
     *             if userId is <code>null</code>
     */
    public GetUserWithProfileCommand(Role role, Long userId) {
        super(role, userId);
        requireUserId();
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        String hql = "select u from User u join fetch u.userProfile where u.id = :userId";
        result = (User) evt.getSession().createQuery(hql).setLong("userId",
                getUserId()).uniqueResult();
        if (result != null) {
            // make extra sure the data is retrieved from the db before lazy
            // loading becomes unavailable.
            result.getUserProfile();
        }
    }

    /**
     * @return the user profile of the given user or null if that user
     */
    public User getResult() {
        return result;
    }
}
