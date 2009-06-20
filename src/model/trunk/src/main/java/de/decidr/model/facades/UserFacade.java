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
import de.decidr.model.commands.user.GetAdministratedWorkflowModelCommand;
import de.decidr.model.commands.user.GetAdminstratedWorkflowInstancesCommand;
import de.decidr.model.commands.user.GetAllUsersCommand;
import de.decidr.model.commands.user.GetHighestUserRoleCommand;
import de.decidr.model.commands.user.GetJoinedTenantsCommand;
import de.decidr.model.commands.user.GetUserByLoginCommand;
import de.decidr.model.commands.user.GetUserProfileCommand;
import de.decidr.model.commands.user.GetUserRoleForTenantCommand;
import de.decidr.model.commands.user.GetWorkitemsCommand;
import de.decidr.model.commands.user.RegisterUserCommand;
import de.decidr.model.commands.user.SetPasswordCommand;
import de.decidr.model.commands.user.SetUserPropertyCommand;
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
     * @param userProfile
     *            the user profile data
     * @return The id of the user that has been registered
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

        // create the new user profile
        UserProfile profile = new UserProfile();
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setCity(city);
        profile.setStreet(street);
        profile.setPostalCode(postalCode);

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
     * @return user id
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

        return cmd.getUser().getId();
    }

    /**
     * Checks whether the given authentication key matches the user's
     * authentication key in the database.
     * 
     * @param userId
     *            the user to check
     * @param authKey
     *            the authentication key to check
     * @return true iff the authKey matches the given user's authentication key.
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
     * @param newEmail
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
     * @throws TransactionException
     */
    public Boolean requestPasswordReset(String emailOrUsername)
            throws TransactionException {
        throw new UnsupportedOperationException();
    }

    public void setProfile(Long userId, Item newProfile)
            throws TransactionException {
        throw new UnsupportedOperationException();
    }

    public void leaveTenant(Long userId, Long tenantId)
            throws TransactionException {
        throw new UnsupportedOperationException();
    }

    public void removeFromTenant(Long userId, Long tenantId)
            throws TransactionException {
        throw new UnsupportedOperationException();
    }

    public void confirmPasswordReset(Long userId, String authKey) {
        
    }
    
    public void confirmRegistration(Long userId, String authKey) throws TransactionException {
        throw new UnsupportedOperationException();
    }

    public void confirmChangeEmailRequest(Long userId, String requestAuthKey)
            throws TransactionException {
        throw new UnsupportedOperationException();
    }

    public void confirmInvitation(Long invitationId)
            throws TransactionException {
        throw new UnsupportedOperationException();
    }

    public void refuseInviation(Long invitationId) {
        throw new UnsupportedOperationException();
    }

    
    /**
     * 
     * Returns the user profile of the given user.
     * 
     * @param userId
     * @return
     * @throws TransactionException
     */
    public Item getUserProfile(Long userId) throws TransactionException {

        String[] properties = { "city", "firstName", "lastName", "postalCode",
                "street", "username" };

        GetUserProfileCommand command = new GetUserProfileCommand(actor, userId);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);

        return new BeanItem(command.getResult(), properties);

    }

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
     * @return
     * @throws TransactionException
     */
    public Class<? extends UserRole> getHighestUserRole(Long userId)
            throws TransactionException {

        GetHighestUserRoleCommand command = new GetHighestUserRoleCommand(
                actor, userId);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);

        return command.getResult();

    }

    /**
     * 
     * Returns the highest role of the user in the given tenant in the result
     * variable. If the user is not even a tenant member the result will be
     * null.
     * 
     * @param userId
     * @param tenantId
     * @return
     * @throws TransactionException
     */
    public Class<? extends UserRole> getUserRoleForTenant(Long userId,
            Long tenantId) throws TransactionException {

        GetUserRoleForTenantCommand command = new GetUserRoleForTenantCommand(
                actor, userId, tenantId);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);

        return command.getResult();

    }

    // FIXME Do we really need this?
    public List<Item> getAdministratedTenants(Long userId)
            throws TransactionException {
        return null;
    }

    /**
     * 
     * Returns all administrated workflow instances of the given user as Item.
     * Each item has the following properties:<br>
     * -id<br>
     * -startedDate<br>
     * -completedDate<br>
     * -model
     * 
     * @param userId
     * @return
     * @throws TransactionException
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
     * @return Vaadin items representing the joined tenants.
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
     * @return
     * @throws TransactionException
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
     * 
     * Returns the workitems of the given user as List<Item> with the following
     * properties: - creationDate<br>
     * - userId<br>
     * - id<br>
     * - tenantName<br - workItemName<br>
     * - workItemStatus
     * 
     * @param userId
     * @param filters
     * @param paginator
     * @return
     * @throws TransactionException
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
}