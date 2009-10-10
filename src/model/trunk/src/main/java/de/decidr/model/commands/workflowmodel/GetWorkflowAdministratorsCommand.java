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
import de.decidr.model.transactions.TransactionEvent;

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
     * Constructor.
     * 
     * @param role
     * @param workflowModelId
     */
    public GetWorkflowAdministratorsCommand(Role role, Long workflowModelId) {
        super(role, workflowModelId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        workflowAdmins = new ArrayList<User>();

        WorkflowModel model = fetchWorkflowModel(evt.getSession());

        Query q = evt.getSession().createQuery(
                "select rel.user from UserAdministratesWorkflowModel "
                        + "where rel.workflowModel = :model");

        q.setEntity("model", model);

        workflowAdmins.addAll(q.list());

        for (User admin : workflowAdmins) {
            // Make sure that Hibernate loads the profile if the user has one.
            admin.getUserProfile();
        }
    }

    /**
     * @return the workflow administrators
     */
    public ArrayList<User> getWorkflowAdmins() {
        return workflowAdmins;
    }

}
