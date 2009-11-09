package de.decidr.test.database.factories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Session;

import de.decidr.model.entities.Server;
import de.decidr.model.entities.ServerType;
import de.decidr.model.enums.ServerTypeEnum;
import de.decidr.test.database.main.ProgressListener;

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
     * @param session
     * @param progressListener
     */
    public ServerFactory(Session session, ProgressListener progressListener) {
        super(session, progressListener);
    }

    /**
     * Creates one persisted server for each supported server type.
     * 
     * @return the list of generated servers
     */
    public List<Server> createRandomServers() {
        ArrayList<Server> result = new ArrayList<Server>();

        // Server types and locations
        Map<ServerTypeEnum, List<String>> servers = new HashMap<ServerTypeEnum, List<String>>();

        // ODE
        List<String> odeServers = new ArrayList<String>();
        odeServers.add("174.129.24.232:8080");
        servers.put(ServerTypeEnum.Ode, odeServers);

        // ESB
        List<String> esbServers = new ArrayList<String>();
        esbServers.add("174.129.25.57:8280");
        servers.put(ServerTypeEnum.Esb, esbServers);

        // Web Portal
        List<String> portalServers = new ArrayList<String>();
        portalServers.add("174.129.25.24:8080");
        servers.put(ServerTypeEnum.WebPortal, portalServers);

        // Storage
        List<String> storageServers = new ArrayList<String>();
        storageServers.add("localhost");
        servers.put(ServerTypeEnum.Storage, storageServers);

        // create at least one server for each server type
        int doneServers = 0;
        int totalSize = odeServers.size() + esbServers.size()
                + portalServers.size() + storageServers.size();
        for (Entry<ServerTypeEnum, List<String>> entry : servers.entrySet()) {
            for (String location : entry.getValue()) {
                doneServers++;

                ServerType serverType = (ServerType) session.createQuery(
                        "from ServerType s where s.name = :serverType")
                        .setString("serverType", entry.getKey().toString())
                        .setMaxResults(1).uniqueResult();

                // create the server type if necessary
                if (serverType == null) {
                    serverType = new ServerType(entry.getKey().toString());
                    session.save(serverType);
                }

                Server server = new Server();
                server.setDynamicallyAdded(false);
                server.setLastLoadUpdate(getRandomDate(false, true, SPAN_WEEK));
                server.setLoad((byte) rnd.nextInt(100));
                server.setLocation(location);
                server.setLocked(false);
                server.setServerType(serverType);

                session.save(server);

                fireProgressEvent(totalSize, doneServers);
            }
        }
        return result;
    }
}