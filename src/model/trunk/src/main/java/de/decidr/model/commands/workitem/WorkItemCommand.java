package de.decidr.model.commands.workitem;

import java.util.Collection;

import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;

public abstract class WorkItemCommand extends AclEnabledCommand{

    public WorkItemCommand(Role role, Collection<Permission> permission) {
        super(role, permission);
        // TODO Auto-generated constructor stub
    }
}