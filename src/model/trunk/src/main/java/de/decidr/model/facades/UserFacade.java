package de.decidr.model.facades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

import de.decidr.model.commands.user.GetAdministratedWorkflowModelCommand;
import de.decidr.model.commands.user.GetJoinedTenantsCommand;
import de.decidr.model.commands.user.GetUserByLoginCommand;
import de.decidr.model.commands.user.GetWorkitemsCommand;
import de.decidr.model.commands.user.RegisterUserCommand;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.entities.WorkItemSummaryView;
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
     * FIXME continue here
     * @param userId
     * @param authKey
     * @return
     * @throws TransactionException
     * @throws EntityNotFoundException
     *             iff the given user does not exist or the authentication key
     *             does not match.
     */
    public Boolean authKeyMatches(Long userId, String authKey)
            throws TransactionException, EntityNotFoundException {
        throw new UnsupportedOperationException();
    }

    public void setEmailAddress(Long userId, String newEmail) {
        throw new UnsupportedOperationException();
    }

    public void setDisableSince(Long userId, Date date) {
        throw new UnsupportedOperationException();
    }

    public void setUnavailableSince(Long userId, Date date) {
        throw new UnsupportedOperationException();
    }

    public void setPassword(Long userId, String oldPassword, String newPassword) {
        throw new UnsupportedOperationException();
    }

    public void requestPasswordReset(String emailOrUsername) {
        throw new UnsupportedOperationException();
    }

    public void setProfile(Long userId, Item newProfile) {
        throw new UnsupportedOperationException();
    }

    public void leaveTenant(Long userId, Long tenantId) {
        throw new UnsupportedOperationException();
    }

    public void removeFromTenant(Long userId, Long tenantId) {
        throw new UnsupportedOperationException();
    }

    public void confirmRegistration(String authKey) {
        throw new UnsupportedOperationException();
    }

    public void confirmChangeEmailRequest(String requestAuthKey) {
        throw new UnsupportedOperationException();
    }

    public void confirmInvitation(Long invitationId) {
        throw new UnsupportedOperationException();
    }

    public void refuseInviation(Long invitationId) {
        throw new UnsupportedOperationException();
    }

    //FIXME HIER TREFFEN
    public Item getUserProfile(Long userId) {
        throw new UnsupportedOperationException();
    }

    public List<Item> getAllUsers(List<Filter> filters, Paginator paginator) {
        throw new UnsupportedOperationException();
    }

    public Class<? extends UserRole> getHighestUserRole(Long userId) {
        throw new UnsupportedOperationException();
    }

    public Class<? extends UserRole> getUserRoleForTenant(Long userId,
            Long tenantId) {
        throw new UnsupportedOperationException();
    }

    public List<Item> getAdministratedTenants(Long userId) {
        throw new UnsupportedOperationException();
    }

    
    public List<Item> getAdminstratedWorkflowInstances(Long userId) {
        throw new UnsupportedOperationException();
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
     * Returns the workitems of the given user as List<Item> with the following properties:
     * - creationDate<br>
     * - userId<br>
     * - id<br>
     * - tenantName<br
     * - workItemName<br>
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
        GetWorkitemsCommand command = new GetWorkitemsCommand(
                actor, userId,filters, paginator);

        tac.runTransaction(command);
        inList = command.getResult();

        for (WorkItemSummaryView model : inList) {
            outList.add(new BeanItem(model));
        }

        return outList;
        
    }
}