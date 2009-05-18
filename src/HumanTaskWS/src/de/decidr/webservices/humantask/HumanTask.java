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

import javax.jws.WebService;

import de.decidr.model.facades.UserFacade;
import de.decidr.model.facades.WorkItemFacade;
import de.decidr.model.soap.exceptions.InvalidUserException;
import de.decidr.model.soap.types.TaskIdentifier;
import de.decidr.model.soap.types.IDList;
import de.decidr.model.soap.types.ItemList;

/**
 * This is an implementation of the {@link HumanTaskInterface DecidR HumanTask
 * interface}.
 * 
 * @author RR
 */
@WebService(endpointInterface = "HumanTaskInterface")
public class HumanTask implements HumanTaskInterface {
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.decidr.webservices.humantask.HumanTaskInterface#createTask(long,
	 * long, long, java.lang.String, boolean, java.lang.String,
	 * de.decidr.model.soap.types.ItemList)
	 */
	@Override
	public TaskIdentifier createTask(long wfmID, long processID,
			long userID, String taskName, boolean userNotification,
			String description, ItemList taskData) {
		// user validity check
		if (new UserFacade(1L).getUserProfile(userID) == null) {
			throw new InvalidUserException("User ID " + userID
					+ "doesn't exist in the system.");
		}

		TaskIdentifier id = new TaskIdentifier();
		id.setProcessID(processID);
		id.setUserID(userID);
		// FIXME: proper way to get a WorkItemFacade instance
		// FIXME: properly pass proper data
		// FIXME: throw DatabaseUnavailableException if necessary
		id.setTaskID(new WorkItemFacade(1L).createWorkItem(userID, wfmID,
				processID + "", taskName, description, (byte[]) null));

		if (userNotification) {
			// FIXME: somehow notify the user
		}

		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.decidr.webservices.humantask.HumanTaskInterface#removeTask(de.decidr
	 * .model.soap.types.IDList)
	 */
	@Override
	public void removeTask(IDList taskIDList) {
		// FIXME: proper way to get a WorkItemFacade instance
		// FIXME: throw DatabaseUnavailableException if necessary
		for (long id : taskIDList.getId()) {
			new WorkItemFacade(1L).deleteWorkItem(id);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.decidr.webservices.humantask.HumanTaskInterface#removeTasks(long)
	 */
	@Override
	public void removeTasks(long processID) {
		// FIXME: find a way to get tasks associated with
		throw new java.lang.UnsupportedOperationException("Please implement "
				+ this.getClass().getName() + "#removeTasks");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.decidr.webservices.humantask.HumanTaskInterface#taskCompleted(long)
	 */
	@Override
	public void taskCompleted(long taskID) {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException("Please implement "
				+ this.getClass().getName() + "#taskCompleted");
	}
}
