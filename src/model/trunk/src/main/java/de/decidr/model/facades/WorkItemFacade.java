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

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

import de.decidr.model.annotations.AllowedRole;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.commands.workitem.CreateWorkItemCommand;
import de.decidr.model.commands.workitem.DeleteWorkItemCommand;
import de.decidr.model.commands.workitem.GetWorkItemCommand;
import de.decidr.model.commands.workitem.SetDataCommand;
import de.decidr.model.commands.workitem.SetStatusCommand;
import de.decidr.model.enums.WorkItemStatus;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.HumanTaskRole;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.UserRole;
import de.decidr.model.permissions.WorkflowAdminRole;
import de.decidr.model.transactions.HibernateTransactionCoordinator;

/**
 * Provides an interface for retrieving and manipulating work items.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class WorkItemFacade extends AbstractFacade {

    /**
     * Constructor TODO document
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
     * <li>id: Long - work item id</li>
     * <li>creationDate: Date - date when the work item was created</li>
     * <li>status: String - see WorkItemStatus</li>
     * <li>data: byte[] - raw xml bytes</li>
     * <li>name: String - work item name</li>
     * <li>description: String -</li>
     * </ul>
     * 
     * @param workItemId
     *            TODO document
     * @return Vaadin item TODO document
     * @throws TransactionException
     *             on rollback or if the given work item doesn't exist.
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
     *            the data associated with this work item (XML)
     * @param notifyUser
     *            whether the user should be notified by email that a new work
     *            item has been created.
     * @return the ID of the new work item.
     * @throws TransactionException
     *             on rollback
     */
    @AllowedRole(HumanTaskRole.class)
    public Long createWorkItem(Long userId, Long deployedWorkflowModelId,
            String odePid, String name, String description, byte[] data,
            Boolean notifyUser) throws TransactionException {

        CreateWorkItemCommand cmd = new CreateWorkItemCommand(actor, userId,
                deployedWorkflowModelId, odePid, name, description, data,
                notifyUser);

        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        return cmd.getWorkItemId();
    }

    /**
     * Overwrites the XML data of the given work item with the given raw data.
     * 
     * @param workItemId
     *            the work item to modify
     * @param data
     *            the raw bytes to write to the work item
     * @throws TransactionException
     *             on rollback
     */
    @AllowedRole(UserRole.class)
    public void setData(Long workItemId, byte[] data)
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
     *            the raw bytes to write to the work item
     * @throws TransactionException
     *             on rollback
     */
    @AllowedRole(UserRole.class)
    public void setDataAndMarkAsDone(Long workItemId, byte[] data)
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
     *            TODO document
     * @throws TransactionException
     *             on rollback
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
     *            TODO document
     * @throws TransactionException
     *             on rollback
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
     * @param workItemId
     *            the work item to retrieve
     * @return Vaadin item containing the properties of the work item.
     * @throws TransactionException
     *             on rollback
     */
    @AllowedRole(UserRole.class)
    public Item getWorkItemAndMarkAsInProgress(Long workItemId)
            throws TransactionException {

        GetWorkItemCommand getCommand = new GetWorkItemCommand(actor,
                workItemId);

        TransactionalCommand[] commands = {
                new SetStatusCommand(actor, workItemId,
                        WorkItemStatus.InProgress), getCommand };

        HibernateTransactionCoordinator.getInstance().runTransaction(commands);

        String[] properties = { "id", "workflowInstance", "creationDate",
                "status", "data", "name", "description" };

        return new BeanItem(getCommand.getResult(), properties);
    }
}