package de.decidr.model.commands.system;

import org.hibernate.Query;

import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Updates the server load of a given server at the database.
 * 
 * @author Markus Fischer
 *
 * @version 0.1
 */
public class UpdateServerLoadCommand extends SystemCommand{

    private String location = null;
    private byte load;
    
    /**
     * Updates the server load of a given server at the database.
     * If given server does not exists nothing will happen.
     * 
     * @param role the user who wants to execute the command
     * @param location the location of the server, which should be updated
     * @param load the new load
     */
    //FIXME servers should be identified through their ID - location might be the same
    public UpdateServerLoadCommand(Role role, String location, byte load) {
        super(role, null);
        this.location = location;
        this.load = load;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt) {
        
        Query q = evt.getSession().createQuery("update Server set load =  :newLoad where location= :loc");
        q.setString("newLoad", String.valueOf(load));
        q.setString("loc", location);
        q.executeUpdate();
        
    }

}
