package de.decidr.model.commands.tenant;

import java.util.List;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

public class DisapproveTenantsCommand extends TenantCommand {

    private List<Long> tenantIds;
    
    /**
     * 
     * Creates a new ApproveTenantsCommand. This Command will disapprove all
     * tenants which corresponds to the given ids (tenants will be deleted).
     * 
     * @param role the user which executes the command
     * @param tenantIds
     */
    public DisapproveTenantsCommand(Role role, List<Long> tenantIds) {
        super(role, null);
        
        this.tenantIds=tenantIds;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        
        for(Long tenantid:tenantIds){
            Tenant tenant = (Tenant)evt.getSession().get(Tenant.class, tenantid);
            if(tenant != null){
                if(tenant.getApprovedSince()==null){
                    evt.getSession().delete(tenant);
                }
                
            }
            
        }
        

    }

}
