package de.decidr.model.commands.user;

import java.util.List;

import org.hibernate.Query;

import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Fetches all approved tenants of which the given user is a member from the
 * database.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class GetJoinedTenantsCommand extends UserCommand {

    private List<Tenant> result;

    /**
     * Creates a new getJoinedTenantsCommand. This command writes all tenants
     * the given user is member of in the result variable.
     * 
     * @param role
     *            the user which executes the command
     * @param userId
     *            the ID of the user whose joined tenants should be returned
     */
    public GetJoinedTenantsCommand(Role role, Long userId) {
        super(role, userId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        String hql = "select rel.tenant from UserIsMemberOfTenant as rel "
                + "where (rel.user.id = :userId) and "
                + "(rel.tenant.approvedSince is not null)";

        Query q = evt.getSession().createQuery(hql).setLong("useId",
                getUserId());

        result = q.list();
    }

    /**
     * @return the joined tenants.
     */
    public List<Tenant> getResult() {
        return result;
    }
}
