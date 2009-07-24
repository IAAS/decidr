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
package de.decidr.model.facades;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ObjectProperty;

import de.decidr.model.commands.user.CheckAuthKeyCommand;
import de.decidr.model.commands.user.ConfirmInviationCommand;
import de.decidr.model.commands.user.ConfirmPasswordResetCommand;
import de.decidr.model.commands.user.GetAdministratedWorkflowModelCommand;
import de.decidr.model.commands.user.GetAdminstratedWorkflowInstancesCommand;
import de.decidr.model.commands.user.GetAllUsersCommand;
import de.decidr.model.commands.user.GetHighestUserRoleCommand;
import de.decidr.model.commands.user.GetInvitationCommand;
import de.decidr.model.commands.user.GetJoinedTenantsCommand;
import de.decidr.model.commands.user.GetUserByLoginCommand;
import de.decidr.model.commands.user.GetUserProfileCommand;
import de.decidr.model.commands.user.GetUserRoleForTenantCommand;
import de.decidr.model.commands.user.GetWorkitemsCommand;
import de.decidr.model.commands.user.IsRegisteredCommand;
import de.decidr.model.commands.user.LeaveTenantCommand;
import de.decidr.model.commands.user.RefuseInviationCommand;
import de.decidr.model.commands.user.RegisterUserCommand;
import de.decidr.model.commands.user.RemoveFromTenantCommand;
import de.decidr.model.commands.user.RequestPasswordResetCommand;
import de.decidr.model.commands.user.SetPasswordCommand;
import de.decidr.model.commands.user.SetUserProfileCommand;
import de.decidr.model.commands.user.SetUserPropertyCommand;
import de.decidr.model.entities.PasswordResetRequest;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.entities.WorkItemSummaryView;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.UserRole;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionCoordinator;

