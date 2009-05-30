package de.decidr.model.commands.system;

import java.util.List;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

public class GetServerStatisticsCommand extends SystemCommand {

    List<ServerLoadView> result;
    
    public GetServerStatisticsCommand(Role role) {
        super(role, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt) {
       
        result = evt.getSession().createQuery("from ServerLoadView").list();
        
    }

    /**
     * @return the result
     */
    public List<ServerLoadView> getResult() {
        return result;
    }

}
