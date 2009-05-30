package de.decidr.model.permissions.asserters;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.UserIsMemberOfTenantId;
import de.decidr.model.permissions.Asserter;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.TenantPermission;
import de.decidr.model.permissions.UserRole;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionAbortedEvent;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Asserts that the given user is the admin of the given tenant.
 * 
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class IsTenantAdmin implements Asserter, TransactionalCommand {

    private Long userId = null;

    private Long tenantId = null;

    private Boolean userIsAdmin = false;

    @Override
    public Boolean assertRule(Role role, Permission permission) {
        Boolean result = null;

        if ((role instanceof UserRole)
                && (permission instanceof TenantPermission)) {
            userId = role.getActorId();
            tenantId = ((TenantPermission) permission).getId();

            HibernateTransactionCoordinator.getInstance().runTransaction(this);

            result = userIsAdmin;
        }

        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt) {
        userIsAdmin = false;

        userIsAdmin = ((Tenant) evt.getSession().get(Tenant.class, tenantId))
                .getAdmin().getId().equals(userId);
    }

    @Override
    public void transactionAborted(TransactionAbortedEvent evt) {
    }

    @Override
    public void transactionCommitted(TransactionEvent evt) {
    }
}