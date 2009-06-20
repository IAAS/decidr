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

package de.decidr.model;

import org.hibernate.Session;

import de.decidr.model.entities.ChangeEmailRequest;
import de.decidr.model.entities.Invitation;
import de.decidr.model.entities.PasswordResetRequest;
import de.decidr.model.entities.RegistrationRequest;

/**
 * Uses the global system settings to determine whether the lifetime of certain
 * objects such as invitations or password reset requests has expired.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class LifetimeValidator {

    /**
     * TODO add comment
     * 
     * @param request
     * @param session
     * @return
     */
    public static Boolean isPasswordResetRequestValid(
            PasswordResetRequest request, Session session) {
        return false;
    }

    /**
     * TODO add comment
     * 
     * @param invitation
     * @param session
     * @return
     */
    public static Boolean isInvitationValid(Invitation invitation,
            Session session) {
        return false;
    }

    /**
     * TODO add comment
     * 
     * @param request
     * @param session
     * @return
     */
    public static Boolean isRegistrationRequestValid(
            RegistrationRequest request, Session session) {
        return false;
    }

    /**
     * TODO add comment
     * 
     * @param request
     * @param session
     * @return
     */
    public static Boolean isChangeEmailRequestValid(ChangeEmailRequest request,
            Session session) {
        return false;
    }

}
