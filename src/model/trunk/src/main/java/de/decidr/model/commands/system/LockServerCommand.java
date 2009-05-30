package de.decidr.model.commands.system;

import org.hibernate.Query;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

public class LockServerCommand extends SystemCommand {

    private Boolean lock = true;
    private String location = null;
    
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
