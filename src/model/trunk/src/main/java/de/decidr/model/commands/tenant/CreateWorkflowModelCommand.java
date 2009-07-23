package de.decidr.model.commands.tenant;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Creates a new workflow model with the given property Name. Furthermore the
 * model will be added to the given tenant. If the given tenant does not exist
 * an exception will be thrown.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class CreateWorkflowModelCommand extends TenantCommand {

    private Long tenantId;
    private String WorkflowModelName;
    private Long workflowModelId;

    /**
     * Creates a new CreateWorkflowModelCommand. This command creates a new
     * Workflow model with the given property Name. Furthermore the model will
     * be added to the given tenant. If the given tenant does not exist an
     * exception will be thrown.
     * 
     * @param role
     *            TODO document
     * @param tenantId
     *            TODO document
     * @param WorkflowModelName
     *            TODO document
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
            model.setCreationDate(DecidrGlobals.getTime().getTime());
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
