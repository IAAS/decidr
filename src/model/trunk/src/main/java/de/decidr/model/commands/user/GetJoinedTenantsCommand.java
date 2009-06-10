package de.decidr.model.commands.user;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.UserIsMemberOfTenant;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

public class GetJoinedTenantsCommand extends UserCommand {

    private Long userId;
    private List<Tenant> result;
    
    /**
     * 
     * Creates a new getJoinedTenantsCommand. This command writes all tenante the given user
     * is member of in the result variable.
     * 
     * @param role the user which executes the command
     * @param userId
     */
    public GetJoinedTenantsCommand(Role role, Long userId) {
        super(role, null);
        
        this.userId=userId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        
        List<UserIsMemberOfTenant> qList = new ArrayList();
        
        Query q = evt.getSession().createQuery("select UserIsMemberOfTenant where user.id=:id");
        qList= q.list();
        
        for(UserIsMemberOfTenant listItem: qList){
            result.add(listItem.getTenant());
        }
    }

    public List<Tenant> getResult() {
        // TODO Auto-generated method stub
        return result;
    }

}
