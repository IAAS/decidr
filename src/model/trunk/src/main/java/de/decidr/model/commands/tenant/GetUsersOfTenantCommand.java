package de.decidr.model.commands.tenant;

import java.util.List;

import de.decidr.model.entities.User;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.PaginatingCriteria;
import de.decidr.model.filters.Paginator;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

public class GetUsersOfTenantCommand extends TenantCommand {

    private Paginator paginator;
    private List<User> result;
    
    public GetUsersOfTenantCommand(Role role, Long tenantId, Paginator paginator) {
        super(role, tenantId);
        
        this.paginator=paginator;
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        
        PaginatingCriteria c = new PaginatingCriteria(User.class, evt.getSession());
        
        paginator.apply(c);
        
        result = c.list();

    }

    public List<User> getResult() {
        return result;
    }

}
