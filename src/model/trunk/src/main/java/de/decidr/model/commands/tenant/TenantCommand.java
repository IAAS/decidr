package de.decidr.model.commands.tenant;

import de.decidr.model.acl.access.TenantAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;

/**
 * The Abstract Tenant Command.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public abstract class TenantCommand extends AclEnabledCommand implements TenantAccess{

    private Long tenantId;

    public TenantCommand(Role role, Long tenantId) {
        super(role, (Permission)null);
        this.tenantId = tenantId;
    }

    public Long getTenantId() {
        return tenantId;
    }
    
    public Long[] getTenantIds() {
        Long[] result={tenantId};
        return result;
    }
    
}