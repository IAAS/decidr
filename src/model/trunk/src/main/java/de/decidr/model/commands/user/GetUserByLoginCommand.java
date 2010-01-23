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

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.Password;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.Login;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Finds the user that belongs to the given username or a email/password
 * combination.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class GetUserByLoginCommand extends AclEnabledCommand {

    private User user;
    private String emailOrUsername;
    private String passwordPlaintext;
    private Boolean passwordCorrect = false;

    /**
     * Creates a new GetUserByLoginCommand.
     * <p>
     * This command finds the user that belongs to the given username|email /
     * password combination.
     * 
     * @param role
     *            the user which executes the command
     * @param emailOrUsername
     *            the users username or email address
     * @param passwordPlaintext
     *            the users password input in plain text
     * @throws IllegalArgumentException
     *             if emailOrUsername or passwordPlaintext is <code>null</code>
     */
    public GetUserByLoginCommand(Role role, String emailOrUsername,
            String passwordPlaintext) {
        super(role, (Permission) null);

        if ((emailOrUsername == null) || emailOrUsername.isEmpty()) {
            throw new IllegalArgumentException(
                    "Email/username cannot be null or empty.");
        }

        if ((passwordPlaintext == null) || passwordPlaintext.isEmpty()) {
            throw new IllegalArgumentException(
                    "Password cannot be null or empty.");
        }

        this.emailOrUsername = emailOrUsername;
        this.passwordPlaintext = passwordPlaintext;
    }

    /**
     * @return whether the given password was correct.
     */
    public Boolean getPasswordCorrect() {
        return passwordCorrect;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    @Override
    public void transactionAllowed(TransactionStartedEvent evt)
            throws TransactionException {
        passwordCorrect = false;

        // find the existing user
        Criteria crit = evt.getSession().createCriteria(User.class, "u");
        crit.createAlias("userProfile", "p", CriteriaSpecification.LEFT_JOIN);
        // the DecidR username criteria do not allow non-alphanumeric characters
        // in usernames, so the case user1.username = user2.email should not
        // occur. Otherwise, uniqueResult() will throw a runtime exception.
        crit.add(Restrictions.or(Restrictions.eq("u.email", emailOrUsername),
                Restrictions.eq("p.username", emailOrUsername)));

        User existingUser = (User) crit.uniqueResult();

        if (existingUser == null) {
            // user account not found
            throw new EntityNotFoundException(User.class);
        }

        String hash = Password.getHash(passwordPlaintext, existingUser
                .getUserProfile().getPasswordSalt());

        // is the password correct?
        passwordCorrect = hash.equals(existingUser.getUserProfile()
                .getPasswordHash());
        user = existingUser;

        // log the login to the database
        Login thisLogin = new Login();
        thisLogin.setLoginDate(DecidrGlobals.getTime().getTime());
        thisLogin.setSuccess(passwordCorrect);
        thisLogin.setUser(existingUser);
        evt.getSession().save(thisLogin);
    }
}
