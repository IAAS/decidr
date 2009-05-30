package de.decidr.model.commands.tenant;

import java.util.Collection;

import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;

public abstract class TenantCommand extends AclEnabledCommand{

    public TenantCommand(Role role, Collection<Permission> permission) {
        super(role, permission);
        // TODO Auto-generated constructor stub
    }
}