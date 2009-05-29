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

    /**
     * Creates a new TenantAdminRole with its actor id set to unknown;
     * 
     */
    public TenantAdminRole() {
        this(UNKNOWN_USER_ID);
    }
}