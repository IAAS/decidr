package de.decidr.model.acl.asserters;

import org.hibernate.Query;

import de.decidr.model.acl.access.WorkflowModelAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Asserts that the given user owns the given workflow model(s).
 * 
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class UserOwnsWorkflowModelAsserter extends CommandAsserter {

    private Long userId = null;
    private Long[] workflowModelIds = null;
    private Boolean isOwner = false;

    @Override
    public Boolean assertRule(Role role, Permission permission)
            throws TransactionException {

        Boolean result = false;

        if (role instanceof UserRole) {
            userId = role.getActorId();
            TransactionalCommand command = getCommandInstance(permission);

            if (command instanceof WorkflowModelAccess) {
                workflowModelIds = ((WorkflowModelAccess) command)
                        .getWorkflowModelIds();
                HibernateTransactionCoordinator.getInstance().runTransaction(
                        this);
                result = isOwner;
            }
        }
        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt) {
        if (workflowModelIds == null || workflowModelIds.length == 0) {
            // no work items to check against
            isOwner = false;
        } else {
            // we can assert the ownership for all workflow models with a single
            // query: the user owns the workflow iff he is the tenant admin
            String hql = "select count(*) from WorkflowModel w where "
                    + "w.tenant.admin.id = :userId and "
                    + "w.id in (:workflowModelIds)";
            Query q = evt.getSession().createQuery(hql).setLong("userId",
                    userId).setParameterList("workflowModelIds",
                    workflowModelIds);

            isOwner = ((Number) q.uniqueResult()).intValue() == workflowModelIds.length;
        }
    }
}
