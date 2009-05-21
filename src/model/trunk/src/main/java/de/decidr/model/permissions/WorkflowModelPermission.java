package de.decidr.model.permissions;

import de.decidr.model.entities.WorkflowModel;

/**
 * Represents the permission to a workflow model
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class WorkflowModelPermission extends EntityPermission {
    
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param workflowModelId
     */
    public WorkflowModelPermission(Long workflowModelId) {
        super(WorkflowModel.class.getCanonicalName(), workflowModelId);
    }
}