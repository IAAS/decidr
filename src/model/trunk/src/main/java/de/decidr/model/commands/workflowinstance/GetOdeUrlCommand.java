package de.decidr.model.commands.workflowinstance;

import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Returns the Url of the server on which the given WorkflowInstance is executed
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class GetOdeUrlCommand extends WorkflowInstanceCommand {

    private Long id;
    private String result;
    
    public GetOdeUrlCommand(Role role, Long workflowInstanceId) {
        super(role, null);
        this.id = workflowInstanceId;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt) {
        
        WorkflowInstance instance = (WorkflowInstance)evt.getSession().load(WorkflowInstance.class, id);
        
        //FIXME nicht sicher was drinstehen soll
        result = instance.getServer().getLocation();
        
    }

    public String getResult() {
        return result;
    }

}
