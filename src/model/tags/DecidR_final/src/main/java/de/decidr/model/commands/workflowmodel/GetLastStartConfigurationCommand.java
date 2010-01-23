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

import java.util.Set;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.StartConfiguration;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Retrieves the last start configuration that was used to create a workflow
 * instance from the given workflow model. If the workflow model has no deployed
 * version or has no last start configuration, getStartConfiguration() returns
 * null.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class GetLastStartConfigurationCommand extends WorkflowModelCommand {

    private StartConfiguration startConfiguration;

    /**
     * Creates a new GetLastStartConfigurationCommand that retrieves the last
     * start configuration that was used to create a workflow instance from the
     * given workflow model.
     * 
     * @param role
     *            user / system executing the command
     * @param workflowModelId
     *            ID of workflow model whose last start configuration should be
     *            retrieved.
     * @throws IllegalArgumentException
     *             if workflowModelId is <code>null</code>
     */
    public GetLastStartConfigurationCommand(Role role, Long workflowModelId) {
        super(role, workflowModelId);
        requireWorkflowModelId();
    }

    /**
     * @return the startConfiguration or null if none has been created.
     */
    public StartConfiguration getStartConfiguration() {
        return startConfiguration;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        startConfiguration = null;

        DeployedWorkflowModel deployedModel = fetchCurrentDeployedWorkflowModel(evt
                .getSession());

        if (deployedModel != null) {

            Set<StartConfiguration> configs = deployedModel
                    .getStartConfigurations();

            // We're only expecting one start configuration at the moment.
            for (StartConfiguration firstConfig : configs) {
                this.startConfiguration = firstConfig;
                break;
            }
        }
    }

}
