/**
 * HumanTask.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */
package de.decidr.webservices.humantask;

import javax.jws.WebService;

import de.decidr.model.facades.UserFacade;
import de.decidr.model.facades.WorkItemFacade;
import de.decidr.model.soap.exceptions.InvalidUserException;
import de.decidr.model.soap.types.CreateTaskResponse;
import de.decidr.model.soap.types.IDList;
import de.decidr.model.soap.types.ItemList;

/**
 * HumanTask java skeleton for the axisService
 */
@WebService(endpointInterface = "HumanTaskInterface")
public class HumanTask implements HumanTaskInterface {
	@Override
	public CreateTaskResponse createTask(long wfmID, long processID,
			long userID, String taskName, boolean userNotification,
			String description, ItemList taskData) {
		// user validity check
		if (new UserFacade(1L).getUserProfile(userID) == null) {
			throw new InvalidUserException("User ID " + userID
					+ "doesn't exist in the system.");
		}

		CreateTaskResponse id = new CreateTaskResponse();
		id.setProcessID(processID);
		id.setUserID(userID);
		// FIXME: proper way to get a WorkItemFacade instance
		// FIXME: properly pass proper data
		// FIXME: throw DatabaseUnavailableException if necessary
		id.setTaskID(new WorkItemFacade(1L).createWorkItem(userID, wfmID,
				processID + "", taskName, description, (byte[]) null));
		return id;
	}

	@Override
	public void removeTask(IDList taskIDList) {
		// FIXME: proper way to get a WorkItemFacade instance
		// FIXME: throw DatabaseUnavailableException if necessary
		for (long id : taskIDList.getId()) {
			new WorkItemFacade(1L).deleteWorkItem(id);
		}
	}

	@Override
	public void removeTasks(long processID) {
		// FIXME: find a way to get tasks associated with
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
