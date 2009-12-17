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

package de.decidr.ui.controller.show;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;

import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.entities.WorkItem;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.WorkItemFacade;
import de.decidr.model.workflowmodel.dwdl.transformation.TransformUtil;
import de.decidr.model.workflowmodel.humantask.THumanTaskData;
import de.decidr.ui.beans.WorkItemSummaryViewBean;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;
import de.decidr.ui.view.windows.WorkItemWindow;

/**
 * This action opens a window for representing the work item the user has chosen
 * to work on.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2349", currentReviewState = State.Passed)
public class ShowWorkItemWindowAction implements ClickListener {

	private static final long serialVersionUID = 1L;

	private HttpSession session = Main.getCurrent().getSession();

	private Role role = (Role) session.getAttribute("role");

	private WorkItemFacade workItemFacade = new WorkItemFacade(role);

	private Table table = null;

	private Long workItemId = null;

	private byte[] ht = null;

	private THumanTaskData tHumanTaskData = null;

	/**
	 * Stores the given work item table
	 */
	public ShowWorkItemWindowAction(Table table) {
		this.table = table;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
	 * ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		Set<?> value = (Set<?>) table.getValue();
		if ((value != null) && (value.size() == 1)) {
			for (Iterator<?> iter = value.iterator(); iter.hasNext();) {
				WorkItemSummaryViewBean workItemSummaryViewBean = (WorkItemSummaryViewBean) iter.next();
				workItemId = workItemSummaryViewBean.getId();
				try {
					WorkItem workItem = workItemFacade
							.getWorkItemAndMarkAsInProgress(workItemId);
					ht = workItem.getData();
					tHumanTaskData = TransformUtil.bytesToHumanTask(ht);

					Main.getCurrent().getMainWindow().addWindow(
							new WorkItemWindow(tHumanTaskData, workItemId));
					table.requestRepaint();
				} catch (TransactionException exception) {
					Main.getCurrent().getMainWindow().addWindow(
							new TransactionErrorDialogComponent(exception));
				} catch (JAXBException exception) {
					Main.getCurrent().getMainWindow().showNotification(
							"JAXB error!");
				}
			}

		} else {
			Main.getCurrent().getMainWindow().addWindow(
					new InformationDialogComponent(
							"Please select a work item!", "Information"));
		}
	}
}
