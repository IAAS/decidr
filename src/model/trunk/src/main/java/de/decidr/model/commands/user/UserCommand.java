package de.decidr.model.commands.user;

import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;

public abstract class UserCommand extends AclEnabledCommand{

    public UserCommand(Role role, Permission permission) {
        super(role, permission);
        // TODO Auto-generated constructor stub
    }
}