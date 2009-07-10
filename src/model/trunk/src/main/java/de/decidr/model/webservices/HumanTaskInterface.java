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
package de.decidr.model.webservices;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.namespace.QName;

import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.soap.exceptions.ReportingException;
import de.decidr.model.soap.types.IDList;
import de.decidr.model.soap.types.TaskIdentifier;

/**
 * This is the DecidR HumanTask interface, a java representation of the
 * HumanTask.wsdl. It manages the web frontend based dataflow between the
 * workflow instance and its participants.
 * 
 * @author Reinhold
 */
@WebService(name = HumanTaskInterface.PORT_TYPE_NAME, portName = "HumanTaskSOAP", serviceName = "HumanTask", targetNamespace = HumanTaskInterface.TARGET_NAMESPACE, wsdlLocation = "HumanTask.wsdl")
public interface HumanTaskInterface {

    public static final String SERVICE_NAME = "HumanTask";
    public static final String PORT_TYPE_NAME = "HumanTaskPT";
    public static final String TARGET_NAMESPACE = "http://decidr.de/webservices/HumanTask";
    public final static QName SERVICE = new QName(TARGET_NAMESPACE,
            SERVICE_NAME);
    public final static QName ENDPOINT = new QName(TARGET_NAMESPACE,
            "HumanTaskSOAP");

    /**
     * This method creates an entry in the database representing a work item to
     * be processed by a specified user.
     * 
     * @param wfmID
     *            The workflow model ID for associating a work item with a
     *            specific workflow model.
     * @param processID
     *            The process ID identifies the workflow instance responsible
     *            for the work item.
     * @param userID
     *            The user ID identifies the user who should process the work
     *            item.
     * @param taskName
     *            The name of the work item.
     * @param userNotification
     *            Whether the user should be notified that a new work item is
     *            ready to be processed.
     * @param description
     *            A quick description of the work item.
     * @param taskData
     *            A list of fields and default values associated with the work
     *            item needed by the UI to properly render it.
     * @return A {@link TaskIdentifier} as required by the BEPL process.
     * @throws TransactionException
     *             Should a <code>{@link TransactionException}</code> be thrown
     *             by the facade.
     */
    @WebMethod(operationName = "createTask", action = TARGET_NAMESPACE
            + "/createTask")
    public TaskIdentifier createTask(@WebParam(name = "wfmID") long wfmID,
            @WebParam(name = "processID") String processID,
            @WebParam(name = "userID") long userID,
            @WebParam(name = "taskName") String taskName,
            @WebParam(name = "userNotification") boolean userNotification,
            @WebParam(name = "description") String description,
            @WebParam(name = "taskData") String taskData)
            throws TransactionException;

    /**
     * Removes a list of tasks from the database.
     * 
     * @param taskIDList
     *            A list of tasks identified by their task ID.
     * @throws TransactionException
     *             Should a <code>{@link TransactionException}</code> be thrown
     *             by the facade.
     */
    @WebMethod(operationName = "removeTask", action = TARGET_NAMESPACE
            + "/removeTask")
    public void removeTask(@WebParam(name = "taskIDList") IDList taskIDList)
            throws TransactionException;

    /**
     * Removes all tasks belonging to a specified workflow instance.
     * 
     * @param wfmID
     *            The workflow model ID for associating a work item with a
     *            specific workflow model.
     * @param processID
     *            The process ID identifies the workflow instance responsible
     *            for the work item.
     * @throws TransactionException
     *             Should a <code>{@link TransactionException}</code> be thrown
     *             by the facade.
     */
    @WebMethod(operationName = "removeTasks", action = TARGET_NAMESPACE
            + "/removeTasks")
    public void removeTasks(@WebParam(name = "wfmID") long wfmID,
            @WebParam(name = "processID") String processID)
            throws TransactionException;

    /**
     * Notify the BEPL web service that a work item has been completed and pass
     * it the associated data.
     * 
     * @param taskID
     *            An ID identifying the completed work item.
     * @throws TransactionException
     *             Should a <code>{@link TransactionException}</code> be thrown
     *             by the facade.
     */
    @WebMethod(operationName = "taskCompleted", action = TARGET_NAMESPACE
            + "/taskCompleted")
    public void taskCompleted(@WebParam(name = "taskID") long taskID)
            throws TransactionException, ReportingException;
}
