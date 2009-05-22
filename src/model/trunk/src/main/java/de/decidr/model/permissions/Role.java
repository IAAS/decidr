package de.decidr.model.permissions;

/**
 * Represents a person, system or principal that requests access to a particular
 * permission and is identified by a numeric id.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public interface Role {

    /**
     * @return the id of the person / system / principal that is requesting
     *         access to a permission.
     */
    public Long getActorId();

}