package de.decidr.model.commands.tenant;

import java.util.List;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.PaginatingCriteria;
import de.decidr.model.filters.Paginator;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

public class GetUsersOfTenantCommand extends TenantCommand {

    private Paginator paginator;
    private List<User> result;

    /**
     * Creates a new GetUsersOfTenantCommand. The Command saves the Users of the
     * given tenant in the result variable. The tenant admin will not be part of
     * the result.
     * 
     * @param role
     *            the user which executes the command
     * @param tenantId
     *            the ID of the tenant whose users should be requested
     * @param paginator
     *            {@link Paginator}
     */
    public GetUsersOfTenantCommand(Role role, Long tenantId, Paginator paginator) {
        super(role, tenantId);

        this.paginator = paginator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        PaginatingCriteria c = new PaginatingCriteria(User.class, evt
                .getSession());
        Tenant t = new Tenant();
        t.setId(getTenantId());

        ProjectionList plist = Projections.projectionList();
        plist.add(Projections.property("username"));
        plist.add(Projections.property("firstName"));
        plist.add(Projections.property("lastName"));

        c.createCriteria("userIsMemberOfTenants").add(
                Restrictions.eq("tenant", t)).createCriteria("userProfile",
                CriteriaSpecification.INNER_JOIN).setProjection(plist);

        if (paginator != null) {
            paginator.apply(c);
        }
        result = c.list();
    }

    public List<User> getResult() {
        return result;
    }
}
