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

package de.decidr.ui.controller.authentication;

import javax.servlet.http.HttpSession;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.acl.roles.TenantAdminRole;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.acl.roles.WorkflowAdminRole;
import de.decidr.model.commands.tenant.GetTenantSettingsCommand;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.model.facades.UserFacade;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.ui.controller.UIDirector;
import de.decidr.ui.controller.tenant.TenantView;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.uibuilder.SuperAdminViewBuilder;
import de.decidr.ui.view.uibuilder.TenantAdminViewBuilder;
import de.decidr.ui.view.uibuilder.UIBuilder;
import de.decidr.ui.view.uibuilder.UserViewBuilder;
import de.decidr.ui.view.uibuilder.WorkflowAdminViewBuilder;

/**
 * This class handles the login for the web application. If a user logs in one
 * tenant will be selected which the user belongs to. Only if the login is
 * successful the specific user interface will be loaded. The user interface
 * which will be loaded depends on the user's role. Simultaneously the specific
 * tenant theme will be loaded. Also the user id, the tenant name and the user's
 * role will be stored in the session. So these information are accessible
 * application wide.
 * 
 * @author AT
 */
public class Login {

	private UIDirector uiDirector = UIDirector.getInstance();
	private UIBuilder uiBuilder = null;

	private UserFacade userFacade = new UserFacade(new UserRole());
	private TenantFacade tenantFacade = null;

	private Long userId = null;
	private Long tenantId = null;
	private Class<? extends Role> role = null;
	private String tenantName = null;
	private Tenant tenant = null;

	private TenantView tenantView = null;

	/**
	 * The method generates the session and checks if the given username and
	 * password are stored in the database and are correct. If login is
	 * successful the user id, the tenant name and the user's role is stored in
	 * the session and the tenant specific theme will be loaded.
	 * 
	 * @param username
	 *            - Username of the user which logs into the application
	 * @param password
	 *            - Password of the user which logs into the application
	 * @throws TransactionException
	 */
	public void authenticate(String username, String password)
			throws TransactionException {

		HttpSession session = Main.getCurrent().getSession();

		userId = userFacade.getUserIdByLogin(username, password);

		userFacade = new UserFacade(new UserRole(userId));

		if (session.getAttribute("tenant") == null) {
			tenantId = userFacade.getCurrentTenantId(userId);
		} else {
			tenantFacade = new TenantFacade(new UserRole(userId));
			tenantId = tenantFacade.getTenantId((String) Main.getCurrent()
					.getSession().getAttribute("tenant"));
			userFacade.setCurrentTenantId(userId, tenantId);
		}

		if (tenantId == null) {
			tenant = DecidrGlobals.getDefaultTenant();
			tenantId = tenant.getId();
			userFacade.setCurrentTenantId(userId, tenantId);
		}

		GetTenantSettingsCommand cmd = new GetTenantSettingsCommand(
				new SuperAdminRole(DecidrGlobals.getSettings().getSuperAdmin()
						.getId()), tenantId);
		HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
		Tenant tenant = cmd.getTenantSettings();
		tenantName = tenant.getName();

		role = userFacade.getUserRoleForTenant(userId, tenantId);

		session.setAttribute("userId", userId);
		session.setAttribute("tenant", tenantName);
		session.setAttribute("role", role);

		loadProtectedResources();

	}

	/**
	 * Loads the protected resources if and only if the user is logged in.
	 * 
	 */
	private void loadProtectedResources() {
		if (UserRole.class.equals(role) || role == null) {
			uiBuilder = new UserViewBuilder();
			uiDirector.setUiBuilder(uiBuilder);
		} else if (WorkflowAdminRole.class.equals(role)) {
			uiBuilder = new WorkflowAdminViewBuilder();
			uiDirector.setUiBuilder(uiBuilder);
		} else if (TenantAdminRole.class.equals(role)) {
			uiBuilder = new TenantAdminViewBuilder();
			uiDirector.setUiBuilder(uiBuilder);
		} else if (SuperAdminRole.class.equals(role)) {
			uiBuilder = new SuperAdminViewBuilder();
			uiDirector.setUiBuilder(uiBuilder);
		} else {
			Main.getCurrent().getMainWindow().showNotification(
					"Role class not found");
		}
		uiDirector.constructView();

		tenantView = new TenantView();
		tenantView.synchronize();
		Main.getCurrent().setTheme(tenantName);
	}

}
