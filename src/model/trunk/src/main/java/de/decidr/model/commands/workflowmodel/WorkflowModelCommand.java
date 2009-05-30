package de.decidr.model.commands.workflowmodel;

import java.util.Collection;

import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;

public abstract class WorkflowModelCommand extends AclEnabledCommand{

    public WorkflowModelCommand(Role role, Collection<Permission> permission) {
        super(role, permission);
        // TODO Auto-generated constructor stub
    }
}