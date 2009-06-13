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

import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.exceptions.WorkflowModelNotStartableException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionAbortedEvent;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Creates a new workflow instance on the Apache ODE and writes a corresponding
 * entry to the database.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class StartWorkflowInstanceCommand extends WorkflowModelCommand {

    private byte[] startConfiguration;

    private WorkflowInstance newWorkflowInstance;

    /**
     * Constructor.
     * <p>
     * This command creates a new workflow instance on the Apache ODE and writes
     * a corresponding entry to the database.
     * 
     * @param role
     * @param workflowModelId
     * @param startConfiguration
     */
    public StartWorkflowInstanceCommand(Role role, Long workflowModelId,
            byte[] startConfiguration) {
        super(role, workflowModelId);
        this.startConfiguration = startConfiguration;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException, WorkflowModelNotStartableException {
        /*
         * FIXME use ODE communication component written by Modood Alvi
         */
        throw new WorkflowModelNotStartableException(getWorkflowModelId());
    }

    @Override
    public void transactionAborted(TransactionAbortedEvent evt)
            throws TransactionException {
        /*
         * TODO (low priority?) add compensation action (kill any created
         * workflow instance) on rollback
         */
    }

    /**
     * @return the new workflow instance
     */
    public WorkflowInstance getNewWorkflowInstance() {
        return newWorkflowInstance;
    }

}
