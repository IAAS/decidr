package de.decidr.model.commands.tenant;

import org.hibernate.Query;

import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Delete the isMemberOf relation of the given objects.
 * 
 * @author Markus Fischer
 *
 * @version 0.1
 */
public class RemoveTenentMemberCommand extends TenantCommand {

    private Long tenantId;
    private Long userId;
    
/**
 * 
 * Creates a new RemoveTenantCommand. The command will delete the isMemberOf relation of the given objects.
 * 
 * @param role  user which executes the command
 * @param tenantId
 * @param userId
 */
    public RemoveTenentMemberCommand(Role role,
            Long tenantId, Long userId) {
        super(role, null);
        this.tenantId=tenantId;
        this.userId=userId;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
      
        Query q = evt.getSession().createQuery("delete UserIsMemberOfTenant where tenant.id=:tid and user.id=:uid");
        q.setLong("tid", tenantId);
        q.setLong("uid", userId);
        q.executeUpdate();
      
      

    }

}
