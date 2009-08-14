package de.decidr.test.database.factories;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import de.decidr.model.entities.Server;
import de.decidr.model.entities.ServerType;
import de.decidr.model.enums.ServerTypeEnum;

/**
 * Creates randomized servers for testing purposes.
 * 
 * @author Thomas Karsten
 * @author Daniel Huss
 * @version 0.1
 */
public class ServerFactory extends EntityFactory {

    /**
     * Constructor
     */
    public ServerFactory(Session session) {
        super(session);
    }

    /**
     * Creates one persisted server for each supported server type.
     * 
     * @return the list of generated servers
     */
    public List<Server> createRandomServers() {
        ArrayList<Server> result = new ArrayList<Server>();

        // create one server for each server type
        for (ServerTypeEnum type : ServerTypeEnum.values()) {
            Server server = new Server();
            server.setDynamicallyAdded(false);
            server.setLoad((byte) 50);
            server.setLocation("http://localhost/server/" + type.toString());
            server.setLocked(false);
            server.setServerType(new ServerType(type.toString()));
            session.save(server);
            result.add(server);
        }

        return result;
    }
}