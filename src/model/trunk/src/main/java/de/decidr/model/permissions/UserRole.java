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
     * The lowest user id that can belong to a logged in user.
     */
    public static final Long MIN_VALID_USER_ID = 0L;

    public static final Long UNKNOWN_USER_ID = -1L;

    /**
     * Constructor.
     * 
     * @param userId
     */
    public UserRole(Long userId) {
        super(userId);
    }

    /**
     * Creates a new UserRole with its actor id set to unknown;
     * 
     */
    public UserRole() {
        this(UNKNOWN_USER_ID);
    }
}