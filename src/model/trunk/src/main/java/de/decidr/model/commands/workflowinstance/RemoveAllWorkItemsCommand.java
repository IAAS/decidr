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
package de.decidr.model.commands.workflowinstance;

import de.decidr.model.acl.access.DeployedworkflowModelAccess;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Deletes all work items of the given workflow instance.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class RemoveAllWorkItemsCommand extends WorkflowInstanceCommand
        implements DeployedworkflowModelAccess {

    private String odePid;
    private Long deployedWorkflowModelId;

    /**
     * 
     * Creates a new RemoveAllWorkItemsCommand. This command deletes all work
     * items of the given workflow instance.
     * 
     * @param role
     *            user/system executing the command
     * @param odePid
     *            workflow instance process ID.
     * @param deployedWorkflowModelId
     *            ID of corresponding deployed workflow model.
     */
    public RemoveAllWorkItemsCommand(Role role, String odePid,
            Long deployedWorkflowModelId) {
        super(role, null, null);

        if ((odePid == null) || (deployedWorkflowModelId == null)) {
            throw new IllegalArgumentException(
                    "ODE PID and deployed workflow model ID must not be null.");
        }

        this.odePid = odePid;
        this.deployedWorkflowModelId = deployedWorkflowModelId;
    }

    @Override
    public Long getDeployedWorkflowModelId() {
        return deployedWorkflowModelId;
    }

    @Override
    public Long[] getDeployedWorkflowModelIds() {
        Long[] result = { deployedWorkflowModelId };
        return result;
    }

    @Override
    public void transactionAllowed(TransactionStartedEvent evt)
            throws TransactionException {
        evt.getSession().createQuery(
                "delete from WorkItem item "
                        + "where exists(from WorkflowInstance wi"
                        + " where item.workflowInstance.id = wi.id"
                        + " and wi.odePid = :pid "
                        + "and wi.deployedWorkflowModel.id = :wid)").setString(
                "pid", odePid).setLong("wid", deployedWorkflowModelId)
                .executeUpdate();
    }
}
