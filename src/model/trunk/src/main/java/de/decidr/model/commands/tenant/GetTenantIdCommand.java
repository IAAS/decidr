package de.decidr.model.commands.tenant;

import java.util.List;
import org.hibernate.Query;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

public class GetTenantIdCommand extends TenantCommand {

    private String tenantName;
    private Long tenantId = null;
    
    /**
     * 
     *  Creates a net GetTenantIsCommand. This Command writes the id of the given
     *  tenant in the variable result.
     * 
     * @param role user which executes the command
     * @param tenantName
     */
    public GetTenantIdCommand(Role role, String tenantName) {
        super(role, null);
       this.tenantName=tenantName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        
        Query q = evt.getSession().createQuery("from Tenant where name=:tname");
        q.setString("tname", tenantName);
        List<Tenant> result = q.list();
        
        if (result.size()>1){
            throw new TransactionException("More than one Tenant found. But Tenant name schould be unique.");
        }
        else if (result.size()==0){
            throw new EntityNotFoundException(Tenant.class);
        }
        else{
            tenantId = result.get(0).getId();
        }
        
        

    }

    public Long getTenantId() {
        return tenantId;
    }

}
