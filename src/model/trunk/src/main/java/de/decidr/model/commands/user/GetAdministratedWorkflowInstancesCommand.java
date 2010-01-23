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

package de.decidr.model.commands.user;

import java.util.ArrayList;
import java.util.List;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.User;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Retrieves all {@link WorkflowInstance}s that are administrated by a given
 * user.
 * 
 * XXX why does this command not support pagination and filters? ~dh
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class GetAdministratedWorkflowInstancesCommand extends UserCommand {

    List<WorkflowInstance> result = new ArrayList<WorkflowInstance>();

    /**
     * Creates a new GetAdminstratedWorkflowInstancesCommand. This Command
     * retrieves all workflow instances that are administrated by the given
     * user.
     * 
     * @param role
     *            user which executes the command
     * @param userId
     *            the ID of the user whose administrated workflow instances
     *            should be retrieved
     * @throws IllegalArgumentException
     *             if userId is <code>null</code>.
     */
    public GetAdministratedWorkflowInstancesCommand(Role role, Long userId) {
        super(role, userId);
        requireUserId();
    }

    /**
     * @return List of WorkflowInstances which are administrated by the given
     *         user
     */
    public List<WorkflowInstance> getResult() {
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionStartedEvent evt)
            throws TransactionException {

        // does the user exist? returning an empty list might be ambigous.
        String hql = "select u.id from User u where u.id = :userId";
        Object id = evt.getSession().createQuery(hql).setLong("userId",
                getUserId()).setMaxResults(1).uniqueResult();

        if (id == null) {
            throw new EntityNotFoundException(User.class, getUserId());
        }

        /*-
         * A user administers a workflow instance iff:
         * 
         *  - an explicit ownership relation has been created.
         *                        OR
         *  - the user is the administrator of the tenant that owns 
         *    the worflow instance.
         *                        OR
         *  - the user is the superadmin who administers ALL workflow
         *    instances (careful, you might get a lot of results)
         */
        hql = "select wi from WorkflowInstance wi "
                + "join fetch wi.deployedWorkflowModel where "
                + "exists( from UserAdministratesWorkflowInstance rel "
                + "where rel.workflowInstance = wi and rel.user.id = :userId) "
                + "or wi.deployedWorkflowModel.tenant.admin.id = :userId "
                + "or exists(from SystemSettings s where s.superAdmin.id = :userId)";

        result = evt.getSession().createQuery(hql).setLong("userId",
                getUserId()).list();
    }
}
