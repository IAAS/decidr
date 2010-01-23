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

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.acl.roles.TenantAdminRole;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.acl.roles.WorkflowAdminRole;
import de.decidr.model.annotations.AllowedRole;
import de.decidr.model.commands.tenant.AddTenantMemberCommand;
import de.decidr.model.commands.tenant.ApproveTenantsCommand;
import de.decidr.model.commands.tenant.CreateTenantCommand;
import de.decidr.model.commands.tenant.CreateWorkflowModelCommand;
import de.decidr.model.commands.tenant.DeleteTenantCommand;
import de.decidr.model.commands.tenant.GetAllTenantsCommand;
import de.decidr.model.commands.tenant.GetCurrentColorSchemeCommand;
import de.decidr.model.commands.tenant.GetTenantCommand;
import de.decidr.model.commands.tenant.GetTenantLogoCommand;
import de.decidr.model.commands.tenant.GetTenantsToApproveCommand;
import de.decidr.model.commands.tenant.GetUsersOfTenantCommand;
import de.decidr.model.commands.tenant.GetWorkflowInstancesCommand;
import de.decidr.model.commands.tenant.GetWorkflowModelsCommand;
import de.decidr.model.commands.tenant.ImportPublishedWorkflowModelsCommand;
import de.decidr.model.commands.tenant.InviteUsersAsTenantMembersCommand;
import de.decidr.model.commands.tenant.RejectTenantsCommand;
import de.decidr.model.commands.tenant.SetColorSchemeCommand;
import de.decidr.model.commands.tenant.SetCurrentColorSchemeCommand;
import de.decidr.model.commands.tenant.SetTenantDescriptionCommand;
import de.decidr.model.commands.tenant.SetTenantLogoCommand;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.TenantSummaryView;
import de.decidr.model.entities.TenantWithAdminView;
import de.decidr.model.entities.User;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.transactions.HibernateTransactionCoordinator;

