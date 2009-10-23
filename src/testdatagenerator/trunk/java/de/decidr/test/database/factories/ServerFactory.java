package de.decidr.test.database.factories;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.ServerType;
import de.decidr.model.entities.SystemSettings;
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

        SystemSettings settings = DecidrGlobals.getSettings();

        int numServerTypes = ServerTypeEnum.values().length;
        int doneServerTypes = 0;

        // create at least one server for each server type
        for (ServerTypeEnum type : ServerTypeEnum.values()) {
            doneServerTypes++;

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

            Set<Integer> randomIps = new HashSet<Integer>();
            Integer randomIp;
            for (int i = 0; i < numServers; i++) {
                do {
                    randomIp = rnd.nextInt();
                } while (!isValidIp(randomIp) || randomIps.contains(randomIp));

                randomIps.add(randomIp);

                Integer randomPort = rnd.nextInt(65535) + 1;

                Server server = new Server();
                server.setDynamicallyAdded(i + 1 > settings
                        .getServerPoolInstances());
                server.setLoad((byte) rnd.nextInt(101));
                server.setLastLoadUpdate(rnd.nextBoolean() ? null
                        : getRandomDate(false, true, 30000));
                server.setLocation(intToIp(randomIp) + ":" + randomPort);
                // at least one server remains unlocked
                server.setLocked(i == 0 ? false : server.isDynamicallyAdded()
                        && rnd.nextBoolean());
                server.setServerType(serverType);
                session.save(server);
                result.add(server);
            }

            fireProgressEvent(numServerTypes, doneServerTypes);
        }

        return result;
    }

    /**
     * Converts an integer to an IPv4 string.
     * 
     * @param ip
     *            address to convert
     * @return dot-separated string representation of the ip address.
     */
    private String intToIp(int ip) {
        return ((ip >> 24) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 8) & 0xFF) + "." + (ip & 0xFF);
    }

    /**
     * Performs pseudo validation on the given ipv4 address.
     * 
     * @param ip
     *            address to validate
     * @return true if the given ip contains no 0 or 255 parts.
     */
    private Boolean isValidIp(int ip) {
        int[] parts = { (ip >> 24) & 0xFF, (ip >> 16) & 0xFF, (ip >> 8) & 0xFF,
                ip & 0xFF };

        for (int part : parts) {
            if (part == 0 || part == 255) {
                return false;
            }
        }

        return true;
    }
}