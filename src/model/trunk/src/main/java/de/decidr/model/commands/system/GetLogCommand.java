package de.decidr.model.commands.system;

import java.util.List;

import de.decidr.model.entities.Log;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Filters;
import de.decidr.model.filters.PaginatingCriteria;
import de.decidr.model.filters.Paginator;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

public class GetLogCommand extends SystemCommand {

    private List<Log> result;
    private List<Filter> filters;
    private Paginator paginator;

    /**
     * Creates a new <code>{@link GetLogCommand}</code>. The command saves the
     * logs in the variable result.
     * 
     * @param role
     *            the user who executes the command
     * @param filters TODO document
     * @param paginator TODO document
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
     * TODO document
     * 
     * @return
     */
    public List<Log> getResult() {
        return result;
    }

}
