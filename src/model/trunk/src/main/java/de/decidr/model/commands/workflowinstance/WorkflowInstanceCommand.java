package de.decidr.model.commands.workflowinstance;

import java.util.Collection;

import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;

public abstract class WorkflowInstanceCommand extends AclEnabledCommand{

    public WorkflowInstanceCommand(Role role, Collection<Permission> permission) {
        super(role, permission);
        // nothing to do
    }
}