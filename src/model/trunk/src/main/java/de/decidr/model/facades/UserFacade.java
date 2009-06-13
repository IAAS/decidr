package de.decidr.model.facades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

import de.decidr.model.commands.user.GetAdministratedWorkflowModelCommand;
import de.decidr.model.commands.user.GetJoinedTenantsCommand;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.WorkflowModel;
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
     * Adds a new user to the system.
     * 
     * @param email
     * @param password
     * @param userProfile
     * @return
     */
    public Long registerUser(String email, String password, Item userProfile) {
        throw new UnsupportedOperationException();
    }

    public Long getUserIdByLogin(String emailOrUsername, String password) {
        throw new UnsupportedOperationException();
    }

    public Long getUserId(String authCode) {
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

    public Item getUserProfile(Long userId) {
        throw new UnsupportedOperationException();
    }

    // FIXME paginator not yet implemented
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
     * following properties:<br>
     * - id<br>
     * - name
     * 
     * 
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Item> getJoinedTenants(Long userId) throws TransactionException {

        List<Tenant> inList;
        List<Item> outList = new ArrayList();

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        GetJoinedTenantsCommand command = new GetJoinedTenantsCommand(actor,
                userId);

        tac.runTransaction(command);

        inList = command.getResult();
        String[] properties = { "id", "name" };

        for (Tenant model : inList) {
            outList.add(new BeanItem(model, properties));
        }

        return outList;

    }

    /**
     * Returns the workflow models which the given user administrates:<br>
     * - id<br>
     * - name<br>
     * - description
     * 
     * @param userId
     * @return
     * @throws TransactionException
     */
    @SuppressWarnings("unchecked")
    public List<Item> getAdministratedWorkflowModels(Long userId)
            throws TransactionException {

        List<WorkflowModel> inList;
        List<Item> outList = new ArrayList();

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        GetAdministratedWorkflowModelCommand command = new GetAdministratedWorkflowModelCommand(
                actor, userId);

        tac.runTransaction(command);
        inList = command.getResult();
        String[] properties = { "id", "name", "description" };

        for (WorkflowModel model : inList) {
            outList.add(new BeanItem(model, properties));
        }

        return outList;
    }

    // FIXME paginator not yet implemented
    public List<Item> getWorkItems(Long userId, List<Filter> filters,
            Paginator paginator) {
        throw new UnsupportedOperationException();
    }
}