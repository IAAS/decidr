package de.decidr.test.database.factories;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.ServerType;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.enums.ServerTypeEnum;

/**
 * Creates randomized servers for testing purposes. Requires system settings to
 * be present.
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

        SystemSettings settings = DecidrGlobals.getSettings();

        // create at least one server for each server type
        for (ServerTypeEnum type : ServerTypeEnum.values()) {
            int numServers;
            if (type.equals(ServerTypeEnum.Esb)) {
                numServers = 1;
            } else {
                numServers = rnd.nextInt(16) + 1;
            }

            ServerType serverType = (ServerType) session.createQuery(
                    "from ServerType s where s.name = :serverType").setString(
                    "serverType", type.toString()).setMaxResults(1)
                    .uniqueResult();

            // create the server type if necessary
            if (serverType == null) {
                serverType = new ServerType(type.toString());
                session.save(serverType);
            }

            for (int i = 0; i < numServers; i++) {
                Server server = new Server();
                server.setDynamicallyAdded(i + 1 > settings
                        .getServerPoolInstances());
                server.setLoad((byte) rnd.nextInt(101));
                server.setLocation("http://localhost/server/" + type.toString()
                        + "/" + Integer.toString(i));
                // at least one server remains unlocked
                server.setLocked(i == 0 ? false : server.isDynamicallyAdded()
                        && rnd.nextBoolean());
                server.setServerType(serverType);
                session.save(server);
                result.add(server);
            }
        }

        return result;
    }
}