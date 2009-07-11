package de.decidr.model.commands.system;

import de.decidr.model.entities.Server;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * XXX: clarify that these are ODE servers? if not, how do I add ODE servers?
 * Adds a server to the DecidR database. The Server will not really be created.
 * It's only a representation of the real Server.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class AddServerCommand extends SystemCommand {

    String location = null;

    /**
     * 
     * Creates a new AddServerCommand. The created command adds a server to the
     * DecidR database. The Server will not really be created. It's only a
     * representation of the real Server.
     * 
     * @param role
     *            the user who wants to execute the command
     * @param location
     *            the location of the server
     */
    public AddServerCommand(Role role, String location) {
        super(role, null);
        this.location = location;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt) {
        Server server = new Server();
        server.setLocation(location);
        server.setDynamicallyAdded(true);
        server.setLoad((byte) 0);
        server.setLocked(false);
        evt.getSession().save(server);
    }
}
