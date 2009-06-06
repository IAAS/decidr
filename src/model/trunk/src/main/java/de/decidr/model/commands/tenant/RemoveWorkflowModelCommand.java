package de.decidr.model.commands.tenant;

import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

public class RemoveWorkflowModelCommand extends TenantCommand {

    private Long workflowModelId;
    
    /**
     * 
     * Creates a new RemoveWorkflowModelCommand. This command deletes the given WorkflowModel.
     * If the given workflow model doesn't exist nothing will happen.
     * 
     * @param role user which executes the command
     * @param tenantId
     * @param workflowModelId
     */
    public RemoveWorkflowModelCommand(Role role,Long workflowModelId) {
        super(role, null);
        
        this.workflowModelId=workflowModelId;
        
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        
        WorkflowModel model = (WorkflowModel)evt.getSession().get(WorkflowModel.class, workflowModelId);
        if(model !=null){
        evt.getSession().delete(model);
        }

    }

}
