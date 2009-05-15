/**
 * HumanTask.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */
package de.decidr.webservices.humantask;

import javax.jws.WebService;

import de.decidr.schema.decidrtypes.TIDList;
import de.decidr.schema.decidrtypes.TItemList;

/**
 * HumanTask java skeleton for the axisService
 */
@WebService(endpointInterface = "HumanTaskInterface")
public class HumanTask implements HumanTaskInterface {
	@Override
	public CreateTaskResponse createTask(long wfmID, long processID,
			long userID, String taskName, boolean userNotification,
			String description, TItemList taskData) {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException("Please implement "
				+ this.getClass().getName() + "#createTask");
	}

	@Override
	public void removeTask(TIDList taskIDList) {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException("Please implement "
				+ this.getClass().getName() + "#removeTask");
	}

	@Override
	public void removeTasks(long processID) {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException("Please implement "
				+ this.getClass().getName() + "#removeTasks");
	}

	@Override
	public void taskCompleted(long taskID) {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException("Please implement "
				+ this.getClass().getName() + "#taskCompleted");
	}
}
