package de.decidr.model.commands.system;

import org.hibernate.Query;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

public class RemoveServerCommand extends SystemCommand {

    private String location = null;
    
    public RemoveServerCommand(Role role, String location) {
        super(role, null);
        this.location=location;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt) {
                 
        Query q = evt.getSession().createQuery("delete Server where location= :loc");
        q.setString("loc", location);
        q.executeUpdate();
        
    }

}
