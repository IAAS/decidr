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
 * instance from the given workflow model.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class GetLastStartConfigurationCommand extends WorkflowModelCommand {

    private StartConfiguration startConfiguration;

    /**
     * Constructor.
     * <p>
     * This command retrieves the last start configuration that was used to
     * create a workflow instance from the given workflow model.
     * 
     * @param role
     * @param workflowModelId
     */
    public GetLastStartConfigurationCommand(Role role, Long workflowModelId) {
        super(role, workflowModelId);
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        DeployedWorkflowModel deployedModel = fetchCurrentDeployedWorkflowModel(evt
                .getSession());

        Set<StartConfiguration> configs = deployedModel
                .getStartConfigurations();

        // We're only expecting one start configuration at the moment.
        for (StartConfiguration firstConfig : configs) {
            this.startConfiguration = firstConfig;
            break;
        }
    }

    /**
     * @return the startConfiguration
     */
    public StartConfiguration getStartConfiguration() {
        return startConfiguration;
    }

}
