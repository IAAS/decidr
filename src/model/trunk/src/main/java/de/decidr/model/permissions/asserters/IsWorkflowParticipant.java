package de.decidr.model.permissions.asserters;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.entities.UserParticipatesInWorkflow;
import de.decidr.model.entities.UserParticipatesInWorkflowId;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Asserter;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.UserRole;
import de.decidr.model.permissions.WorkflowInstancePermission;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionAbortedEvent;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Asserts that the given user participates in the given workflow instance.
 * 
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class IsWorkflowParticipant implements Asserter, TransactionalCommand {

    private UserParticipatesInWorkflowId id = null;

    private Boolean isWorkflowParticipant = false;

    @Override
    public Boolean assertRule(Role role, Permission permission) throws TransactionException{
        Boolean result = false;

        if ((role instanceof UserRole)
                && (permission instanceof WorkflowInstancePermission)) {

            id = new UserParticipatesInWorkflowId(role.getActorId(),
                    ((WorkflowInstancePermission) permission).getId());

            HibernateTransactionCoordinator.getInstance().runTransaction(this);
            result = isWorkflowParticipant;
        }

        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt) {
        isWorkflowParticipant = evt.getSession().get(
                UserParticipatesInWorkflow.class, id) != null;
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
