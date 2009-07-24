package de.decidr.model.facades;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ObjectProperty;
import de.decidr.model.VaadinTools;
import de.decidr.model.annotations.AllowedRole;
import de.decidr.model.commands.tenant.AddTenantMemberCommand;
import de.decidr.model.commands.tenant.ApproveTenantsCommand;
import de.decidr.model.commands.tenant.CreateTenantCommand;
import de.decidr.model.commands.tenant.CreateWorkflowModelCommand;
import de.decidr.model.commands.tenant.DeleteTenantCommand;
import de.decidr.model.commands.tenant.DisapproveTenantsCommand;
import de.decidr.model.commands.tenant.GetAllTenantsCommand;
import de.decidr.model.commands.tenant.GetCurrentColorSchemeCommand;
import de.decidr.model.commands.tenant.GetTenantIdCommand;
import de.decidr.model.commands.tenant.GetTenantLogoCommand;
import de.decidr.model.commands.tenant.GetTenantsToApproveCommand;
import de.decidr.model.commands.tenant.GetUsersOfTenantCommand;
import de.decidr.model.commands.tenant.GetWorkflowInstancesCommand;
import de.decidr.model.commands.tenant.GetWorkflowModelsCommand;
import de.decidr.model.commands.tenant.ImportPublishedWorkflowModelsCommand;
import de.decidr.model.commands.tenant.InviteUsersAsTenantMemberCommand;
import de.decidr.model.commands.tenant.RemoveWorkflowModelCommand;
import de.decidr.model.commands.tenant.SetAdvancedColorSchemeCommand;
import de.decidr.model.commands.tenant.SetCurrentColorSchemeCommand;
import de.decidr.model.commands.tenant.SetSimpleColorSchemeCommand;
import de.decidr.model.commands.tenant.SetTenantDescriptionCommand;
import de.decidr.model.commands.tenant.SetTenantLogoCommand;
import de.decidr.model.entities.TenantSummaryView;
import de.decidr.model.entities.TenantWithAdminView;
import de.decidr.model.entities.User;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionCoordinator;
import de.decidr.model.permissions.UserRole;
import de.decidr.model.permissions.TenantAdminRole;
import de.decidr.model.permissions.SuperAdminRole;
import de.decidr.model.permissions.WorkflowAdminRole;

