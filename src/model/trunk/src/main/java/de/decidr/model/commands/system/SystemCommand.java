package de.decidr.model.commands.system;

import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;

public abstract class SystemCommand extends AclEnabledCommand {

    public SystemCommand(Role role, Permission permission) {
        super(role, permission);
        // TODO Auto-generated constructor stub
    }
}