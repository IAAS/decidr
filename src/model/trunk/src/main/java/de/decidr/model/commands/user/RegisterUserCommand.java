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
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.RegistrationRequest;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Password;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Creates a user profile (and if necessary, the user) for the given email
 * address.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class RegisterUserCommand extends AclEnabledCommand {

    private User registeredUser;
    private String email;
    private String passwordPlaintext;
    private UserProfile profile;

    /**
     * Creates a new RegisterUserCommand.
     * <p>
     * This command creates a user profile (and if necessary, the user) for the
     * given email address.
     * 
     * @param role
     *            user which executes the command
     * @param email
     *            the email address of the user which should be registered
     * @param passwordPlaintext
     *            the password as plain text of the user which should be
     *            registered
     * @param profile
     *            profile data to apply to the new user account.
     */
    public RegisterUserCommand(Role role, String email,
            String passwordPlaintext, UserProfile profile) {
        super(role, (Permission) null);
        this.email = email;
        this.passwordPlaintext = passwordPlaintext;
        // do not modify the incoming object, create a copy.
        this.profile = new UserProfile();
        this.profile.setFirstName(profile.getFirstName());
        this.profile.setLastName(profile.getLastName());
        this.profile.setCity(profile.getCity());
        this.profile.setStreet(profile.getStreet());
        this.profile.setPostalCode(profile.getPostalCode());
        this.profile.setUsername(profile.getUsername());
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        /*
         * does a user who has the given email or username already exist?
         */
        Criteria crit = evt.getSession().createCriteria(User.class, "u");
        crit.createAlias("userProfile", "p", CriteriaSpecification.LEFT_JOIN);
        crit.add(Restrictions.or(Restrictions.eq("u.email", email),
                Restrictions.eq("p.username", profile.getUsername())));

        User existingUser = (User) crit.uniqueResult();

        if (existingUser == null) {
            /*
             * There is no such user - create one!
             */
            existingUser = new User();
            existingUser.setEmail(email);
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    new CreateNewUnregisteredUserCommand(role, existingUser));
        } else {
            /*
             * there is a user with the given email address. Has the user
             * already registered?
             */
            if (existingUser.getUserProfile() == null) {
                /*
                 * The user has not yet registered - nothing needs to be done.
                 */
            } else {
                /*
                 * The user has already registered, don't register again!
                 */
                throw new TransactionException(String.format(
                        "%1$s is already registered as %2$s", email,
                        existingUser.getUserProfile().getUsername()));
            }
        }

        /*
         * Now existingUser is an existing user that has no profile - lets
         * assign his profile and create a registration request!
         */
        try {
            profile.setPasswordSalt(Password.getRandomSalt());
            profile.setPasswordHash(Password.getHash(passwordPlaintext, profile
                    .getPasswordSalt()));
            profile.setUserId(existingUser.getId());
            existingUser.setUserProfile(profile);
            evt.getSession().update(existingUser);

            RegistrationRequest request = new RegistrationRequest();

            request.setAuthKey(Password.getRandomAuthKey());
            request.setCreationDate(DecidrGlobals.getTime().getTime());
            request.setUser(existingUser);
            evt.getSession().saveOrUpdate(request);

        } catch (UnsupportedEncodingException e) {
            throw new TransactionException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new TransactionException(e);
        }

        registeredUser = existingUser;
    }

    /**
     * @return the registered user
     */
    public User getRegisteredUser() {
        return registeredUser;
    }
}
