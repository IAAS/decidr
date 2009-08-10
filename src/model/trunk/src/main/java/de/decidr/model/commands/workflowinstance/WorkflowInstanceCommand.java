package de.decidr.model.commands.workflowinstance;

import java.util.Collection;

import de.decidr.model.acl.access.WorkflowInstanceAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;

public abstract class WorkflowInstanceCommand extends AclEnabledCommand
        implements WorkflowInstanceAccess {

    private Long WorkflowInstanceId;

    public WorkflowInstanceCommand(Role role,
            Collection<Permission> permission, Long workflowInstanceId) {
        super(role, permission);
        this.WorkflowInstanceId = workflowInstanceId;

    }

    public Long[] getWorkflowInstanceIds() {
        Long[] result = { WorkflowInstanceId };
        return result;
    }
}
