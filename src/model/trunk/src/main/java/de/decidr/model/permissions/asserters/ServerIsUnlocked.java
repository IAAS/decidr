package de.decidr.model.permissions.asserters;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.entities.Server;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Asserter;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.ServerPermission;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionAbortedEvent;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Asserts that the given server hasn't been locked manually by the system admin.
 * 
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class ServerIsUnlocked implements Asserter, TransactionalCommand {

    private Long serverId = null;

    private Boolean serverIsUnlocked = false;

    @Override
    public Boolean assertRule(Role role, Permission permission) throws TransactionException{
        Boolean result = false;

        if (permission instanceof ServerPermission) {
            serverId = ((ServerPermission) permission).getId();
            HibernateTransactionCoordinator.getInstance().runTransaction(this);
            result = serverIsUnlocked;
        }

        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt) {
        Server server = (Server) evt.getSession().get(Server.class, serverId);
        serverIsUnlocked = (server != null) && (!server.isLocked());
    }

    @Override
    public void transactionAborted(TransactionAbortedEvent evt) {
    }

    @Override
    public void transactionCommitted(TransactionEvent evt) {
    }

}
