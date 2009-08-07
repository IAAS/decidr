package de.decidr.model.acl.asserters;

import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.permissions.UserProfilePermission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;

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
