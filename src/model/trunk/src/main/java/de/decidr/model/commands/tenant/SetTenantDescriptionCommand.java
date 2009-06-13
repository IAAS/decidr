package de.decidr.model.commands.tenant;

import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Updates the description of a tenant.
 * 
 * @author Markus Fischer
 *
 * @version 0.1
 */
public class SetTenantDescriptionCommand extends TenantCommand {

    private String description;
    
    /**
     * 
     * Creates a new Command which updates the description of a tenant.
     * 
     * @param role user which executes the command
     * @param tenantId the id of the tenant which should be updated
     * @param description the new description
     */
    public SetTenantDescriptionCommand(Role role,Long tenantId, String description){
        super(role, tenantId);  
        this.description=description;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        
        Tenant tenant = (Tenant)evt.getSession().get(Tenant.class, this.getTenantId());
        
        if(tenant != null){
            tenant.setDescription(description);
            evt.getSession().update(tenant);
        }
        else{
            throw new EntityNotFoundException(Tenant.class,this.getTenantId());
        }

        
    }

}
