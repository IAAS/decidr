package de.decidr.model.commands.system;

import java.util.List;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.Log;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Filters;
import de.decidr.model.filters.PaginatingCriteria;
import de.decidr.model.filters.Paginator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Returns the logs saved in the database.
 * 
 * @author Daniel Huss
 */
public class GetLogCommand extends SystemCommand {

    private List<Log> result;
    private List<Filter> filters;
    private Paginator paginator;

    /**
     * Creates a new <code>{@link GetLogCommand}</code>. The command saves the
     * logs in the variable <code>{@link #result}</code>.
     * 
     * @param role
     *            the user who executes the command
     * @param filters
     *            filters the result by the given criteria
     * @param paginator
     *            splits the document into several pages
     */
    public GetLogCommand(Role role, List<Filter> filters, Paginator paginator) {
        super(role, null);
        this.filters = filters;
        this.paginator = paginator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        PaginatingCriteria c = new PaginatingCriteria(Log.class, evt
                .getSession());

        Filters.apply(c, filters, paginator);

        result = c.list();
    }

    /**
     * Returns the result.
     * 
     * @return List of the logs.
     */
    public List<Log> getResult() {
        return result;
    }
}
