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
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Retrieves all {@link WorkflowInstance}s that are administrated by a given
 * user.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
@SuppressWarnings("unchecked")
public class GetAdministratedWorkflowInstancesCommand extends UserCommand {

    List<WorkflowInstance> result = new ArrayList();

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
     */
    public GetAdministratedWorkflowInstancesCommand(Role role, Long userId) {
        super(role, userId);
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        String hql = "select rel.workflowInstance "
                + "from UserAdministratesWorkflowInstance rel "
                + "join fetch rel.workflowInstance wi "
                + "join fetch wi.deployedWorkflowModel "
                + "where rel.user.id = :userId";

        result = evt.getSession().createQuery(hql).setLong("userId",
                getUserId()).list();
    }

    /**
     * @return List of WorkflowInstances which are administrated by the given
     *         user
     */
    public List<WorkflowInstance> getResult() {
        return result;
    }
}
