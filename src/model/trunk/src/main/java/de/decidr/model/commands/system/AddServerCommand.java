package de.decidr.model.commands.system;

import de.decidr.model.entities.Server;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Adds a server to the DecidR database. The Server will not be
 * created in reality. It's just an representation of the real Server.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class AddServerCommand extends SystemCommand {

    String location = null;
   
    /**
    * 
    * Creates a new AddServerCommand. The created command
    * adds a server to the DecidR database. The Server will not be
    * created in reality. It's just an representation of the real Server.
    * 
    * @param role the user which want's to execute the command
    * @param location the location of the server
    */
    public AddServerCommand(Role role, String location) {
        super(role, null);
        this.location=location;
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
