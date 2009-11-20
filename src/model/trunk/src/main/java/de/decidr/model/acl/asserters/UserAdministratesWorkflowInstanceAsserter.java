package de.decidr.model.acl.asserters;

import org.hibernate.Query;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.access.WorkflowInstanceAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.WorkflowAdminRole;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Asserts that the given user administrates the given workflow instance(s).
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class UserAdministratesWorkflowInstanceAsserter extends CommandAsserter {

    private Long[] workflowInstanceIds = null;
    private Long userId = null;
    private Boolean isWorkflowAdmin = false;

    @Override
    public Boolean assertRule(Role role, Permission permission)
            throws TransactionException {
        Boolean result = false;

        if (role instanceof WorkflowAdminRole) {
            userId = role.getActorId();

            TransactionalCommand command = getCommandInstance(permission);
            if (command instanceof WorkflowInstanceAccess) {
                workflowInstanceIds = ((WorkflowInstanceAccess) command)
                        .getWorkflowInstanceIds();
                HibernateTransactionCoordinator.getInstance().runTransaction(
                        this);
                result = isWorkflowAdmin;
            }
        }

        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt) {
        if (workflowInstanceIds == null || workflowInstanceIds.length == 0) {
            isWorkflowAdmin = false;
        } else {
            /*
             * The user administrates the workflow instance if explicitly stated
             * by the database relation user_administrates_workflow_instance or
             * if the is the administrator of the tenant that owns the workflow
             * instance.
             * 
             * The default tenant is a special case: every user is a workflow
             * admin there.
             */
            String hql = "select rel.user.id from UserAdministratesWorkflowInstance rel where "
                    + "rel.workflowInstance.id = :workflowInstanceId and "
                    + "rel.user.id = :userId";

            Query relationQuery = evt.getSession().createQuery(hql).setLong(
                    "userId", userId).setLong("defaultTenantId",
                    DecidrGlobals.DEFAULT_TENANT_ID).setMaxResults(1);

            hql = "select wi.id WorkflowInstance wi where "
                    + "wi.id = :workflowInstanceId and "
                    + "(wi.deployedWorkflowModel.tenant.admin.id = :userId or "
                    + "wi.deployedWorkflowModel.tenant.id = :defaultTenantId)";

            Query adminQuery = evt.getSession().createQuery(hql).setLong(
                    "userId", userId).setLong("defaultTenantId",
                    DecidrGlobals.DEFAULT_TENANT_ID).setMaxResults(1);

            isWorkflowAdmin = true;
            // assume the user is a workflow admin until proven false
            for (Long workflowInstanceId : workflowInstanceIds) {
                relationQuery.setLong("workflowInstanceId", workflowInstanceId);
                Object found = relationQuery.uniqueResult();

                if (found == null) {
                    isWorkflowAdmin = adminQuery.uniqueResult() != null;
                }

                if (!isWorkflowAdmin) {
                    break;
                }
            }
        }
    }
}