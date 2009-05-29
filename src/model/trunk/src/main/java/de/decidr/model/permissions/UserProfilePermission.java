package de.decidr.model.permissions;

import de.decidr.model.entities.UserProfile;

/**
 * Represents permissions that refer to a user profile.
 * 
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class UserProfilePermission extends EntityPermission {

    private static final long serialVersionUID = 1L;

    public UserProfilePermission(Long id) {
        super(UserProfile.class.getCanonicalName(), id);
    }

}
