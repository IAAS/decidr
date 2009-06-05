package de.decidr.model.commands.workflowmodel;

import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.WorkflowModelPermission;

/**
 * 
 * Base class for commands that modify workflow models.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public abstract class WorkflowModelCommand extends AclEnabledCommand {

    protected Long workflowModelId = null;

    public WorkflowModelCommand(Role role, Long workflowModelId) {
        super(role, new WorkflowModelPermission(workflowModelId));
        this.workflowModelId = workflowModelId;
    }

    /**
     * @return the workflowModelId
     */
    public Long getWorkflowModelId() {
        return workflowModelId;
    }
}