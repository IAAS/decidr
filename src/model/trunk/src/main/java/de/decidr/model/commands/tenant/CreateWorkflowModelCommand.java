package de.decidr.model.commands.tenant;

import java.util.Date;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

public class CreateWorkflowModelCommand extends TenantCommand {

    private Long tenantId;
    private String WorkflowModelName;
    private Long workflowModelId;

    /**
     * 
     * Creates a new CreateWorkflowModelCommand. This command creates a new
     * Workflow model with the given property Name. Furthermore the model will
     * be added to the given tenant. If the given tenant does not exist an exception
     * will be thrown.
     * 
     * @param role
     * @param tenantId
     * @param WorkflowModelName
     */
    public CreateWorkflowModelCommand(Role role, Long tenantId,
            String WorkflowModelName) {
        super(role, tenantId);
        this.WorkflowModelName = WorkflowModelName;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        Tenant tenant = (Tenant) evt.getSession().get(Tenant.class, tenantId);

        if (tenant == null) {
            throw new EntityNotFoundException(Tenant.class, tenantId);
        } else {

            WorkflowModel model = new WorkflowModel();
            model.setName(WorkflowModelName);
            model.setCreationDate(new Date());
            model.setExecutable(false);
            model.setPublished(false);
            model.setTenant(tenant);

            evt.getSession().update(tenant);

            workflowModelId = model.getId();
        }

    }

    public Long getWorkflowModelId() {
        return workflowModelId;
    }

}
