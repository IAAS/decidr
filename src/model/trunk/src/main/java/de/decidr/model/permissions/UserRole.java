package de.decidr.model.permissions;

/**
 * Represents a user that is requesting access to a permission.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class UserRole implements Role {
    /**
     * The user's id.
     */
    protected Long userId;

    /**
     * Constructor.
     * 
     * @param userId
     */
    public UserRole(Long userId) {
        this.userId = userId;
    }

    /**
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }
}