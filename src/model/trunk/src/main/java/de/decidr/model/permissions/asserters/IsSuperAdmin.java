package de.decidr.model.permissions.asserters;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.permissions.Asserter;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionAbortedEvent;
import de.decidr.model.transactions.TransactionCoordinator;
import de.decidr.model.transactions.TransactionEvent;

public class IsSuperAdmin implements Asserter, TransactionalCommand {

    private Long userid = null;
    Boolean result = false;

    @Override
    public Boolean assertRule(Role role, Permission permission) {

        userid = role.getActorId();

        TransactionCoordinator htac = HibernateTransactionCoordinator
                .getInstance();

        htac.runTransaction(this);

        return result;
    }

    @Override
    public void transactionAborted(TransactionAbortedEvent evt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void transactionCommitted(TransactionEvent evt) {
        // TODO Auto-generated method stub

    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionStarted(TransactionEvent evt) {
        Session session = evt.getSession();

        String qtext = "from SystemSettings";

        Query q = session.createQuery(qtext);
        List<SystemSettings> qResult = q.list();

        SystemSettings settings = qResult.get(0);

        result = (settings.getSuperAdmin().getId() == userid);
        

    }
}