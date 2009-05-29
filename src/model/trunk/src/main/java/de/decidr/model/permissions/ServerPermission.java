package de.decidr.model.permissions;

import de.decidr.model.entities.Server;

/**
 *  Represents permissions that refer to server.
 * 
 * @author Daniel Huss
 *
 * @version 0.1
 */
public class ServerPermission extends EntityPermission {

    private static final long serialVersionUID = 1L;

    public ServerPermission( Long id) {
        super(Server.class.getCanonicalName(), id);
    }

}
