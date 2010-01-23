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

import java.util.Set;

import de.decidr.model.acl.roles.HumanTaskRole;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.WorkflowAdminRole;
import de.decidr.model.annotations.AllowedRole;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.commands.workflowinstance.DeleteWorkflowInstanceCommand;
import de.decidr.model.commands.workflowinstance.GetParticipatingUsersCommand;
import de.decidr.model.commands.workflowinstance.RemoveAllWorkItemsCommand;
import de.decidr.model.commands.workflowinstance.StopWorkflowInstanceCommand;
import de.decidr.model.entities.User;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;

/**
 * Contains all functions which are available to create and control workflow
 * instances.
 * 
 * @author Daniel Huss
 * @author Markus Fischer
 */
public class WorkflowInstanceFacade extends AbstractFacade {

    /**
     * Creates a new WorkflowInstaceFacade. All Commands will be executed by the
     * given actor.
     * 
     * @param actor
     *            user which executes all commands of the created facade
     */
    public WorkflowInstanceFacade(Role actor) {
        super(actor);
    }

    /**
     * Stops the WorkflowInstance and deletes the corresponding representation
     * object at the database.
     * 
     * @param workflowInstanceId
     *            the ID of the WorkflowInstance
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             if the workflow instance does not exist.
     * @throws IllegalArgumentException
     *             if workflowInstanceId is <code>null</code>
     */
    @AllowedRole(WorkflowAdminRole.class)
    public void deleteWorkflowInstance(Long workflowInstanceId)
            throws TransactionException {
        StopWorkflowInstanceCommand command = new StopWorkflowInstanceCommand(
                actor, workflowInstanceId);
        DeleteWorkflowInstanceCommand command2 = new DeleteWorkflowInstanceCommand(
                actor, workflowInstanceId);
        HibernateTransactionCoordinator.getInstance().runTransaction(
                new TransactionalCommand[] { command, command2 });
    }

    /**
     * Returns all participants of the given {@link WorkflowInstance}.
     * 
     * @param workflowInstanceId
     *            the ID of the workflow instance whose participating users
     *            should be requested
     * @return list of participating Users
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             if the workflow instance does not exist.
     * @throws IllegalArgumentException
     *             if workflowInstanceId is <code>null</code>
     */
    @AllowedRole(WorkflowAdminRole.class)
    public Set<User> getParticipatingUsers(Long workflowInstanceId)
            throws TransactionException {
        GetParticipatingUsersCommand command = new GetParticipatingUsersCommand(
                actor, workflowInstanceId);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return command.getResult();
    }

    /**
     * Removes all work items from the workflow instance that is identified by
     * the given ODE process id and the given workflow model id. The workflow
     * instance itself is <b>not</b> deleted from the database.
     * 
     * @param odePid
     *            the process ID of the workflow at the ODE
     * @param deployedWorkflowModelId
     *            ID of the deployed workflow model whose workitems should be
     *            removed
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws IllegalArgumentException
     *             if odePid or deployedWorkflowModelId is <code>null</code>
     */
    @AllowedRole(HumanTaskRole.class)
    public void removeAllWorkItems(String odePid, Long deployedWorkflowModelId)
            throws TransactionException {
        RemoveAllWorkItemsCommand command = new RemoveAllWorkItemsCommand(
                actor, odePid, deployedWorkflowModelId);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Stops the given WorkflowInstance.
     * 
     * @param workflowInstanceId
     *            the ID of the WorkflowInstance
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             if the workflow instance does not exist.
     * @throws IllegalArgumentException
     *             if workflowInstanceId is <code>null</code>
     */
    @AllowedRole(WorkflowAdminRole.class)
    public void stopWorkflowInstance(Long workflowInstanceId)
            throws TransactionException {
        StopWorkflowInstanceCommand command = new StopWorkflowInstanceCommand(
                actor, workflowInstanceId);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }
}