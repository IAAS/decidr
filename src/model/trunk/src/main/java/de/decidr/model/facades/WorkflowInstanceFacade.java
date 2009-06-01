package de.decidr.model.facades;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.commands.workflowinstance.DeleteWorkFlowInstanceCommand;
import de.decidr.model.commands.workflowinstance.GetAllWorkitemsCommand;
import de.decidr.model.commands.workflowinstance.GetOdeUrlCommand;
import de.decidr.model.commands.workflowinstance.GetParticipatingUsersCommand;
import de.decidr.model.commands.workflowinstance.StopWorkflowInstanceCommand;
import de.decidr.model.entities.User;
import de.decidr.model.entities.WorkItem;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionCoordinator;

public class WorkflowInstanceFacade extends AbstractFacade {

    /**
     * 
     * Creates a new WorkflowInstaceFacade. All Commands will be executed by the given actor.
     * 
     * @param actor
     */
    public WorkflowInstanceFacade(Role actor) {
        super(actor);
    }

    
    /**
     * Stops the given WorkflowInstance.
     * 
     * @param workflowInstanceId the id of the WorkflowInstance
     */
    public void stopWorkflowInstance(Long workflowInstanceId) {

        TransactionCoordinator tac = HibernateTransactionCoordinator
        .getInstance();
        StopWorkflowInstanceCommand command = new StopWorkflowInstanceCommand(actor,workflowInstanceId);
        
        tac.runTransaction(command);
    
    }

    /**
     * Returns the OdeUrl of the given WorkflowInstance.
     * 
     * @param workflowInstanceId
     * @return
     */
    public String getOdeUrl(Long workflowInstanceId) {

        TransactionCoordinator tac = HibernateTransactionCoordinator
        .getInstance();
        
        GetOdeUrlCommand command = new GetOdeUrlCommand(actor,workflowInstanceId);
        
        tac.runTransaction(command);
        return command.getResult();

    }

    /**
     * Returns all Participants of the given WorkflowInstance
     * 
     * @param workflowInstanceId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Item> getParticipatingUsers(Long workflowInstanceId) {
        
        TransactionCoordinator tac = HibernateTransactionCoordinator
        .getInstance();
        
        GetParticipatingUsersCommand command = new GetParticipatingUsersCommand(actor,workflowInstanceId);
        
        tac.runTransaction(command);
        
        Set<User> inSet = command.getResult();
        List<Item> outList = new ArrayList();
        //FIXME Werte anpassen
        String[] properties={"id,email"};
        
        for(User user: inSet){
            outList.add(new BeanItem(user, properties));
        }
        
        return outList;
    }

    /**
     * Stops the WorkflowInstance and deletes the corresponding representation object
     * at the database.
     * 
     * @param workflowInstanceId the id of the WorkflowInstance
     */
    public void deleteWorkflowInstance(Long workflowInstanceId) {
        
        TransactionCoordinator tac = HibernateTransactionCoordinator
        .getInstance();
        
        StopWorkflowInstanceCommand command = new StopWorkflowInstanceCommand(actor,workflowInstanceId);
        DeleteWorkFlowInstanceCommand command2 = new DeleteWorkFlowInstanceCommand(actor,workflowInstanceId);
        
        TransactionalCommand[] commands = {command,command2};
        
        tac.runTransaction(commands);
        
    }

    /**
     * 
     * Returns all WorkItems of a WorkflowInstance as a Vadim-Item with
     * the following properties:
     * - id
     * 
     * @param workflowInstanceId the Id of the WorkflowInstance
     * @return List<Items>
     */
    @SuppressWarnings("unchecked")
    public List<Item> getAllWorkItems(Long workflowInstanceId) {
        
        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        GetAllWorkitemsCommand command = new GetAllWorkitemsCommand(actor,workflowInstanceId);
        
        String[] properties = {"id"};
        List<Item> outList = new ArrayList();
        Set<WorkItem> inSet = new HashSet();
        
        tac.runTransaction(command);

        for(WorkItem item:inSet){
            outList.add(new BeanItem(item, properties));
        }
        
        return outList;
    }
}