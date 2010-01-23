/*
 * The DecidR Development Team licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.decidr.model.acl.asserters;

import org.hibernate.Query;

import de.decidr.model.acl.access.WorkflowModelAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.WorkflowAdminRole;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Asserts that the given user administrates the given workflow model(s).
 * 
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class UserAdministratesWorkflowModelAsserter extends CommandAsserter {

    private Long[] workflowModelIds = null;
    private Long userId = null;
    private boolean isWorkflowAdmin = false;

    @Override
    public boolean assertRule(Role role, Permission permission)
            throws TransactionException {
        boolean result = false;

        if (role instanceof WorkflowAdminRole) {
            userId = role.getActorId();

            TransactionalCommand command = getCommandInstance(permission);
            if (command instanceof WorkflowModelAccess) {
                workflowModelIds = ((WorkflowModelAccess) command)
                        .getWorkflowModelIds();
                HibernateTransactionCoordinator.getInstance().runTransaction(
                        this);
                result = isWorkflowAdmin;
            }
        }

        return result;
    }

    @Override
    public void transactionStarted(TransactionStartedEvent evt) {
        if (workflowModelIds == null) {
            isWorkflowAdmin = false;
        } else {
            /*
             * The user administrates the workflow model if explicitly stated by
             * the database relation user_administrates_workflow_model or if he
             * is the administrator of the tenant that owns the workflow model.
             * This also covers the special case of the default tenant because
             * the super admin is the tenant admin of the default tenant.
             */
            Query relationQuery = evt.getSession().createQuery(
                    "select rel.user.id from UserAdministratesWorkflowModel rel where "
                            + "rel.workflowModel.id = :workflowModelId and "
                            + "rel.user.id = :userId")
                    .setLong("userId", userId).setMaxResults(1);

            Query adminQuery = evt.getSession().createQuery(
                    "select wm.id from WorkflowModel wm where "
                            + "wm.id = :workflowModelId and "
                            + "wm.tenant.admin.id = :userId").setLong("userId",
                    userId).setMaxResults(1);

            isWorkflowAdmin = true;
            // assume the user is a workflow admin until proven false
            for (Long workflowModelId : workflowModelIds) {
                relationQuery.setLong("workflowModelId", workflowModelId);
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