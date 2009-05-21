package de.decidr.model.permissions;

/**
 * Represents a workflow admin.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class WorkflowAdminRole extends UserRole {

    /**
     * Constructor.
     * 
     * @param userId
     */
    public WorkflowAdminRole(Long userId) {
        super(userId);
    }
}