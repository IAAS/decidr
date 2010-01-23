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

import org.hibernate.Session;

import de.decidr.model.acl.access.UserAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.EntityNotFoundException;

/**
 * Abstract base class for commands that deal with users.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public abstract class UserCommand extends AclEnabledCommand implements
        UserAccess {

    private Long userId;

    /**
     * Creates a new UserCommand.
     * 
     * @param role
     *            user which executes the command
     * @param userId
     *            ID of the user object which should be accessed
     */
    public UserCommand(Role role, Long userId) {
        super(role, (Permission) null);
        this.userId = userId;
    }

    /**
     * Fetches the user from the database.
     * 
     * @param session
     *            the Hibernate session to use for lookup.
     * @return user which corresponds to the given session
     * @throws EntityNotFoundException
     *             if no user is found
     */
    protected User fetchUser(Session session) throws EntityNotFoundException {
        User user = (User) session.get(User.class, userId);

        if (user == null) {
            throw new EntityNotFoundException(User.class, userId);
        }

        return user;
    }

    /**
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @return the userIds
     */
    public Long[] getUserIds() {
        Long[] result = { userId };
        return result;
    }

    /**
     * Throws an {@link IllegalArgumentException} if the user ID hasn't been set
     * to a non-<code>null</code> value.
     * 
     * @throws IllegalArgumentException
     */
    protected void requireUserId() {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null.");
        }
    }
}