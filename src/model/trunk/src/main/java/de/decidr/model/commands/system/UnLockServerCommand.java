package de.decidr.model.commands.system;

import org.hibernate.Query;

import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Sets the server of the given server to unlocked.
 * 
 * @author Markus Fischer
 *
 * @version 0.1
 */
public class UnLockServerCommand extends SystemCommand {

    private Boolean lock = false;
    private String location = null;
    
    /**
     * Unlocks the given server.
     * 
     * @param role user, who wants to execute the command
     * @param location location of the server which should be locked
     */
    public UnLockServerCommand(Role role, String location) {
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
