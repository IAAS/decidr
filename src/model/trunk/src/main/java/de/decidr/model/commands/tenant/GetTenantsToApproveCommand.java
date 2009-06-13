package de.decidr.model.commands.tenant;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.TenantWithAdminView;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Filters;
import de.decidr.model.filters.PaginatingCriteria;
import de.decidr.model.filters.Paginator;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Save all Tenants which have to bee approve in the result variable.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class GetTenantsToApproveCommand extends AclEnabledCommand {

    private Paginator paginator;
    private List<Filter> filters;
    private List<TenantWithAdminView> result;

    /**
     * 
     * Creates a new GetTenantsTo ApproveCommand. This command will save all
     * Tenants which have to bee approve in the result variable.
     * 
     * @param role
     *            the user which executes the command
     * @param filters
     * @param paginator
     */
    public GetTenantsToApproveCommand(Role role, List<Filter> filters,
            Paginator paginator) {
        super(role, (Permission)null);

        this.paginator = paginator;
        this.filters = filters;

    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        PaginatingCriteria c = new PaginatingCriteria(
                TenantWithAdminView.class, evt.getSession());
        c.add(Restrictions.isNotNull("approvedSince"));

        Filters.apply(c, filters, paginator);

        result = c.list();

    }

    /**
     * 
     * @return the result
     */
    public List<TenantWithAdminView> getResult() {
        return result;
    }

}
