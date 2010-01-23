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
package de.decidr.webservices.humantask;

import java.util.List;

import javax.jws.WebService;

import org.apache.log4j.Logger;

import de.decidr.model.acl.roles.HumanTaskRole;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.commands.workitem.SetStatusCommand;
import de.decidr.model.entities.WorkItem;
import de.decidr.model.enums.WorkItemStatus;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.WorkItemFacade;
import de.decidr.model.facades.WorkflowInstanceFacade;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.soap.exceptions.ReportingException;
import de.decidr.model.soap.types.IDList;
import de.decidr.model.soap.types.ReducedHumanTaskData;
import de.decidr.model.soap.types.TaskDataItem;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionStartedEvent;
import de.decidr.model.webservices.HumanTaskInterface;
import de.decidr.model.workflowmodel.dwdl.transformation.TransformUtil;
import de.decidr.model.workflowmodel.humantask.THumanTaskData;
import de.decidr.model.workflowmodel.humantask.TInformation;
import de.decidr.model.workflowmodel.humantask.TTaskItem;

/**
 * This is an implementation of the {@link HumanTaskInterface DecidR HumanTask
 * interface}.
 * 
 * @author Reinhold
 */
@WebService(endpointInterface = "de.decidr.model.webservices.HumanTaskInterface", targetNamespace = HumanTaskInterface.TARGET_NAMESPACE, portName = HumanTaskInterface.PORT_NAME, serviceName = HumanTaskInterface.SERVICE_NAME)
// @HandlerChain(file = "handler-chain.xml")
public class HumanTask implements HumanTaskInterface {
    /**
     * Invokes the callback of the BPEL process and sets the workitem status to
     * "done".
     */
    static class TaskCompletedCommand extends AbstractTransactionalCommand {
        private Long taskID;

        /**
         * Creates a new {@link TaskCompletedCommand} that invokes the callback
         * of the BPEL process and sets the workitem status to "done".
         * 
         * @param taskID
         *            task identifier (= workitem ID)
         */
        public TaskCompletedCommand(Long taskID) {
            super();
            this.taskID = taskID;
        }

        /**
         * Translates regular human task data into a reduced form that doesn't
         * contain labels or hints.
         * 
         * @param taskData
         *            human task data
         * @return reduced human task data
         */
        private ReducedHumanTaskData getReducedHumanTaskData(
                THumanTaskData taskData) {
            ReducedHumanTaskData result = new ReducedHumanTaskData();
            List<TaskDataItem> dataList = result.getDataItem();
            for (Object object : taskData.getTaskItemOrInformation()) {
                if (object instanceof TInformation) {
                    continue;
                }
                TTaskItem task = (TTaskItem) object;
                TaskDataItem item = new TaskDataItem();
                item.setName(task.getName());
                item.setType(task.getType().value());
                item.setValue(task.getValue());
                dataList.add(item);
            }
            return result;
        }

        @Override
        public void transactionStarted(TransactionStartedEvent evt)
                throws TransactionException {
            log.debug("getting data associated with the task");
            WorkItem workItem = (WorkItem) evt.getSession().get(WorkItem.class,
                    taskID);
            if (workItem == null) {
                throw new EntityNotFoundException(WorkItem.class, taskID);
            }

            try {
                log.debug("attempting to parse the data string into an Object");
                ReducedHumanTaskData data = getReducedHumanTaskData(TransformUtil
                        .bytesToHumanTask(workItem.getData()));
                log.debug("calling Callback");
                Long deployedWorkflowModelId = workItem.getWorkflowInstance()
                        .getDeployedWorkflowModel().getId();
                new BasicProcessClient(deployedWorkflowModelId)
                        .getBPELCallbackInterfacePort().taskCompleted(taskID,
                                data);
            } catch (Exception e) {
                throw new TransactionException(e.getMessage(), e);
            }

            // Set status to "done"
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    new SetStatusCommand(HUMANTASK_ROLE, taskID,
                            WorkItemStatus.Done));
        }
    }

    private static Logger log = DefaultLogger.getLogger(HumanTask.class);

    private static final Role HUMANTASK_ROLE = HumanTaskRole.getInstance();

    @Override
    public Long createTask(long wfmID, String processID, long userID,
            String taskName, boolean userNotification, String description,
            THumanTaskData taskData) throws TransactionException {
        log.trace("Entering method: createTask");
        log.debug("wfmID=" + wfmID + ", processID=" + processID + ", userID="
                + userID + ", taskName=" + taskName + ", userNotification="
                + userNotification + ", description=" + description
                + ", taskData=" + taskData);

        log.debug("creating work item in database");
        long taskID = new WorkItemFacade(HUMANTASK_ROLE).createWorkItem(userID,
                wfmID, processID, taskName, description, taskData,
                userNotification);

        log.trace("Leaving method: createTask");
        return taskID;
    }

    @Override
    public void removeTask(IDList taskIDList) throws TransactionException {
        log.trace("Entering method: removeTask");
        WorkItemFacade facade = new WorkItemFacade(HUMANTASK_ROLE);
        for (long id : taskIDList.getId()) {
            log.debug("removing work item with ID: " + id);
            facade.deleteWorkItem(id);
        }
        log.trace("Leaving method: removeTask");
    }

    @Override
    public void removeTasks(long wfmID, String processID)
            throws TransactionException {
        log.trace("Entering method: removeTasks");
        log.debug("calling WorkflowInstanceFacade to remove the items");
        new WorkflowInstanceFacade(HUMANTASK_ROLE).removeAllWorkItems(
                processID, wfmID);
        log.trace("Leaving method: removeTasks");
    }

    @Override
    public void taskCompleted(long taskID) throws TransactionException,
            ReportingException {
        log.trace("Entering method: taskCompleted");

        try {
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    new TaskCompletedCommand(taskID));
        } catch (Exception e) {
            throw new ReportingException(e.getMessage(), e);
        }

        log.trace("Leaving method: taskCompleted");
    }
}
