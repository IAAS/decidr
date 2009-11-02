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

import de.decidr.model.acl.access.WorkflowModelAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Makes one or more workflow models available to the public. Other tenants will
 * be able to import the given workflow models. This command can also be used to
 * un-publish a workflow model.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class PublishWorkflowModelsCommand extends AclEnabledCommand implements
        TransactionalCommand, WorkflowModelAccess {

    private List<Long> workflowModelIds = null;
    private Boolean publish;

    /**
     * Constructor.
     * 
     * @param role
     * @param workflowModelIds
     */
    public PublishWorkflowModelsCommand(Role role, List<Long> workflowModelIds,
            Boolean publish) {
        super(role, (Permission) null);

        this.workflowModelIds = workflowModelIds;
        this.publish = publish;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        if (workflowModelIds != null) {
            for (Long id : workflowModelIds) {
                WorkflowModel workflowModel = (WorkflowModel) evt.getSession()
                        .get(WorkflowModel.class, id);

                if (workflowModel != null) {
                    workflowModel.setPublished(publish);
                    evt.getSession().update(workflowModel);
                }
            }
        }
    }

    @Override
    public Long[] getWorkflowModelIds() {
        Long[] result = new Long[0];
        return workflowModelIds.toArray(result);
    }

}
