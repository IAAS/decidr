package de.decidr.model.permissions.asserters;

import org.hibernate.Query;
import org.hibernate.Session;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Asserter;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.UserRole;
import de.decidr.model.permissions.WorkItemPermission;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionAbortedEvent;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Asserts that the given user owns the given work item.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class UserOwnsWorkItem implements Asserter, TransactionalCommand {

    private Long userId = null;
    private Long workItemId = null;
    private Boolean isOwner = false;

    @Override
    public Boolean assertRule(Role role, Permission permission)
            throws TransactionException {

        Boolean result = false;

        if ((role instanceof UserRole)
                && (permission instanceof WorkItemPermission)) {

            userId = role.getActorId();
            workItemId = ((WorkItemPermission) permission).getId();

            HibernateTransactionCoordinator.getInstance().runTransaction(this);

            result = isOwner;
        }

        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt) {
        Session session = evt.getSession();

        Query q = session
                .createQuery("select count(*) from WorkItem w where w.user.id = :userId and w.id = :workItemId");
        q.setEntity("userId", userId);
        q.setLong("workItemId", workItemId);

        isOwner = q.uniqueResult().equals(1L);
    }

    @Override
    public void transactionAborted(TransactionAbortedEvent evt) {
        // nothing to do
    }

    @Override
    public void transactionCommitted(TransactionEvent evt) {
        // nothing to do
    }

}
