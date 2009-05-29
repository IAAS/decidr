package de.decidr.model.permissions;

/**
 * Checks whether a {@link UserRole} is logged in by looking at its actor id. The role is
 * considered to have logged in if its actor id is a valid user id.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class LoggedIn implements Asserter {

    @Override
    public Boolean assertRule(Role role, Permission permission) {
        Boolean result = false;
        if (role instanceof UserRole) {
            UserRole userRole = (UserRole) role;
            result = userRole.getActorId() >= UserRole.MIN_VALID_USER_ID;
        }
        return result;
    }

}