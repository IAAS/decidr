package de.decidr.model.commands.workflowinstance;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.transactions.TransactionEvent;
import de.decidr.model.workflowmodel.instancemanagement.InstanceManagerImpl;


/**
 * 
 * Stops the workflow instance which corresponds to the given id.
 * 
 * @author Markus Fischer
 *
 * @version 0.1
 */
public class StopWorkflowInstanceCommand extends WorkflowInstanceCommand {

    private Long workflowInstanceId;
    
    public StopWorkflowInstanceCommand(Role role, Long workflowinstanceId) {
        super(role, null);
        
        this.workflowInstanceId=workflowinstanceId;

    }

    @Override
    public void transactionAllowed(TransactionEvent evt) {
        
        InstanceManagerImpl iManager = new InstanceManagerImpl();
        
        iManager.stopInstance((WorkflowInstance)evt.getSession().load(WorkflowInstance.class, workflowInstanceId));

    }

}
