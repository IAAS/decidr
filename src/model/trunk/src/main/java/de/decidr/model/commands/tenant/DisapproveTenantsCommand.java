package de.decidr.model.commands.tenant;

import java.util.List;

import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Disapproves all tenants which corresponds to the given ids (tenants will be
 * deleted).
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class DisapproveTenantsCommand extends AclEnabledCommand {

    private List<Long> tenantIds;

    /**
     * 
     * Creates a new ApproveTenantsCommand. This Command will disapprove all
     * tenants which corresponds to the given ids (tenants will be deleted).
     * 
     * @param role
     *            the user which executes the command
     * @param tenantIds
     */
    public DisapproveTenantsCommand(Role role, List<Long> tenantIds) {
        super(role, (Permission)null);

        this.tenantIds = tenantIds;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        for (Long tenantid : tenantIds) {
            Tenant tenant = (Tenant) evt.getSession().get(Tenant.class,
                    tenantid);
            if (tenant != null) {
                if (tenant.getApprovedSince() == null) {
                    evt.getSession().delete(tenant);
                }

            }

        }

    }

}
