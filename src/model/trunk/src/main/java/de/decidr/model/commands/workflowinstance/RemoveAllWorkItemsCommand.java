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

import org.hibernate.Query;

import de.decidr.model.acl.access.DeployedworkflowModelAccess;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Deletes all work items of the given workflow instance.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class RemoveAllWorkItemsCommand extends WorkflowInstanceCommand implements DeployedworkflowModelAccess{

    private String odePid;
    private Long deployedWorkflowModelId;

    /**
     * 
     * Creates a new RemoveAllWorkItemsCommand. This command deletes all work
     * items of the given workflow instance.
     * 
     * @param role
     * @param odePid
     * @param deployedWorkflowModelId
     */
    public RemoveAllWorkItemsCommand(Role role, String odePid,
            Long deployedWorkflowModelId) {
        super(role, null,null);

        this.odePid = odePid;
        this.deployedWorkflowModelId = deployedWorkflowModelId;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        Query q = evt
                .getSession()
                .createQuery(
                        "DELETE from WorkItem item WHERE item.workflowinstance.odePid = :pid AND item.workflowinstance.deployedWorkflowModel.id = :wid");

        q.setString("pid", odePid);
        q.setLong("wid", deployedWorkflowModelId);

        q.executeUpdate();

    }

    @Override
    public Long getDeployedWorkflowModelId() {
        return deployedWorkflowModelId;
    }

    @Override
    public Long[] getDeployedWorkflowModelIds() {
        Long[] result = {deployedWorkflowModelId};
        return result;
    }
}
