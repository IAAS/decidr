package de.decidr.model.commands.workflowinstance;

import java.util.Collection;

import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;

public abstract class WorkflowInstanceCommand extends AclEnabledCommand{

    public WorkflowInstanceCommand(Role role, Collection<Permission> permission) {
        super(role, permission);
        // nothing to do
    }
}