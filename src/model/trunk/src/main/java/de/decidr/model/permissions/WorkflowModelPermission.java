package de.decidr.model.permissions;

import de.decidr.model.entities.WorkflowModel;

public class WorkflowModelPermission extends
		de.decidr.model.permissions.Permission {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Long workflowModelId;

	public WorkflowModelPermission(Long workflowModelId) {
		super(WorkflowModel.class.getCanonicalName() + "."
				+ workflowModelId.toString());
		this.workflowModelId = workflowModelId;
	}
}