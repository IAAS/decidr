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
 * Thrown when a user has set his status to unavailable, but an available user
 * was expected.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class UserUnavailableException extends TransactionException {

    private static final long serialVersionUID = 1L;

    private User user;

    /**
     * @param user
     *            the unavailable user
     */
    public UserUnavailableException(User user) {
        super("The user has set his or her status to unavailable");
        this.user = user;
    }

    /**
     * @return the unavailable user
     */
    public User getUser() {
        return user;
    }
}
