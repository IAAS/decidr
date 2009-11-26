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
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.namespace.QName;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.soap.exceptions.ReportingException;
import de.decidr.model.soap.types.IDList;
import de.decidr.model.workflowmodel.humantask.THumanTaskData;

/**
 * This is the DecidR HumanTask interface, a java representation of the
 * HumanTask.wsdl. It manages the web frontend based dataflow between the
 * workflow instance and its participants.
 * 
 * @author Reinhold
 */
@WebService(name = HumanTaskInterface.SERVICE_NAME, portName = HumanTaskInterface.PORT_NAME, serviceName = HumanTaskInterface.SERVICE_NAME, targetNamespace = HumanTaskInterface.TARGET_NAMESPACE, wsdlLocation = "HumanTask.wsdl")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.WRAPPED, style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
@XmlSeeAlso( { ObjectFactory.class,
        de.decidr.model.exceptions.ObjectFactory.class,
        de.decidr.model.soap.exceptions.ObjectFactory.class,
        de.decidr.model.soap.types.ObjectFactory.class,
        de.decidr.model.workflowmodel.humantask.ObjectFactory.class })
public interface HumanTaskInterface {

    public static final String SERVICE_NAME = "HumanTask";
    public static final String BINDING_NAME = "HumanTaskSOAP11";
    public static final String PORT_TYPE_NAME = "HumanTaskPT";
    public static final String PORT_NAME = "HumanTaskSOAP11";
    public static final String ENDPOINT_NAME = "HumanTaskProxyHttpSoap11Endpoint";
    public static final String TARGET_NAMESPACE = "http://decidr.de/webservices/HumanTask";
    // DH RR XXX revert once the ESB works
    public final static QName SERVICE = new QName(TARGET_NAMESPACE,
            SERVICE_NAME + "." + PORT_NAME);
    public final static QName ENDPOINT = new QName(TARGET_NAMESPACE,
            ENDPOINT_NAME);

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
     * @return The new task's ID.
     * @throws TransactionException
     *             Should a <code>{@link TransactionException}</code> be thrown
     *             by the facade.
     */
    @WebMethod(operationName = "createTask", action = "urn:createTask")
    @RequestWrapper(localName = "createTask", targetNamespace = TARGET_NAMESPACE, className = "de.decidr.model.webservices.CreateTask")
    @ResponseWrapper(localName = "createTaskResponse", targetNamespace = TARGET_NAMESPACE, className = "de.decidr.model.webservices.CreateTaskResponse")
    public Long createTask(@WebParam(name = "wfmID") long wfmID,
            @WebParam(name = "processID") String processID,
            @WebParam(name = "userID") long userID,
            @WebParam(name = "taskName") String taskName,
            @WebParam(name = "userNotification") boolean userNotification,
            @WebParam(name = "description") String description,
            @WebParam(name = "taskData") THumanTaskData taskData)
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
    @WebMethod(operationName = "removeTask", action = "urn:removeTask")
    @RequestWrapper(localName = "removeTask", targetNamespace = TARGET_NAMESPACE, className = "de.decidr.model.webservices.RemoveTask")
    @ResponseWrapper(localName = "removeTaskResponse", targetNamespace = TARGET_NAMESPACE, className = "de.decidr.model.webservices.RemoveTaskResponse")
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
    @WebMethod(operationName = "removeTasks", action = "urn:removeTasks")
    @RequestWrapper(localName = "removeTasks", targetNamespace = TARGET_NAMESPACE, className = "de.decidr.model.webservices.RemoveTasks")
    @ResponseWrapper(localName = "removeTasksResponse", targetNamespace = TARGET_NAMESPACE, className = "de.decidr.model.webservices.RemoveTasksResponse")
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
    @WebMethod(operationName = "taskCompleted", action = "urn:taskCompleted")
    @RequestWrapper(localName = "taskCompleted", targetNamespace = TARGET_NAMESPACE, className = "de.decidr.model.webservices.TaskCompleted")
    @ResponseWrapper(localName = "taskCompletedResponse", targetNamespace = TARGET_NAMESPACE, className = "de.decidr.model.webservices.TaskCompletedResponse")
    public void taskCompleted(@WebParam(name = "taskID") long taskID)
            throws TransactionException, ReportingException;
}
