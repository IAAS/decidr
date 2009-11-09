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

import java.util.List;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.User;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Retrieves all {@link WorkflowModel}s that are administrated by a given user.
 * 
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class GetAdministratedWorkflowModelsCommand extends UserCommand {

    private Long userId;
    private List<WorkflowModel> result;

    /**
     * Creates a new GetAdministratedWorkflowModelCommand. This command will
     * write all WorkflowModels which the user administrates as a list in the
     * result variable.
     * 
     * @param role
     *            user which executes the command
     * @param userId
     *            the ID of the user whose administrated wokflow models should
     *            be requested
     * @throws IllegalArgumentException
     *             if userId is <code>null</code>.
     */
    public GetAdministratedWorkflowModelsCommand(Role role, Long userId) {
        super(role, userId);
        requireUserId();
        this.userId = userId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        result = null;

        // does the user exist? returning an empty list might be ambigous.
        String hql = "select u.id from User u where u.id = :userId";
        Object id = evt.getSession().createQuery(hql).setLong("userId",
                getUserId()).setMaxResults(1).uniqueResult();

        if (id == null) {
            throw new EntityNotFoundException(User.class, getUserId());
        }

        hql = "from WorkflowModel m where "
                + "exists(from UserAdministratesWorkflowModel rel "
                + "where rel.workflowModel = m and rel.user.id = :userId)";

        result = evt.getSession().createQuery(hql).setLong("userId", userId)
                .list();
    }

    /**
     * @return List of workflow models which are administrated by the given user
     */
    public List<WorkflowModel> getResult() {
        return result;
    }
}
