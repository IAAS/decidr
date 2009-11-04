package de.decidr.model.commands.tenant;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Updates the description of a tenant.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class SetTenantDescriptionCommand extends TenantCommand {

    private String description;

    /**
     * Creates a new Command which updates the description of a tenant.
     * 
     * @param role
     *            user / system which executes the command
     * @param tenantId
     *            the id of the tenant which should be updated
     * @param description
     *            the new description
     * @throws IllegalArgumentException
     *             if tenantId is <code>null</code>
     */
    public SetTenantDescriptionCommand(Role role, Long tenantId,
            String description) {
        super(role, tenantId);
        requireTenantId();
        this.description = description;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        Tenant tenant = fetchTenant(evt.getSession());
        tenant.setDescription(description);
        evt.getSession().update(tenant);
    }
}
