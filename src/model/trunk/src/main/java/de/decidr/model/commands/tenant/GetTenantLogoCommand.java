package de.decidr.model.commands.tenant;

import java.io.InputStream;

import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Saves the TenantLogo in the result variable.
 * 
 * @author Markus Fischer
 *
 * @version 0.1
 */
public class GetTenantLogoCommand extends TenantCommand {

    private Long tenantId;
    private InputStream logoStream;
    
    public GetTenantLogoCommand(Role role,Long tenantId){
        super(role, null);
        this.tenantId=tenantId;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        
        Tenant tenant = (Tenant)evt.getSession().load(Tenant.class, tenantId);
        tenant.getLogo();
        //SECIT FileHandling fehlt noch
        
    }

    public InputStream getLogoStream() {
        return logoStream;
    }

}
