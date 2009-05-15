package de.decidr.model.permissions;

import de.decidr.model.entities.WorkflowInstance;

public class WorkflowInstancePermission extends Permission {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Long workflowInstanceId;

	public WorkflowInstancePermission(Long workflowInstanceId) {
		super(WorkflowInstance.class.getCanonicalName() + "."
				+ workflowInstanceId.toString());
		this.workflowInstanceId = workflowInstanceId;
	}
}