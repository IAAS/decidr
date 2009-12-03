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

import java.util.Calendar;
import java.util.Date;

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
     * Checks whether a password reset request is still valid.
     * 
     * @param request
     *            the request which should be checked
     * @return true iff the given password reset request is still valid
     */
    public static Boolean isPasswordResetRequestValid(
            PasswordResetRequest request) {
        return requestIsAlive(request.getCreationDate(), DecidrGlobals
                .getSettings().getPasswordResetRequestLifetimeSeconds());
    }

    /**
     * Checks whether an invitation is still valid.
     * 
     * @param invitation
     *            the invitation which should be checked
     * @return true iff the given invitation is still valid
     */
    public static Boolean isInvitationValid(Invitation invitation) {
        return requestIsAlive(invitation.getCreationDate(), DecidrGlobals
                .getSettings().getInvitationLifetimeSeconds());
    }

    /**
     * Checks whether a registration request is still valid.
     * 
     * @param request
     *            the request which should be checked
     * @return true iff the given registration request is still valid.
     */
    public static Boolean isRegistrationRequestValid(RegistrationRequest request) {
        return requestIsAlive(request.getCreationDate(), DecidrGlobals
                .getSettings().getRegistrationRequestLifetimeSeconds());
    }

    /**
     * Checks whether a change email request is still valid.
     * 
     * @param request
     *            the request which should be checked
     * @return true iff the given request is still valid.
     */
    public static Boolean isChangeEmailRequestValid(ChangeEmailRequest request) {
        return requestIsAlive(request.getCreationDate(), DecidrGlobals
                .getSettings().getChangeEmailRequestLifetimeSeconds());
    }

    /**
     * Compares the current date and time to the given creation date and
     * lifetime.
     * 
     * @param creationDate
     *            the date of the creation
     * @param lifetimeSeconds
     *            the lifetime of the object
     * @return true iff the request is still alive/valid
     */
    private static Boolean requestIsAlive(Date creationDate, int lifetimeSeconds) {
        if (creationDate == null) {
            return false;
        } else {
            // request is alive <-> createdDate + lifetime > now
            Calendar maxValidDate = DecidrGlobals.getTime();
            maxValidDate.setTime(creationDate);
            maxValidDate.add(Calendar.SECOND, lifetimeSeconds);
            return maxValidDate.after(DecidrGlobals.getTime());
        }
    }
}
