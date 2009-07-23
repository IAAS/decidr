package de.decidr.model.commands.tenant;

import java.util.List;

import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Imports the given workflow models to the given tenant. If one or more of the
 * given models are not public an exception will be thrown.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class ImportPublishedWorkflowModelsCommand extends TenantCommand {

    private List<Long> modelIdList;
    private List<WorkflowModel> modelList;

    /**
     * Creates a new ImportPublishedWorkflowModelsCommand. This command imports
     * the given workflow models to the given tenant. If one or more of the
     * given models are not public an exception will be thrown.
     * 
     * @param role
     *            TODO document
     * @param tenantId
     *            TODO document
     * @param workflowModelIds
     *            TODO document
     */
    public ImportPublishedWorkflowModelsCommand(Role role, Long tenantId,
            List<Long> workflowModelIds) {
        super(role, tenantId);
        this.modelIdList = workflowModelIds;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        Tenant t = (Tenant) evt.getSession().get(Tenant.class, getTenantId());

        if (t == null) {
            throw new EntityNotFoundException(Tenant.class, getTenantId());
        }

        for (Long id : modelIdList) {
            modelList.add((WorkflowModel) evt.getSession().load(
                    WorkflowModel.class, id));
        }

        for (WorkflowModel model : modelList) {

            if (model.isPublished()) {
                model.setId(null);
                model.setTenant(t);
                evt.getSession().save(t);
            } else {
                throw new TransactionException(
                        "Given workflowModel is not published.");
            }
        }
    }
}
