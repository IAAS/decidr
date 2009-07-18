package de.decidr.model.commands.system;

import org.hibernate.Query;

import de.decidr.model.entities.Server;
import de.decidr.model.entities.ServerType;
import de.decidr.model.enums.ServerTypeEnum;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Adds a server to the DecidR database. The server will not really be created.
 * It's only a representation of the real server.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class AddServerCommand extends SystemCommand {

    private ServerTypeEnum type = null;
    private String location = null;
    private Byte initialLoad = null;
    private Boolean locked = null;
    private Boolean dynamicallyAdded = null;

    private Server newServer = null;

    /**
     * 
     * Creates a new AddServerCommand. The created command adds a server to the
     * DecidR database. The server will not really be created. It's only a
     * representation of the real server.
     * 
     * @param actor
     * @param type
     * @param location
     * @param initialLoad
     * @param locked
     * @param dynamicallyAdded
     */
    public AddServerCommand(Role actor, ServerTypeEnum type, String location,
            Byte initialLoad, Boolean locked, Boolean dynamicallyAdded) {
        super(actor, null);
        this.type = type;
        this.location = location;
        this.initialLoad = initialLoad;
        this.locked = locked;
        this.dynamicallyAdded = dynamicallyAdded;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        String hql = "from ServerType s where s.name = :serverType limit 1";
        Query q = evt.getSession().createQuery(hql).setString("serverType",
                type.toString());
        ServerType serverType = (ServerType) q.uniqueResult();

        if (serverType == null) {
            throw new EntityNotFoundException(ServerType.class);
        }

        Server newServer = new Server();
        newServer.setDynamicallyAdded(dynamicallyAdded);
        newServer.setLoad(initialLoad);
        newServer.setLocation(location);
        newServer.setLocked(locked);
        newServer.setServerType(serverType);

        evt.getSession().save(newServer);

        this.newServer = newServer;
    }

    /**
     * @return the new server
     */
    public Server getNewServer() {
        return newServer;
    }
}
