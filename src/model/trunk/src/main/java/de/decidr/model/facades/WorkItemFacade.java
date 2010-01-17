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

import java.net.MalformedURLException;

import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.HumanTaskRole;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.acl.roles.WorkflowAdminRole;
import de.decidr.model.annotations.AllowedRole;
import de.decidr.model.commands.workitem.CreateWorkItemCommand;
import de.decidr.model.commands.workitem.DeleteWorkItemCommand;
import de.decidr.model.commands.workitem.GetWorkItemCommand;
import de.decidr.model.commands.workitem.SetDataCommand;
import de.decidr.model.commands.workitem.SetStatusCommand;
import de.decidr.model.entities.WorkItem;
import de.decidr.model.enums.WorkItemStatus;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.soap.exceptions.ReportingException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.webservices.HumanTaskClientStatic;
import de.decidr.model.webservices.HumanTaskInterface;
import de.decidr.model.workflowmodel.humantask.THumanTaskData;

/**
 * Provides an interface for retrieving and manipulating workitems.
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
     *            user/system that is using this facade to access workitems.
     */
    public WorkItemFacade(Role actor) {
        super(actor);
    }

    /**
     * Fetches a workitem from the database.
     * 
     * Preloaded foreign key properties:
     * <ul>
     * <li>workflowInstance</li>
     * <li>workflowInstance.deployedWorkflowModel</li>
     * </ul>
     * 
     * @param workItemId
     *            the ID of the workitem which should be requested
     * @return workitem
     * @throws TransactionException
     *             iff the transaction is aborted for any reason
     * @throws EntityNotFoundException
     *             if the workitem does not exist.
     * @throws IllegalArgumentException
     *             if workItemId is <code>null</code>
     */
    @AllowedRole(UserRole.class)
    public WorkItem getWorkItem(Long workItemId) throws TransactionException {
        GetWorkItemCommand cmd = new GetWorkItemCommand(actor, workItemId);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        return cmd.getResult();
    }

    /**
     * Creates a new workitem in the database, making it available to the user.
     * 
     * @param userId
     *            the user to whom the workitem belongs
     * @param deployedWorkflowModelId
     *            the deployed workflow model id of the workflow instance.
     * @param odePid
     *            the ODE process id of the workflow instance.
     * @param name
     *            the name of the new workitem.
     * @param description
     *            the description of the new workitem
     * @param data
     *            the data associated with this workitem
     * @param notifyUser
     *            whether the user should be notified by email that a new work
     *            item has been created.
     * @return the ID of the new workitem.
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
     * Overwrites the current data of the given workitem.
     * 
     * @param workItemId
     *            the workitem to modify
     * @param data
     *            the new workitem data
     * @throws TransactionException
     *             iff the transaction is aborted for any reason
     * @throws EntityNotFoundException
     *             if the workitem does not exist
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
     * Overwrites the data of the given workitem and sets its status to "done"
     * by invoking the HumanTask WS.
     * 
     * @param workItemId
     *            the workitem to modify
     * @param data
     *            the new workitem data.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason
     * @throws EntityNotFoundException
     *             if the workitem does not exist
     * @throws IllegalArgumentException
     *             if workItemId is or data is <code>null</code>.
     */
    @AllowedRole(UserRole.class)
    public void setDataAndMarkAsDone(Long workItemId, THumanTaskData data)
            throws TransactionException {
        HibernateTransactionCoordinator.getInstance().runTransaction(
                new SetDataCommand(actor, workItemId, data));
        markWorkItemAsDone(workItemId);
    }

    /**
     * Sets the status of the given workitem to "done" by invoking the HumanTask
     * WS.
     * 
     * @param workItemId
     *            ID of the workitem which should be marked as done
     * @throws TransactionException
     *             iff the transaction is aborted for any reason
     * @throws EntityNotFoundException
     *             if the workitem does not exist.
     * @throws IllegalArgumentException
     *             if workItemId is <code>null</code>.
     */
    @AllowedRole(BasicRole.class)
    public void markWorkItemAsDone(Long workItemId) throws TransactionException {
        // XXX no acl check ~dh
        try {
            HumanTaskInterface humanTask = new HumanTaskClientStatic()
                    .getHumanTaskSOAP();
            humanTask.taskCompleted(workItemId);
        } catch (ReportingException e) {
            throw new TransactionException(e);
        } catch (MalformedURLException e) {
            throw new TransactionException(e);
        }
    }

    /**
     * Deletes the given workitem from the database.
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
     * Fetches a workitem from the database and sets the status of the given
     * workitem to "in progress".
     * 
     * @param workItemId
     *            the workitem to retrieve
     * @return workitem
     * @throws TransactionException
     *             iff the transaction is aborted for any reason
     * @throws EntityNotFoundException
     *             if the workitem does not exist
     * @throws IllegalArgumentException
     *             if workItemId is <code>null</code>
     */
    @AllowedRole(UserRole.class)
    public WorkItem getWorkItemAndMarkAsInProgress(Long workItemId)
            throws TransactionException {
        GetWorkItemCommand getCommand = new GetWorkItemCommand(actor,
                workItemId);
        SetStatusCommand statusCommand = new SetStatusCommand(actor,
                workItemId, WorkItemStatus.InProgress);
        HibernateTransactionCoordinator.getInstance().runTransaction(
                getCommand, statusCommand);
        return getCommand.getResult();
    }
}