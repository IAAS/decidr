package de.decidr.model.commands.workflowinstance;

import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;
import de.decidr.model.workflowmodel.instancemanagement.InstanceManagerImpl;


//FIXME Comment Me
public class StopWorkflowInstanceCommand extends WorkflowInstanceCommand {

    private Long workflowInstanceId;
    
    public StopWorkflowInstanceCommand(Role role, Long workflowinstanceId) {
        super(role, null);
        
        this.workflowInstanceId=workflowinstanceId;

    }

    @Override
    // FIXME Comment Me
    public void transactionAllowed(TransactionEvent evt) {
        
        InstanceManagerImpl iManager = new InstanceManagerImpl();
        
        // FIMXE stopInstance fehlt
        //.startInstance(dwfm, startConfiguration, serverStatistics);

    }

}
