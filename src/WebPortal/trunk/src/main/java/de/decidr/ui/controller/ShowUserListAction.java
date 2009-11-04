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

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.acl.roles.TenantAdminRole;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.SiteFrame;
import de.decidr.ui.view.TransactionErrorDialogComponent;
import de.decidr.ui.view.UserListComponent;

/**
 * This actions shows the UserListComponent in the content area
 * 
 * @author AT
 */
public class ShowUserListAction implements ClickListener {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8622917418547074320L;

	private UIDirector uiDirector = UIDirector.getInstance();
	private SiteFrame siteFrame = uiDirector.getTemplateView();

	private UserFacade userFacade = null;
	private TenantFacade tenantFacade = null;
	private Long tenantId = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
	 * ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		userFacade = new UserFacade(new UserRole((Long) Main.getCurrent()
				.getSession().getAttribute("userId")));
		tenantFacade = new TenantFacade(new UserRole((Long) Main.getCurrent()
				.getSession().getAttribute("userId")));
		try {
			tenantId = tenantFacade.getTenantId((String) Main.getCurrent()
					.getSession().getAttribute("tenant"));
			siteFrame.setContent(new UserListComponent());
			if (userFacade.getUserRoleForTenant(
					(Long) Main.getCurrent().getSession()
							.getAttribute("userId"), tenantId).equals(
					SuperAdminRole.class)) {
				((UserListComponent) siteFrame.getContent())
						.changeToSuperAdmin();
			} else if (userFacade.getUserRoleForTenant(
					(Long) Main.getCurrent().getSession()
							.getAttribute("userId"), tenantId).equals(
					TenantAdminRole.class)) {
				((UserListComponent) siteFrame.getContent())
						.changeToTenantAdmin();
			} else {
				((UserListComponent) siteFrame.getContent()).getUserListTable()
						.removeContainerProperty("Edit");
			}
		} catch (TransactionException exception) {
			Main.getCurrent().addWindow(new TransactionErrorDialogComponent());
		}

	}

}
