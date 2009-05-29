package de.decidr.model.permissions.asserters;

import de.decidr.model.permissions.Asserter;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.UserProfilePermission;
import de.decidr.model.permissions.UserRole;

/**
 * Asserts that the given user is the owner of the given profile.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class UserOwnsProfile implements Asserter {

    @Override
    public Boolean assertRule(Role role, Permission permission) {
        Boolean result = false;

        if ((permission instanceof UserProfilePermission)
                && (role instanceof UserRole)) {
            result = ((UserProfilePermission) permission).getId().equals(
                    ((UserRole) role).getActorId());
        }

        return result;
    }
}
