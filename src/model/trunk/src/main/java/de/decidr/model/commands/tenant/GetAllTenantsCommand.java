package de.decidr.model.commands.tenant;

import java.util.ArrayList;
import java.util.List;

import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.TenantSummaryView;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Filters;
import de.decidr.model.filters.PaginatingCriteria;
import de.decidr.model.filters.Paginator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Saves all Tenants in the result variable.
 * 
 * @author Markus Fischer
 * @version 0.1
 */
@SuppressWarnings("unchecked")
public class GetAllTenantsCommand extends AclEnabledCommand {

    private List<Filter> filters = new ArrayList();
    private Paginator paginator;
    private List<TenantSummaryView> result;

    /**
     * Creates a new GetAllTenantsCommand. This command saves all Tenants in the
     * result variable.
     * 
     * @param role
     *            user which executes the command
     * @param filters
     *            {@link Filter}
     * @param paginator
     *            {@link Paginator}
     */
    public GetAllTenantsCommand(Role role, List<Filter> filters,
            Paginator paginator) {
        super(role, (Permission) null);

        this.filters = filters;
        this.paginator = paginator;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        PaginatingCriteria c = new PaginatingCriteria(TenantSummaryView.class,
                evt.getSession());
        Filters.apply(c, filters, paginator);

        result = c.list();
    }

    /**
     * 
     * @return TenantSummaryView
     */
    public List<TenantSummaryView> getResult() {
        return result;
    }
}
