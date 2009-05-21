package de.decidr.model.permissions;

import de.decidr.model.entities.WorkItem;

/**
 * Represents the permission to a work item.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 *
 * @version 0.1
 */
public class WorkItemPermission extends EntityPermission {
    
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param workItemId
	 */
	public WorkItemPermission(Long workItemId) {
		super(WorkItem.class.getCanonicalName(), workItemId);
	}
}