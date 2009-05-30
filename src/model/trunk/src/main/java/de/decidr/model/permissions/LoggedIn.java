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
package de.decidr.model.permissions;

/**
 * Checks whether a {@link UserRole} is logged in by looking at its actor id. The role is
 * considered to have logged in if its actor id is a valid user id.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class LoggedIn implements Asserter {

    @Override
    public Boolean assertRule(Role role, Permission permission) {
        Boolean result = false;
        if (role instanceof UserRole) {
            UserRole userRole = (UserRole) role;
            result = userRole.getActorId() >= UserRole.MIN_VALID_USER_ID;
        }
        return result;
    }

}