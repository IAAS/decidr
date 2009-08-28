package de.decidr.model.facades;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

import de.decidr.model.acl.roles.HumanTaskRole;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.WorkflowAdminRole;
import de.decidr.model.annotations.AllowedRole;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.commands.workflowinstance.DeleteWorkflowInstanceCommand;
import de.decidr.model.commands.workflowinstance.GetAllWorkitemsCommand;
import de.decidr.model.commands.workflowinstance.GetParticipatingUsersCommand;
import de.decidr.model.commands.workflowinstance.RemoveAllWorkItemsCommand;
import de.decidr.model.commands.workflowinstance.StopWorkflowInstanceCommand;
import de.decidr.model.entities.User;
import de.decidr.model.entities.WorkItem;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;

/**
 * The WorkflowInstanceFacade contains all functions which are available to
 * create and control workflow instacnes.
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
     * 
     */
    public WorkflowInstanceFacade(Role actor) {
        super(actor);
    }

    /**
     * Stops the given WorkflowInstance.
     * 
     * @param workflowInstanceId
     *            the ID of the WorkflowInstance
     */
    @AllowedRole(WorkflowAdminRole.class)
    public void stopWorkflowInstance(Long workflowInstanceId)
            throws TransactionException {

        StopWorkflowInstanceCommand command = new StopWorkflowInstanceCommand(
                actor, workflowInstanceId);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Returns all participants of the given WorkflowInstance
     * 
     * @param workflowInstanceId
     *            the ID of the workflow instance whose participating users
     *            should be requested
     * @return List of participatin Users
     */
    @SuppressWarnings("unchecked")
    @AllowedRole(WorkflowAdminRole.class)
    public List<Item> getParticipatingUsers(Long workflowInstanceId)
            throws TransactionException {

        GetParticipatingUsersCommand command = new GetParticipatingUsersCommand(
                actor, workflowInstanceId);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);

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
     *            the ID of the WorkflowInstance
     */
    @AllowedRole(WorkflowAdminRole.class)
    public void deleteWorkflowInstance(Long workflowInstanceId)
            throws TransactionException {

        StopWorkflowInstanceCommand command = new StopWorkflowInstanceCommand(
                actor, workflowInstanceId);
        DeleteWorkflowInstanceCommand command2 = new DeleteWorkflowInstanceCommand(
                actor, workflowInstanceId);

        TransactionalCommand[] commands = { command, command2 };

        HibernateTransactionCoordinator.getInstance().runTransaction(commands);

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
     */
    @AllowedRole(HumanTaskRole.class)
    public void removeAllWorkItems(String odePid, Long deployedWorkflowModelId)
            throws TransactionException {

        RemoveAllWorkItemsCommand command = new RemoveAllWorkItemsCommand(
                actor, odePid, deployedWorkflowModelId);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * 
     * Returns all WorkItems of a WorkflowInstance as a Vaadin-Item with the
     * following properties:<br>
     * <ul>
     * <li>id</li>
     * </ul>
     * 
     * @param workflowInstanceId
     *            the ID of the WorkflowInstance
     * @return List<Items> list of items described above
     */
    @SuppressWarnings("unchecked")
    @AllowedRole(WorkflowAdminRole.class)
    public List<Item> getAllWorkItems(Long workflowInstanceId)
            throws TransactionException {

        GetAllWorkitemsCommand command = new GetAllWorkitemsCommand(actor,
                workflowInstanceId);

        String[] properties = { "id" };
        List<Item> outList = new ArrayList();
        Set<WorkItem> inSet;

        HibernateTransactionCoordinator.getInstance().runTransaction(command);

        inSet = command.getResult();

        for (WorkItem item : inSet) {
            outList.add(new BeanItem(item, properties));
        }

        return outList;
    }
}