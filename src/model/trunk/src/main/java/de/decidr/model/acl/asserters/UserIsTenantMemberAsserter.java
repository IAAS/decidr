package de.decidr.model.acl.asserters;

import org.hibernate.Query;

import de.decidr.model.acl.access.TenantAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Asserts that the given user is a member of any kind of the given tenant.
 * 
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class UserIsTenantMemberAsserter extends CommandAsserter {

    private Long userId = null;
    private Long[] tenantIds = null;
    private Boolean userIsMember = false;

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
                result = userIsMember;
            }
        }

        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt) {
        // a user is a member of a tenant if he is the admin or a regular member
        String hql = "select (select count(*) from UserIsMemberOfTenant rel where "
                + "rel.user.id = :userId and rel.tenant.id = :tenantId) +"
                + "(select count(*) from Tenant t where "
                + "t.admin.id = :userId and t.id = :tenantId)";

        Query q = evt.getSession().createQuery(hql).setLong("userId", userId);

        if (tenantIds == null) {
            // no tenant ids to check against, so we're returning false
            userIsMember = false;
        } else {
            // the user must be a member of each given tenant
            userIsMember = true;
            for (Long tenantId : tenantIds) {
                q.setLong("tenantId", tenantId);
                Number membershipCount = (Number) q.uniqueResult();
                userIsMember = userIsMember && (membershipCount.intValue() > 0);
                if (!userIsMember) {
                    break;
                }
            }
        }
    }
}
