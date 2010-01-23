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

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Retrieves a single workflow model from the database.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class GetWorkflowModelCommand extends WorkflowModelCommand {

    protected WorkflowModel result = null;

    /**
     * Creates a new GetWorkflowModelCommand that retrieves a single workflow
     * model from the database.
     * 
     * @param role
     *            user / system executing the command
     * @param workflowModelId
     *            ID of workflow model to fetch
     * @throws IllegalArgumentException
     *             if workflowModelId is <code>null</code>.
     */
    public GetWorkflowModelCommand(Role role, Long workflowModelId) {
        super(role, workflowModelId);
        requireWorkflowModelId();
    }

    /**
     * @return the result
     */
    public WorkflowModel getResult() {
        return result;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        // in case the next statement fails
        result = null;
        result = fetchWorkflowModel(evt.getSession());
        // make sure Hibernate loads the required properties.
        result.getDwdl();
        result.getModifiedDate();
        result.getCreationDate();
        if (result.getModifiedByUser() != null) {
            result.getModifiedByUser().getUserProfile();
        }
    }

}
