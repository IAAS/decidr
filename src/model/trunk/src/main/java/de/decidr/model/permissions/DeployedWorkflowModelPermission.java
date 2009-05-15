package de.decidr.model.permissions;

import de.decidr.model.entities.DeployedWorkflowModel;

public class DeployedWorkflowModelPermission extends
		de.decidr.model.permissions.Permission {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Long deployedWorkflowModelId;

	public DeployedWorkflowModelPermission(Long deployedWorkflowModelId) {
		super(DeployedWorkflowModel.class.getCanonicalName() +  "." + deployedWorkflowModelId.toString());
	}
}