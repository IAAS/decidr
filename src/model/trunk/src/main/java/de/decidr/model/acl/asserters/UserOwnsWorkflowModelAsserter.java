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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Query;

import de.decidr.model.acl.access.WorkflowModelAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Asserts that the given user owns the given workflow model(s).
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class UserOwnsWorkflowModelAsserter extends CommandAsserter {

    private Long userId = null;
    private Set<Long> workflowModelIds = null;
    private boolean isOwner = false;

    @Override
    public boolean assertRule(Role role, Permission permission)
            throws TransactionException {

        boolean result = false;

        if (role instanceof UserRole) {
            userId = role.getActorId();
            TransactionalCommand command = getCommandInstance(permission);

            if (command instanceof WorkflowModelAccess) {
                workflowModelIds = new HashSet<Long>(Arrays
                        .asList(((WorkflowModelAccess) command)
                                .getWorkflowModelIds()));
                HibernateTransactionCoordinator.getInstance().runTransaction(
                        this);
                result = isOwner;
            }
        }
        return result;
    }

    @Override
    public void transactionStarted(TransactionStartedEvent evt) {
        if ((workflowModelIds == null) || (workflowModelIds.size() == 0)) {
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

            isOwner = ((Number) q.uniqueResult()).intValue() == workflowModelIds
                    .size();
        }
    }
}
