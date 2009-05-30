package de.decidr.model.permissions.asserters;

import org.hibernate.Query;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.permissions.Asserter;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.SuperAdminRole;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionAbortedEvent;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Asserts that the given super admin is actually the super admin.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 *
 * @version 0.1
 */
public class IsSuperAdmin implements Asserter, TransactionalCommand {

    private Long userid = null;

    Boolean isSuperAdmin = false;

    @Override
    public Boolean assertRule(Role role, Permission permission) {
        Boolean result = false;

        if (role instanceof SuperAdminRole) {
            userid = role.getActorId();
            HibernateTransactionCoordinator.getInstance().runTransaction(this);
        }

        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt) {
        isSuperAdmin = false;

        Query q = evt.getSession().createQuery("from SystemSettings");
        SystemSettings settings = (SystemSettings) q.list().get(0);

        isSuperAdmin = (settings.getSuperAdmin().getId() == userid);
    }

    @Override
    public void transactionAborted(TransactionAbortedEvent evt) {
    }

    @Override
    public void transactionCommitted(TransactionEvent evt) {
    }

}