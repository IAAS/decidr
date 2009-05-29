package de.decidr.model.permissions;

/**
 * Represents the super admin.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class SuperAdminRole extends TenantAdminRole {

    /**
     * Constructor.
     * 
     * @param userId
     */
    public SuperAdminRole(Long userId) {
        super(userId);
    }

    /**
     * Creates a new SuperAdminRole with its actor id set to unknown;
     * 
     */
    public SuperAdminRole() {
        this(UNKNOWN_USER_ID);
    }
}