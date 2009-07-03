package de.decidr.model.commands.tenant;

import java.util.Date;
import java.util.List;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Approves all tenants which corresponds to the given IDs.<br>
 * Not existing tenants will we ignored.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 *
 * @version 0.1
 */
public class ApproveTenantsCommand extends AclEnabledCommand {

    private List<Long> tenantIds;
    
    /**
     * 
     * Creates a new ApproveTenantsCommand. This Command will approve all
     * tenants which corresponds to the given IDs. Not existing tenants will we ignored.
     * 
     * @param role the user which executes the command
     * @param tenantIds
     */
    public ApproveTenantsCommand(Role role, List<Long> tenantIds) {
        super(role, (Permission)null);
        
        this.tenantIds=tenantIds;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        
        for(Long tenantid:tenantIds){
            Tenant tenant = (Tenant)evt.getSession().get(Tenant.class, tenantid);
            if(tenant != null){
                if(tenant.getApprovedSince()==null){
                    tenant.setApprovedSince(DecidrGlobals.getTime().getTime());
                    evt.getSession().update(tenant);
                }
              }
            
        }
        

    }

}
