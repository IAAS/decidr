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

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.Password;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.RegistrationRequest;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Creates a user profile (and if necessary, the user) for the given email
 * address. Registering the same user twice results in a TransactionException.
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

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        if (passwordPlaintext == null || passwordPlaintext.isEmpty()) {
            throw new IllegalArgumentException(
                    "Password cannot be null or empty");
        }

        if (profile == null || profile.getUsername() == null
                || profile.getUsername().isEmpty()) {
            throw new IllegalArgumentException(
                    "Username cannot be null or empty");
        }

        this.email = email;
        this.passwordPlaintext = passwordPlaintext;
        // do not modify the given user profile object, create a copy in case
        // the object is modified by someone else.
        this.profile = new UserProfile();
        try {
            BeanUtils.copyProperties(this.profile, profile);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        registeredUser = null;
        /*
         * Does a user who has the given email or username already exist?
         * 
         * The DecidR username criteria prevent that two users user1 and user2
         * exist where user1.email = user2.username. Should the database be
         * inconsistent, uniqueResult will throw a runtime exception.
         */
        String hql = "from User u "
                + "where (u.userProfile.username = :username) "
                + "or (u.email = :email)";

        User existingUser = (User) evt.getSession().createQuery(hql).setString(
                "username", profile.getUsername()).setString("email", email)
                .uniqueResult();

        if (existingUser == null) {
            /*
             * There is no such user - create one!
             */
            existingUser = new User(email, DecidrGlobals.getTime().getTime());
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    new CreateNewUnregisteredUserCommand(role, existingUser));
        } else {
            /*
             * there is a user with the given email address. Has the user
             * already registered?
             */
            if (existingUser.getRegisteredSince() == null) {
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

        profile.setPasswordSalt(Password.getRandomSalt());
        profile.setPasswordHash(Password.getHash(passwordPlaintext, profile
                .getPasswordSalt()));
        profile.setId(existingUser.getId());
        profile.setUser(existingUser);
        existingUser.setUserProfile(profile);
        existingUser.setRegisteredSince(null);
        evt.getSession().save(profile);
        evt.getSession().update(existingUser);

        RegistrationRequest request = new RegistrationRequest(existingUser,
                DecidrGlobals.getTime().getTime(), Password.getRandomAuthKey());
        evt.getSession().saveOrUpdate(request);
        existingUser.setRegistrationRequest(request);

        registeredUser = existingUser;
    }

    /**
     * @return the registered user
     */
    public User getRegisteredUser() {
        return registeredUser;
    }
}
