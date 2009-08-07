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
package de.decidr.model.acl.roles;


/**
 * Represents a user that is requesting access to a permission.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class UserRole extends BasicRole {

    /**
     * The lowest user id that can belong to a logged in user.
     */
    public static final Long MIN_VALID_USER_ID = 0L;

    public static final Long UNKNOWN_USER_ID = -1L;

    /**
     * Constructor.
     * 
     * @param userId
     */
    public UserRole(Long userId) {
        super(userId);
    }

    /**
     * Creates a new UserRole with its actor id set to unknown;
     * 
     */
    public UserRole() {
        this(UNKNOWN_USER_ID);
    }
}