/**
 * Provide a simplified interface to the business logic that deals with users.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class UserFacade extends AbstractFacade {

    /**
     * Creates a new UserFacade. All operations will be executed as the given
     * actor.
     * 
     * @param actor
     *            the user which will execute all commands of the facade
     */
    public UserFacade(Role actor) {
        super(actor);
    }

    /**
     * Creates a new user (if necessary) and adds a user profile and a
     * registration request. The following properties are expected to be present
     * in the given Vaadin {@link Item}:
     * <ul>
     * <li>username - the desired username</li>
     * <li>firstName - first name of the user</li>
     * <li>lastName - last name of the user</li>
     * <li>city - name of the city where the user lives</li>
     * <li>street - street where the user lives</li>
     * <li>postalCode - postal code of the city</li>
     * </ul>
     * 
     * @param email
     *            email address of user to register
     * @param passwordPlaintext
     *            the password as plain text string
     * @param userProfile
     *            the user profile data
     * @return The ID of the user that has been registered
     * @throws TransactionException
     *             if the transaction is aborted for any reason
     * @throws NullPointerException
     *             if at least one of the required properties is missing.
     */
    public Long registerUser(String email, String passwordPlaintext,
            Item userProfile) throws TransactionException, NullPointerException {

        // retrieve needed properties from Vaadin item.
        String firstName = userProfile.getItemProperty("firstName").getValue()
                .toString();
        String lastName = userProfile.getItemProperty("lastname").getValue()
                .toString();
        String city = userProfile.getItemProperty("city").getValue().toString();
        String street = userProfile.getItemProperty("street").getValue()
                .toString();
        String postalCode = userProfile.getItemProperty("postalCode")
                .getValue().toString();
        String username = userProfile.getItemProperty("username").getValue()
                .toString();

        // create the new user profile
        UserProfile profile = new UserProfile();
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setCity(city);
        profile.setStreet(street);
        profile.setPostalCode(postalCode);
        profile.setUsername(username);

        RegisterUserCommand cmd = new RegisterUserCommand(actor, email,
                passwordPlaintext, profile);

        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        return cmd.getRegisteredUser().getId();
    }

    /**
     * Returns the user id that belongs to a given username or email / password
     * combination iff the account exists and the password matches.
     * 
     * @param emailOrUsername
     *            can be either an email address or the user's username.
     * @param passwordPlaintext
     *            the account password as plain text (no hash).
     * @return user ID
     * @throws TransactionException
     *             iff the transaction
     * @throws EntityNotFoundException
     *             iff no such account exists or the password doesn't match.
     */
    public Long getUserIdByLogin(String emailOrUsername,
            String passwordPlaintext) throws TransactionException,
            EntityNotFoundException {

        GetUserByLoginCommand cmd = new GetUserByLoginCommand(actor,
                emailOrUsername, passwordPlaintext);

        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        if (cmd.getPasswordCorrect()) {
            return cmd.getUser().getId();
        } else {
            throw new EntityNotFoundException(User.class);
        }
    }

    /**
     * Checks whether the given authentication key matches the user's
     * authentication key in the database.
     * 
     * @param userId
     *            the user to check
     * @param authKey
     *            the authentication key to check
     * @return <code>true</code> iff the authKey matches the given user's
     *         authentication key.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             iff the given user does not exist.
     */
    public Boolean authKeyMatches(Long userId, String authKey)
            throws TransactionException, EntityNotFoundException {
        CheckAuthKeyCommand cmd = new CheckAuthKeyCommand(actor, userId,
                authKey);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        return cmd.getAuthKeyMatches();
    }

    /**
     * Sets the email address of the given user to the new value.
     * 
     * @param userId
     *            the id of the user whose email address should be set
     * @param newEmail
     *            the new email address
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    public void setEmailAddress(Long userId, String newEmail)
            throws TransactionException {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("email", newEmail);

        SetUserPropertyCommand cmd = new SetUserPropertyCommand(actor, userId,
                properties);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
    }

    /**
     * Sets the disabled date of the user, effectively preventing the user from
     * logging in again. To re-activate the user account, set his disabled date
     * to null.
     * 
     * @param userId
     *            the id of the user whose disable date should be set
     * @param date
     *            to re-active the user, set this parameter to null.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    public void setDisableSince(Long userId, Date date)
            throws TransactionException {
        Map<String, Date> properties = new HashMap<String, Date>();
        properties.put("disabledSince", date);

        SetUserPropertyCommand cmd = new SetUserPropertyCommand(actor, userId,
                properties);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
    }

    /**
     * Sets the unavailable date of the user, which flags the user as
     * unavailable for workflow participation. To flag the user as available,
     * set his unavailable date to null.
     * 
     * @param userId
     *            the id of the user whose unavailable date should be set
     * @param date
     *            to flag the user as available, set this parameter to null.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    public void setUnavailableSince(Long userId, Date date)
            throws TransactionException {
        Map<String, Date> properties = new HashMap<String, Date>();
        properties.put("unavailableSince", date);

        SetUserPropertyCommand cmd = new SetUserPropertyCommand(actor, userId,
                properties);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
    }

    /**
     * Sets a new password for the given user if the provided current password
     * is correct.
     * 
     * @param userId
     *            the id of the user whose password should be set
     * @param oldPassword
     *            the user's current password (plaintext).
     * @param newPassword
     *            the new password (plaintext).
     * @return true if the password was changed to newPassword, false if
     *         oldPassword doesn't match.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    public Boolean setPassword(Long userId, String oldPassword,
            String newPassword) throws TransactionException {
        SetPasswordCommand cmd = new SetPasswordCommand(actor, userId,
                oldPassword, newPassword);

        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        return cmd.getPasswordWasChanged();
    }

    /**
     * Creates a new password reset request for the given username or email
     * address. A notification email is sent to the user
     * 
     * @param emailOrUsername
     *            the email address or username whose password should be reset
     * @return true iff the notification mail has been sent to the user, false
     *         if no user with the given username or email exists.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    public Boolean requestPasswordReset(String emailOrUsername)
            throws TransactionException {
        RequestPasswordResetCommand cmd = new RequestPasswordResetCommand(
                actor, emailOrUsername);

        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        return cmd.getRequestWasCreated();
    }

    /**
     * Updates an existing user profile with new data. The following properties
     * are expected to be present in the given Vaadin {@link Item}:
     * <ul>
     * <li>firstName - first name of the user</li>
     * <li>lastName - last name of the user</li>
     * <li>city - name of the city where the user lives</li>
     * <li>street - street where the user lives</li>
     * <li>postalCode - postal code of the city</li>
     * </ul>
     * 
     * Other properties will be ignored.
     * 
     * @param userId
     *            the id of the user whose profile should be set
     * @param newProfile
     *            the profile by which the old one should be replaced
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             iff the given user doesn't exist or doesn't have a user
     *             profile.
     * @throws NullPointerException
     *             if at least one of the required properties is missing.
     */
    public void setProfile(Long userId, Item newProfile)
            throws TransactionException, EntityNotFoundException,
            NullPointerException {

        // retrieve needed properties from Vaadin item.
        String firstName = newProfile.getItemProperty("firstName").getValue()
                .toString();
        String lastName = newProfile.getItemProperty("lastname").getValue()
                .toString();
        String city = newProfile.getItemProperty("city").getValue().toString();
        String street = newProfile.getItemProperty("street").getValue()
                .toString();
        String postalCode = newProfile.getItemProperty("postalCode").getValue()
                .toString();

        // create the new user profile
        UserProfile profile = new UserProfile();
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setCity(city);
        profile.setStreet(street);
        profile.setPostalCode(postalCode);

        // We're not using the SetUserPropertyCommand because that might
        // potentially insert a user profile even if the user didn't previously
        // have one, making him a registered user without actually registering.
        SetUserProfileCommand cmd = new SetUserProfileCommand(actor, userId,
                profile);

        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
    }

    /**
     * Dissociates the given user from the given tenant. This is only possible
     * if the user is not participating in any workflow that belongs to that
     * tenant.
     * 
     * @param userId
     *            the ID of the user which should leave the tenant
     * @param tenantId
     *            the ID of the tenant which the user should leave
     * @return true iff the tenant was successfully left.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    public Boolean leaveTenant(Long userId, Long tenantId)
            throws TransactionException {
        LeaveTenantCommand cmd = new LeaveTenantCommand(actor, userId, tenantId);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        return cmd.getLeftTenant();
    }

    /**
     * Removes the user from the given tenant and notifies the user.
     * 
     * @param userId
     *            the ID of the user which should be removed from tenant
     * @param tenantId
     *            the ID of the tenent where the user should be removed
     * @return true iff the user was sucessfully removed from the tenant.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    public Boolean removeFromTenant(Long userId, Long tenantId)
            throws TransactionException {
        RemoveFromTenantCommand cmd = new RemoveFromTenantCommand(actor,
                userId, tenantId);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        return cmd.getLeftTenant();
    }

    /**
     * Sets the password of the user to a new (generated) password and deletes
     * the request from the database if authKey matches the authentication key
     * in the user's current password reset request. The user is notified via
     * email about the new password.
     * 
     * @param userId
     *            the id of the user whose request should be confirmed
     * @param authKey
     *            the auth key which was sent to user by requesting the reset
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             iff the user doesn't exist or has no user profile or has no
     *             password reset request or the authentication key doesn't
     *             match or the request has expired.
     */
    public void confirmPasswordReset(Long userId, String authKey)
            throws TransactionException, EntityNotFoundException {
        ConfirmPasswordResetCommand cmd = new ConfirmPasswordResetCommand(
                actor, userId, authKey);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        if (cmd.getRequestExpired()) {
            throw new EntityNotFoundException(PasswordResetRequest.class);
        }
    }

    // FIXME Implement Me
    public void confirmRegistration(Long userId, String authKey)
            throws TransactionException {
        throw new UnsupportedOperationException();
    }

    // FIXME Implement Me
    public void confirmChangeEmailRequest(Long userId, String requestAuthKey)
            throws TransactionException {
        throw new UnsupportedOperationException();
    }

    /**
     * Confirms the given invitation.
     * 
     * @param invitationId
     *            the id of the invitation which should be confirmed
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    public void confirmInvitation(Long invitationId)
            throws TransactionException {
        ConfirmInviationCommand command = new ConfirmInviationCommand(actor,
                invitationId);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Refuses the given invitation an sends a notification email to the
     * inviting user.
     * 
     * @param invitationId
     *            invitation to refuse
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    public void refuseInviation(Long invitationId) throws TransactionException {

        RefuseInviationCommand command = new RefuseInviationCommand(actor,
                invitationId);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Returns the user profile of the given user that contains the following
     * properties:
     * <ul>
     * <li>city: String - city part of user address</li>
     * <li>firstName: String</li>
     * <li>lastName: String</li>
     * <li>postalCode: String - postal code part of user address</li>
     * <li>street: String - street part of user address</li>
     * <li>username: String</li>
     * </ul>
     * 
     * @param userId
     *            the id of the user whose profile should be returned
     * @return Vaadin item which is described above
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    public Item getUserProfile(Long userId) throws TransactionException {

        String[] properties = { "city", "firstName", "lastName", "postalCode",
                "street", "username" };

        GetUserProfileCommand command = new GetUserProfileCommand(actor, userId);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return new BeanItem(command.getResult(), properties);
    }

    /**
     * Returns a list of all users as vaadin items with the following
     * properties:
     * <ul>
     * <li>id: Long</li>
     * <li>email: String</li>
     * <li>firstName: String</li>
     * <li>lastName: String</li>
     * <li>username</li>
     * </ul>
     * 
     * @param filters {@link Filter}
     *            
     * @param paginator
     *            {@link Paginator}
     * @return list of vaadin items which are described above
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    @SuppressWarnings("unchecked")
    public List<Item> getAllUsers(List<Filter> filters, Paginator paginator)
            throws TransactionException {

        GetAllUsersCommand command = new GetAllUsersCommand(actor, filters,
                paginator);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);

        List<User> inList = command.getResult();
        Item item;

        // build the Vaadin item
        String[] properties = { "id", "email" };

        List<Item> result = new ArrayList();
        for (User user : inList) {

            item = new BeanItem(user, properties);

            if (user.getUserProfile() != null) {
                item.addItemProperty("firstName", new ObjectProperty(user
                        .getUserProfile().getFirstName(), String.class));
                item.addItemProperty("lastName", new ObjectProperty(user
                        .getUserProfile().getFirstName(), String.class));
                item.addItemProperty("username", new ObjectProperty(user
                        .getUserProfile().getFirstName(), String.class));
            } else {
                item.addItemProperty("firstName", new ObjectProperty(null,
                        String.class));
                item.addItemProperty("lastName", new ObjectProperty(null,
                        String.class));
                item.addItemProperty("username", new ObjectProperty(null,
                        String.class));
            }
        }

        return result;
    }

    /**
     * Returns the highest user role of the given system.
     * 
     * @param userId
     *            the ID of the user whose highest role should be requested
     * @return UserRole highest user role of the user
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    public Class<? extends UserRole> getHighestUserRole(Long userId)
            throws TransactionException {

        GetHighestUserRoleCommand command = new GetHighestUserRoleCommand(
                actor, userId);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);

        return command.getResult();
    }

    /**
     * Returns the highest role of the user in the given tenant in the result
     * variable. If the user is not even a tenant member the result will be
     * <code>null</code>.
     * 
     * @param userId
     *            the ID of the user whose role should be requested for the
     *            given tenant
     * @param tenantId
     *            the ID of the tenant where the role should be appointed
     * @return UserRole highest user role of the user for the given tenant
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    public Class<? extends UserRole> getUserRoleForTenant(Long userId,
            Long tenantId) throws TransactionException {

        GetUserRoleForTenantCommand command = new GetUserRoleForTenantCommand(
                actor, userId, tenantId);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);

        return command.getResult();
    }

    /**
     * Returns all administrated workflow instances of the given user as Item.
     * Each item has the following properties:<br>
     * <ul>
     * <li>id</li>
     * <li>startedDate</li>
     * <li>completedDate</li>
     * <li>model</li>
     * </ul>
     * 
     * @param userId
     *            the id of the user whose administrated workflow models should be requested
     * @return List of workflow models which are administrated by the given user
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    @SuppressWarnings("unchecked")
    public List<Item> getAdminstratedWorkflowInstances(Long userId)
            throws TransactionException {

        GetAdminstratedWorkflowInstancesCommand command = new GetAdminstratedWorkflowInstancesCommand(
                actor, userId);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);

        List<WorkflowInstance> inList = command.getResult();

        // build the Vaadin item
        String[] properties = { "id", "startedDate", "completedDate" };

        List<Item> result = new ArrayList();
        for (WorkflowInstance instance : inList) {

            Item item = new BeanItem(instance, properties);
            item.addItemProperty("model", new ObjectProperty(instance
                    .getDeployedWorkflowModel().getName()));

            result.add(item);

        }

        return result;
    }

    /**
     * Returns all tenants the given user is member of as item with the
     * following properties:
     * <ul>
     * <li>id - tenant id</li>
     * <li>name - tenant name</li>
     * </ul>
     * 
     * @param userId
     *            the ID of the user whose joined tenants should be requested
     * @return Vaadin items representing the joined tenants.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    @SuppressWarnings("unchecked")
    public List<Item> getJoinedTenants(Long userId) throws TransactionException {

        GetJoinedTenantsCommand command = new GetJoinedTenantsCommand(actor,
                userId);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);

        List<Tenant> tenants = command.getResult();

        // build the Vaadin item
        String[] properties = { "id", "name" };

        List<Item> result = new ArrayList();
        for (Tenant tenant : tenants) {
            result.add(new BeanItem(tenant, properties));
        }

        return result;
    }

    /**
     * Returns the workflow models which the given user administrates. The item
     * properties are:
     * <ul>
     * <li>id - workflow model id</li>
     * <li>name - workflow model name</li>
     * <li>description - workflow model description</li>
     * </ul>
     * 
     * @param userId
     *            the ID of the user whose administrated workflow models should
     *            be requested
     * @return list of workflow models which are administrated by the given user
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    public List<Item> getAdministratedWorkflowModels(Long userId)
            throws TransactionException {

        GetAdministratedWorkflowModelCommand command = new GetAdministratedWorkflowModelCommand(
                actor, userId);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);

        List<WorkflowModel> models = command.getResult();
        List<Item> result = new ArrayList<Item>();

        String[] properties = { "id", "name", "description" };

        for (WorkflowModel model : models) {
            result.add(new BeanItem(model, properties));
        }

        return result;
    }

    /**
     * Returns the workitems of the given user as List<Item> with the following
     * properties:<br>
     * <ul>
     * <li>creationDate</li>
     * <li>userId</li>
     * <li>id</li>
     * <li>tenantName</li>
     * <li>workItemName</li>
     * <li>workItemStatus</li>
     * </ul>
     * 
     * @param userId
     *            the ID of the user whose workitems should be requested
     * @param filters
     *            {@link Filter}
     * @param paginator
     *            {@link Paginator}
     * @return list ob vaadin item described above
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    @SuppressWarnings("unchecked")
    public List<Item> getWorkItems(Long userId, List<Filter> filters,
            Paginator paginator) throws TransactionException {

        List<WorkItemSummaryView> inList;
        List<Item> outList = new ArrayList();

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        GetWorkitemsCommand command = new GetWorkitemsCommand(actor, userId,
                filters, paginator);

        tac.runTransaction(command);
        inList = command.getResult();

        for (WorkItemSummaryView model : inList) {
            outList.add(new BeanItem(model));
        }

        return outList;
    }

    /**
     * Returns information about the given invitaion. The Vaadin item contains
     * the following properties:
     * <ul>
     * <li>senderFirstName: String - first name of the sender</li>
     * <li>senderLastName: String - last name of the sender</li>
     * <li>receiverFirstName: String - first name of the receiver</li>
     * <li>receiverLastName: String - last name of the receiver</li>
     * <li>administratedWorkflowModelName: String - name of the administrated
     * workflow model</li>
     * <li>joinTenantName: String - name of the tenant which should be joined</li>
     * <li>workflowInstanceId: Long - id of the participation instance</li>
     * <li>creationDate: Date - Date on which the invitation was created</li>
     * </ul>
     * 
     * @param invitationId
     *            the ID of the invitation which should be returned
     * @return Vaadin item described above
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    public Item getInvitation(Long invitationId) throws TransactionException {
        GetInvitationCommand command = new GetInvitationCommand(actor,
                invitationId);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);

        return new BeanItem(command.getResult());
    }

    /**
     * Returns <code>true</code> if the given user is registered else
     * <code>false</code>.
     * 
     * @param userId
     *            the ID of the user which should be checked if he's registered
     *            or not
     * @return <code>true</code> if the given user is registered else
     *         <code>false</code>.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    public Boolean isRegistered(Long userId) throws TransactionException {
        IsRegisteredCommand command = new IsRegisteredCommand(actor, userId);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);

        return command.getResult();
    }
}