/**
 * The tenant facade contains all functions which are available to modify tenant
 * data/settings.
 * 
 * @author Markus Fischer
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
     */
    @AllowedRole(UserRole.class)
    public Long createTenant(String name, String description, Long adminId)
            throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();

        CreateTenantCommand command = new CreateTenantCommand(actor, name,
                description, adminId);

        tac.runTransaction(command);

        return command.getTenantId();
    }

    /**
     * Sets the description of the given tenant. If the given tenant does not
     * exists an Exception will be thrown.
     * 
     * @param tenantId
     *            the id of the tenant which should be changed
     * @param description
     *            the new description
     */
    @AllowedRole(TenantAdminRole.class)
    public void setDescription(Long tenantId, String description)
            throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        SetTenantDescriptionCommand command = new SetTenantDescriptionCommand(
                actor, tenantId, description);

        tac.runTransaction(command);

    }

    /**
     * Returns the tenant logo as <code>{@link InputStream}</code>.
     * 
     * @param tenantId
     *            the id of the tenant
     * @return tenant logo the logo of the tenant
     */
    @AllowedRole(Role.class)
    public InputStream getLogo(Long tenantId) throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        GetTenantLogoCommand command = new GetTenantLogoCommand(actor, tenantId);

        tac.runTransaction(command);

        return command.getLogoStream();

    }

    /**
     * Saves the given <code>{@link FileInputStream}</code> as Logo of the given
     * tenant.
     * 
     * @param tenantId
     *            the tenant to which the logo will be set
     * @param logo
     *            the logo
     * @param mimeType
     *            the mimetype of the logo file
     * @param fileName
     *            the file name of the logo file
     */
    @AllowedRole(TenantAdminRole.class)
    public void setLogo(Long tenantId, FileInputStream logo, String mimeType,
            String fileName) throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        SetTenantLogoCommand command = new SetTenantLogoCommand(actor,
                tenantId, logo, mimeType, fileName);

        tac.runTransaction(command);

    }

    /**
     * Sets the simple color scheme of the given tenant.
     * 
     * @param tenantId
     *            the id of the tenant where the scheme should be set
     * @param simpleColorScheme
     *            the color scheme file
     * @param mimeType
     *            mime type of the file
     * @param fileName
     *            file name of the file
     */
    @AllowedRole(TenantAdminRole.class)
    public void setSimpleColorScheme(Long tenantId,
            FileInputStream simpleColorScheme, String mimeType, String fileName)
            throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        SetSimpleColorSchemeCommand command = new SetSimpleColorSchemeCommand(
                actor, tenantId, simpleColorScheme, mimeType, fileName);

        tac.runTransaction(command);

    }

    /**
     * Sets the advanced color scheme of the given tenant.
     * 
     * @param tenantId
     *            the ID of the tenant where the scheme should be set
     * @param advancedColorScheme
     *            the color scheme file
     * @param mimeType
     *            mime type of the file
     * @param fileName
     *            file name of the file
     */
    @AllowedRole(TenantAdminRole.class)
    public void setAdvancedColorScheme(FileInputStream advancedColorScheme,
            Long tenantId, String mimeType, String fileName)
            throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        SetAdvancedColorSchemeCommand command = new SetAdvancedColorSchemeCommand(
                actor, tenantId, advancedColorScheme, mimeType, fileName);

        tac.runTransaction(command);

    }

    /**
     * Sets the current color scheme of the given tenant.
     * 
     * @param tenantId
     *            the ID of the tenant where the scheme should be set
     * @param advancedColorScheme
     *            the color scheme file
     * @param mimeType
     *            mime type of the file
     * @param fileName
     *            file name of the file
     */
    @AllowedRole(TenantAdminRole.class)
    public void setCurrentColorScheme(FileInputStream currentColorScheme,
            Long tenantId, String mimeType, String fileName)
            throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        SetCurrentColorSchemeCommand command = new SetCurrentColorSchemeCommand(
                actor, tenantId, currentColorScheme, mimeType, fileName);

        tac.runTransaction(command);
    }

    /**
     * Returns the current color scheme as <code>{@link InputStream}</code>.
     * 
     * @param tenantId
     *           the ID of the tenant for which the color scheme is requested
     * @return tenant logo
     */
    @AllowedRole(Role.class)
    public InputStream getCurrentColorScheme(Long tenantId)
            throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        GetCurrentColorSchemeCommand command = new GetCurrentColorSchemeCommand(
                actor, tenantId);

        tac.runTransaction(command);

        return command.getSchemeStream();

    }

    /**
     * Adds a user as member of a tenant. Both IDs mustn't be <code>null</code>.
     * 
     * @param tenantId
     *            the ID of the tenant to which the user should be added
     * @param memberId
     *            the ID of the user
     */
    @AllowedRole(TenantAdminRole.class)
    public void addTenantMember(Long tenantId, Long memberId)
            throws TransactionException {

        if (tenantId == null) {
            throw new NullPointerException("tenantId mustn't be null");
        } else if (memberId == null) {
            throw new NullPointerException("memberID mustn't be null");
        } else {
            TransactionCoordinator tac = HibernateTransactionCoordinator
                    .getInstance();
            AddTenantMemberCommand command = new AddTenantMemberCommand(actor,
                    tenantId, memberId);

            tac.runTransaction(command);
        }
    }

    /**
     * Creates a new workflow model for the given tenant. If the given tenant
     * does not exist an exception will be thrown. TenantId and name mustn't be
     * <code>null</code>.
     * 
     * @param tenantId
     *            the id of the tenant to which the workflow model should be added
     * @param name
     *            the name of the workflow model which should be created
     * @return id of the new workflow model
     */
    @AllowedRole(TenantAdminRole.class)
    public Long createWorkflowModel(Long tenantId, String name)
            throws TransactionException {

        if (tenantId == null) {
            throw new NullPointerException("tenantId mustn't be null");
        } else if ((name == null) || ("".equals(name))) {
            throw new IllegalArgumentException("name must have a content");
        } else {
            TransactionCoordinator tac = HibernateTransactionCoordinator
                    .getInstance();
            CreateWorkflowModelCommand command = new CreateWorkflowModelCommand(
                    actor, tenantId, name);

            tac.runTransaction(command);
            return command.getWorkflowModelId();
        }
    }

    /**
     * Removes the relation between the given workflow model and the tenant. If
     * the given workflow model doesn't exist nothing will happen.
     * 
     * @param tenantId
     *            the id of the tenant from which the model should be removed
     * @param workflowModelId
     *            the id of the model which should be removed
     */
    @AllowedRole(TenantAdminRole.class)
    public void removeWorkflowModel(Long workflowModelId)
            throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        RemoveWorkflowModelCommand command = new RemoveWorkflowModelCommand(
                actor, workflowModelId);

        tac.runTransaction(command);
    }

    /**
     * Approves the given tenants. Not existing and already approved/disapproved
     * tenants will we ignored.
     * 
     * @param tenantIds
     *            a list of tenant IDs which should be approved
     */
    @AllowedRole(TenantAdminRole.class)
    public void approveTenants(List<Long> tenantIds)
            throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        ApproveTenantsCommand command = new ApproveTenantsCommand(actor,
                tenantIds);

        tac.runTransaction(command);

    }

    /**
     * Disapproves all given tenants and sends a notification mail to the tenant
     * owners. Already approved/disapproved tenants will be ignored.
     * 
     * @param tenantIds
     *            a list of IDs of tenants which should be disapproved
     */
    @AllowedRole(TenantAdminRole.class)
    public void disapproveTenants(List<Long> tenantIds)
            throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        DisapproveTenantsCommand command = new DisapproveTenantsCommand(actor,
                tenantIds);

        tac.runTransaction(command);

    }

    /**
     * Deletes given tenant. Non-existing tenants will be ignored.
     * 
     * @param tenantIds
     *            a list of IDs of tenants which should be deleted
     */
    @AllowedRole(SuperAdminRole.class)
    public void deleteTenant(Long tenantId) throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        DeleteTenantCommand command = new DeleteTenantCommand(actor, tenantId);

        tac.runTransaction(command);

    }

    /**
     * Returns the tenantId of the tenant which corresponds to the given name.
     * If the tenantName doesn't exists, an exception will be thrown.
     * 
     * @param tenantName
     *            the name of the tenant
     * @return tenandId, if tenant doesn't exist, an exception is thrown
     * @throws TransactionException
     *             if tenant is not unique
     */
    @AllowedRole(UserRole.class)
    public Long getTenantId(String tenantName) throws TransactionException {
        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        GetTenantIdCommand command = new GetTenantIdCommand(actor, tenantName);
        tac.runTransaction(command);

        return command.getTenantId();
    }

    /**
     * Returns the Users of the given tenant.<br>
     * The tenant admin will not be part of the result. <br>
     * <br>
     * The returned Items contain the following properties:<br>
     * <ul>
     * <il>username</il>
     * <il>first_name</il>
     * <il>last_name</il>
     * <il>email</il>
     * </ul>
     * 
     * @param tenantId
     *            the id of the tenant of which the users should be given back
     * @param paginator
     *            TODO document
     * @return <code>{@link List<Item>}</code> Users of the given tenant
     */
    @SuppressWarnings("unchecked")
    @AllowedRole(WorkflowAdminRole.class)
    public List<Item> getUsersOfTenant(Long tenantId, Paginator paginator)
            throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        GetUsersOfTenantCommand command = new GetUsersOfTenantCommand(actor,
                tenantId, paginator);

        List<Item> outList = new ArrayList<Item>();
        List<User> inList = new ArrayList();
        String[] properties = { "email" };
        Item currentItem = null;

        tac.runTransaction(command);
        inList = command.getResult();

        for (User user : inList) {

            currentItem = new BeanItem(user, properties);

            if (user.getUserProfile() != null) {
                currentItem.addItemProperty("username", new ObjectProperty(user
                        .getUserProfile().getUsername(), String.class));
                currentItem.addItemProperty("first_name", new ObjectProperty(
                        user.getUserProfile().getFirstName(), String.class));
                currentItem.addItemProperty("last_name", new ObjectProperty(
                        user.getUserProfile().getLastName(), String.class));
            } else {
                currentItem.addItemProperty("username", VaadinTools
                        .getEmptyProperty());
                currentItem.addItemProperty("first_name", VaadinTools
                        .getEmptyProperty());
                currentItem.addItemProperty("last_name", VaadinTools
                        .getEmptyProperty());
            }

            outList.add(currentItem);
        }

        return outList;
    }

    /**
     * Returns the WorkflowItems of the given tenant. <br>
     * <br>
     * The returned Items contain the following properties:<br>
     * <ul>
     * <li>id</li>
     * <li>startDate</li>
     * <li>workflow_model</li>
     * <li>tenant_name</li>
     * </ul>
     * 
     * @param tenantId
     *            id of the tenant
     * @param paginator
     *            TODO document
     * @return TODO document
     */
    @SuppressWarnings("unchecked")
    @AllowedRole(WorkflowAdminRole.class)
    public List<Item> getWorkflowInstances(Long tenantId, Paginator paginator)
            throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        GetWorkflowInstancesCommand command = new GetWorkflowInstancesCommand(
                actor, tenantId, paginator);

        List<Item> outList = new ArrayList<Item>();
        List<WorkflowInstance> inList = new ArrayList();
        String[] properties = { "id,startDate" };
        Item currentItem = null;

        tac.runTransaction(command);
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
     * Returns a list of all tenants which have to be approved as List<Item>
     * Each Item has the following properties:<br>
     * <ul>
     * <li>id</li>
     * <li>tenant_name</li>
     * <li>first_name</li>
     * <li>last_name</li>
     * </ul>
     * 
     * @param filters
     *            TODO document
     * @param paginator
     *            TODO document
     * @return TODO document
     */
    @SuppressWarnings("unchecked")
    @AllowedRole(SuperAdminRole.class)
    public List<Item> getTenantsToApprove(List<Filter> filters,
            Paginator paginator) throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        GetTenantsToApproveCommand command = new GetTenantsToApproveCommand(
                actor, filters, paginator);

        List<Item> outList = new ArrayList<Item>();
        List<TenantWithAdminView> inList = new ArrayList();

        String[] properties = { "id", "adminFirstName", "adminLastName" };

        tac.runTransaction(command);
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
     * <li>adminFirstName</li>
     * <li>adminLastName</li>
     * <li>id</li>
     * <li>numDeployedWorkflowModels</li>
     * <li>numMembers</li>
     * <li>numWorkflowInstance</li>
     * <li>tenantName</li>
     * </ul>
     * 
     * @param filters
     *            TODO document
     * @param paginator
     *            TODO document
     * @return TODO document
     */
    @SuppressWarnings("unchecked")
    @AllowedRole(SuperAdminRole.class)
    public List<Item> getAllTenants(List<Filter> filters, Paginator paginator)
            throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        GetAllTenantsCommand command = new GetAllTenantsCommand(actor, filters,
                paginator);

        List<Item> outList = new ArrayList<Item>();
        List<TenantSummaryView> inList = new ArrayList();

        tac.runTransaction(command);
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
     * <li>name</li>
     * <li>creationDate</li>
     * <li>published</li>
     * </ul>
     * 
     * @param tenantId
     *            TODO document
     * @param filters
     *            TODO document
     * @param paginator
     *            TODO document
     * @return TODO document
     */
    @SuppressWarnings("unchecked")
    @AllowedRole(TenantAdminRole.class)
    public List<Item> getWorkflowModels(Long tenantId, List<Filter> filters,
            Paginator paginator) throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        GetWorkflowModelsCommand command = new GetWorkflowModelsCommand(actor,
                tenantId, filters, paginator);

        List<Item> outList = new ArrayList<Item>();
        List<WorkflowModel> inList = new ArrayList();
        String[] properties = { "name", "creationDate", "published" };

        tac.runTransaction(command);
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
     *            a list of strings which includes email addresses and usernames which should be invited
     */
    @AllowedRole(WorkflowAdminRole.class)
    public void inviteUsersAsMembers(Long tenantId, List<String> emails,
            List<String> userNames) throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        InviteUsersAsTenantMemberCommand command = new InviteUsersAsTenantMemberCommand(
                actor, tenantId, emails, userNames);

        tac.runTransaction(command);
    }

    /**
     * Imports the given workflow models to the given tenant. If one or more of
     * the given models are not public an exception will be thrown.
     * 
     * @param tenantId
     *            id of the tenant where the models should be imported
     * @param workflowModelIds
     *            a list of the IDs of the models which should be imported
     */
    @AllowedRole(TenantAdminRole.class)
    public void importPublishedWorkflowModels(Long tenantId,
            List<Long> workflowModelIds) throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        ImportPublishedWorkflowModelsCommand command = new ImportPublishedWorkflowModelsCommand(
                actor, tenantId, workflowModelIds);

        tac.runTransaction(command);
    }
}