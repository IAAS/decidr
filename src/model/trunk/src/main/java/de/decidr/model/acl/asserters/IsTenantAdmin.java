package de.decidr.model.acl.asserters;

import de.decidr.model.acl.access.TenantAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Asserts that the given user is the admin of the tenant(s) that are being
 * accessed by a given command.
 * 
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class IsTenantAdmin extends CommandAsserter {

    private Long userId = null;
    private Long[] tenantIds = null;
    private Boolean userIsAdmin = false;

    @Override
    public Boolean assertRule(Role role, Permission permission)
            throws TransactionException {
        Boolean result = false;

        if (role instanceof UserRole) {
            userId = role.getActorId();
            TransactionalCommand command = getCommandInstance(permission);

            if (command instanceof TenantAccess) {
                tenantIds = ((TenantAccess) command).getTenantIds();
                HibernateTransactionCoordinator.getInstance().runTransaction(
                        this);
                result = userIsAdmin;
            }
        }

        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt) {
        if (tenantIds == null) {
            userIsAdmin = false;
        } else {
            // the user must be the administrator of every given tenant
            userIsAdmin = true;
            for (Long tenantId : tenantIds) {
                userIsAdmin = userIsAdmin
                        && ((Tenant) evt.getSession().get(Tenant.class,
                                tenantId)).getAdmin().getId().equals(userId);
                if (!userIsAdmin) {
                    break;
                }
            }
        }
    }
}
