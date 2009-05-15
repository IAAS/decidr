/**
 * HumanTaskInterface.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */
package de.decidr.webservices.humantask;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import de.decidr.schema.decidrtypes.TIDList;
import de.decidr.schema.decidrtypes.TItemList;

/**
 * HumanTaskInterface java skeleton interface for the axisService
 */
@WebService(name = "HumanTaskPT", portName = "HumanTaskSOAP", serviceName = "HumanTask", targetNamespace = "http://decidr.de/webservices/HumanTask", wsdlLocation = "HumanTask.wsdl")
public interface HumanTaskInterface {

	/**
	 * Auto generated method signature
	 * 
	 * @param removeTask
	 */
	@WebMethod(operationName = "removeTask")
	public void removeTask(@WebParam(name = "taskIDList") TIDList taskIDList);

	/**
	 * Auto generated method signature
	 * 
	 * @param taskCompleted
	 */
	@WebMethod(operationName = "taskCompleted")
	public void taskCompleted(@WebParam(name = "taskID") long taskID);

	/**
	 * Auto generated method signature
	 * 
	 * @param createTask
	 */
	@WebMethod(operationName = "createTask")
	public CreateTaskResponse createTask(@WebParam(name = "wfmID") long wfmID,
			@WebParam(name = "wfmID") long processID,
			@WebParam(name = "wfmID") long userID,
			@WebParam(name = "taskName") String taskName,
			@WebParam(name = "userNotification") boolean userNotification,
			@WebParam(name = "description") String description,
			@WebParam(name = "taskData") TItemList taskData);

	/**
	 * Auto generated method signature
	 * 
	 * @param removeTasks
	 */
	@WebMethod(operationName = "removeTasks")
	public void removeTasks(@WebParam(name = "processID") long processID);
}
