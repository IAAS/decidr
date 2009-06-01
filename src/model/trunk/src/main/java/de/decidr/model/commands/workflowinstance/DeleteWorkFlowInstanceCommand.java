package de.decidr.model.commands.workflowinstance;

import java.util.Collection;

import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Deletes the representative object of the given WowrkFlowInstance at the database
 * 
 * @author Markus Fischer
 *
 * @version 0.1
 */
public class DeleteWorkFlowInstanceCommand extends WorkflowInstanceCommand {

    private Long id;
    
    public DeleteWorkFlowInstanceCommand(Role role,Long WorkflowInstanceId) {
        super(role, null);
        this.id=WorkflowInstanceId;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt) {
        
        WorkflowInstance instance = (WorkflowInstance)evt.getSession().load(WorkflowInstance.class, id);
        
        evt.getSession().delete(instance);

    }

}
