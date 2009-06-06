package de.decidr.model.commands.tenant;

import java.util.Date;
import java.util.List;

import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

public class ApproveTenantsCommand extends TenantCommand {

    private List<Long> tenantIds;
    
    /**
     * 
     * Creates a new ApproveTenantsCommand. This Command will approve all
     * tenants which corresponds to the given ids.
     * 
     * @param role the user which executes the command
     * @param tenantIds
     */
    public ApproveTenantsCommand(Role role, List<Long> tenantIds) {
        super(role, null);
        
        this.tenantIds=tenantIds;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        
        for(Long tenantid:tenantIds){
            Tenant tenant = (Tenant)evt.getSession().get(Tenant.class, tenantid);
            if(tenant != null){
                tenant.setApprovedSince(new Date());
                evt.getSession().update(tenant);
            }
            
        }
        

    }

}
