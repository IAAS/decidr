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

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.NotificationEvents;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.PasswordResetRequest;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Password;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Searches the user that belongs to a given email address or username. If such
 * a user exists, a password reset request is created and a noticiation email is
 * sent to the user.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class RequestPasswordResetCommand extends AclEnabledCommand {

    private String emailOrUsername;
    private Boolean requestWasCreated = false;

    /**
     * Creates a new RequestPasswordResetCommand.
     * 
     * @param role
     * @param emailOrUsername
     */
    public RequestPasswordResetCommand(Role role, String emailOrUsername) {
        super(role, (Permission) null);
        this.emailOrUsername = emailOrUsername;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        requestWasCreated = false;

        String hql = "select distinct u.id, u.email, p.username, p.firstName, p.LastName "
                + "from User u left join fetch u.userProfile p "
                + "where (u.email = :emailOrUsername) or "
                + "(p.username = :emailOrUsername) limit 1";

        User user = (User) evt.getSession().createQuery(hql).setString(
                "emailOrUsername", emailOrUsername).uniqueResult();

        if (user != null) {
            try {
                PasswordResetRequest request = new PasswordResetRequest();
                request.setUserId(user.getId());
                request.setUser(user);
                request.setAuthKey(Password.getRandomAuthKey());
                request.setCreationDate(DecidrGlobals.getTime().getTime());

                // Overwrite an existing password reset request
                evt.getSession().saveOrUpdate(request);

                NotificationEvents.createdPasswordResetRequest(user);
                requestWasCreated = true;

            } catch (UnsupportedEncodingException e) {
                throw new TransactionException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new TransactionException(e);
            }
        }
    }

    /**
     * @return true if the password reqest has been created an a notification
     *         email has been sent. False if the user with the given email
     *         address or username ordoesn't exist.
     */
    public Boolean getRequestWasCreated() {
        return requestWasCreated;
    }

}
