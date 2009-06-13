package de.decidr.model.commands.system;

import java.util.List;

import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Writes a list of the existing unlocked servers in the result variable.
 * 
 * 
 * @author Markus Fischer
 * 
 *
 * @version 0.1
 */
public class GetServerStatisticsCommand extends SystemCommand {

    List<ServerLoadView> result;
    
    /**
     * Creates a new GetServerStatisticsCommand. The command saves all
     * unlocked servers in the result variable.
     * 
     * @param role the user who wants to execute the command
     */
    public GetServerStatisticsCommand(Role role) {
        super(role, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt) {
       
        result = evt.getSession().createQuery("from ServerLoadView where locked=false").list();
        
    }

    /**
     * @return the result
     */
    public List<ServerLoadView> getResult() {
        return result;
    }

}
