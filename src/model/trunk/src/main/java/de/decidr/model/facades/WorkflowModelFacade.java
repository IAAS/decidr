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

import java.util.List;

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
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.exceptions.UserDisabledException;
import de.decidr.model.exceptions.UserUnavailableException;
import de.decidr.model.exceptions.UsernameNotFoundException;
import de.decidr.model.exceptions.WorkflowModelNotStartableException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.workflowmodel.dwdl.Workflow;
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
     * Sets the properties of an existing workflow model, incrementing its
     * version by one.
     * 
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             if the workflow model does not exist.
     * @throws IllegalArgumentException
     *             if workflowModelId is <code>null</code> or if name is
     *             <code>null</code> or empty.
     */
    @AllowedRole(TenantAdminRole.class)
    public void saveWorkflowModel(Long workflowModelId, String name,
            String description, Workflow dwdl) throws TransactionException {

        HibernateTransactionCoordinator.getInstance().runTransaction(
                new SaveWorkflowModelCommand(actor, workflowModelId, name,
                        description, dwdl));
    }

    /**
     * Fetches a worfklow model from the database.<br>
     * Preloaded foreign key properties:
     * <ul>
     * <li>modifiedByUser</li>
     * <li>modifiedByUser.userProfile</li>
     * </ul>
     * 
     * @param workflowModelId
     *            the id of the workflow model which should be returned
     * @return workflow model
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             if the workflow model does not exist.
     * @throws IllegalArgumentException
     *             if workflowModelId is <code>null</code>.
     */
    @AllowedRole(TenantAdminRole.class)
    public WorkflowModel getWorkflowModel(Long workflowModelId)
            throws TransactionException {
        GetWorkflowModelCommand cmd = new GetWorkflowModelCommand(actor,
                workflowModelId);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        return cmd.getResult();
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
     * @throws IllegalArgumentException
     *             if workflowModelIds is <code>null</code>.
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
     * @throws IllegalArgumentException
     *             if workflowModelIds is <code>null</code>.
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
     *            whether to make the workflow model executable or
     *            non-executable (true means executable).
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             if the workflow model does not exist.
     * @throws IllegalArgumentException
     *             if workflowModelId is <code>null</code>.
     */
    @AllowedRole(TenantAdminRole.class)
    public void setExecutable(Long workflowModelId, boolean executable)
            throws TransactionException {
        HibernateTransactionCoordinator.getInstance().runTransaction(
                new MakeWorkflowModelExecutableCommand(actor, workflowModelId,
                        executable));
    }

    /**
     * Returns a list of all tenant members that administer the given worfklow
     * model, excluding the tenant admin (who implicitly administrates all
     * workflow models and instances). <br>
     * Preloaded foreign key properties:
     * <ul>
     * <li>userProfile</li>
     * </ul>
     * 
     * @param workflowModelId
     *            workflow model of which the administrators should be retrieved
     * @return List of administrators
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             if the workflow model does not exist.
     * @throws IllegalArgumentException
     *             if workflowModelId is <code>null</code>.
     */
    @AllowedRole(TenantAdminRole.class)
    public List<User> getWorkflowAdministrators(Long workflowModelId)
            throws TransactionException {
        GetWorkflowAdministratorsCommand cmd = new GetWorkflowAdministratorsCommand(
                actor, workflowModelId);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        return cmd.getWorkflowAdmins();
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
     * @throws EntityNotFoundException
     *             if the workflow model does not exist.
     * @throws IllegalArgumentException
     *             if workflowModelId is <code>null</code>.
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
     * @throws IllegalArgumentException
     *             if workflowModelIds is <code>null</code>.
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
     * Returns a list of all workflow instances of the given workflow model. <br>
     * Preloaded foreign key properties:
     * <ul>
     * <li>deployedWorkflowModel</li>
     * </ul>
     * 
     * @param workflowModelId
     *            id of workflow model of which all workflow instances should be
     *            retrieved.
     * @param paginator
     *            optional pagination component
     * @return workflow instances that are associated swith this model.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             if the workflow model does not exist.
     * @throws IllegalArgumentException
     *             if workflowModelId is <code>null</code>.
     */
    @AllowedRole(WorkflowAdminRole.class)
    public List<WorkflowInstance> getWorkflowInstances(Long workflowModelId,
            Paginator paginator) throws TransactionException {
        GetWorkflowInstancesCommand cmd = new GetWorkflowInstancesCommand(
                actor, workflowModelId, paginator);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        return cmd.getResult();
    }

    /**
     * Returns a list of all workflow model that have been published. <br>
     * Preloaded foreign key properties:
     * <ul>
     * <li>tenant</li>
     * </ul>
     * 
     * @param filters
     *            an optional list of filters to apply to the query.
     * @param paginator
     *            an optional paginator to split the query into several pages
     * @return published workflow models.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    @AllowedRole(TenantAdminRole.class)
    public List<WorkflowModel> getAllPublishedWorkflowModels(
            List<Filter> filters, Paginator paginator)
            throws TransactionException {
        GetPublishedWorkflowModelsCommand cmd = new GetPublishedWorkflowModelsCommand(
                actor, filters, paginator);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        return cmd.getResult();
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
     * @throws EntityNotFoundException
     *             if the workflow model does not exist.
     * @throws IllegalArgumentException
     *             if workflowModelId or startConfiguration is <code>null</code>
     */
    @AllowedRole(WorkflowAdminRole.class)
    public Long startWorkflowInstance(Long workflowModelId,
            TConfiguration startConfiguration, boolean startImmediately)
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
     * @throws EntityNotFoundException
     *             if the workflow model does not exist.
     * @throws IllegalArgumentException
     *             if workflowModelId is <code>null</code>.
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