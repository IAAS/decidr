package de.decidr.model.commands.user;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;

import de.decidr.model.entities.SystemSettings;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.SuperAdminRole;
import de.decidr.model.permissions.TenantAdminRole;
import de.decidr.model.permissions.UserRole;
import de.decidr.model.permissions.WorkflowAdminRole;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Saves the highest role of the given user in the result variable.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class GetHighestUserRoleCommand extends UserCommand {

    Class<? extends UserRole> result;

    /**
     * 
     * Creates a new GetHighestUserRoleCommand. This command saves the highest
     * role of the given user in the result variable.
     * 
     * @param role
     * @param userId
     */
    public GetHighestUserRoleCommand(Role role, Long userId) {
        super(role, userId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        // check if super admin
        Criteria c = evt.getSession().createCriteria(SystemSettings.class);
        List<SystemSettings> results = c.list();
        SystemSettings innerResult;

        if (results.size() > 0) {
            throw new TransactionException(
                    "More than one system settings found, but system settings should be unique");
        } else if (results.size() == 0) {
            throw new EntityNotFoundException(SystemSettings.class);
        } else {
            innerResult = results.get(0);
        }

        if (getUserId() == innerResult.getSuperAdmin().getId()) {
            result = SuperAdminRole.class;
            return;
        }

        // check if tenant admin

        Query q = evt.getSession().createQuery(
                "select count(*) from Tenant a where a.admin.id = :userId");
        q.setLong("userId", getUserId());

        Number count = (Number) q.uniqueResult();

        if (count == null) {
            throw new TransactionException("Query didn't return a result.");
        } else if (count.intValue() > 0) {
            result = TenantAdminRole.class;
            return;
        }

        // check if workflow admin
        Query q2 = evt
                .getSession()
                .createQuery(
                        "select count(*) from UserAdministratesWorkflowInstance a where a.user.id = :userId");
        q.setLong("userId", getUserId());

        count = (Number) q2.uniqueResult();

        if (count == null) {
            throw new TransactionException("Query didn't return a result.");
        } else if (count.intValue() > 0) {
            result = WorkflowAdminRole.class;
            return;
        }

        // check if member

        Query q3 = evt
                .getSession()
                .createQuery(
                        "select count(*) from UserIsMemberOfTenant a where a.user.id = :userId");
        q.setLong("userId", getUserId());

        count = (Number) q3.uniqueResult();

        if (count == null) {
            throw new TransactionException("Query didn't return a result.");
        } else if (count.intValue() > 0) {
            result = UserRole.class;
            return;
        }

        // if user isn't member of any tenant
        result = null;

    }

    /**
     * @return the result
     */
    public Class<? extends UserRole> getResult() {
        return result;
    }

}
