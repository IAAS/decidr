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
    private Long actorId;

    /**
     * Constructor.
     * 
     * @param actorId
     *            Id of the person / system / pricipal that is requesting access
     *            to a permission.
     */
    public AbstractRole(Long actorId) {
        super();
        this.actorId = actorId;
    }

    @Override
    public Long getActorId() {
        return this.actorId;
    }

    @Override
    public boolean equals(Object obj) {
        return ((obj != null) && (obj.getClass().equals(this.getClass())));
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

    @Override
    public String toString() {
        return this.getClass().getName().toString();
    }

}
