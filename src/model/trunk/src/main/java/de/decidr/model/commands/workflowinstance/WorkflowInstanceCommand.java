package de.decidr.model.commands.workflowinstance;

import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;

public abstract class WorkflowInstanceCommand extends AclEnabledCommand{

    public WorkflowInstanceCommand(Role role, Permission permission) {
        super(role, permission);
        // TODO Auto-generated constructor stub
    }
}