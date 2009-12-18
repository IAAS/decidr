package de.decidr.model.acl.asserters;

import org.hibernate.Query;

import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Asserts that the given user is not participating in any worklfow instance.
 * <p>
 * The {@link Permission} passed to <code>assertRule</code> is ignored.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class UserNotParticipatingInAnyWorkflowAsserter extends
        AbstractTransactionalCommand implements Asserter {

    private Long userid = null;
    private boolean notParticipating = false;

    @Override
    public boolean assertRule(Role role, Permission permission)
            throws TransactionException {

        boolean result = false;

        if (role instanceof UserRole) {
            userid = role.getActorId();
            HibernateTransactionCoordinator.getInstance().runTransaction(this);
            result = notParticipating;
        }
        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt) {
        notParticipating = false;
        Query q = evt.getSession().createQuery(
                "select rel.user.id from UserParticipatesInWorkflow rel "
                        + "where rel.user.id = :userId");
        q.setLong("userId", userid).setMaxResults(1);

        notParticipating = q.uniqueResult() == null;
    }
}
