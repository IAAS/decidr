package de.decidr.model.acl.asserters;

import org.hibernate.Query;

import de.decidr.model.acl.access.WorkflowInstanceAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Asserts that the given user participates in the given workflow instance(s).
 * 
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class UserIsWorkflowParticipantAsserter extends CommandAsserter {

    private Long[] workflowInstanceIds = null;
    private Long userId = null;
    private Boolean isWorkflowParticipant = false;

    @Override
    public Boolean assertRule(Role role, Permission permission)
            throws TransactionException {
        Boolean result = false;

        if (role instanceof UserRole) {
            userId = role.getActorId();
            TransactionalCommand command = getCommandInstance(permission);
            if (command instanceof WorkflowInstanceAccess) {
                workflowInstanceIds = ((WorkflowInstanceAccess) command)
                        .getWorkflowInstanceIds();
                HibernateTransactionCoordinator.getInstance().runTransaction(
                        this);
                result = isWorkflowParticipant;
            }
        }

        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt) {
        // Tenant administrators do NOT implicitly participate in all workflow
        // instances!
        if (workflowInstanceIds == null || workflowInstanceIds.length == 0) {
            isWorkflowParticipant = false;
        } else {
            // we can assert participation in all workflow instances with a
            // single query!
            String hql = "select count(*) from UserParticipatesInWorkflow rel where "
                    + "rel.user.id = :userId and "
                    + "rel.workflowInstance.id in (:workflowInstanceIds)";
            Query q = evt.getSession().createQuery(hql).setLong("userId",
                    userId).setParameterList("workflowInstanceIds",
                    workflowInstanceIds);
            isWorkflowParticipant = ((Number) q.uniqueResult()).intValue() == workflowInstanceIds.length;
        }
    }
}
