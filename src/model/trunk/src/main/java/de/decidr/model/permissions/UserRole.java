package de.decidr.model.permissions;

/**
 * Represents a user that is requesting access to a permission.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class UserRole extends AbstractRole {

    /**
     * Constructor.
     * 
     * @param userId
     */
    public UserRole(Long userId) {
        super(userId);
    }
}