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

package de.decidr.ui.controller.tenant;

import javax.servlet.http.HttpSession;
import com.vaadin.ui.Form;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.TenantAdminRole;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.controller.UIDirector;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.SiteFrame;
import de.decidr.ui.view.TenantSettingsComponent;
import de.decidr.ui.view.navigationmenus.HorizontalNavigationMenu;
import de.decidr.ui.view.uibuilder.TenantAdminViewBuilder;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This action creates a new tenant
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class CreateTenantAction implements ClickListener {

	private HttpSession session = Main.getCurrent().getSession();

	private Long userId = (Long) session.getAttribute("userId");
	private TenantFacade tenantFacade = new TenantFacade(new UserRole(userId));
	private UserFacade userFacade = new UserFacade(new UserRole(userId));

	private UIDirector uiDirector = UIDirector.getInstance();
	private TenantAdminViewBuilder tenantAdminViewBuilder = new TenantAdminViewBuilder();
	private SiteFrame siteFrame = uiDirector.getTemplateView();

	private Form createForm = null;

	public CreateTenantAction(Form form) {
		createForm = form;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
	 * ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		boolean notEmpty = true;

		for (Object id : createForm.getItemPropertyIds()) {
			if (notEmpty) {
				if (createForm.getField(id).getValue().toString().equals("")
						&& createForm.getField(id).isRequired()) {
					notEmpty = false;
				}
			}
		}

		if (notEmpty) {
			createForm.commit();

			try {
				Long tenantId = tenantFacade.createTenant(createForm.getItemProperty(
						"tenantName").getValue().toString(), createForm
						.getItemProperty("tenantDescription").getValue()
						.toString(), userId);
				//TODO: Wie kann ich die rolle neu setzen
				Class<? extends Role> role = userFacade.getHighestUserRole(userId);
				System.out.println(role);
				System.out.println(userFacade.getUserRoleForTenant(userId, tenantId));
				Main.getCurrent().getSession().setAttribute("role", role);
				Main.getCurrent().getMainWindow().addWindow(
						new InformationDialogComponent(
								"Tenant successfully created", "Success"));
				uiDirector.setUiBuilder(tenantAdminViewBuilder);
				uiDirector.constructView();
				siteFrame.setContent(new TenantSettingsComponent());
				((HorizontalNavigationMenu) uiDirector.getTemplateView()
						.getHNavigation()).getLogoutButton().setVisible(true);
			} catch (TransactionException e) {
				Main.getCurrent().getMainWindow().addWindow(
						new TransactionErrorDialogComponent(e));
			}
		} else {
			Main.getCurrent().getMainWindow().addWindow(
					new InformationDialogComponent(
							"Please enter the required information",
							"Form not complete"));
		}

	}
}