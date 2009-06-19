package de.decidr.model.permissions.asserters;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.entities.UserAdministratesWorkflowInstance;
import de.decidr.model.entities.UserAdministratesWorkflowInstanceId;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Asserter;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.WorkflowAdminRole;
import de.decidr.model.permissions.WorkflowInstancePermission;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionAbortedEvent;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Asserts that the given user administrates the given workflow instance.
 * 
 * @author Daniel Huss
 *
 * @version 0.1
 */
public class IsWorkflowAdminOfInstance implements Asserter,
        TransactionalCommand {

    UserAdministratesWorkflowInstanceId id = null;

    private Boolean isWorkflowAdmin = false;

    @Override
    public Boolean assertRule(Role role, Permission permission) throws TransactionException{
        Boolean result = false;

        if ((role instanceof WorkflowAdminRole)
                && (permission instanceof WorkflowInstancePermission)) {
            id = new UserAdministratesWorkflowInstanceId(role.getActorId(),
                    ((WorkflowInstancePermission) permission).getId());

            HibernateTransactionCoordinator.getInstance().runTransaction(this);

            result = isWorkflowAdmin;
        }

        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt) {
        isWorkflowAdmin = evt.getSession().get(
                UserAdministratesWorkflowInstance.class, id) != null;
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