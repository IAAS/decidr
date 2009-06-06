package de.decidr.model.commands.tenant;

import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionCoordinator;
import de.decidr.model.transactions.TransactionEvent;

public class CreateTenantCommand extends TenantCommand {

    private String name;
    private String description;
    private Long adminId;
    private Long tenantId;
    private Role actor;
    
    /**
     * Creates a new CreateTenantCommand. The tenant which will be created in this
     * command will have the given properties.
     * 
     * @param role user which executes the command
     * @param name tenant name
     * @param description tenant description
     * @param adminId id of the tenant admin
     */
    public CreateTenantCommand(Role role,String name, String description, Long adminId) {
        super(role, null);
        
        this.adminId=adminId;
        this.name=name;
        this.description=description;
        this.actor = role;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt) throws TransactionException{
    
        User admin = (User)evt.getSession().get(User.class, adminId);
        
        if(admin==null){
          throw new EntityNotFoundException(User.class, adminId);  
        }
        
        TransactionCoordinator tac = HibernateTransactionCoordinator
        .getInstance();
    
        GetTenantIdCommand command = new GetTenantIdCommand(actor,name);
        
        try {
            tac.runTransaction(command);
        } catch (EntityNotFoundException e) {
            
            Tenant tenant = new Tenant();
            tenant.setName(name);
            tenant.setDescription(description);
            tenant.setAdmin(admin);
            tenant.setApprovedSince(null);
               
            evt.getSession().save(tenant);
            
            tenantId = tenant.getId();
            return;
            
        } catch (TransactionException e){
            throw e;
        }
        
        throw new TransactionException("Tenant name already exists.");
                  
      

    }

    /**
     * 
     * @return id of the new tenant
     */
    public Long getTenantId() {
        return tenantId;
    }

}
