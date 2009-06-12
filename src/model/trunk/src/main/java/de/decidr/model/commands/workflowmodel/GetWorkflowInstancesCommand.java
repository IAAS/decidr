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

import java.util.List;

import org.hibernate.Query;

import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Retrieves the workflow instances that belong to the given workflow model.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class GetWorkflowInstancesCommand extends WorkflowModelCommand {

    private List<WorkflowInstance> result;

    /**
     * Constructor
     * 
     * @param role
     * @param workflowModelId
     */
    public GetWorkflowInstancesCommand(Role role, Long workflowModelId) {
        super(role, workflowModelId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        WorkflowModel model = fetchWorkflowModel(evt.getSession());

        String hql = "from WorkflowInstance as wi "
                + "inner join fetch DeployedWorkflowModel as dwm "
                + "where dwm.originalWorkflowModel = :model ";

        Query q = evt.getSession().createQuery(hql).setEntity("model", model);

        List<WorkflowInstance> instances = q.list();
        /*
         * make sure the needed properties are retrieved.
         */
        for (WorkflowInstance instance : instances) {
            instance.getId();
            instance.getCompletedDate();
            instance.getStartedDate();
            instance.getDeployedWorkflowModel().getName();
            instance.getDeployedWorkflowModel().getDescription();
            instance.getDeployedWorkflowModel().getDeployDate();
        }

        result = instances;
    }

    /**
     * @return the result
     */
    public List<WorkflowInstance> getResult() {
        return result;
    }

}
