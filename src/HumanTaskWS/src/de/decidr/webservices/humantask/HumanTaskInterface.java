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

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import de.decidr.model.soap.types.TaskIdentifier;
import de.decidr.model.soap.types.IDList;
import de.decidr.model.soap.types.ItemList;

/**
 * This is the DecidR HumanTask interface, a java representation of the
 * HumanTask.wsdl. It manages the web frontend based dataflow between the
 * workflow instance and its participants.
 * 
 * @author RR
 */
@WebService(name = "HumanTaskPT", portName = "HumanTaskSOAP", serviceName = "HumanTask", targetNamespace = "http://decidr.de/webservices/HumanTask", wsdlLocation = "HumanTask.wsdl")
public interface HumanTaskInterface {

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
	 */
	@WebMethod(operationName = "createTask")
	public TaskIdentifier createTask(@WebParam(name = "wfmID") long wfmID,
			@WebParam(name = "processID") long processID,
			@WebParam(name = "userID") long userID,
			@WebParam(name = "taskName") String taskName,
			@WebParam(name = "userNotification") boolean userNotification,
			@WebParam(name = "description") String description,
			@WebParam(name = "taskData") ItemList taskData);

	/**
	 * Removes a list of tasks from the database.
	 * 
	 * @param taskIDList
	 *            A list of tasks identified by their task ID.
	 */
	@WebMethod(operationName = "removeTask")
	public void removeTask(@WebParam(name = "taskIDList") IDList taskIDList);

	/**
	 * Removes all tasks belonging to a specified workflow instance.
	 * 
	 * @param processID
	 *            A workflow instance identifier.
	 */
	@WebMethod(operationName = "removeTasks")
	public void removeTasks(@WebParam(name = "processID") long processID);

	/**
	 * Notify the BEPL web service that a work item has been completed and pass
	 * it the associated data.
	 * 
	 * @param taskID
	 *            An ID identifying the completed work item.
	 */
	@WebMethod(operationName = "taskCompleted")
	public void taskCompleted(@WebParam(name = "taskID") long taskID);
}
