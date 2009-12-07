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

package de.decidr.ui.controller.workflowmodel;

import javax.servlet.http.HttpSession;

import com.vaadin.data.Item;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.model.facades.WorkflowModelFacade;
import de.decidr.ui.controller.UIDirector;
import de.decidr.ui.data.ModelingTool;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.SiteFrame;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This action creates a new, empty workflow model
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class CreateWorkflowModelAction implements ClickListener {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;

	private HttpSession session = Main.getCurrent().getSession();

	private Role role = (Role) session.getAttribute("role");
	private TenantFacade tenantFacade = new TenantFacade(role);
	private WorkflowModelFacade workflowModelFacade = new WorkflowModelFacade(
			role);

	private Long tenantId = null;

	private TextField name = null;

	private Table table = null;
	
	UIDirector uiDirector = Main.getCurrent().getUIDirector();
	SiteFrame siteFrame = uiDirector.getTemplateView();

	/**
	 * Constructor with a given workflow model name.
	 * 
	 */
	public CreateWorkflowModelAction(TextField textField, Table table) {
		this.table = table;
		this.name = textField;
		name.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
	 * ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		
		try {
			tenantId = (Long) Main.getCurrent().getSession().getAttribute(
					"tenantId");
			Long workflowModelId = tenantFacade.createWorkflowModel(tenantId,
					name.getValue().toString());
			Main.getCurrent().getMainWindow().removeWindow(event.getButton().getWindow());
			Item item = workflowModelFacade.getWorkflowModel(workflowModelId);
			table.addItem(item);
			table.requestRepaint();
			
			siteFrame.setContent(new ModelingTool(workflowModelId));
		} catch (TransactionException exception) {
			Main.getCurrent().getMainWindow().addWindow(
					new TransactionErrorDialogComponent(exception));
			exception.printStackTrace();
		}

	}
}