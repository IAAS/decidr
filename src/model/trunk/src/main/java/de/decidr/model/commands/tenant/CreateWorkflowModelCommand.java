package de.decidr.model.commands.tenant;

import java.util.Date;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

public class CreateWorkflowModelCommand extends TenantCommand {

    private Long tenantId;
    private String WorkflowModelName;
    private Long workflowModelId;
    
    /**
     * 
     * creates a new CreateWorkflowModelCommand. This command creates a new workflow model
     * with the given property Name. Furthermore the model will be added to the given tenant.
     * 
     * @param role
     * @param tenantId
     * @param WorkflowModelName
     */
    public CreateWorkflowModelCommand(Role role,Long tenantId, String WorkflowModelName) {
        super(role, null);
        
        this.tenantId=tenantId;
        this.WorkflowModelName=WorkflowModelName;
        
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        
        Tenant tenant = (Tenant)evt.getSession().load(Tenant.class, tenantId);
        
        WorkflowModel model = new WorkflowModel();
        model.setName(WorkflowModelName);
        model.setCreationDate(new Date());
        model.setExecutable(false);
        model.setPublished(false); 
        model.setTenant(tenant);
        
        evt.getSession().update(tenant);
        
        workflowModelId = model.getId();
        

    }

    public Long getWorkflowModelId() {
        return workflowModelId;
    }

}
