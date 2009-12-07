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

import javax.servlet.http.HttpSession;

import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.WorkflowInstanceFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This action terminates a workflow instance
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class TerminateWorkflowInstanceAction implements ClickListener {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;

	private HttpSession session = Main.getCurrent().getSession();

	private Role role = (Role) session.getAttribute("role");
	private WorkflowInstanceFacade wfiFacade = new WorkflowInstanceFacade(
			role);

	private Table table = null;

	/**
	 * Constructor, requires id of the instance to be terminated
	 * 
	 * @param id
	 */
	public TerminateWorkflowInstanceAction(Table table) {
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
		if (table.getValue() == null) {
			Main.getCurrent().getMainWindow().addWindow(
					new InformationDialogComponent("Please select an item",
							"Information"));
		} else {
			Long instanceId = (Long) table.getItem(table.getValue())
					.getItemProperty("id").getValue();
			try {
				wfiFacade.stopWorkflowInstance(instanceId);
			} catch (TransactionException e) {
				Main.getCurrent().getMainWindow().addWindow(
						new TransactionErrorDialogComponent(e));
			}
		}

	}
}