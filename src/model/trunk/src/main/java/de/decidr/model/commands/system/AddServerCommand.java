package de.decidr.model.commands.system;

import de.decidr.model.entities.Server;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

public class AddServerCommand extends SystemCommand {

    String location = null;
   
    public AddServerCommand(Role role, String location) {
        super(role, null);
        this.location=location;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt) {
        
        Server server = new Server();
        server.setLocation(location);
        server.setDynamicallyAdded(true);
        server.setLocked(false);
        
        evt.getSession().save(server);
        
    }

}
