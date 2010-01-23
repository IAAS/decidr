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

package de.decidr.model.commands.workflowmodel;

import java.util.ArrayList;

import org.hibernate.Query;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.User;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Retrieves all workflow administators of the given worfklow model excluding
 * the tenant admin who implicitly administrates all workflow models and
 * instances of his tenant.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class GetWorkflowAdministratorsCommand extends WorkflowModelCommand {

    private ArrayList<User> workflowAdmins = null;

    /**
     * Creates a new {@link GetWorkflowAdministratorsCommand} that retrieves all
     * workflow administrators of the given workflow model excluding the tenant
     * admin.
     * 
     * @param role
     *            user / system executing the command
     * @param workflowModelId
     *            ID of workflow model whose administrators should be retrieved.
     * @throws IllegalArgumentException
     *             if workflowModelId is <code>null</code>.
     */
    public GetWorkflowAdministratorsCommand(Role role, Long workflowModelId) {
        super(role, workflowModelId);
        requireWorkflowModelId();
    }

    /**
     * @return the workflow administrators
     */
    public ArrayList<User> getWorkflowAdmins() {
        return workflowAdmins;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionStartedEvent evt)
            throws TransactionException {
        workflowAdmins = new ArrayList<User>();

        WorkflowModel model = fetchWorkflowModel(evt.getSession());

        Query q = evt.getSession().createQuery(
                "select rel.user from UserAdministratesWorkflowModel rel "
                        + "join fetch rel.user.userProfile "
                        + "where rel.workflowModel = :model");

        q.setEntity("model", model);

        workflowAdmins.addAll(q.list());

        for (User admin : workflowAdmins) {
            // Make sure that Hibernate loads the profile if the user has one.
            admin.getUserProfile();
        }
    }

}
