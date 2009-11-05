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

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.WorkflowModelFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * TODO: add comment
 * 
 * @author AT
 */
public class RemoveWorkflowModelsAction implements ClickListener {

	private WorkflowModelFacade workflowModelFacade = new WorkflowModelFacade(
			new UserRole((Long) Main.getCurrent().getSession().getAttribute(
					"userId")));
	
	private Table table = null;
	
	/**
	 * TODO: add comment
	 *
	 */
	public RemoveWorkflowModelsAction(Table table) {
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
		List<Long> wfms = new LinkedList<Long>();
		Set<?> value = (Set<?>) table.getValue();
		if ((value != null) && (value.size() != 0)) {
			for (Iterator<?> iter = value.iterator(); iter.hasNext();) {
				wfms.add((Long) table
							.getContainerProperty(iter.next(), "id")
							.getValue());
			}
		}
		try {
			workflowModelFacade.deleteWorkflowModels(wfms);
			Set<?> values = (Set<?>) table.getValue();
			if ((values != null) && (values.size() != 0)) {
				for (Iterator<?> iter = values.iterator(); iter.hasNext();) {
					table.removeItem(iter.next());
				}
			}
		} catch (TransactionException e) {
			Main.getCurrent().getMainWindow().addWindow(
					new TransactionErrorDialogComponent());
		}

	}

}