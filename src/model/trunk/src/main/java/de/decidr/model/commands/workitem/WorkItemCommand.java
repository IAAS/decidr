package de.decidr.model.commands.workitem;

import org.hibernate.Session;

import de.decidr.model.acl.access.WorkItemAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.WorkItem;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;

/**
 * Abstract base class for commands that modify work items.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public abstract class WorkItemCommand extends AclEnabledCommand implements
        WorkItemAccess {

    protected Long workItemId = null;

    /**
     * Creates a new {@link WorkItemCommand}
     * 
     * @param role
     *            user / system executing the command
     * @param workItemId
     */
    public WorkItemCommand(Role role, Long workItemId) {
        super(role, (Permission) null);
        this.workItemId = workItemId;
    }

    /**
     * @return the workItemId
     */
    public Long getWorkItemId() {
        return workItemId;
    }

    /**
     * @return the workItemIds
     */
    public Long[] getWorkItemIds() {
        Long[] result = { workItemId };
        return result;
    }

    /**
     * Fetches the work item identified by this.workItemId from the database.
     * 
     * @param session
     *            current Hibernate session
     * @return the work item identified by this.workItemId
     * @throws EntityNotFoundException
     *             if the work item does not exist
     */
    public WorkItem fetchWorkItem(Session session) throws TransactionException {
        WorkItem result = (WorkItem) session.get(WorkItem.class, workItemId);
        if (result == null) {
            throw new EntityNotFoundException(WorkItem.class, workItemId);
        }
        return result;
    }
}