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

import com.vaadin.data.Item;

import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.WorkItemFacade;
import de.decidr.model.facades.WorkflowInstanceFacade;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.permissions.HumanTaskRole;
import de.decidr.model.permissions.Role;
import de.decidr.model.soap.types.IDList;
import de.decidr.model.soap.types.TaskIdentifier;

/**
 * This is an implementation of the {@link HumanTaskInterface DecidR HumanTask
 * interface}.
 * 
 * @author Reinhold
 */
@WebService(endpointInterface = "HumanTaskInterface")
public class HumanTask implements HumanTaskInterface {
    Logger log;
    private static final Role HUMANTASK_ROLE = HumanTaskRole.getInstance();

    /**
     * Default constructor to activate logging.
     */
    public HumanTask() {
        log = DefaultLogger.getLogger(this.getClass());
    }

    @Override
    public TaskIdentifier createTask(long wfmID, long processID, long userID,
            String taskName, boolean userNotification, String description,
            String taskData) throws TransactionException {
        log.debug("Entering method: createTask");

        log.debug("creating work item in database");
        long taskID = new WorkItemFacade(HUMANTASK_ROLE).createWorkItem(userID,
                wfmID, processID + "", taskName, description, taskData
                        .getBytes(), userNotification);

        // id is needed by the ODE Engine to identify this task
        TaskIdentifier id = new TaskIdentifier(taskID, processID, userID);

        log.debug("Leaving method: createTask");
        return id;
    }

    @Override
    public void removeTask(IDList taskIDList) throws TransactionException {
        log.debug("Entering method: removeTask");
        WorkItemFacade facade = new WorkItemFacade(HUMANTASK_ROLE);
        for (long id : taskIDList.getId()) {
            log.debug("removing work item with ID: " + id);
            facade.deleteWorkItem(id);
        }
        log.debug("Leaving method: removeTask");
    }

    @Override
    public void removeTasks(long processID) throws TransactionException {
        log.debug("Entering method: removeTasks");
        List<Item> workItems = new WorkflowInstanceFacade(HUMANTASK_ROLE)
                .getAllWorkItems(processID);
        WorkItemFacade facade = new WorkItemFacade(HUMANTASK_ROLE);
        for (Item item : workItems) {
            log.debug("The ID property's original value: <"
                    + item.getItemProperty("id").getValue() + ">");
            log.debug("Getting ID and casting it to long");
            long id = (Long) item.getItemProperty("id").getValue();
            log.debug("removing work item with ID: " + id);
            facade.deleteWorkItem(id);
        }
        log.debug("Leaving method: removeTasks");
    }

    @Override
    public void taskCompleted(long taskID) throws TransactionException {
        log.debug("Entering method: taskCompleted");
        Object taskData = new WorkItemFacade(HUMANTASK_ROLE)
                .getWorkItem(taskID).getItemProperty("data").getValue();
        // TODO Auto-generated method stub
        log.debug("Leaving method: taskCompleted");
        throw new java.lang.UnsupportedOperationException("Please implement "
                + this.getClass().getName() + "#taskCompleted");
    }
}
