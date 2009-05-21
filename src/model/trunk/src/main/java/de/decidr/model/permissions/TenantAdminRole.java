package de.decidr.model.permissions;

/**
 * Represents a tenant admin.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class TenantAdminRole extends WorkflowAdminRole {

    /**
     * Constructor.
     * 
     * @param userId
     */
    public TenantAdminRole(Long userId) {
        super(userId);
    }
}