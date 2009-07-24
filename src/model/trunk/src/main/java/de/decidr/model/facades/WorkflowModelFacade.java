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
import java.util.List;
import java.util.Map;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ObjectProperty;

import de.decidr.model.annotations.AllowedRole;
import de.decidr.model.commands.workflowmodel.DeleteWorkflowModelCommand;
import de.decidr.model.commands.workflowmodel.GetLastStartConfigurationCommand;
import de.decidr.model.commands.workflowmodel.GetPublishedWorkflowModelsCommand;
import de.decidr.model.commands.workflowmodel.GetWorkflowAdministrationStateCommand;
import de.decidr.model.commands.workflowmodel.GetWorkflowAdminstratorsCommand;
import de.decidr.model.commands.workflowmodel.GetWorkflowInstancesCommand;
import de.decidr.model.commands.workflowmodel.GetWorkflowModelCommand;
import de.decidr.model.commands.workflowmodel.MakeWorkflowModelExecutableCommand;
import de.decidr.model.commands.workflowmodel.PublishWorkflowModelsCommand;
import de.decidr.model.commands.workflowmodel.SaveStartConfigurationCommand;
import de.decidr.model.commands.workflowmodel.SaveWorkflowModelCommand;
import de.decidr.model.commands.workflowmodel.SetWorkflowAdministratorsCommand;
import de.decidr.model.commands.workflowmodel.StartWorkflowInstanceCommand;
import de.decidr.model.commands.workflowmodel.UndeployWorkflowModelCommand;
import de.decidr.model.entities.StartConfiguration;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.enums.UserWorkflowAdminState;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.exceptions.WorkflowModelNotStartableException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.TenantAdminRole;
import de.decidr.model.permissions.WorkflowAdminRole;
import de.decidr.model.transactions.HibernateTransactionCoordinator;

