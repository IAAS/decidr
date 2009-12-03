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

package de.decidr.model.exceptions;

import de.decidr.model.entities.User;

/**
 * Thrown when a user account has been disabled by the super admin but an active
 * account is required.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class UserDisabledException extends TransactionException {

    private static final long serialVersionUID = 1L;

    private User user;

    /**
     * @param user
     *            the disabled user
     */
    public UserDisabledException(User user) {
        super("The user has been disabled by the super admin");
        this.user = user;
    }

    /**
     * @return the disabled user
     */
    public User getUser() {
        return user;
    }
}
