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

import java.util.Set;

import javax.xml.bind.JAXBException;

import org.hibernate.Query;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.XmlTools;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.commands.file.AssociateFileWithWorkItemCommand;
import de.decidr.model.entities.User;
import de.decidr.model.entities.WorkItem;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.enums.WorkItemStatus;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.notifications.NotificationEvents;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionEvent;
import de.decidr.model.workflowmodel.dwdl.transformation.TransformUtil;
import de.decidr.model.workflowmodel.humantask.THumanTaskData;

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
    private THumanTaskData data;
    private Boolean notifyUser;

    private Long workItemId = null;

    /**
     * Creates a new instance of the CreateWorkItemCommand.
     * 
     * @param role
     *            user which executes the command
     * @param userId
     *            the ID of the user which owns the new workitem
     * @param deployedWorkflowModelId
     *            the ID of the WorkflowModel
     * @param odePid
     *            the Process ID of the instance at the ODE
     * @param name
     *            the name of the new workitem
     * @param description
     *            the description of the workitem (what has the user to do)
     * @param data
     *            the workitem data
     * @param notifyUser
     *            true if user should be notified that he has a new workitem,
     *            else false
     * @throws IllegalArgumentException
     *             if any parameter is <code>null</code>.
     */
    public CreateWorkItemCommand(Role role, Long userId,
            Long deployedWorkflowModelId, String odePid, String name,
            String description, THumanTaskData data, Boolean notifyUser) {
        super(role, (Permission) null);

        if (userId == null || deployedWorkflowModelId == null || odePid == null
                || name == null || description == null || data == null
                || notifyUser == null) {
            throw new IllegalArgumentException("No parameter can be null.");
        }

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
        q.setString("odePid", odePid);
        q.setLong("deployedWorkflowModelId", deployedWorkflowModelId);

        WorkflowInstance owningInstance = (WorkflowInstance) q.uniqueResult();
        if (owningInstance == null) {
            throw new EntityNotFoundException(WorkflowInstance.class, "PID: "
                    + odePid + ", DWFMID: " + deployedWorkflowModelId);
        }

        User owningUser = (User) evt.getSession().get(User.class, userId);
        if (owningUser == null) {
            throw new EntityNotFoundException(User.class, userId);
        }

        WorkItem newWorkItem = new WorkItem();
        newWorkItem.setCreationDate(DecidrGlobals.getTime().getTime());
        try {
            newWorkItem.setData(TransformUtil.humanTaskToByte(data));
        } catch (JAXBException e) {
            throw new TransactionException(e);
        }
        newWorkItem.setDescription(description);
        newWorkItem.setName(name);
        newWorkItem.setStatus(WorkItemStatus.Fresh.toString());
        newWorkItem.setUser(owningUser);
        newWorkItem.setWorkflowInstance(owningInstance);

        evt.getSession().save(newWorkItem);

        /*
         * Now that we have saved the work item, we must persist and associate
         * any files that the HumanTaskData references.
         */
        persistAndAssociateFiles(newWorkItem);

        workItemId = newWorkItem.getId();

        if (notifyUser) {
            NotificationEvents.createdWorkItem(newWorkItem);
        }
    }

    /**
     * @param newWorkItem
     *            newly created workitem to associate files with.
     * @throws TransactionException
     *             if the transaction is aborted for any reason.
     */
    private void persistAndAssociateFiles(WorkItem newWorkItem)
            throws TransactionException {
        Set<Long> fileIds = XmlTools.getFileIds(data);
        for (Long fileId : fileIds) {
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    new AssociateFileWithWorkItemCommand(role, fileId,
                            newWorkItem.getId()));
        }
    }

    /**
     * @return the id of the new work item.
     */
    public Long getWorkItemId() {
        return workItemId;
    }
}
