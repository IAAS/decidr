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

package de.decidr.model.commands.workitem;

import org.hibernate.Query;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.User;
import de.decidr.model.entities.WorkItem;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.enums.WorkItemStatus;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.notifications.NotificationEvents;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Creates a new work item with the given specs in the database and notifies the
 * user that a new work item has been created.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class CreateWorkItemCommand extends AclEnabledCommand {

    private Long userId;
    private Long deployedWorkflowModelId;
    private String odePid;
    private String name;
    private String description;
    private byte[] data;
    private Boolean notifyUser;

    private Long workItemId = null;

    /**
     * Constructor TODO document
     * 
     * @param role
     *            TODO document
     * @param userId
     *            TODO document
     * @param deployedWorkflowModelId
     *            TODO document
     * @param odePid
     *            TODO document
     * @param name
     *            TODO document
     * @param description
     *            TODO document
     * @param data
     *            TODO document
     * @param notifyUser
     *            TODO document
     * 
     */
    public CreateWorkItemCommand(Role role, Long userId,
            Long deployedWorkflowModelId, String odePid, String name,
            String description, byte[] data, Boolean notifyUser) {
        super(role, (Permission) null);
        this.userId = userId;
        this.deployedWorkflowModelId = deployedWorkflowModelId;
        this.odePid = odePid;
        this.name = name;
        this.description = description;
        this.data = data;
        this.notifyUser = notifyUser;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        /*
         * A workflow instance is also uniquely identified by its deployed
         * workflow model and the ode process id. We're using this fact here to
         * find the workflow instance that "owns" the new work item.
         */
        String hql = "from WorkflowInstance w where w.odePid = :odePid "
                + "and w.deployedWorkflowModel.id = :deployedWorkflowModelId";

        Query q = evt.getSession().createQuery(hql);
        q.setString("odePid", odePid).setLong(":deployedWorkflowModelId",
                deployedWorkflowModelId);

        WorkflowInstance owningInstance = (WorkflowInstance) q.uniqueResult();
        User owningUser = new User();
        owningUser.setId(userId);

        WorkItem newWorkItem = new WorkItem();
        newWorkItem.setCreationDate(DecidrGlobals.getTime().getTime());
        newWorkItem.setData(data);
        newWorkItem.setDescription(description);
        newWorkItem.setName(name);
        newWorkItem.setStatus(WorkItemStatus.Fresh.toString());
        newWorkItem.setUser(owningUser);
        newWorkItem.setWorkflowInstance(owningInstance);

        evt.getSession().save(newWorkItem);

        workItemId = newWorkItem.getId();

        if (notifyUser) {
            NotificationEvents.createdWorkItem(newWorkItem);
        }
    }

    /**
     * @return the id of the new work item.
     */
    public Long getWorkItemId() {
        return workItemId;
    }
}
