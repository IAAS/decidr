package de.decidr.model.commands.system;

import java.util.Collection;

import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;

/**
 * 
 * The abstract system command. All system commands are derived from this class
 *  
 * @author Markus Fischer
 *
 * @version 0.1
 */
public abstract class SystemCommand extends AclEnabledCommand {

    public SystemCommand(Role role, Collection<Permission> permission) {
        super(role, permission);
    }
}