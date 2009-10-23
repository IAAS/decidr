package de.decidr.model.acl.asserters;

import java.util.List;

import de.decidr.model.acl.access.TenantAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.commands.TransactionalCommand;
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
public class UserIsTenantAdminAsserter extends CommandAsserter {

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
                if (tenantIds == null || tenantIds.length == 0
                        || userId == null) {
                    // no database query necessary
                    result = false;
                } else {
                    HibernateTransactionCoordinator.getInstance()
                            .runTransaction(this);
                    result = userIsAdmin;
                }
            }
        }

        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt) {
        userIsAdmin = false;
        // the user must be the administrator of every given tenant
        String hql = "select distinct t.admin.id from Tenant t "
                + "where t.id in (:tenantIds)";

        List<?> result = evt.getSession().createQuery(hql).setParameterList(
                "tenantIds", tenantIds).list();

        // due to distinct selection, the user is administrator of all given
        // tenants if the result contains exactly one match and it's equal to
        // the user's id.
        userIsAdmin = result != null && result.size() == 1
                && userId.equals(result.get(0));
    }
}
