package de.decidr.model.facades;

import de.decidr.model.permissions.Role;

/**
 * 
 * This facade is used as father of all facades. It guarantees, that all facades
 * have a constructor with role parameter. This parameters says under which role
 * the facade executes the commands.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public abstract class AbstractFacade {

    protected Role actor;

    public AbstractFacade(Role actor) {
        this.actor = actor;
    }
}