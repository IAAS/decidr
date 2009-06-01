package de.decidr.model.commands.workitem;

import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.WorkItemPermission;

/**
 * Abstract base class for all commands that deal with work items.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public abstract class WorkItemCommand extends AclEnabledCommand {

    protected Long workItemId = null;

    public WorkItemCommand(Role role, Long workItemId) {
        super(role, new WorkItemPermission(workItemId));
        this.workItemId = workItemId;
    }

    /**
     * @return the workItemId
     */
    public Long getWorkItemId() {
        return workItemId;
    }

}