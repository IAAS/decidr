package de.decidr.model.commands.user;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.WorkItemSummaryView;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Filters;
import de.decidr.model.filters.PaginatingCriteria;
import de.decidr.model.filters.Paginator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Saves the workitem of the given user in the result variable as
 * list<WorkItemSummaryView>.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class GetWorkitemsCommand extends UserCommand {

    private List<WorkItemSummaryView> result;
    private Paginator paginator;
    private List<Filter> filters;

    /**
     * Creates a new GetWorkitemsCommand. The command saves the workitem of the
     * given user in the result variable as
     * <code>{@link List<WorkItemSummaryView>}</code>.
     * 
     * @param role
     *            the user which executes the command
     * @param userId
     *            the id of the user whose workitems should be requested
     */
    public GetWorkitemsCommand(Role role, Long userId, List<Filter> filters,
            Paginator paginator) {
        super(role, userId);

        this.paginator = paginator;
        this.filters = filters;

    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        PaginatingCriteria c = new PaginatingCriteria(
                WorkItemSummaryView.class, evt.getSession());
        c.add(Restrictions.eq("userId", getUserId()));

        Filters.apply(c, filters, paginator);

        result = c.list();
    }

    /**
     *  
     * @return list of the workitems of the given user
     */
    public List<WorkItemSummaryView> getResult() {

        return result;
    }
}