/**
 * Provides an interface for retrieving and modifying (deployed) workflow
 * models.
 * 
 * FIXME make it possible to choose between starting a workflow instance
 * immediately and only when the last user confirms his invitation!
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class WorkflowModelFacade extends AbstractFacade {

    /**
     * Creates a new WorkflowModelFacade.
     * 
     * @param actor
     *            user/system that is using this facade to access workflow
     *            models.
     */
    public WorkflowModelFacade(Role actor) {
        super(actor);
    }

    /**
     * Saves the given properties as workflow model.
     * 
     * @throws TransactionException
     *             if the transaction is aborted for any reason.
     */
    @AllowedRole(TenantAdminRole.class)
    public void saveWorkflowModel(Long workflowModelId, String name,
            String description, byte[] dwdl) throws TransactionException {

        HibernateTransactionCoordinator.getInstance().runTransaction(
                new SaveWorkflowModelCommand(actor, workflowModelId, name,
                        description, dwdl));
    }

    /**
     * Returns the properties of the given workflow model as a Vaadin Item.
     * 
     * @param workflowModelId
     *            the ID of the workflow model which should be returned
     * @return TODO document
     * @throws TransactionException
     *             if the transaction is aborted for any reason.
     */
    @AllowedRole(TenantAdminRole.class)
    public Item getWorkflowModel(Long workflowModelId)
            throws TransactionException {

        String[] properties = { "id", "dwdl", "modifiedDate", "version",
                "name", "description", "creationDate", "published" };

        GetWorkflowModelCommand cmd = new GetWorkflowModelCommand(actor,
                workflowModelId);

        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        return new BeanItem(cmd.getResult(), properties);
    }

    /**
     * Makes the given workflow models available for import by other tenants. No
     * exception will be thrown if one or more of the given workflow models do
     * not exist.
     * 
     * @param workflowModelIds
     *            the workflow models to publish.
     * @throws TransactionException
     *             if the transaction is aborted for any reason.
     */
    @AllowedRole(TenantAdminRole.class)
    public void publishWorkflowModels(List<Long> workflowModelIds)
            throws TransactionException {

        HibernateTransactionCoordinator.getInstance()
                .runTransaction(
                        new PublishWorkflowModelsCommand(actor,
                                workflowModelIds, true));
    }

    /**
     * Makes the given workflow models unavailable for import by other tenants.
     * No exception will be thrown if one or more of the given workflow models
     * do not exist.
     * 
     * @param workflowModelIds
     *            the workflow models to publish.
     * @throws TransactionException
     *             if the transaction is aborted for any reason.
     */
    @AllowedRole(TenantAdminRole.class)
    public void unpublishWorkflowModels(List<Long> workflowModelIds)
            throws TransactionException {

        HibernateTransactionCoordinator.getInstance()
                .runTransaction(
                        new PublishWorkflowModelsCommand(actor,
                                workflowModelIds, false));
    }

    /**
     * Sets the executable flag of the given workflow model. A workflow model is
     * made executable by deploying it on the Apache ODE iff it isn't already
     * deployed.
     * 
     * Workflow instances can only be created from workflow models that are
     * executable.
     * 
     * @param workflowModelId
     *            the workflow model to deploy
     * @param excecutable
     * @throws TransactionException
     *             if the transaction is aborted for any reason.
     */
    @AllowedRole(TenantAdminRole.class)
    public void setExecutable(Long workflowModelId, Boolean excecutable)
            throws TransactionException {

        HibernateTransactionCoordinator.getInstance().runTransaction(
                new MakeWorkflowModelExecutableCommand(actor, workflowModelId,
                        excecutable));
    }

    /**
     * Returns a list of all tenant members that administrate the given worfklow
     * model, excluding the tenant admin (who implicitly administrates all
     * workflow models and instances).
     * 
     * @param workflowModelId
     *            workflow model of which the administrators should be retrieved
     * @return List of administrators as Vaadin items.
     * @throws TransactionException
     *             if the transaction is aborted for any reason.
     */
    @AllowedRole(TenantAdminRole.class)
    public List<Item> getWorkflowAdministrators(Long workflowModelId)
            throws TransactionException {
        ArrayList<Item> result = new ArrayList<Item>();

        GetWorkflowAdminstratorsCommand cmd = new GetWorkflowAdminstratorsCommand(
                actor, workflowModelId);

        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        String[] properties = { "id", "email" };

        for (User admin : cmd.getWorkflowAdmins()) {
            BeanItem adminItem = new BeanItem(admin, properties);

            UserProfile profile = admin.getUserProfile();

            if (profile != null) {
                adminItem.addItemProperty("username", new ObjectProperty(
                        profile.getUsername()));
                adminItem.addItemProperty("firstName", new ObjectProperty(
                        profile.getFirstName()));
                adminItem.addItemProperty("lastName", new ObjectProperty(
                        profile.getLastName()));
            }
        }

        return result;
    }

    /**
     * Sets the users that may administrate the given workflow model. The tenant
     * admin always implicitly administrates all workflow models. If his user id
     * is included in userIds, he will be ignored. Only members of the tenant
     * that owns the given worflow model can be set as tenant admin using this
     * method.
     * 
     * @param workflowModelId
     *            the workflow model to administrate
     * @param userIds
     *            the appointed workflow administrators excluding the tenant
     *            admin. If this list is empty or null, no one except the tenant
     *            admin may administrate the workflow model.
     * @throws TransactionException
     *             if the transaction is aborted for any reason.
     */
    @AllowedRole(TenantAdminRole.class)
    public void setWorkflowAdministrators(Long workflowModelId,
            List<Long> userIds) throws TransactionException {

        if (userIds == null) {
            userIds = new ArrayList<Long>();
        }

        HibernateTransactionCoordinator.getInstance().runTransaction(
                new SetWorkflowAdministratorsCommand(actor, workflowModelId,
                        userIds));
    }

    /**
     * Returns a list of items that can be used to tell whether the given
     * usernames or emails belong to:
     * 
     * <ul>
     * <li>users that are unknown to the system. (invitations type A must be
     * sent)</li>
     * <li>users that are known to the system, but are not a member of the
     * tenant that owns the given workflow. (invitations type B must be sent)</li>
     * <li>users that are members of the tenant, but do not currently
     * administrate the given workflow model.</li>
     * <li>users that already administrate the given workflow model.</li>
     * </ul>
     * 
     * <p>
     * Each item contains the user's properties "id" , "username", "email" and a
     * "state" property of the type UserWorkflowAdminState.
     * 
     * @param workflowModelId
     *            TODO document
     * @param usernamesOrEmails
     *            TODO document
     * @return a list of corresponding users
     * @throws TransactionException
     *             if the transaction is aborted for any reason.
     */
    @AllowedRole(TenantAdminRole.class)
    public List<Item> getWorkflowAdministrationState(Long workflowModelId,
            List<String> usernames, List<String> emails)
            throws TransactionException {

        String[] properties = { "id", "email" };

        GetWorkflowAdministrationStateCommand cmd = new GetWorkflowAdministrationStateCommand(
                actor, workflowModelId, usernames, emails);

        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        /*
         * Build the Vaadin item
         */
        List<Item> result = new ArrayList<Item>();
        Map<User, UserWorkflowAdminState> map = cmd.getResult();

        for (User u : map.keySet()) {
            UserWorkflowAdminState state = map.get(u);

            BeanItem item = new BeanItem(u, properties);
            if (u.getUserProfile() != null) {
                item.addItemProperty("username", new ObjectProperty(u
                        .getUserProfile().getUsername()));
            } else {
                item.addItemProperty("username", new ObjectProperty(null,
                        Object.class, true));
            }

            item.addItemProperty("state", new ObjectProperty(state));

            result.add(item);
        }

        return result;
    }

    /**
     * Removes the given workflow models, but retains those deployed workflow
     * models that still have running instances.
     * 
     * @param workflowModelIds
     *            TODO document
     * @throws TransactionException
     *             if the transaction is aborted for any reason.
     */
    @AllowedRole(TenantAdminRole.class)
    public void deleteWorkflowModels(List<Long> workflowModelIds)
            throws TransactionException {
        /*
         * We can't delete all workflow models in a single transaction since a
         * SOAP communication failure would cause a rollback, possibly leaving
         * the database in an inconsistent state.s
         */
        for (Long workflowModelId : workflowModelIds) {

            UndeployWorkflowModelCommand undeployCommand = new UndeployWorkflowModelCommand(
                    actor, workflowModelId);
            DeleteWorkflowModelCommand deleteCommand = new DeleteWorkflowModelCommand(
                    actor, workflowModelId);

            // Delete each model in a separate transaction.
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    undeployCommand, deleteCommand);
        }
    }

    /**
     * Returns all workflow instances of the given workflow model as a list of
     * Vaadin items. The item properties are:
     * <ul>
     * <li>id - the id of the workflow instance.
     * <li>workflowModelName - name of the associated <b>deployed</b> workflow
     * model.
     * <li>workflowModelDescription - description of the associated
     * <b>deployed</b> workflow model.
     * <li>deployDate - date when the workflow model was deployed on the Apache
     * ODE.
     * <li>startedDate - date when the workflow instance was create.
     * <li>completedDate - date when the workflow instance ended (can be null).
     * </ul>
     * 
     * @param workflowModelId
     *            TODO document
     * @param paginator
     *            TODO document
     * @return Vaadin items representing the workflow instances that are
     *         associated swith this model.
     * @throws TransactionException
     *             if the transaction is aborted for any reason.
     */
    @AllowedRole(WorkflowAdminRole.class)
    public List<Item> getWorkflowInstances(Long workflowModelId,
            Paginator paginator) throws TransactionException {
        GetWorkflowInstancesCommand cmd = new GetWorkflowInstancesCommand(
                actor, workflowModelId, paginator);

        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        // build the vaadin items
        List<WorkflowInstance> instances = cmd.getResult();
        ArrayList<Item> result = new ArrayList<Item>();
        String[] properties = { "id", "startedDate", "completedDate" };

        for (WorkflowInstance instance : instances) {
            Item item = new BeanItem(instance, properties);

            item.addItemProperty("workflowModelName", new ObjectProperty(
                    instance.getDeployedWorkflowModel().getName()));
            item.addItemProperty("workflowModelDescription",
                    new ObjectProperty(instance.getDeployedWorkflowModel()
                            .getDescription()));
            item.addItemProperty("deployDate", new ObjectProperty(instance
                    .getDeployedWorkflowModel().getDeployDate()));

            result.add(item);
        }

        return result;
    }

    /**
     * Returns a list of all workflow model that have been published. The item
     * properties are:
     * 
     * <ul>
     * <li>id</li>
     * <li>name</li>
     * <li>description</li>
     * <li>modifiedDate</li>
     * <li>tenantName</li>
     * <li>tenantId</li>
     * </ul>
     * 
     * @param filters
     *            an optional list of filters to apply to the query.
     * @param paginator
     *            an optional paginator to split the query into several pages
     * @return Vaadin items representing the published workflow models.
     * @throws TransactionException
     *             if the transaction is aborted for any reason.
     */
    @AllowedRole(TenantAdminRole.class)
    public List<Item> getAllPublishedWorkflowModels(List<Filter> filters,
            Paginator paginator) throws TransactionException {

        GetPublishedWorkflowModelsCommand cmd = new GetPublishedWorkflowModelsCommand(
                actor, filters, paginator);

        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        List<WorkflowModel> models = cmd.getResult();

        List<Item> result = new ArrayList<Item>();

        String[] properties = { "id", "name", "description", "modifiedDate" };

        // build the Vaadin item
        for (WorkflowModel model : models) {
            BeanItem item = new BeanItem(model, properties);
            item.addItemProperty("tenantId", new ObjectProperty(model
                    .getTenant().getId()));
            item.addItemProperty("tenantName", new ObjectProperty(model
                    .getTenant().getName()));
        }

        return result;
    }

    /**
     * Creates a new instance of the the given workflow model, assuming that the
     * workflow model has already been deployed and is flagged as executable.
     * <p>
     * Upon success, the given start configuration is saved as the last start
     * configuration of the workflow model.
     * 
     * @param workflowModelId
     *            TODO document
     * @param startConfiguration
     *            TODO document
     * @return the id of the created workflow instance.
     * @throws TransactionException
     *             if the transaction is aborted for any reason.
     * @throws WorkflowModelNotStartableException
     *             if the workflow model to start is not really startable.
     */
    @AllowedRole(WorkflowAdminRole.class)
    public Long startWorkflowInstance(Long workflowModelId,
            byte[] startConfiguration) throws TransactionException,
            WorkflowModelNotStartableException {

        StartWorkflowInstanceCommand startCmd = new StartWorkflowInstanceCommand(
                actor, workflowModelId, startConfiguration);

        SaveStartConfigurationCommand saveCmd = new SaveStartConfigurationCommand(
                actor, workflowModelId, startConfiguration);

        HibernateTransactionCoordinator.getInstance().runTransaction(startCmd,
                saveCmd);

        return startCmd.getCreatedWorkflowInstance().getId();
    }

    /**
     * Returns the raw XML data of the last used start configuration of the
     * given workflow model. If applicable start configuration exists, null is
     * returned.
     * 
     * @param workflowModelId
     *            the id of the workflow model to which the last stat configuration should be requested
     * @return the raw XML data of the last used start configuration or null.
     * @throws TransactionException
     *             if the transaction is aborted for any reason.
     */
    @AllowedRole(WorkflowAdminRole.class)
    public byte[] getLastStartConfiguration(Long workflowModelId)
            throws TransactionException {

        GetLastStartConfigurationCommand cmd = new GetLastStartConfigurationCommand(
                actor, workflowModelId);

        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        StartConfiguration config = cmd.getStartConfiguration();
        if (config == null) {
            return null;
        } else {
            return config.getStartConfiguration();
        }
    }
}