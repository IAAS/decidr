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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ObjectProperty;

import de.decidr.model.VaadinTools;
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
import de.decidr.model.commands.tenant.GetTenantIdCommand;
import de.decidr.model.commands.tenant.GetTenantLogoCommand;
import de.decidr.model.commands.tenant.GetTenantSettingsCommand;
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
import de.decidr.model.commands.workflowmodel.DeleteWorkflowModelCommand;
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
     * Creates a new tenant with the given properties.
     * 
     * @param name
     *            name of the tenant
     * @param description
     *            description of the tenant
     * @param adminId
     *            the id of the tenant admin user
     * @return id of the new tenant
     * @throws TransactionException
     *             TODO document
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
     * Sets the description of the given tenant. If the given tenant does not
     * exist an Exception will be thrown.
     * 
     * @param tenantId
     *            the id of the tenant which should be changed
     * @param description
     *            the new description
     * @throws TransactionException
     *             TODO document
     */
    @AllowedRole(TenantAdminRole.class)
    public void setDescription(Long tenantId, String description)
            throws TransactionException {

        SetTenantDescriptionCommand command = new SetTenantDescriptionCommand(
                actor, tenantId, description);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Returns the tenant logo as <code>{@link InputStream}</code>.<br>
     * XXX what if the tenant currently doesn't have a logo? ~rr
     * 
     * @param tenantId
     *            the id of the tenant
     * @return tenant logo the logo of the tenant
     * @throws TransactionException
     *             TODO document
     */
    @AllowedRole(Role.class)
    public InputStream getLogo(Long tenantId) throws TransactionException {
        GetTenantLogoCommand command = new GetTenantLogoCommand(actor, tenantId);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);

        return command.getLogoStream();
    }

    /**
     * Saves the given <code>{@link FileInputStream}</code> as Logo of the given
     * tenant. To remove the current logo including the file on the storage
     * backend, pass <code>null</code> as the fileId.
     * 
     * @param tenantId
     *            the tenant to which the logo will be set
     * @param fileId
     *            the id of the file that should be used as the tenant logo. Use
     *            <code>null</code> to remove the file.
     * @throws TransactionException
     *             TODO document
     * @throws IllegalArgumentException
     *             TODO document
     */
    @AllowedRole(TenantAdminRole.class)
    public void setLogo(Long tenantId, Long fileId) throws TransactionException {
        SetTenantLogoCommand command = new SetTenantLogoCommand(actor,
                tenantId, fileId);

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
     *             TODO document
     */
    @AllowedRole(TenantAdminRole.class)
    public void setColorScheme(Long tenantId, Long fileId, Boolean advanced)
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
     *             TODO document
     */
    @AllowedRole(TenantAdminRole.class)
    public void setCurrentColorScheme(Long tenantId,
            Boolean useAdvancedColorScheme) throws TransactionException {

        SetCurrentColorSchemeCommand command = new SetCurrentColorSchemeCommand(
                actor, tenantId, useAdvancedColorScheme);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Returns the current color scheme as an <code>{@link InputStream}</code>.
     * 
     * @param tenantId
     *            the ID of the tenant for which the color scheme is requested
     * @return The image data of the current tenant logo. You must close this
     *         stream once you're done reading from it.
     * @throws TransactionException
     *             TODO document
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
     *             if the transaction is aborted for any reason.
     */
    @AllowedRole(TenantAdminRole.class)
    public void addTenantMember(Long tenantId, Long memberId)
            throws TransactionException {

        AddTenantMemberCommand command = new AddTenantMemberCommand(actor,
                tenantId, memberId);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Creates a new workflow model for the given tenant. If the given tenant
     * does not exist an exception will be thrown. TenantId and name mustn't be
     * <code>null</code>.
     * 
     * @param tenantId
     *            the id of the tenant to which the workflow model should be
     *            added
     * @param name
     *            the name of the workflow model which should be created
     * @return id of the new workflow model
     * @throws IllegalArgumentException
     *             if the tenant ID is <code>null</code> or if the workflow
     *             model name is <code>null</code> or empty.
     * @throws TransactionException
     *             TODO document
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
     * Removes the relation between the given workflow model and the tenant. If
     * the given workflow model doesn't exist nothing will happen.
     * 
     * @param tenantId
     *            the id of the tenant from which the model should be removed
     * @param workflowModelId
     *            the id of the model which should be removed
     * @throws TransactionException
     *             TODO document
     */
    @AllowedRole(TenantAdminRole.class)
    public void removeWorkflowModel(Long workflowModelId)
            throws TransactionException {

        DeleteWorkflowModelCommand command = new DeleteWorkflowModelCommand(
                actor, workflowModelId);

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
     *             TODO document
     */
    @AllowedRole(SuperAdminRole.class)
    public void approveTenants(List<Long> tenantIds)
            throws TransactionException {

        if (tenantIds == null) {
            throw new IllegalArgumentException(
                    "List of tenant IDs must not be null");
        }

        ApproveTenantsCommand command = new ApproveTenantsCommand(actor,
                tenantIds);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Rejects all given tenants and sends a notification mail to the tenant
     * owners. Already approved tenants will be ignored.
     * 
     * @param tenantIds
     *            a list of IDs of tenants which should be rejected
     * @throws TransactionException
     *             TODO document
     */
    @AllowedRole(SuperAdminRole.class)
    public void rejectTenants(List<Long> tenantIds) throws TransactionException {
        RejectTenantsCommand command = new RejectTenantsCommand(actor,
                tenantIds);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Deletes given tenant. Non-existing tenants will be ignored.
     * 
     * @param tenantIds
     *            a list of IDs of tenants which should be deleted
     * @throws TransactionException
     *             TODO document
     */
    @AllowedRole(SuperAdminRole.class)
    public void deleteTenant(Long tenantId) throws TransactionException {
        DeleteTenantCommand command = new DeleteTenantCommand(actor, tenantId);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Returns the tenantId of the tenant which corresponds to the given name.
     * If the tenantName doesn't exist, an exception will be thrown.
     * 
     * @param tenantName
     *            the name of the tenant
     * @return tenandId, if tenant doesn't exist, an exception is thrown
     * @throws TransactionException
     *             if the transaction is aborted for any reason
     * @throws EntityNotFoundException
     *             iff no tenant with the given name exists.
     */
    @AllowedRole(UserRole.class)
    public Long getTenantId(String tenantName) throws TransactionException {
        GetTenantIdCommand command = new GetTenantIdCommand(actor, tenantName);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);

        return command.getTenantId();
    }

    /**
     * Returns a list of all members of the given tenant including the tenant
     * admin.<br>
     * <br>
     * The returned Items contain the following properties:<br>
     * <ul>
     * <li>id: {@link Long} - user id</li>
     * <li>username {@link String} - username (only if the user is registered)</li>
     * <li>firstName {@link String} - user first name (only if the user is
     * registered)</li>
     * <li>lastName {@link String} - user last name (only if the user is
     * registered)</li>
     * <li>email</li>
     * </ul>
     * 
     * @param tenantId
     *            the id of the tenant of which the users should be given back
     * @param paginator
     *            {@link Paginator}
     * @return <code>{@link List<Item>}</code> Users of the given tenant
     * @throws IllegalArgumentException
     *             if the given tenant ID is <code>null</code>.
     * @throws TransactionException
     *             TODO document
     */
    @AllowedRole(WorkflowAdminRole.class)
    public List<Item> getUsersOfTenant(Long tenantId, Paginator paginator)
            throws TransactionException {

        GetUsersOfTenantCommand command = new GetUsersOfTenantCommand(actor,
                tenantId, paginator);

        List<Item> outList = new ArrayList<Item>();
        List<User> inList = new ArrayList<User>();
        String[] userProperties = { "id", "email" };
        String[] profileProperties = { "username", "firstName", "lastName" };

        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        inList = command.getResult();

        for (User user : inList) {

            BeanItem currentItem = new BeanItem(user, userProperties);

            if (user.getUserProfile() != null) {
                VaadinTools.addBeanPropertiesToItem(user.getUserProfile(),
                        currentItem, profileProperties);
            }

            outList.add(currentItem);
        }

        return outList;
    }

    /**
     * Returns a list of Vaadin items representing the workflow instances of the
     * given tenant. <br>
     * <br>
     * The returned items contain the following properties:<br>
     * <ul>
     * <li>id: Long - ID of workflow instance</li>
     * <li>startDate: Date - date when the workflow instance was started (null
     * if it hasn't been started yet)</li>
     * <li>workflowModelName: String - name of the corresponding workflow model</li>
     * <li>tenantName: String - name of tenant that owns the workflow instance</li>
     * </ul>
     * 
     * @param tenantId
     *            id of the tenant
     * @param paginator
     *            {@link Paginator}
     * @return list of vaadin items described above
     * @throws IllegalArgumentException
     *             if the given tenant ID is <code>null</code>
     * @throws EntityNotFoundException
     *             if the given tenant does not exist
     * @throws TransactionException
     *             TODO document
     */
    @AllowedRole(WorkflowAdminRole.class)
    public List<Item> getWorkflowInstances(Long tenantId, Paginator paginator)
            throws TransactionException {

        GetWorkflowInstancesCommand command = new GetWorkflowInstancesCommand(
                actor, tenantId, paginator);

        List<Item> outList = new ArrayList<Item>();
        List<WorkflowInstance> inList = new ArrayList<WorkflowInstance>();
        String[] properties = { "id", "startDate" };
        Item currentItem = null;

        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        inList = command.getResult();

        for (WorkflowInstance instance : inList) {

            currentItem = new BeanItem(instance, properties);

            currentItem.addItemProperty("workflowModelName",
                    new ObjectProperty(instance.getDeployedWorkflowModel()
                            .getName(), String.class));
            currentItem.addItemProperty("tenantName", new ObjectProperty(
                    instance.getDeployedWorkflowModel().getTenant().getName(),
                    String.class));

            outList.add(currentItem);
        }

        return outList;
    }

    /**
     * Returns a list of all tenants which have to be approved as {@link List}
     * {@code <}{@link Item}{@code >}. Each {@link Item} has the following
     * properties:<br>
     * <ul>
     * <li>id: Long - tenant ID</li>
     * <li>name: String - tenant name</li>
     * <li>adminFirstName: String - first name of tenant admin</li>
     * <li>adminLastName: String - last name of tenant admin</li>
     * <li>adminId: Long - user ID of tenant admin</li>
     * </ul>
     * 
     * @param filters
     *            {@link Filter}
     * @param paginator
     *            {@link Paginator}
     * @return list of vaadin items described above
     * @throws TransactionException
     *             TODO document
     */
    @AllowedRole(SuperAdminRole.class)
    public List<Item> getTenantsToApprove(List<Filter> filters,
            Paginator paginator) throws TransactionException {

        GetTenantsToApproveCommand command = new GetTenantsToApproveCommand(
                actor, filters, paginator);

        List<Item> outList = new ArrayList<Item>();
        List<TenantWithAdminView> inList = new ArrayList<TenantWithAdminView>();

        String[] properties = { "id", "name", "adminFirstName",
                "adminLastName", "adminId" };

        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        inList = command.getResult();

        for (TenantWithAdminView item : inList) {
            outList.add(new BeanItem(item, properties));
        }

        return outList;
    }

    /**
     * Returns a list of all Tenants as <code>{@link List<Item>}</code>. The
     * result can be filtered by using filters and split in several pages by
     * using a paginator. Each item has the following properties:<br>
     * <ul>
     * <li>adminFirstName: String - the first name of the tenant admin</li>
     * <li>adminLastName: String - the last name of the tenant admin</li>
     * <li>id: Long - the tenant id</li>
     * <li>numDeployedWorkflowModels: Long - the number of deployed workflow
     * models that this tenant owns.</li>
     * <li>numWorkflowModels: Long - the number of workflow models that this
     * tenant owns.</li>
     * <li>numMembers: Long - the number of users that are members of this
     * tenant.</li>
     * <li>numWorkflowInstances: Long - the number of workflow instances that
     * this tenant owns.</li>
     * <li>tenantName: String - the name of this tenant.</li>
     * </ul>
     * 
     * @param filters
     *            {@link Filter}
     * @param paginator
     *            {@link Paginator}
     * @return list of vaadin items described above
     * @throws TransactionException
     *             TODO document
     */
    @AllowedRole(SuperAdminRole.class)
    public List<Item> getAllTenants(List<Filter> filters, Paginator paginator)
            throws TransactionException {

        GetAllTenantsCommand command = new GetAllTenantsCommand(actor, filters,
                paginator);

        List<Item> outList = new ArrayList<Item>();
        List<TenantSummaryView> inList = new ArrayList<TenantSummaryView>();

        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        inList = command.getResult();

        for (TenantSummaryView tenant : inList) {

            outList.add(new BeanItem(tenant));

        }

        return outList;
    }

    /**
     * Returns the WorkflowModels of the given tenant as List<Item>. Each Item
     * has the following properties<br>
     * <ul>
     * <li>name: String - name of the workflow model</li>
     * <li>creationDate: Date - date when the workflow model was created</li>
     * <li>published: Boolean - whether this workflow model is available for
     * import by other tenants.</li>
     * </ul>
     * XXX what about ID?
     * 
     * @param tenantId
     *            ID of the tenant whose workflow models should be requested
     * @param filters
     *            {@link Filter}
     * @param paginator
     *            {@link Paginator}
     * @return list of vaadin items described above
     * @throws TransactionException
     *             TODO document
     */
    @AllowedRole(TenantAdminRole.class)
    public List<Item> getWorkflowModels(Long tenantId, List<Filter> filters,
            Paginator paginator) throws TransactionException {

        GetWorkflowModelsCommand command = new GetWorkflowModelsCommand(actor,
                tenantId, filters, paginator);

        List<Item> outList = new ArrayList<Item>();
        List<WorkflowModel> inList = new ArrayList<WorkflowModel>();
        String[] properties = { "name", "creationDate", "published" };

        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        inList = command.getResult();

        for (WorkflowModel model : inList) {

            outList.add(new BeanItem(model, properties));

        }

        return outList;
    }

    /**
     * Invites the given Users. If a given username does not exist an exception
     * will be thrown.
     * 
     * @param tenantId
     *            the id of the tenant where the users should be invited to
     * @param emailOrUsernames
     *            a list of strings which includes email addresses and usernames
     *            which should be invited
     * @throws TransactionException
     *             TODO document
     */
    @AllowedRole(WorkflowAdminRole.class)
    public void inviteUsersAsMembers(Long tenantId, List<String> emails,
            List<String> userNames) throws TransactionException {

        InviteUsersAsTenantMembersCommand command = new InviteUsersAsTenantMembersCommand(
                actor, tenantId, emails, userNames);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Imports the given workflow models to the given tenant. If one or more of
     * the given models are not public an exception will be thrown.
     * 
     * @param tenantId
     *            id of the tenant where the models should be imported
     * @param workflowModelIds
     *            a list of the IDs of the models which should be imported
     * @throws TransactionException
     *             TODO document
     */
    @AllowedRole(TenantAdminRole.class)
    // DH how do I get the IDs of the newly created WFMs? ~rr
    public void importPublishedWorkflowModels(Long tenantId,
            List<Long> workflowModelIds) throws TransactionException {

        ImportPublishedWorkflowModelsCommand command = new ImportPublishedWorkflowModelsCommand(
                actor, tenantId, workflowModelIds);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Retrieves the current tenant settings from the database as a Vaadin Item
     * with the following properties:
     * 
     * <ul>
     * <li>id: {@link Long} - tenant id (should be the same as the given
     * tenantId)</li>
     * <li>name: {@link String} - tenant name</li>
     * <li>description: {@link String} - tenant description</li>
     * <li>approvedSince: {@link Date} - date when the tenant was approved</li>
     * <li>simpleColorSchemeId: {@link Long} - file ID of the simple color
     * scheme</li>
     * <li>advancedColorSchemeId: {@link Long} - file ID of the advanced color
     * scheme</li>
     * <li>currentColorSchemeId: {@link Long} - file ID of the current color
     * scheme (which is either the simple or advanced scheme)</li>
     * </ul>
     * 
     * @param tenantId
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             iff the given tenant does not exist
     * @throws TransactionException
     *             TODO document
     */
    @AllowedRole(TenantAdminRole.class)
    public Item getTenantSettings(Long tenantId) throws TransactionException {
        GetTenantSettingsCommand cmd = new GetTenantSettingsCommand(actor,
                tenantId);

        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        String[] properties = { "id", "name", "description", "approvedSince" };

        Tenant settings = cmd.getTenantSettings();
        BeanItem result = new BeanItem(settings, properties);
        result.addItemProperty("simpleColorSchemeId", new ObjectProperty(
                settings.getSimpleColorScheme().getId()));
        result.addItemProperty("advancedColorSchemeId", new ObjectProperty(
                settings.getAdvancedColorScheme().getId()));
        result.addItemProperty("currentColorSchemeId", new ObjectProperty(
                settings.getCurrentColorScheme().getId()));

        return result;
    }
}