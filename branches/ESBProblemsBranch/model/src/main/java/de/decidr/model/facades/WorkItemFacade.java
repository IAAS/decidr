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

import java.util.Date;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

import de.decidr.model.acl.roles.HumanTaskRole;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.acl.roles.WorkflowAdminRole;
import de.decidr.model.annotations.AllowedRole;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.commands.workitem.CreateWorkItemCommand;
import de.decidr.model.commands.workitem.DeleteWorkItemCommand;
import de.decidr.model.commands.workitem.GetWorkItemCommand;
import de.decidr.model.commands.workitem.SetDataCommand;
import de.decidr.model.commands.workitem.SetStatusCommand;
import de.decidr.model.enums.WorkItemStatus;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.workflowmodel.humantask.THumanTaskData;

/**
 * Provides an interface for retrieving and manipulating work items.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class WorkItemFacade extends AbstractFacade {

    /**
     * Creates a new instance of the WorkItemFacade.
     * 
     * @param actor
     *            user/system that is using this facade to access work items.
     */
    public WorkItemFacade(Role actor) {
        super(actor);
    }

    /**
     * Returns all properties of the given work item:
     * <ul>
     * <li>id: {@link Long} - work item id</li>
     * <li>creationDate: {@link Date} - date when the work item was created</li>
     * <li>status: {@link String} - see WorkItemStatus</li>
     * <li>data: byte[] - raw xml bytes of a {@link THumanTaskData} instance</li>
     * <li>name: {@link String} - work item name</li>
     * <li>description: {@link String} -</li>
     * </ul>
     * 
     * @param workItemId
     *            the ID of the workitem which should be requested
     * @return Vaadin item described above
     * @throws TransactionException
     *             iff the transaction is aborted for any reason
     * @throws EntityNotFoundException
     *             if the work item does not exist.
     * @throws IllegalArgumentException
     *             if workItemId is <code>null</code>
     */
    @AllowedRole(UserRole.class)
    public Item getWorkItem(Long workItemId) throws TransactionException {

        GetWorkItemCommand cmd = new GetWorkItemCommand(actor, workItemId);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        String[] properties = { "id", "creationDate", "status", "data", "name",
                "description" };

        return new BeanItem(cmd.getResult(), properties);
    }

    /**
     * Creates a new work item in the database, making it available to the user.
     * 
     * @param userId
     *            the user to whom the work item belongs
     * @param deployedWorkflowModelId
     *            the deployed workflow model id of the workflow instance.
     * @param odePid
     *            the ODE process id of the workflow instance.
     * @param name
     *            the name of the new work item.
     * @param description
     *            the description of the new work item
     * @param data
     *            the data associated with this work item
     * @param notifyUser
     *            whether the user should be notified by email that a new work
     *            item has been created.
     * @return the ID of the new work item.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason
     * @throws IllegalArgumentException
     *             if any parameter is <code>null</code>
     */
    @AllowedRole(HumanTaskRole.class)
    public Long createWorkItem(Long userId, Long deployedWorkflowModelId,
            String odePid, String name, String description,
            THumanTaskData data, Boolean notifyUser)
            throws TransactionException {

        CreateWorkItemCommand cmd = new CreateWorkItemCommand(actor, userId,
                deployedWorkflowModelId, odePid, name, description, data,
                notifyUser);

        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        return cmd.getWorkItemId();
    }

    /**
     * Overwrites the current data of the given work item.
     * 
     * @param workItemId
     *            the work item to modify
     * @param data
     *            the new work item data
     * @throws TransactionException
     *             iff the transaction is aborted for any reason
     * @throws EntityNotFoundException
     *             if the work item does not exist
     * @throws IllegalArgumentException
     *             if any parameter is <code>null</code>
     */
    @AllowedRole(UserRole.class)
    public void setData(Long workItemId, THumanTaskData data)
            throws TransactionException {
        HibernateTransactionCoordinator.getInstance().runTransaction(
                new SetDataCommand(actor, workItemId, data));
    }

    /**
     * Overwrites the xml data of the given work item with the given raw data.
     * 
     * @param workItemId
     *            the work item to modify
     * @param data
     *            the new work item data.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason
     * @throws EntityNotFoundException
     *             if the work item does not exist
     * @throws IllegalArgumentException
     *             if workItemId is or data is <code>null</code>.
     */
    @AllowedRole(UserRole.class)
    public void setDataAndMarkAsDone(Long workItemId, THumanTaskData data)
            throws TransactionException {
        TransactionalCommand[] commands = {
                new SetDataCommand(actor, workItemId, data),
                new SetStatusCommand(actor, workItemId, WorkItemStatus.Done) };

        HibernateTransactionCoordinator.getInstance().runTransaction(commands);
    }

    /**
     * Sets the work item status of the given work item to "done".
     * 
     * @param workItemId
     *            ID of the workitem which should be marked as done
     * @throws TransactionException
     *             iff the transaction is aborted for any reason
     * @throws EntityNotFoundException
     *             if the work item does not exist.
     * @throws IllegalArgumentException
     *             if workItemId is <code>null</code>.
     */
    @AllowedRole(UserRole.class)
    public void markWorkItemAsDone(Long workItemId) throws TransactionException {
        HibernateTransactionCoordinator.getInstance().runTransaction(
                new SetStatusCommand(actor, workItemId, WorkItemStatus.Done));
    }

    /**
     * Deletes the given work item from the database.
     * 
     * @param workItemId
     *            ID of the workitem which should be deleted
     * @throws TransactionException
     *             iff the transaction is aborted for any reason
     * @throws IllegalArgumentException
     *             if workItemId is <code>null</code>.
     */
    @AllowedRole( { WorkflowAdminRole.class, HumanTaskRole.class })
    public void deleteWorkItem(Long workItemId) throws TransactionException {
        HibernateTransactionCoordinator.getInstance().runTransaction(
                new DeleteWorkItemCommand(actor, workItemId));
    }

    /**
     * Returns the properties of the given work item and sets the status of the
     * given work item to "in progress".
     * 
     * Work item properties:
     * <ul>
     * <li>id: {@link Long} - work item id</li>
     * <li>creationDate: {@link Date} - date when the work item was created</li>
     * <li>status: {@link String} - see WorkItemStatus</li>
     * <li>data: byte[] - raw xml bytes of a {@link THumanTaskData} instance</li>
     * <li>name: {@link String} - work item name</li>
     * <li>description: {@link String} -</li>
     * </ul>
     * 
     * @param workItemId
     *            the work item to retrieve
     * @return Vaadin item containing the properties of the work item
     * @throws TransactionException
     *             iff the transaction is aborted for any reason
     * @throws EntityNotFoundException
     *             if the work item does not exist
     * @throws IllegalArgumentException
     *             if workItemId is <code>null</code>
     */
    @AllowedRole(UserRole.class)
    public Item getWorkItemAndMarkAsInProgress(Long workItemId)
            throws TransactionException {

        GetWorkItemCommand getCommand = new GetWorkItemCommand(actor,
                workItemId);

        SetStatusCommand statusCommand = new SetStatusCommand(actor,
                workItemId, WorkItemStatus.InProgress);

        HibernateTransactionCoordinator.getInstance().runTransaction(
                getCommand, statusCommand);

        String[] properties = { "id", "workflowInstance", "creationDate",
                "status", "data", "name", "description" };

        return new BeanItem(getCommand.getResult(), properties);
    }
}