package de.decidr.model.commands.workflowinstance;

import java.util.HashSet;
import java.util.Set;

import de.decidr.model.entities.WorkItem;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Writes all Workitems of the given WorkflowInstance in the
 * result variable
 * 
 * @author Markus Fischer
 *
 * @version 0.1
 */
public class GetAllWorkitemsCommand extends WorkflowInstanceCommand {

    private Long workflowInstanceId;
    @SuppressWarnings("unchecked")
    private Set<WorkItem> result =  new HashSet();
    
    
    /**
     * 
     * Creates a new GetAllWorkitemsCommand.
     * 
     * @param role the user which want to execute the command
     * @param WorkflowInstanceId the id of the corresponding WorkflowInstance
     */
    public GetAllWorkitemsCommand(Role role, Long WorkflowInstanceId) {
        super(role, null);
        this.workflowInstanceId = WorkflowInstanceId;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt) {
        
        WorkflowInstance instance = (WorkflowInstance)evt.getSession().load(WorkflowInstance.class, workflowInstanceId);
        
        result = instance.getWorkItems();
    }

    public Set<WorkItem> getResult() {
        return result;
    }

}
