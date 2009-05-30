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

	String WorkItemId = null;
		
	/**
	 * Constructor.
	 * 
	 * @param workItemId
	 */
	public WorkItemPermission(Long workItemId) {
		super(WorkItem.class.getCanonicalName(), workItemId);
	}

    /**
     * @return the workItemId
     */
    public String getWorkItemId() {
        return WorkItemId;
    }

    /**
     * @param workItemId the workItemId to set
     */
    public void setWorkItemId(String workItemId) {
        WorkItemId = workItemId;
    }
}