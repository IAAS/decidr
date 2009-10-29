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

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ObjectProperty;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.TenantAdminRole;
import de.decidr.model.acl.roles.WorkflowAdminRole;
import de.decidr.model.annotations.AllowedRole;
import de.decidr.model.commands.workflowmodel.DeleteWorkflowModelCommand;
import de.decidr.model.commands.workflowmodel.GetLastStartConfigurationCommand;
import de.decidr.model.commands.workflowmodel.GetPublishedWorkflowModelsCommand;
import de.decidr.model.commands.workflowmodel.GetWorkflowAdministratorsCommand;
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
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.exceptions.UserDisabledException;
import de.decidr.model.exceptions.UserUnavailableException;
import de.decidr.model.exceptions.UsernameNotFoundException;
import de.decidr.model.exceptions.WorkflowModelNotStartableException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.workflowmodel.wsc.TConfiguration;

/**
 * Provides an interface for retrieving and modifying (deployed) workflow
 * models.
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
     *             iff the transaction is aborted for any reason.
     */
    @AllowedRole(TenantAdminRole.class)
    public void saveWorkflowModel(Long workflowModelId, String name,
            String description, byte[] dwdl) throws TransactionException {

        HibernateTransactionCoordinator.getInstance().runTransaction(
                new SaveWorkflowModelCommand(actor, workflowModelId, name,
                        description, dwdl));
    }

    /**
     * Returns the properties of the given workflow model as a Vaadin
     * {@link Item} with the following properties:
     * <ul>
     * <li>id: Long - workflow model id</li>
     * <li>dwdl: byte[] - dwdl raw xml data</li>
     * <li>modifiedDate: Date - date of last modification</li>
     * <li>version: Long - version/revision number of the workflow model</li>
     * <li>name: String - workflow model name</li>
     * <li>description: String - workflow model description</li>
     * <li>creationDate: Date - date when the workflow model was created</li>
     * <li>published: Boolean - whether the workflow model</li>
     * <li>modifyingUserFirstName: String - modifying user first name</li>
     * <li>modifyingUserLastName: String - modifying user last name</li>
     * <li>modifyingUserId: Long - modifying user id</li>
     * <li>modifyingUserEmail: String - modifying user email</li>
     * <li>modifyingUserUsername: String - modifying user username</li>
     * </ul>
     * 
     * @param workflowModelId
     *            the id of the workflow model which should be returned
     * @return Vaadin item
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    @AllowedRole(TenantAdminRole.class)
    public Item getWorkflowModel(Long workflowModelId)
            throws TransactionException {

        String[] properties = { "id", "dwdl", "modifiedDate", "version",
                "name", "description", "creationDate", "published" };

        GetWorkflowModelCommand cmd = new GetWorkflowModelCommand(actor,
                workflowModelId);

        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        // build Vaadin item
        WorkflowModel model = cmd.getResult();
        User modifyingUser = model.getModifiedByUser();

        BeanItem result = new BeanItem(model, properties);

        if (modifyingUser != null) {
            result.addItemProperty("modifyingUserId", new ObjectProperty(
                    modifyingUser.getId(), Long.class));
            result.addItemProperty("modifyingUserEmail", new ObjectProperty(
                    modifyingUser.getEmail(), String.class));

            UserProfile profile = modifyingUser.getUserProfile();
            if (profile != null) {
                result
                        .addItemProperty("modifyingUserFirstName",
                                new ObjectProperty(profile.getFirstName(),
                                        String.class));
                result
                        .addItemProperty("modifyingUserLastName",
                                new ObjectProperty(profile.getLastName(),
                                        String.class));
                result
                        .addItemProperty("modifyingUserUsername",
                                new ObjectProperty(profile.getUsername(),
                                        String.class));
            }
        }

        return result;
    }

    /**
     * Makes the given workflow models available for import by other tenants. No
     * exception will be thrown if one or more of the given workflow models do
     * not exist.
     * 
     * @param workflowModelIds
     *            the workflow models to publish.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
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
     *             iff the transaction is aborted for any reason.
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
     * @param executable
     *            TODO document
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    @AllowedRole(TenantAdminRole.class)
    public void setExecutable(Long workflowModelId, Boolean executable)
            throws TransactionException {

        HibernateTransactionCoordinator.getInstance().runTransaction(
                new MakeWorkflowModelExecutableCommand(actor, workflowModelId,
                        executable));
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
     *             iff the transaction is aborted for any reason.
     */
    @AllowedRole(TenantAdminRole.class)
    public List<Item> getWorkflowAdministrators(Long workflowModelId)
            throws TransactionException {
        ArrayList<Item> result = new ArrayList<Item>();

        GetWorkflowAdministratorsCommand cmd = new GetWorkflowAdministratorsCommand(
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
     * admin always implicitly administrates all workflow models. If the lists
     * of new workflow admins contain the tenant admin he will be ignored.
     * <p>
     * Invitations are automatically sent to unregistered users and to users who
     * are not already a member of the tenant that owns the workflow model.
     * <p>
     * This method will fail with an exception if the lists new workflow admins
     * contain:
     * <ul>
     * <li>a username that is unknown to the system</li>
     * <li>a username that belongs to an inactive, disabled or unavailable user
     * account</li>
     * <li>an email address that belongs to an inactive, disabled or unavailable
     * user account</li>
     * </ul>
     * 
     * @param workflowModelId
     *            the workflow model to administrate
     * @param newAdminEmails
     *            email addresses of new tenant administrators
     * @param newAdminUsernames
     *            usernames of new tenant administrators
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws UsernameNotFoundException
     *             if a username is unknown to the system
     * @throws UserUnavailableException
     *             if a user has set his status to unavailable
     * @throws UserDisabledException
     *             if a user has been disabled by the super admin
     */
    @AllowedRole(TenantAdminRole.class)
    public void setWorkflowAdministrators(Long workflowModelId,
            List<String> newAdminEmails, List<String> newAdminUsernames)
            throws TransactionException, UsernameNotFoundException,
            UserUnavailableException, UserDisabledException {
        SetWorkflowAdministratorsCommand cmd = new SetWorkflowAdministratorsCommand(
                actor, workflowModelId, newAdminUsernames, newAdminEmails);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
    }

    /**
     * Removes the given workflow models, but retains those deployed workflow
     * models that still have running instances.
     * 
     * @param workflowModelIds
     *            list of ids of the workflow models to delete
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    @AllowedRole(TenantAdminRole.class)
    public void deleteWorkflowModels(List<Long> workflowModelIds)
            throws TransactionException {
        /*
         * We can't delete all workflow models in a single transaction since a
         * SOAP communication failure would cause a rollback, possibly leaving
         * the database in an inconsistent state.
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
     *            id of workflow model of which all workflow instances should be
     *            retrieved.
     * @param paginator
     *            optional pagination component
     * @return Vaadin items representing the workflow instances that are
     *         associated swith this model.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
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
     * <li>id: Long</li>
     * <li>name: String</li>
     * <li>description: String</li>
     * <li>modifiedDate: Date</li>
     * <li>tenantName: String</li>
     * <li>tenantId: Long</li>
     * </ul>
     * 
     * @param filters
     *            an optional list of filters to apply to the query.
     * @param paginator
     *            an optional paginator to split the query into several pages
     * @return Vaadin items representing the published workflow models.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
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
            result.add(item);
        }

        return result;
    }

    /**
     * Creates a new instance of the the given workflow model, assuming that the
     * workflow model has already been deployed and is flagged as executable.
     * <p>
     * Invitations are automatically sent to users that are not already a member
     * of the tenant that owns the workflow model.
     * 
     * This method will fail with an exception if the start configuration
     * contains:
     * <ul>
     * <li>a username that is unknown to the system</li>
     * <li>a username that belongs to an inactive, disabled or unavailable user
     * account</li>
     * <li>an email address that belongs to an inactive, disabled or unavailable
     * user account</li>
     * </ul>
     * <p>
     * Upon success, the given start configuration is saved as the last start
     * configuration of the workflow model.
     * 
     * @param workflowModelId
     *            id of workflow model of which an instance should be started
     * @param startConfiguration
     *            xml object of start configuration to use it will be updated
     *            accordingly if new users are created.
     * @param startImmediately
     *            whether the system should delay creating the workflow instance
     *            on the ODE until all users have confirmed their invitations.
     * @return the id of the created workflow instance.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws WorkflowModelNotStartableException
     *             if the workflow model to start is not really startable.
     * @throws UserUnavailableException
     *             if a user has set his status to unavailable
     * @throws UserDisabledException
     *             if a user account has been deactivated by the superadmin.
     */
    @AllowedRole(WorkflowAdminRole.class)
    public Long startWorkflowInstance(Long workflowModelId,
            TConfiguration startConfiguration, Boolean startImmediately)
            throws TransactionException, WorkflowModelNotStartableException,
            UserUnavailableException, UserDisabledException,
            UsernameNotFoundException {

        StartWorkflowInstanceCommand startCmd = new StartWorkflowInstanceCommand(
                actor, workflowModelId, startConfiguration, startImmediately);

        SaveStartConfigurationCommand saveCmd = new SaveStartConfigurationCommand(
                actor, workflowModelId, startConfiguration);

        HibernateTransactionCoordinator.getInstance().runTransaction(startCmd,
                saveCmd);

        return startCmd.getCreatedWorkflowInstance().getId();
    }

    /**
     * Returns the raw XML data of the last used start configuration of the
     * given workflow model. If no applicable start configuration exists, null
     * is returned.
     * 
     * @param workflowModelId
     *            the id of the workflow model to which the last stat
     *            configuration should be requested
     * @return the raw XML data of the last used start configuration or null.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
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