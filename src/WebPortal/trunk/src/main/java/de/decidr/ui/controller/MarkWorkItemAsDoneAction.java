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

package de.decidr.ui.controller;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.vaadin.data.Item;
import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.WorkItemFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * Marks a selected work item as done.
 * 
 * @author AT
 * @reviewed ~tk, ~dh
 */
public class MarkWorkItemAsDoneAction implements ClickListener {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;

	private HttpSession session = Main.getCurrent().getSession();

	private Role role = (Role) session.getAttribute("role");
	private WorkItemFacade workItemFacade = new WorkItemFacade(role);

	private Table table = null;

	/**
	 * Constructor which gets a work item id as a parameter to know which work
	 * item is to be marked done. AT description incorrect, there is no id
	 * parameter ~dh, ~tk
	 */
	public MarkWorkItemAsDoneAction(Table table) {
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
		// AT maximum line width exceeded ~dh, ~tk
		Set<?> value = (Set<?>) table.getValue();
		if ((value != null) && (value.size() != 0)) {
			for (Iterator<?> iter = value.iterator(); iter.hasNext();) {
				Item item = (Item) iter.next();
				Long workItemId = (Long) item.getItemProperty("id").getValue();
				// AT use enumeration instead of magic string ~dh, ~tk
				if (item.getItemProperty("workItemStatus").getValue()
						.toString().equals("Done")) {
					Main.getCurrent().getMainWindow().addWindow(
							new InformationDialogComponent(
									"The selected work item is already done",
									"Information"));
				} else {
					try {
						workItemFacade.markWorkItemAsDone(workItemId);
						item.getItemProperty("workItemStatus").setValue(
								workItemFacade.getWorkItem(workItemId)
										.getItemProperty("status").getValue());
						table.requestRepaint();
						Main.getCurrent().getMainWindow().addWindow(
								new InformationDialogComponent(
										"Marked as done", "Success"));
					} catch (TransactionException e) {
						Main.getCurrent().getMainWindow().addWindow(
								new TransactionErrorDialogComponent(e));
					}
				}
			}
		} else {
			Main.getCurrent().getMainWindow().addWindow(
					new InformationDialogComponent("Please select an item",
							"Information"));
		}
	}
}
