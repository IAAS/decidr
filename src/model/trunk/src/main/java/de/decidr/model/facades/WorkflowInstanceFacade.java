package de.decidr.model.facades;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

import de.decidr.model.annotations.AllowedRole;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.commands.workflowinstance.DeleteWorkFlowInstanceCommand;
import de.decidr.model.commands.workflowinstance.GetAllWorkitemsCommand;
import de.decidr.model.commands.workflowinstance.GetParticipatingUsersCommand;
import de.decidr.model.commands.workflowinstance.RemoveAllWorkItemsCommand;
import de.decidr.model.commands.workflowinstance.StopWorkflowInstanceCommand;
import de.decidr.model.entities.User;
import de.decidr.model.entities.WorkItem;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.HumanTaskRole;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.WorkflowAdminRole;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionCoordinator;

public class WorkflowInstanceFacade extends AbstractFacade {

    /**
     * 
     * Creates a new WorkflowInstaceFacade. All Commands will be executed by the
     * given actor.
     * 
     * @param actor
     */
    public WorkflowInstanceFacade(Role actor) {
        super(actor);
    }

    /**
     * Stops the given WorkflowInstance.
     * 
     * @param workflowInstanceId
     *            the id of the WorkflowInstance
     */
    @AllowedRole(WorkflowAdminRole.class)
    public void stopWorkflowInstance(Long workflowInstanceId)
            throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        StopWorkflowInstanceCommand command = new StopWorkflowInstanceCommand(
                actor, workflowInstanceId);

        tac.runTransaction(command);

    }

    /**
     * Returns all Participants of the given WorkflowInstance
     * 
     * @param workflowInstanceId
     * @return
     */
    @SuppressWarnings("unchecked")
    @AllowedRole(WorkflowAdminRole.class)
    public List<Item> getParticipatingUsers(Long workflowInstanceId)
            throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();

        GetParticipatingUsersCommand command = new GetParticipatingUsersCommand(
                actor, workflowInstanceId);

        tac.runTransaction(command);

        Set<User> inSet = command.getResult();
        List<Item> outList = new ArrayList();

        String[] properties = { "id,email" };

        for (User user : inSet) {
            outList.add(new BeanItem(user, properties));
        }

        return outList;
    }

    /**
     * Stops the WorkflowInstance and deletes the corresponding representation
     * object at the database.
     * 
     * @param workflowInstanceId
     *            the id of the WorkflowInstance
     */
    @AllowedRole(WorkflowAdminRole.class)
    public void deleteWorkflowInstance(Long workflowInstanceId)
            throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();

        StopWorkflowInstanceCommand command = new StopWorkflowInstanceCommand(
                actor, workflowInstanceId);
        DeleteWorkFlowInstanceCommand command2 = new DeleteWorkFlowInstanceCommand(
                actor, workflowInstanceId);

        TransactionalCommand[] commands = { command, command2 };

        tac.runTransaction(commands);

    }

    /**
     * Removes all work items from the workflow instance that is identified by
     * the given ODE process id and the given workflow model id. The workflow
     * instance itself is <b>not</b> deleted from the database.
     * 
     * @param odePid
     * @param deployedWorkflowModelId
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    @AllowedRole(HumanTaskRole.class)
    public void removeAllWorkItems(String odePid, Long deployedWorkflowModelId)
            throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        RemoveAllWorkItemsCommand command = new RemoveAllWorkItemsCommand(
                actor, odePid, deployedWorkflowModelId);

        tac.runTransaction(command);

    }

    /**
     * 
     * Returns all WorkItems of a WorkflowInstance as a Vaadin-Item with the
     * following properties: - id
     * 
     * @param workflowInstanceId
     *            the Id of the WorkflowInstance
     * @return List<Items>
     */
    @SuppressWarnings("unchecked")
    @AllowedRole(WorkflowAdminRole.class)
    public List<Item> getAllWorkItems(Long workflowInstanceId)
            throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        GetAllWorkitemsCommand command = new GetAllWorkitemsCommand(actor,
                workflowInstanceId);

        String[] properties = { "id" };
        List<Item> outList = new ArrayList();
        Set<WorkItem> inSet;

        tac.runTransaction(command);

        inSet = command.getResult();

        for (WorkItem item : inSet) {
            outList.add(new BeanItem(item, properties));
        }

        return outList;
    }
}