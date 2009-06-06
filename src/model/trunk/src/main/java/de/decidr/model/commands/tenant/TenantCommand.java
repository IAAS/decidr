package de.decidr.model.commands.tenant;

import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.TenantPermission;

public abstract class TenantCommand extends AclEnabledCommand{

    private Long tenantId;
    
    public TenantCommand(Role role, Long tenantId) {
        super(role, new TenantPermission(tenantId));
        this.tenantId=tenantId;
        }

    public Long getTenantId() {
        return tenantId;
    }
}