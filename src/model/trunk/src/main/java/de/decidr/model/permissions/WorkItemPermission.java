package de.decidr.model.permissions;

import de.decidr.model.entities.WorkItem;

public class WorkItemPermission extends de.decidr.model.permissions.Permission {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Long workItemId;
	

	public WorkItemPermission(Long workItemId) {
		super(WorkItem.class.getCanonicalName() + "." + workItemId.toString());
		this.workItemId = workItemId;
	}
}