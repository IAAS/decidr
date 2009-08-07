package de.decidr.model.acl.asserters;

import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.permissions.TenantPermission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.UserIsMemberOfTenantId;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionAbortedEvent;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Asserts that the given user is a member of any kind of the given tenant.
 * 
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class IsTenantMember implements Asserter, TransactionalCommand {

    private UserIsMemberOfTenantId id = null;

    private Boolean userIsMember = false;

    @Override
    public Boolean assertRule(Role role, Permission permission)
            throws TransactionException {
        Boolean result = null;

        if ((role instanceof UserRole)
                && (permission instanceof TenantPermission)) {
            id = new UserIsMemberOfTenantId(role.getActorId(),
                    ((TenantPermission) permission).getId());

            HibernateTransactionCoordinator.getInstance().runTransaction(this);

            result = userIsMember;
        }

        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt) {
        Tenant tenant = (Tenant) evt.getSession().get(Tenant.class,
                id.getTenantId());

        userIsMember = (tenant.getAdmin().getId().equals(id.getUserId()))
                || (evt.getSession().get(UserIsMemberOfTenantId.class, id) != null);
    }

    @Override
    public void transactionAborted(TransactionAbortedEvent evt) {
        // nothing to do
    }

    @Override
    public void transactionCommitted(TransactionEvent evt) {
        // nothing to do
    }
}
