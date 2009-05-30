package de.decidr.model.commands.system;

import java.util.List;

import org.hibernate.Query;

import de.decidr.model.entities.SystemSettings;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

public class GetSystemSettingsCommand extends SystemCommand {

    private SystemSettings result;
    
    public GetSystemSettingsCommand(Role role, Permission permission) {
        super(role, permission);
        // TODO Auto-generated constructor stub
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt) {
       
        String qtext = "from SystemSettings";
        
        Query q = evt.getSession().createQuery(qtext);
        List<SystemSettings> results = q.list();
        
        result = results.get(0);
    }

    /**
     * @return the result
     */
    public SystemSettings getResult() {
        return result;
    }
    

}
