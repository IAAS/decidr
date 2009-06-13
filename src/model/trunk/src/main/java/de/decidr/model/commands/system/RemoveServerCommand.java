package de.decidr.model.commands.system;

import org.hibernate.Query;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Removes the server from the database. The corresponding real Server
 * will not be closed. If the server doesn't exist the command will be ignored.
 * 
 * @author Markus Fischer
 *
 * @version 0.1
 */
public class RemoveServerCommand extends SystemCommand {

    private String location = null;
    
    /**
     * 
     * Creates a new RemoveServerCommand. The command
     * removes the server from the database. The corresponding real Server
     * will not be closed. If the server doesn't exist the command will be ignored.
     * 
     * @param role the user who wants to execute the command
     * @param location the location of the server which should be unlocked
     */
    public RemoveServerCommand(Role role, String location) {
        super(role, null);
        this.location=location;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt) {
                 
        Query q = evt.getSession().createQuery("delete Server where location = :loc");
        q.setString("loc", location);
        q.executeUpdate();
        
    }

}
