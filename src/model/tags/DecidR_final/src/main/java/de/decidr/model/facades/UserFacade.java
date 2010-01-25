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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.acl.roles.TenantAdminRole;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.acl.roles.WorkflowAdminRole;
import de.decidr.model.annotations.AllowedRole;
import de.decidr.model.commands.user.CheckAuthKeyCommand;
import de.decidr.model.commands.user.ConfirmChangeEmailRequestCommand;
import de.decidr.model.commands.user.ConfirmInvitationCommand;
import de.decidr.model.commands.user.ConfirmPasswordResetCommand;
import de.decidr.model.commands.user.ConfirmRegistrationCommand;
import de.decidr.model.commands.user.GetAdministratedWorkflowInstancesCommand;
import de.decidr.model.commands.user.GetAdministratedWorkflowModelsCommand;
import de.decidr.model.commands.user.GetAllUsersCommand;
import de.decidr.model.commands.user.GetHighestUserRoleCommand;
import de.decidr.model.commands.user.GetInvitationCommand;
import de.decidr.model.commands.user.GetJoinedTenantsCommand;
import de.decidr.model.commands.user.GetUserByLoginCommand;
import de.decidr.model.commands.user.GetUserPropertiesCommand;
import de.decidr.model.commands.user.GetUserRoleForTenantCommand;
import de.decidr.model.commands.user.GetUserWithProfileCommand;
import de.decidr.model.commands.user.GetWorkItemsCommand;
import de.decidr.model.commands.user.IsUserRegisteredCommand;
import de.decidr.model.commands.user.LeaveTenantCommand;
import de.decidr.model.commands.user.RefuseInvitationCommand;
import de.decidr.model.commands.user.RegisterUserCommand;
import de.decidr.model.commands.user.RemoveFromTenantCommand;
import de.decidr.model.commands.user.RequestChangeEmailCommand;
import de.decidr.model.commands.user.RequestPasswordResetCommand;
import de.decidr.model.commands.user.SetCurrentTenantCommand;
import de.decidr.model.commands.user.SetDisabledCommand;
import de.decidr.model.commands.user.SetPasswordCommand;
import de.decidr.model.commands.user.SetUserProfileCommand;
import de.decidr.model.commands.user.SetUserPropertyCommand;
import de.decidr.model.entities.InvitationView;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.entities.WorkItemSummaryView;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.AuthKeyException;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.RequestExpiredException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.transactions.HibernateTransactionCoordinator;

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
     * @throws IllegalArgumentException
     *             if userId or authKey is <code>null</code>
     */
    @AllowedRole(UserRole.class)
    public Boolean authKeyMatches(Long userId, String authKey)
            throws TransactionException, EntityNotFoundException {
        CheckAuthKeyCommand cmd = new CheckAuthKeyCommand(actor, userId,
                authKey);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        return cmd.getAuthKeyMatches();
    }

    /**
     * Changes the email address of the given user and deletes the corresponding
     * change email request from the database if there is a pending change email
     * request and the given authentication key is correct. If the
     * authentication key is incorrect, an {@link AuthKeyException} is thrown
     * and the request remains unchanged.
     * 
     * @param userId
     *            ID of the user whose request should be treated
     * @param requestAuthKey
     *            the auth key which allows the user to change the address
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws AuthKeyException
     *             iff the given authentication key is incorrect
     * @throws RequestExpiredException
     *             iff the request has expired
     * @throws EntityNotFoundException
     *             iff the user does not exist or has no pending change email
     *             request.
     * @throws IllegalArgumentException
     *             if userId is <code>null</code> or if requestAuthKey is
     *             <code>null</code> or empty.
     */
    @AllowedRole(UserRole.class)
    public void confirmChangeEmailRequest(Long userId, String requestAuthKey)
            throws TransactionException {
        ConfirmChangeEmailRequestCommand command = new ConfirmChangeEmailRequestCommand(
                actor, userId, requestAuthKey);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        if (command.isRequestExpired()) {
            throw new RequestExpiredException();
        }
    }

    /**
     * Confirms the given invitation.
     * 
     * @param invitationId
     *            the id of the invitation which should be confirmed
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws RequestExpiredException
     *             iff the invitation has expired.
     * @throws EntityNotFoundException
     *             iff the invitation does not exist.
     * @throws IllegalArgumentException
     *             if invitationId is <code>null</code>
     */
    @AllowedRole(UserRole.class)
    public void confirmInvitation(Long invitationId)
            throws TransactionException {
        ConfirmInvitationCommand command = new ConfirmInvitationCommand(actor,
                invitationId);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Sets the password of the user to a new (generated) password and deletes
     * the request from the database if authKey matches the authentication key
     * in the user's current password reset request. The user is notified via
     * email about the new password. Successfully confirmed requests as well as
     * expired requests are removed from the database.
     * 
     * @param userId
     *            the id of the user whose request should be confirmed
     * @param authKey
     *            the auth key which was sent to user by requesting the reset
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             iff the user doesn't exist or has no user profile or has no
     *             password reset request.
     * @throws AuthKeyException
     *             if the authentication key doesn't match
     * @throws RequestExpiredException
     *             if the request has expired
     * @throws IllegalArgumentException
     *             if userId is <code>null</code> or if authKey is
     *             <code>null</code> or empty.
     */
    @AllowedRole(UserRole.class)
    public void confirmPasswordReset(Long userId, String authKey)
            throws TransactionException, EntityNotFoundException {
        ConfirmPasswordResetCommand cmd = new ConfirmPasswordResetCommand(
                actor, userId, authKey);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        if (cmd.getRequestExpired()) {
            throw new RequestExpiredException();
        }
    }

    /**
     * Creates the user profile for the given user and removes the auth key.
     * After that the user will be registered. If the user is already
     * registered, this method fails with an EntityNotFoundException.
     * Successfully confirmed requests as well as expired requests are removed
     * from the database.
     * 
     * @param userId
     *            ID of the user whose registration should be treated
     * @param authKey
     *            secret key which allows the user to confirm the registration
     *            (was sent via email to the user)
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             if the user does not exist or if the user has no pending
     *             registration request.
     * @throws AuthKeyException
     *             if the authentication key doesn't match
     * @throws RequestExpiredException
     *             if the request has expuired.
     * @throws IllegalArgumentException
     *             if authKey is <code>null</code> or empty or if userId is
     *             <code>null</code>
     */
    @AllowedRole(UserRole.class)
    public void confirmRegistration(Long userId, String authKey)
            throws TransactionException {
        ConfirmRegistrationCommand command = new ConfirmRegistrationCommand(
                actor, userId, authKey);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        if (command.isRequestExpired()) {
            throw new RequestExpiredException();
        }
    }

    /**
     * Returns all workflow instances which the given user administers.<br>
     * Preloaded foreign key properties:
     * <ul>
     * <li>deployedWorkflowModel</li>
     * </ul>
     * 
     * @param userId
     *            the id of the user whose administrated workflow models should
     *            be requested
     * @return list of workflow models which are administered by the given user.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws IllegalArgumentException
     *             if userId is <code>null</code>.
     * @throws EntityNotFoundException
     *             if the given user does not exist.
     */
    @AllowedRole(WorkflowAdminRole.class)
    public List<WorkflowInstance> getAdministratedWorkflowInstances(Long userId)
            throws TransactionException {
        GetAdministratedWorkflowInstancesCommand command = new GetAdministratedWorkflowInstancesCommand(
                actor, userId);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return command.getResult();
    }

    /**
     * Returns the workflow models which the given user administers.
     * 
     * <br>
     * Preloaded foreign key properties:
     * <ul>
     * <li>deployedWorkflowModel</li>
     * </ul>
     * 
     * @param userId
     *            the ID of the user whose administrated workflow models should
     *            be requested
     * @param filters
     *            optional (nullable) list of filters to apply
     * @param paginator
     *            optional (nullable) paginator
     * @return list of workflow models which are administered by the given user
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws IllegalArgumentException
     *             if userId is <code>null</code>.
     * @throws EntityNotFoundException
     *             if the user does not exist.
     */
    @AllowedRole(WorkflowAdminRole.class)
    public List<WorkflowModel> getAdministratedWorkflowModels(Long userId,
            List<Filter> filters, Paginator paginator)
            throws TransactionException {
        GetAdministratedWorkflowModelsCommand command = new GetAdministratedWorkflowModelsCommand(
                actor, userId, filters, paginator);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return command.getResult();
    }

    /**
     * Returns a list of all users that match the given filter criteria.<br>
     * Preloaded foreign key properties:
     * <ul>
     * <li>userProfile</li>
     * </ul>
     * 
     * @param filters
     *            a {@link Filter}
     * @param paginator
     *            a {@link Paginator}
     * @return list of all users
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @see Filter
     * @see Paginator
     */
    @AllowedRole(SuperAdminRole.class)
    public List<User> getAllUsers(List<Filter> filters, Paginator paginator)
            throws TransactionException {
        GetAllUsersCommand command = new GetAllUsersCommand(actor, filters,
                paginator);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return command.getResult();
    }

    /**
     * Returns the ID of the last tenant that the user has switched to
     * <em>(can be <code>null</code>!)</em>
     * 
     * @param userId
     *            the ID of the user whose current tenant should be retrieved
     * @return the ID of the current tenant or <code>null</code> if the user has
     *         never switched to a tenant or the tenant has been deleted.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason
     * @throws EntityNotFoundException
     *             if the user does not exist.
     * @throws IllegalArgumentException
     *             if userId is <code>null</code>
     */
    @AllowedRole(UserRole.class)
    public Long getCurrentTenantId(Long userId) throws TransactionException {
        GetUserPropertiesCommand cmd = new GetUserPropertiesCommand(actor,
                userId, "currentTenant");
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        Long result = null;
        User user = cmd.getFirstUser();

        if (user == null) {
            throw new EntityNotFoundException(User.class, userId);
        } else if (user.getCurrentTenant() != null) {
            result = user.getCurrentTenant().getId();
        }

        return result;
    }

    /**
     * Returns the most priviledged role that the given user takes within the
     * system (ignoring the fact that any user is a workflow admin in the
     * default tenant).
     * <p>
     * The roles are checked in the following order (from top to bottom, returns
     * the first role that matches):
     * <ul>
     * <li>Is the user a superadmin? -> {@link SuperAdminRole}</li>
     * <li>Does the user administrate <strong>any</strong> tenant?? ->
     * {@link TenantAdminRole}</li>
     * <li>Does the user administrate <strong>any</strong> workflow model within
     * <strong>any</strong> tenant? -> {@link WorkflowAdminRole}</li>
     * <li>Is the user member of any tenant? -> {@link UserRole}</li>
     * <li>If none of the above match, <code>null</code> is returned</li>
     * </ul>
     * 
     * @param userId
     *            the ID of the user whose highest role should be requested
     * @return UserRole highest user role of the user or null if the user is not
     *         even a tenant member.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws IllegalArgumentException
     *             if userId is <code>null</code>.
     */
    @AllowedRole(UserRole.class)
    public Class<? extends UserRole> getHighestUserRole(Long userId)
            throws TransactionException {
        GetHighestUserRoleCommand command = new GetHighestUserRoleCommand(
                actor, userId);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return command.getResult();
    }

    /**
     * Returns information about the given invitation.
     * 
     * @param invitationId
     *            the ID of the invitation which should be returned
     * @return invitation info
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws IllegalArgumentException
     *             if invitationId is <code>null</code>
     */
    @AllowedRole(UserRole.class)
    public InvitationView getInvitation(Long invitationId)
            throws TransactionException {
        GetInvitationCommand command = new GetInvitationCommand(actor,
                invitationId);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return command.getResult();
    }

    /**
     * Returns all tenants the given user is member of (excluding the default
     * tenant).
     * 
     * @param userId
     *            the ID of the user whose joined tenants should be requested
     * @return list of joined tenants.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             if the user does not exist.
     * @throws IllegalArgumentException
     *             if userId is <code>null</code>
     */
    @AllowedRole(UserRole.class)
    public List<Tenant> getJoinedTenants(Long userId)
            throws TransactionException {
        GetJoinedTenantsCommand command = new GetJoinedTenantsCommand(actor,
                userId);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return command.getResult();
    }

    /**
     * Returns the user ID that belongs to a given username/password or
     * email/password combination iff the account exists and the password
     * matches.
     * 
     * If the password does not match, an EntityNotFoundException is thrown.
     * 
     * @param emailOrUsername
     *            can be either an email address or the user's username.
     * @param passwordPlaintext
     *            the account password as plain text (no hash).
     * @return user ID
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             iff no such account exists or the password doesn't match.
     * @throws IllegalArgumentException
     *             if emailOrUsername or passwordPlaintext is <code>null</code>
     */
    @AllowedRole(BasicRole.class)
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
     * Fetches a user including his profile.
     * 
     * @param userId
     *            the id of the user whose profile should be returned
     * @return user including profile
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             iff the user does not exist or if he does not have a profile
     * @throws IllegalArgumentException
     *             if userId is <code>null</code>
     */
    public User getUserProfile(Long userId) throws TransactionException {
        return getUserProfile(userId, true);
    }

    /**
     * Fetches a user including his profile. <br>
     * Preloaded foreign key properties:
     * <ul>
     * <li>userProfile</li>
     * </ul>
     * 
     * @param userId
     *            the id of the user whose profile should be returned
     * @param requireProfile
     *            whether an exception should be thrown if the user has no
     *            profile.
     * @return user including profile
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             iff the user does not exist or if he does not have a profile
     *             and requireProfile is set to true.
     * @throws IllegalArgumentException
     *             if userId is <code>null</code>
     */
    @AllowedRole(UserRole.class)
    public User getUserProfile(Long userId, boolean requireProfile)
            throws TransactionException {

        GetUserWithProfileCommand command = new GetUserWithProfileCommand(
                actor, userId);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);

        User result = command.getResult();
        if (result == null) {
            throw new EntityNotFoundException(User.class, userId);
        }

        UserProfile profile = result.getUserProfile();
        if ((profile == null) && requireProfile) {
            throw new EntityNotFoundException(UserProfile.class, userId);
        }

        return result;
    }

    /**
     * Returns the highest role of the user in the given tenant.
     * <p>
     * The roles are checked in the following order (from top to bottom, returns
     * the first role that matches):
     * <ul>
     * <li>Is the user a superadmin? -> {@link SuperAdminRole}</li>
     * <li>Is the user the tenant admin? -> {@link TenantAdminRole}</li>
     * <li>Does the user administrate <strong>any</strong> workflow model within
     * the given tenant? -> {@link WorkflowAdminRole}</li>
     * <li>Is the user a member of the given tenant? -> {@link UserRole}</li>
     * <li>If none of the above match, <code>null</code> is returned</li>
     * </ul>
     * <b>Special case:</b>
     * <p>
     * If tenantId references the default tenant, this method returns
     * <code>WorkflowAdminRole.class</code> unless userId references the
     * superadmin: in this case <code>SuperAdminRole.class</code> is returned.
     * 
     * @param userId
     *            the ID of the user whose role should be requested for the
     *            given tenant
     * @param tenantId
     *            the ID of the tenant
     * @return highest user role of the user for the given tenant or null if the
     *         user is not a tenant member.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             if the user or the tenant does not exist.
     * @throws IllegalArgumentException
     *             if userId or tenantId is <code>null</code>
     */
    @AllowedRole(UserRole.class)
    public Class<? extends UserRole> getUserRoleForTenant(Long userId,
            Long tenantId) throws TransactionException {
        GetUserRoleForTenantCommand command = new GetUserRoleForTenantCommand(
                actor, userId, tenantId);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return command.getResult();
    }

    /**
     * Returns a list of the workitems of the given user.
     * 
     * @param userId
     *            the ID of the user whose workitems should be returned
     * @param filters
     *            {@link Filter}
     * @param paginator
     *            {@link Paginator}
     * @return list of workitems
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws IllegalArgumentException
     *             if userId is <code>null</code>
     */
    @AllowedRole(UserRole.class)
    public List<WorkItemSummaryView> getWorkItems(Long userId,
            List<Filter> filters, Paginator paginator)
            throws TransactionException {
        GetWorkItemsCommand command = new GetWorkItemsCommand(actor, userId,
                filters, paginator);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return command.getResult();
    }

    /**
     * Checks if the given user has registered / created a user profile.
     * 
     * @param userId
     *            the ID of the user which should be checked if he's registered
     *            or not
     * @return <code>true</code> if the given user is registered,
     *         <code>false</code> otherwise.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             if the user does not exist.
     * @throws IllegalArgumentException
     *             if userId is <code>null</code>
     */
    @AllowedRole(BasicRole.class)
    public Boolean isRegistered(Long userId) throws TransactionException {
        IsUserRegisteredCommand command = new IsUserRegisteredCommand(actor,
                userId);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return command.getResult();
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
     * @throws EntityNotFoundException
     *             if the user or the tenant doesn't exist.
     * @throws IllegalArgumentException
     *             if userId or tenantId is <code>null</code>
     */
    @AllowedRole(UserRole.class)
    public Boolean leaveTenant(Long userId, Long tenantId)
            throws TransactionException {
        LeaveTenantCommand cmd = new LeaveTenantCommand(actor, userId, tenantId);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        return cmd.getLeftTenant();
    }

    /**
     * Refuses the given invitation an sends a notification email to the
     * inviting user.
     * 
     * @param invitationId
     *            invitation to refuse
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws IllegalArgumentException
     *             if invitationId is <code>null</code>
     */
    @AllowedRole(UserRole.class)
    public void refuseInviation(Long invitationId) throws TransactionException {
        RefuseInvitationCommand command = new RefuseInvitationCommand(actor,
                invitationId);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Creates a new user and adds a user profile and a registration request.
     * 
     * @param email
     *            email address of user to register
     * @param passwordPlaintext
     *            the password as plain text string
     * @param userProfile
     *            the user profile data, you need not set properties such as ID
     *            and password salt as they will be generated.
     * @return The ID of the user that has been registered
     * @throws TransactionException
     *             iff the transaction is aborted for any reason
     * @throws IllegalArgumentException
     *             if email, passwordPlaintext or userProfile are either null or
     *             empty
     */
    @AllowedRole(UserRole.class)
    public Long registerUser(String email, String passwordPlaintext,
            UserProfile userProfile) throws TransactionException {
        RegisterUserCommand cmd = new RegisterUserCommand(actor, email,
                passwordPlaintext, userProfile);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        return cmd.getRegisteredUser().getId();
    }

    /**
     * Removes the user from the given tenant and notifies the user.
     * 
     * @param userId
     *            the ID of the user which should be removed from tenant
     * @param tenantId
     *            the ID of the tenant where the user should be removed
     * @return true iff the user was successfully removed from the tenant.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws IllegalArgumentException
     *             if userId or tenantId is <code>null</code>
     */
    @AllowedRole(TenantAdminRole.class)
    public Boolean removeFromTenant(Long userId, Long tenantId)
            throws TransactionException {
        RemoveFromTenantCommand cmd = new RemoveFromTenantCommand(actor,
                userId, tenantId);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        return cmd.getLeftTenant();
    }

    /**
     * Creates a new change email request for the given user. The user must
     * confirm his new email address.<br>
     * This method sends a confirmation email containing a link that leads back
     * to the DecidR web front-end to the new email address.
     * <p>
     * If the user already has a pending change email request, that request is
     * replaced by the new request.
     * <p>
     * Once the request has been confirmed, use the method
     * {@link UserFacade#confirmChangeEmailRequest(Long, String)} to set the new
     * email address.
     * 
     * @param userId
     *            ID of user whose email address should be changed.
     * @param newEmail
     *            the desired new email address that must be confirmed.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             iff the given user does not exist.
     * @throws IllegalArgumentException
     *             if userId is <code>null</code> or if newEmail is
     *             <code>null</code> or empty.
     */
    @AllowedRole(UserRole.class)
    public void requestChangeEmail(Long userId, String newEmail)
            throws TransactionException {
        RequestChangeEmailCommand cmd = new RequestChangeEmailCommand(actor,
                userId, newEmail);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
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
     * @throws IllegalArgumentException
     *             if emailOrUsername is <code>null</code> or empty.
     */
    @AllowedRole(UserRole.class)
    public Boolean requestPasswordReset(String emailOrUsername)
            throws TransactionException {
        RequestPasswordResetCommand cmd = new RequestPasswordResetCommand(
                actor, emailOrUsername);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        return cmd.getRequestWasCreated();
    }

    /**
     * Sets the ID of the last thenant that the user has switched to.
     * 
     * @param userId
     *            user whose current tenant should be set.
     * @param currentTenantId
     *            current tenant id or <code>null</code> if the current tenant
     *            is the default tenant.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason
     * @throws EntityNotFoundException
     *             if the user or the new current tenant does not exist (unless
     *             you pass null as currentTenantId).
     * @throws IllegalArgumentException
     *             if userId is <code>null</code>
     */
    @AllowedRole(UserRole.class)
    public void setCurrentTenantId(Long userId, Long currentTenantId)
            throws TransactionException {
        SetCurrentTenantCommand cmd = new SetCurrentTenantCommand(actor,
                userId, currentTenantId);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
    }

    /**
     * Sets the disabled date of the given user, effectively preventing the user
     * from logging in. To re-activate the user account, set the disabled date
     * to null. Disabling the super admin account has no effect.Disabling a
     * non-existing user has no effect and does not cause an exception.
     * 
     * @param userId
     *            the id of the user whose disabled date should be set.
     * @param date
     *            to re-active the users, set this parameter to null.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             if the given user does not exist.
     * @throws IllegalArgumentException
     *             if userId is <code>null</code>
     */
    @AllowedRole(TenantAdminRole.class)
    public void setDisabledSince(Long userId, Date date)
            throws TransactionException {
        SetDisabledCommand cmd = new SetDisabledCommand(actor, userId, date);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
    }

    /**
     * Sets a new password for the given user if the provided current password
     * is correct. If invoked by a superadmin, the oldPassword parameter will be
     * ignored as the superadmin does not have to know the current password in
     * order to set a new password.
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
     * @throws IllegalArgumentException
     *             if the user ID is <code>null</code> or if new password is
     *             <code>null</code> of if not invoked by a superadmin and no
     *             old password is given.
     */
    @AllowedRole(UserRole.class)
    public Boolean setPassword(Long userId, String oldPassword,
            String newPassword) throws TransactionException {
        SetPasswordCommand cmd = new SetPasswordCommand(actor, userId,
                oldPassword, newPassword);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        return cmd.getPasswordWasChanged();
    }

    /**
     * Updates an existing user profile with new data. The following properties
     * of the given {@link UserProfile} will be considered:
     * <ul>
     * <li>userName - the login username</li>
     * <li>firstName - first name of the user</li>
     * <li>lastName - last name of the user</li>
     * <li>city - name of the city where the user lives</li>
     * <li>street - street where the user lives</li>
     * <li>postalCode - postal code of the city</li>
     * </ul>
     * 
     * Other properties (password hash, password salt, relations, ...) will be
     * ignored.
     * 
     * @param userId
     *            the id of the user whose profile should be set.
     * @param newProfile
     *            the profile that replaces the current profile.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             iff the given user doesn't exist or doesn't have a user
     *             profile.
     * @throws IllegalArgumentException
     *             if userId or newProfile is <code>null</code>
     */
    @AllowedRole(UserRole.class)
    public void setProfile(Long userId, UserProfile newProfile)
            throws TransactionException, EntityNotFoundException,
            NullPointerException {

        // We're not using the SetUserPropertyCommand because that might
        // potentially insert a user profile even if the user didn't previously
        // have one, making him a registered user without actually registering.
        SetUserProfileCommand cmd = new SetUserProfileCommand(actor, userId,
                newProfile);

        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
    }

    /**
     * Sets the unavailable date of the user, which flags the user as
     * unavailable for workflow participation. To flag the user as available,
     * set his unavailable date to null.
     * 
     * @param userId
     *            the id of the user whose unavailable date should be set.
     * @param date
     *            to flag the user as available, set this parameter to null.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws IllegalArgumentException
     *             if userId is <code>null</code>
     */
    @AllowedRole(UserRole.class)
    public void setUnavailableSince(Long userId, Date date)
            throws TransactionException {
        Map<String, Date> properties = new HashMap<String, Date>();
        properties.put("unavailableSince", date);

        SetUserPropertyCommand cmd = new SetUserPropertyCommand(actor, userId,
                properties);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
    }
}