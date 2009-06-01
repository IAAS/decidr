package de.decidr.model.commands.system;

import java.util.List;

import org.hibernate.Query;

import de.decidr.model.entities.SystemSettings;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Writes the System Settings in the variable result.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class GetSystemSettingsCommand extends SystemCommand {

    private SystemSettings result;
    
    /**
     * 
     * @param role the user who wants to execute the command
     */
    public GetSystemSettingsCommand(Role role) {
        super(role, null);
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
