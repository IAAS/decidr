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

import java.io.StringReader;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import de.decidr.model.acl.roles.HumanTaskRole;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.workitem.GetWorkItemCommand;
import de.decidr.model.entities.WorkItem;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.WorkItemFacade;
import de.decidr.model.facades.WorkflowInstanceFacade;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.soap.exceptions.ReportingException;
import de.decidr.model.soap.types.IDList;
import de.decidr.model.soap.types.TaskIdentifier;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.webservices.HumanTaskInterface;
import de.decidr.model.workflowmodel.dwdl.transformation.TransformUtil;
import de.decidr.model.workflowmodel.humantask.THumanTaskData;

/**
 * This is an implementation of the {@link HumanTaskInterface DecidR HumanTask
 * interface}.
 * 
 * @author Reinhold
 */
@WebService(endpointInterface = "de.decidr.model.webservices.HumanTaskInterface", targetNamespace = HumanTaskInterface.TARGET_NAMESPACE, portName = HumanTaskInterface.PORT_NAME, serviceName = HumanTaskInterface.SERVICE_NAME)
@HandlerChain(file = "handler-chain.xml")
public class HumanTask implements HumanTaskInterface {
    private static Logger log = DefaultLogger.getLogger(HumanTask.class);
    private static final Role HUMANTASK_ROLE = HumanTaskRole.getInstance();

    @SuppressWarnings("unchecked")
    @Override
    public TaskIdentifier createTask(long wfmID, String processID, long userID,
            String taskName, boolean userNotification, String description,
            String taskData) throws TransactionException {
        log.trace("Entering method: createTask");

        log.debug("creating work item in database");

        JAXBElement<THumanTaskData> taskDataElement;
        try {
            JAXBContext context = JAXBContext.newInstance(THumanTaskData.class);
            // XXX DH if this fails, remove .getValue() from the end of the next
            // statement. Otherwise remove this comment ~dh
            taskDataElement = (JAXBElement<THumanTaskData>) context
                    .createUnmarshaller().unmarshal(new StringReader(taskData));
        } catch (JAXBException e) {
            throw new TransactionException(e);
        }

        long taskID = new WorkItemFacade(HUMANTASK_ROLE).createWorkItem(userID,
                wfmID, processID + "", taskName, description, taskDataElement
                        .getValue(), userNotification);

        // id is needed by the ODE Engine to identify this task
        // XXX I've talked to modood, he only needs the taskID to identify the
        // work item, so theTaskIdentifier might be reduced to a Long, soon ~dh
        // DH The process ID is needed to select the correct ODE instance. The
        // userID is needed to associate the task with a user in a foreach ~rr
        TaskIdentifier id = new TaskIdentifier(taskID, processID, userID);

        log.trace("Leaving method: createTask");
        return id;
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
        log.debug("getting data associated with task");
        GetWorkItemCommand cmd = new GetWorkItemCommand(HUMANTASK_ROLE, taskID);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        WorkItem workItem = cmd.getResult();

        // XXX see line 85 ~dh
        TaskIdentifier id = new TaskIdentifier(taskID, workItem
                .getWorkflowInstance().getOdePid(), workItem.getUser().getId());

        try {
            log.debug("attempting to parse the data string into an Object");
            THumanTaskData taskData = TransformUtil.bytesToHumanTask(workItem
                    .getData());

            /*
             * DH RR MA Modood is going to create a new type that replaces
             * ItemList. We have to map from THumanTaskData to this new type ~dh
             */
            // log.debug("calling Callback");
            // new BasicProcessClient().getBPELCallbackInterfacePort()
            // .taskCompleted(id, ???);
            // } catch (MalformedURLException e) {
            // throw new ReportingException(e.getMessage(), e);
        } catch (JAXBException e) {
            throw new ReportingException(e.getMessage(), e);
        }

        log.trace("Leaving method: taskCompleted");
        throw new java.lang.UnsupportedOperationException("Please implement "
                + this.getClass().getName() + "#taskCompleted");
    }
}
