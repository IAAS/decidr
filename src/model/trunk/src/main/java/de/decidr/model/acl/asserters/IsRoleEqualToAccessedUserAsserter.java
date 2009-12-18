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

package de.decidr.model.acl.asserters;

import de.decidr.model.acl.access.UserAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.TransactionException;

/**
 * Asserts that the user specified by the {@link Role} is the same users that
 * are accessed by a given command.
 * 
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class IsRoleEqualToAccessedUserAsserter extends CommandAsserter {

    private Long userId = null;
    private Long[] accessedUserIds = null;

    @Override
    public boolean assertRule(Role role, Permission permission)
            throws TransactionException {
        boolean result = false;

        if (role instanceof UserRole) {
            userId = role.getActorId();
            if (userId == null) {
                return false;
            }

            TransactionalCommand command = getCommandInstance(permission);
            if (command instanceof UserAccess) {
                accessedUserIds = ((UserAccess) command).getUserIds();
                if (accessedUserIds != null && userId != null) {
                    // accessedUserIds must consist entirely of the user id
                    result = true;
                    for (Long accessedUserId : accessedUserIds) {
                        if (!(result = userId.equals(accessedUserId))) {
                            break;
                        }
                    }
                }
            }
        }

        return result;
    }
}