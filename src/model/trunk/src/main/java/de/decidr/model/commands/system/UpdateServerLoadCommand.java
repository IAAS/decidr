package de.decidr.model.commands.system;

import org.hibernate.Query;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

public class UpdateServerLoadCommand extends SystemCommand{

    private String location = null;
    private byte load;
    
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
