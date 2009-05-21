package de.decidr.model.permissions;

import de.decidr.model.entities.DeployedWorkflowModel;

/**
 * Represents the permission to read from or write to a deployed workflow model.
 * The deployed workflow model is internally represented by its id.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class DeployedWorkflowModelPermission extends
        de.decidr.model.permissions.Permission {

    private static final long serialVersionUID = 1L;

    /**
     * The id of the deployed workflow model that this permission represents or
     * null if this permission represents all deployed workflow models.
     */
    protected Long deployedWorkflowModelId;

    /**
     * Constructor.
     * 
     * @param deployedWorkflowModelId
     *            if this parameter is not null, the permission represents only
     *            this particular deployed workflow model, otherwise it
     *            represents all deployed workflow model.
     */
    public DeployedWorkflowModelPermission(Long deployedWorkflowModelId) {
        super(DeployedWorkflowModel.class.getCanonicalName() + '.'
                + deployedWorkflowModelId == null ? "*"
                : deployedWorkflowModelId.toString());
        
        this.deployedWorkflowModelId = deployedWorkflowModelId;
    }

    /**
     * @return the deployedWorkflowModelId
     */
    public Long getDeployedWorkflowModelId() {
        return deployedWorkflowModelId;
    }
}