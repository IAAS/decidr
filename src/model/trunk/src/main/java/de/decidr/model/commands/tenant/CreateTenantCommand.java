package de.decidr.model.commands.tenant;

import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

public class CreateTenantCommand extends TenantCommand {

    private String name;
    private String description;
    private Long amdinId;
    private Long tenantId;
    
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
        
        this.amdinId=adminId;
        this.name=name;
        this.description=description;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt){
    
    User admin = (User)evt.getSession().load(User.class, amdinId);
    
    Tenant tenant = new Tenant();
    tenant.setName(name);
    tenant.setDescription(description);
    tenant.setAdmin(admin);
    
    evt.getSession().save(tenant);
    
    tenantId = tenant.getId();

    }

    /**
     * 
     * @return id of the new tenant
     */
    public Long getTenantId() {
        return tenantId;
    }

}
