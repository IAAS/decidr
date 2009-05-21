package de.decidr.model.permissions;

import de.decidr.model.entities.WorkflowInstance;

/**
 * Represents the permission to a workflow instance.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class WorkflowInstancePermission extends EntityPermission {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     * 
     * @param workflowInstanceId
     */
    public WorkflowInstancePermission(Long workflowInstanceId) {
        super(WorkflowInstance.class.getCanonicalName(), workflowInstanceId);
    }
}