package de.decidr.model.commands.system;

import org.hibernate.Query;

import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Sets the server of the given server to locked.
 * If Server does not exist, nothing will happen
 * 
 * @author Markus Fischer
 *
 * @version 0.1
 */
public class LockServerCommand extends SystemCommand {

    private Boolean lock = true;
    private String location = null;
    
    /**
     * Creates a new LockServerCommand. The command locks the given server.
     * If Server does not exist, nothing will happen.
     * 
     * @param role user, who wants to execute the command
     * @param location location of the server which should be locked
     */
    public LockServerCommand(Role role, String location) {
        super(role, null);
        this.location = location;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt) {
        
        Query q = evt.getSession().createQuery("update Server set locked =  :newLock where location= :loc");
        q.setString("newLock", String.valueOf(lock));
        q.setString("loc", location);
        q.executeUpdate();

    }

}
