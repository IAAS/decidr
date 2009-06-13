package de.decidr.model.commands.tenant;

import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Deletes all tenants which corresponds to the given ids including all
 * corresponding WorkflowInstances and workitems.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class DeleteTenantCommand extends TenantCommand {

    private Long tenantId;

    /**
     * 
     * Creates a new DeleteTenantsCommand. This Command will delete all tenants
     * which corresponds to the given ids including all corresponding
     * WorkflowInstances and workitems. all .
     * 
     * @param role
     *            the user which executes the command
     * @param tenantId
     */
    public DeleteTenantCommand(Role role, Long tenantId) {
        super(role, null);

        this.tenantId = tenantId;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        Tenant tenant = (Tenant) evt.getSession().get(Tenant.class, tenantId);

        if (tenant != null) {

            // SECIT FileHandling not yet implemented
            // delete File logo;
            // delete File simpleColorScheme;
            // delete File advancedColorScheme;
            // delete File currentColorScheme;

            for (DeployedWorkflowModel model : tenant
                    .getDeployedWorkflowModels()) {

                // SECIT Say Modood that he should unploy it

                /*
                 * corresponding WorkflowInstances and Woritems will be
                 * automatically deleted by the database
                 */
                evt.getSession().delete(model);

            }

        }

    }

}