/**
 * The tenant facade contains all functions which are available to modify tenant
 * data/settings.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class TenantFacade extends AbstractFacade {

    /**
     * Creates a new tenant facade. All operations are executed by the given
     * actor.
     * 
     * @param actor
     *            actor which executes all command of the facade
     */
    public TenantFacade(Role actor) {
        super(actor);
    }

    /**
     * Adds a user as member of a tenant. Both IDs mustn't be <code>null</code>.
     * If the user is already a member of the tenant, no exception is thrown.
     * 
     * @param tenantId
     *            the ID of the tenant to which the user should be added
     * @param memberId
     *            the ID of the user
     * @throws IllegalArgumentException
     *             if one of the parameters is <code>null</code>
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             if the tenant doesn't exist.
     */
    @AllowedRole(TenantAdminRole.class)
    public void addTenantMember(Long tenantId, Long memberId)
            throws TransactionException {
        AddTenantMemberCommand command = new AddTenantMemberCommand(actor,
                tenantId, memberId);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Approves the given tenants. Not existing and already approved/rejected
     * tenants will we ignored. If the list is empty, the command will succeed
     * without making any changes.
     * 
     * @param tenantIds
     *            a list of tenant IDs which should be approved
     * @throws IllegalArgumentException
     *             if tenantIds is <code>null</code>.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    @AllowedRole(SuperAdminRole.class)
    public void approveTenants(List<Long> tenantIds)
            throws TransactionException {
        ApproveTenantsCommand command = new ApproveTenantsCommand(actor,
                tenantIds);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Creates a new tenant with the given properties. The given user becomes
     * the tenant admin.
     * 
     * @param name
     *            name of the tenant
     * @param description
     *            description of the tenant
     * @param adminId
     *            the ID of the tenant admin user
     * @return ID of the new tenant
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws IllegalArgumentException
     *             if name is <code>null</code> or empty or if adminId is
     *             <code>null</code>
     */
    @AllowedRole(UserRole.class)
    public Long createTenant(String name, String description, Long adminId)
            throws TransactionException {
        CreateTenantCommand command = new CreateTenantCommand(actor, name,
                description, adminId);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return command.getTenantId();
    }

    /**
     * Creates a new workflow model for the given tenant. If the given tenant
     * does not exist an exception will be thrown. TenantId and name mustn't be
     * <code>null</code>.
     * 
     * @param tenantId
     *            the ID of the tenant to which the workflow model should be
     *            added
     * @param name
     *            the name of the workflow model which should be created
     * @return ID of the new workflow model
     * @throws IllegalArgumentException
     *             if the tenant ID is <code>null</code> or if the workflow
     *             model name is <code>null</code> or empty.
     * @throws EntityNotFoundException
     *             if the tenant doesn't exist.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    @AllowedRole(TenantAdminRole.class)
    public Long createWorkflowModel(Long tenantId, String name)
            throws TransactionException {
        CreateWorkflowModelCommand command = new CreateWorkflowModelCommand(
                actor, tenantId, name);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return command.getWorkflowModelId();
    }

    /**
     * Deletes given tenant. Non-existing tenants will be ignored.
     * 
     * @param tenantId
     *            an ID of a tenant which should be deleted
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws IllegalArgumentException
     *             if tenantId is <code>null</code>
     */
    @AllowedRole(SuperAdminRole.class)
    public void deleteTenant(Long tenantId) throws TransactionException {
        DeleteTenantCommand command = new DeleteTenantCommand(actor, tenantId);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Returns a list of all tenants that match the given filter criteria.
     * 
     * @param filters
     *            {@link Filter}
     * @param paginator
     *            {@link Paginator}
     * @return list of tenants
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    @AllowedRole(SuperAdminRole.class)
    public List<TenantSummaryView> getAllTenants(List<Filter> filters,
            Paginator paginator) throws TransactionException {
        GetAllTenantsCommand command = new GetAllTenantsCommand(actor, filters,
                paginator);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return command.getResult();
    }

    /**
     * Returns the current color scheme as an <code>{@link InputStream}</code>.
     * 
     * @param tenantId
     *            the ID of the tenant for which the color scheme is requested
     * @return The image data of the current tenant logo. You must close this
     *         stream once you're done reading from it.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             if the tenant doesn't exist.
     * @throws IllegalArgumentException
     *             if tenantId is <code>null</code>
     */
    @AllowedRole(BasicRole.class)
    public InputStream getCurrentColorScheme(Long tenantId)
            throws TransactionException {
        GetCurrentColorSchemeCommand command = new GetCurrentColorSchemeCommand(
                actor, tenantId);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return command.getCurrentColorScheme();
    }

    /**
     * Returns the tenant logo as <code>{@link InputStream}</code>.<br>
     * 
     * @param tenantId
     *            the ID of the tenant
     * @return the logo image data or null if no logo has been set, yet.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             if the tenant doesn't exist.
     * @throws IllegalArgumentException
     *             if tenantId is null.
     */
    @AllowedRole(Role.class)
    public InputStream getLogo(Long tenantId) throws TransactionException {
        GetTenantLogoCommand command = new GetTenantLogoCommand(actor, tenantId);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return command.getLogoStream();
    }

    /**
     * Returns the tenant which corresponds to the given ID. If the tenant
     * doesn't exist, an {@link EntityNotFoundException} is thrown.
     * 
     * @param tenantId
     *            the ID of the tenant
     * @return tenant entity
     * @throws TransactionException
     *             iff the transaction is aborted for any reason
     * @throws EntityNotFoundException
     *             iff no tenant with the given name exists.
     * @throws IllegalArgumentException
     *             if tenantName is <code>null</code>
     */
    @AllowedRole(BasicRole.class)
    public Tenant getTenant(Long tenantId) throws TransactionException {
        GetTenantCommand command = new GetTenantCommand(actor, tenantId);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return command.getResult();
    }

    /**
     * Returns the tenant which corresponds to the given name. If the tenant
     * doesn't exist, an {@link EntityNotFoundException} is thrown.
     * 
     * @param tenantName
     *            the name of the tenant
     * @return tenant entity
     * @throws TransactionException
     *             iff the transaction is aborted for any reason
     * @throws EntityNotFoundException
     *             iff no tenant with the given name exists.
     * @throws IllegalArgumentException
     *             if tenantName is <code>null</code>
     */
    @AllowedRole(BasicRole.class)
    public Tenant getTenant(String tenantName) throws TransactionException {
        GetTenantCommand command = new GetTenantCommand(actor, tenantName);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return command.getResult();
    }

    /**
     * Returns a list of all tenants that need to be approved.
     * 
     * @param filters
     *            {@link Filter}
     * @param paginator
     *            {@link Paginator}
     * @return list of tenants that need to be approved
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    @AllowedRole(SuperAdminRole.class)
    public List<TenantWithAdminView> getTenantsToApprove(List<Filter> filters,
            Paginator paginator) throws TransactionException {
        GetTenantsToApproveCommand command = new GetTenantsToApproveCommand(
                actor, filters, paginator);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return command.getResult();
    }

    /**
     * Returns a list of all members of the given tenant including the tenant
     * admin.<br>
     * Preloaded foreign key properties:
     * <ul>
     * <li>userProfile</li>
     * </ul>
     * 
     * @param tenantId
     *            the ID of the tenant of which the users should be given back
     * @param paginator
     *            {@link Paginator}
     * @return Users who are members of the given tenant
     * @throws IllegalArgumentException
     *             if the given tenant ID is <code>null</code>.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    @AllowedRole(WorkflowAdminRole.class)
    public List<User> getUsersOfTenant(Long tenantId, Paginator paginator)
            throws TransactionException {
        GetUsersOfTenantCommand command = new GetUsersOfTenantCommand(actor,
                tenantId, paginator);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return command.getResult();
    }

    /**
     * Returns a list of the workflow instances of the given tenant. <br>
     * Preloaded foreign key properties:
     * <ul>
     * <li>deployedWorkflowModel</li>
     * <li>tenant</li>
     * </ul>
     * 
     * @param paginator
     *            {@link Paginator}
     * @return list of workflow instances
     * @throws IllegalArgumentException
     *             if the given tenant ID is <code>null</code>
     * @throws EntityNotFoundException
     *             if the given tenant does not exist
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    @AllowedRole(WorkflowAdminRole.class)
    public List<WorkflowInstance> getWorkflowInstances(Long tenantId,
            Paginator paginator) throws TransactionException {
        GetWorkflowInstancesCommand command = new GetWorkflowInstancesCommand(
                actor, tenantId, paginator);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return command.getResult();
    }

    /**
     * Returns a list of the workflow models of the given tenant.
     * 
     * @param tenantId
     *            ID of the tenant whose workflow models should be returned
     * @param filters
     *            {@link Filter}
     * @param paginator
     *            {@link Paginator}
     * @return list of workflow models
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws IllegalArgumentException
     *             if tenantId is <code>null</code>
     * @throws EntityNotFoundException
     *             if the tenant does not exist.
     */
    @AllowedRole(TenantAdminRole.class)
    public List<WorkflowModel> getWorkflowModels(Long tenantId,
            List<Filter> filters, Paginator paginator)
            throws TransactionException {
        GetWorkflowModelsCommand command = new GetWorkflowModelsCommand(actor,
                tenantId, filters, paginator);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return command.getResult();
    }

    /**
     * Imports the given workflow models to the given tenant. If one or more of
     * the given models are not public a {@link TransactionException} is thrown.
     * Any workflow models that already belong to the target tenant are ignored.
     * 
     * @param tenantId
     *            ID of the tenant where the models should be imported
     * @param workflowModelIds
     *            a list of the IDs of the models which should be imported
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             if the tenant does not exist or if one of the imported
     *             workflow models does not exist.
     * @throws IllegalArgumentException
     *             if tenantId or workflowModelIds is <code>null</code>.
     */
    @AllowedRole(TenantAdminRole.class)
    public void importPublishedWorkflowModels(Long tenantId,
            List<Long> workflowModelIds) throws TransactionException {
        ImportPublishedWorkflowModelsCommand command = new ImportPublishedWorkflowModelsCommand(
                actor, tenantId, workflowModelIds);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Invites the given users to become members of the tenant. If a given
     * username does not exist an exception will be thrown.
     * 
     * @param tenantId
     *            the ID of the tenant where the users should be invited to
     * @param emails
     *            TODO update docu<br>
     *            a list of strings which includes email addresses and usernames
     *            which should be invited
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws IllegalArgumentException
     *             if tenantId is <code>null</code> or if emails and userNames
     *             are both <code>null</code>.
     * 
     */
    @AllowedRole(WorkflowAdminRole.class)
    public void inviteUsersAsMembers(Long tenantId, List<String> emails,
            List<String> userNames) throws TransactionException {
        InviteUsersAsTenantMembersCommand command = new InviteUsersAsTenantMembersCommand(
                actor, tenantId, emails, userNames);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Rejects all given tenants and sends a notification mail to the tenant
     * owners. Already approved tenants will be ignored.
     * 
     * @param tenantIds
     *            a list of IDs of tenants which should be rejected
     * @throws IllegalArgumentException
     *             if tenantIds is <code>null</code>.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    @AllowedRole(SuperAdminRole.class)
    public void rejectTenants(List<Long> tenantIds) throws TransactionException {
        RejectTenantsCommand command = new RejectTenantsCommand(actor,
                tenantIds);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Sets the simple or advanced color scheme of the given tenant.
     * 
     * @param tenantId
     *            the ID of the tenant where the scheme should be set
     * @param fileId
     *            the ID of the file to use as the color scheme
     * @param advanced
     *            whether to set the advanced or the simple color scheme
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             if the tenant doesn't exist.
     * @throws IllegalArgumentException
     *             if tenantId is <code>null</code>
     */
    @AllowedRole(TenantAdminRole.class)
    public void setColorScheme(Long tenantId, Long fileId, boolean advanced)
            throws TransactionException {
        SetColorSchemeCommand command = new SetColorSchemeCommand(actor,
                tenantId, fileId, advanced);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Sets the current color scheme of the given tenant.
     * 
     * @param tenantId
     *            the ID of the tenant where the scheme should be set
     * @param useAdvancedColorScheme
     *            whether to use the advanced color scheme. If set to false the
     *            simple color scheme is used instead.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             if the tenant doesn't exist
     * @throws IllegalArgumentException
     *             if tenantId is <code>null</code>
     */
    @AllowedRole(TenantAdminRole.class)
    public void setCurrentColorScheme(Long tenantId,
            boolean useAdvancedColorScheme) throws TransactionException {
        SetCurrentColorSchemeCommand command = new SetCurrentColorSchemeCommand(
                actor, tenantId, useAdvancedColorScheme);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Sets the description of the given tenant. If the given tenant does not
     * exist an Exception will be thrown.
     * 
     * @param tenantId
     *            the ID of the tenant which should be changed
     * @param description
     *            the new description
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             if the tenant doesn't exist.
     * @throws IllegalArgumentException
     *             if tenantId is <code>null</code>
     */
    @AllowedRole(TenantAdminRole.class)
    public void setDescription(Long tenantId, String description)
            throws TransactionException {
        SetTenantDescriptionCommand command = new SetTenantDescriptionCommand(
                actor, tenantId, description);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Saves the given <code>{@link FileInputStream}</code> as Logo of the given
     * tenant. The old logo is deleted automatically. To remove the current logo
     * including the file on the storage backend, pass <code>null</code> as the
     * fileId.
     * 
     * @param tenantId
     *            the tenant to which the logo will be set
     * @param fileId
     *            the ID of the file that should be used as the tenant logo. Use
     *            <code>null</code> to remove the file.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             if the tenant doesn't exist.
     * @throws IllegalArgumentException
     *             if tenantId is <code>null</code>
     */
    @AllowedRole(TenantAdminRole.class)
    public void setLogo(Long tenantId, Long fileId) throws TransactionException {
        SetTenantLogoCommand command = new SetTenantLogoCommand(actor,
                tenantId, fileId);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }
}