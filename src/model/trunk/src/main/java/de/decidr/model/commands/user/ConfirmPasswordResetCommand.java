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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import sun.util.calendar.CalendarSystem;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.LifetimeValidator;
import de.decidr.model.entities.PasswordResetRequest;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Generates a new password for a user if the given authentication key matches
 * the one in his current password reset request.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class ConfirmPasswordResetCommand extends UserCommand {

    private String authKey = null;

    private Boolean requestExpired = false;

    private String newPassword = null;

    /**
     * Creates a new ConfirmPasswordResetCommand.
     * 
     * @param role
     * @param userId
     */
    public ConfirmPasswordResetCommand(Role role, Long userId, String authKey) {
        super(role, userId);
        if (authKey == null) {
            throw new NullPointerException("authKey must not be null.");
        }
        this.authKey = authKey;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        requestExpired = false;
        newPassword = null;

        PasswordResetRequest request = (PasswordResetRequest) evt.getSession()
                .get(PasswordResetRequest.class, getUserId());

        //FIXME continue here - 
        if ((request == null) || (! authKey.equals(request.getAuthKey()))) {
            throw new EntityNotFoundException(PasswordResetRequest.class,
                    getUserId());
        }

        // is the request still valid?
        Boolean isAlive = LifetimeValidator.isPasswordResetRequestValid(
                request, evt.getSession());

        if (isAlive) {
            // generate a new password
            UserProfile profile = (UserProfile) evt.getSession().get(
                    UserProfile.class, getUserId());

            if (profile == null) {
                throw new EntityNotFoundException(UserProfile.class,
                        getUserId());
            }
        } else {
            // the request has expired
            requestExpired = true;
            // we cannot throw an exception because that would undo the delete.
            evt.getSession().delete(request);
        }
    }

    public String getNewPassword() {
        // TODO Auto-generated method stub
        return null;
    }
}