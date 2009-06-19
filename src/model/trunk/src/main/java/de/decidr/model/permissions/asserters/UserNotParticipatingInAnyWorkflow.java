package de.decidr.model.permissions.asserters;

import org.hibernate.Query;
import org.hibernate.Session;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Asserter;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.UserRole;
import de.decidr.model.permissions.WorkflowModelPermission;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionAbortedEvent;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Asserts that the given user is not participating in any worklfow instance.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 *
 * @version 0.1
 */
public class UserNotParticipatingInAnyWorkflow implements Asserter,
        TransactionalCommand {

    private Long userid = null;

    private Boolean notParticipating = false;

    @Override
    public Boolean assertRule(Role role, Permission permission) throws TransactionException{

        Boolean result = false;

        if ((role instanceof UserRole)
                && (permission instanceof WorkflowModelPermission)) {

            userid = role.getActorId();

            HibernateTransactionCoordinator.getInstance().runTransaction(this);

            result = notParticipating;
        }
        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt) {
        Session session = evt.getSession();

        User user = new User();
        user.setId(userid);

        Query q = session
                .createQuery("select count(*) from UserParticipatesInWorkflow where user = :user");
        q.setEntity("user", user);

        notParticipating = q.uniqueResult().equals(0L);
    }

    @Override
    public void transactionAborted(TransactionAbortedEvent evt) {
        // TODO
    }

    @Override
    public void transactionCommitted(TransactionEvent evt) {
        // TODO
    }

}
