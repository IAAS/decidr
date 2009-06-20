package de.decidr.model.commands.user;

import java.util.List;

import org.hibernate.Criteria;

import de.decidr.model.entities.User;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Filters;
import de.decidr.model.filters.PaginatingCriteria;
import de.decidr.model.filters.Paginator;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Saves all system users in the result variable.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class GetAllUsersCommand extends UserCommand {

    private List<Filter> filters;
    private Paginator paginator;
    private List<User> result;

    /**
     * 
     * Creates a new GetAllUsersCommand. This command saves all system users in
     * the result variable.
     * 
     * @param role
     *            the user which executes the command
     * @param userId
     * @param filters
     * @param paginator
     */
    public GetAllUsersCommand(Role role, List<Filter> filters,
            Paginator paginator) {
        super(role, null);
        
        this.filters = filters;
        this.paginator = paginator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        PaginatingCriteria c = new PaginatingCriteria(User.class, evt
                .getSession());
        
        c.createCriteria("userProfile",Criteria.LEFT_JOIN);

        Filters.apply(c, filters, paginator);

        result = c.list();

    }

    /**
     * 
     * @return the result
     */
    public List<User> getResult() {
        return result;
    }

}