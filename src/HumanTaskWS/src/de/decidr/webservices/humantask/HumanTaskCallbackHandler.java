/**
 * HumanTaskCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */

package de.decidr.webservices.humantask;

/**
 * HumanTaskCallbackHandler Callback class, Users can extend this class and
 * implement their own receiveResult and receiveError methods.
 */
public abstract class HumanTaskCallbackHandler {

	protected Object clientData;

	/**
	 * User can pass in any object that needs to be accessed once the
	 * NonBlocking Web service call is finished and appropriate method of this
	 * CallBack is called.
	 * 
	 * @param clientData
	 *            Object mechanism by which the user can pass in user data that
	 *            will be avilable at the time this callback is called.
	 */
	public HumanTaskCallbackHandler(Object clientData) {
		this.clientData = clientData;
	}

	/**
	 * Please use this constructor if you don't want to set any clientData
	 */
	public HumanTaskCallbackHandler() {
		this.clientData = null;
	}

	/**
	 * Get the client data
	 */

	public Object getClientData() {
		return clientData;
	}

	/**
	 * auto generated Axis2 call back method for removeTask method override this
	 * method for handling normal response from removeTask operation
	 */
	public void receiveResultremoveTask(
			de.decidr.webservices.humantask.RemoveTaskResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from removeTask operation
	 */
	public void receiveErrorremoveTask(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for taskCompleted method override
	 * this method for handling normal response from taskCompleted operation
	 */
	public void receiveResulttaskCompleted(
			de.decidr.webservices.humantask.TaskCompletedResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from taskCompleted operation
	 */
	public void receiveErrortaskCompleted(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for createTask method override this
	 * method for handling normal response from createTask operation
	 */
	public void receiveResultcreateTask(
			de.decidr.webservices.humantask.CreateTaskResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from createTask operation
	 */
	public void receiveErrorcreateTask(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for removeTasks method override
	 * this method for handling normal response from removeTasks operation
	 */
	public void receiveResultremoveTasks(
			de.decidr.webservices.humantask.RemoveTasksResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from removeTasks operation
	 */
	public void receiveErrorremoveTasks(java.lang.Exception e) {
	}

}
