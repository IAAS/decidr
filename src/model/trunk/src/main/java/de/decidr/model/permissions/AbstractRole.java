package de.decidr.model.permissions;

/**
 * Base class for roles that are identified by an id.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public abstract class AbstractRole implements Role {

    /**
     * Id of the person / system / pricipal that is requesting access to a
     * permission.
     */
    protected Long actorId;

    /**
     * Constructor.
     * 
     * @param actorId Id of the person / system / pricipal that is requesting access to a
     * permission.
     */
    public AbstractRole(Long actorId) {
        super();
        this.actorId = actorId;
    }

    @Override
    public Long getActorId() {
        return this.actorId;
    }

}
