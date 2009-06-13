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

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Password;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Finds the user that belongs to the given username|email / password
 * combination.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class GetUserByLoginCommand extends AclEnabledCommand {

    private User user;
    private String emailOrUsername;
    private String passwordPlaintext;

    /**
     * Creates a new GetUserByLoginCommand.
     * <p>
     * This command finds the user that belongs to the given username|email /
     * password combination.
     * 
     * @param role
     * @param emailOrUsername
     * @param passwordPlaintext
     */
    public GetUserByLoginCommand(Role role, String emailOrUsername,
            String passwordPlaintext) {
        super(role, (Permission) null);
        this.emailOrUsername = emailOrUsername;
        this.passwordPlaintext = passwordPlaintext;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        // find the existing user
        Criteria crit = evt.getSession().createCriteria(User.class, "u");
        crit.createAlias("userProfile", "p", Criteria.LEFT_JOIN);
        crit.add(Restrictions.or(Restrictions.eq("u.email", emailOrUsername),
                Restrictions.eq("p.username", emailOrUsername)));

        User existingUser = (User) crit.uniqueResult();

        if (existingUser == null) {
            // user account not found
            throw new EntityNotFoundException(User.class);
        }

        try {

            String hash = Password.getHash(passwordPlaintext, existingUser
                    .getUserProfile().getPasswordSalt());

            if (hash.equals(existingUser.getUserProfile().getPasswordHash())) {
                // given password is correct
                user = existingUser;
            } else {
                // given password is wrong
                throw new EntityNotFoundException(User.class);
            }

        } catch (NoSuchAlgorithmException e) {
            throw new TransactionException(e);
        } catch (UnsupportedEncodingException e) {
            throw new TransactionException(e);
        }
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

}
