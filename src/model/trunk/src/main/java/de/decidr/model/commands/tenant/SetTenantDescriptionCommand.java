package de.decidr.model.commands.tenant;

import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Creates a new Command which updates the description of an tenant.
 * 
 * @author Markus Fischer
 *
 * @version 0.1
 */
public class SetTenantDescriptionCommand extends TenantCommand {

    private Long tenantId;
    private String description;
    
    /**
     * 
     * Creates a new Command which updates the description of an tenant.
     * 
     * @param role user which executes the command
     * @param tenantId
     * @param description
     */
    public SetTenantDescriptionCommand(Role role,Long tenantId, String description){
        super(role, null);
        
        this.tenantId=tenantId;
        this.description=description;
        
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        
        Tenant tenant = (Tenant)evt.getSession().load(Tenant.class, tenantId);
        tenant.setDescription(description);
        evt.getSession().update(tenant);
        
    }

}
