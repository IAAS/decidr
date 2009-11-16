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
package de.decidr.ui.view;

import java.util.LinkedList;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.ui.controller.tenant.SwitchTenantAction;
import de.decidr.ui.data.CurrentTenantContainer;
import de.decidr.ui.data.TenantContainer;
import de.decidr.ui.view.tables.CurrentTenantModelTable;
import de.decidr.ui.view.tables.TenantTable;

public class ChangeTenantComponent extends CustomComponent {

	/**
	 * The user can change the tenant if he wants. He has to choose one tenant
	 * from the table and push the switch tenant button.
	 * 
	 * @author AT
	 */
	private static final long serialVersionUID = 5599429204495615788L;

	private CurrentTenantContainer currentTenantContainer = null;
	private TenantContainer tenantContainer = null;

	private VerticalLayout verticalLayout = null;

	private Label changeTenantLabel = null;
	private Label waitingForApprovalLabel = null;

	private ButtonPanel buttonPanel = null;

	private Button changeTenantButton = null;

	private List<Button> buttonList = new LinkedList<Button>();

	private CurrentTenantModelTable tenantTable = null;
	private TenantTable approvalTable = null;

	/**
	 * Default constructor
	 * 
	 */
	public ChangeTenantComponent(Class<? extends Role> role) {
		init();
		if (role.equals(SuperAdminRole.class)) {
			changeToSuperAdmin();
		}
	}

	/**
	 * This method initializes the components of the change tenant component
	 * 
	 */
	private void init() {
		currentTenantContainer = new CurrentTenantContainer();

		verticalLayout = new VerticalLayout();

		changeTenantLabel = new Label("<h2> Change Tenant </h2>");
		changeTenantLabel.setContentMode(Label.CONTENT_XHTML);

		tenantTable = new CurrentTenantModelTable(currentTenantContainer);

		initButtonPanel();

		setCompositionRoot(verticalLayout);

		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(changeTenantLabel);
		verticalLayout.addComponent(tenantTable);
		verticalLayout.addComponent(buttonPanel);
	}

	/**
	 * Inititalizes the button panel
	 *
	 */
	private void initButtonPanel() {
		changeTenantButton = new Button("Change tenant",
				new SwitchTenantAction(tenantTable));

		buttonList.add(changeTenantButton);

		buttonPanel = new ButtonPanel(buttonList);
		buttonPanel.setCaption("Edit tenant");
	}

	/**
	 * Adds a table to the component with all tenants for the super admin
	 * 
	 */
	private void changeToSuperAdmin() {
		waitingForApprovalLabel = new Label("<h2> Waiting for approval </h2>");
		waitingForApprovalLabel.setContentMode(Label.CONTENT_XHTML);

		tenantContainer = new TenantContainer();

		approvalTable = new TenantTable(tenantContainer);

		verticalLayout.addComponent(waitingForApprovalLabel, 3);
		verticalLayout.addComponent(approvalTable, 4);
	}

}
