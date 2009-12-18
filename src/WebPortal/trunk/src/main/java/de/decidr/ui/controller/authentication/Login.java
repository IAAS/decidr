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
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.commands.tenant.GetTenantSettingsCommand;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
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
import de.decidr.ui.view.windows.InformationDialogComponent;

/**
 * This class handles the login for the web application.<br>
 * If the login procedure is successful, the following actions will be taken:
 * <ul>
 * <li>The currently active tenant will be retrieved from the database.</li>
 * <li>The UI will be loaded. The components that are loaded depend on the
 * user's highest role fot the currently active tenant.</li>
 * <li>A new theme might be loaded, depending on whether the current tenant
 * provides a tenant-specific theme.</li>
 * <li>The user ID, the tenant name and the user's role will be stored in the
 * session. This way, the information is available throughout the application.</li>
 * </ul>
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2350", currentReviewState = State.PassedWithComments)
public class Login {

    private UIDirector uiDirector = Main.getCurrent().getUIDirector();
    private UIBuilder uiBuilder = null;

    private UserFacade userFacade = new UserFacade(new UserRole());

    private Long userId = null;
    private Long tenantId = null;
    private Class<? extends Role> role = null;
    private String tenantName = null;
    private Tenant tenant = null;

    private TenantView tenantView = null;

    /**
     * This method generates the session and checks if the given username and
     * password exist are correct. If login is successful, the user ID, the
     * tenant name and the user's role are stored in the session and the
     * tenant-specific theme will be loaded.
     * 
     * @param username
     *            - Username of the user which logs into the application
     * @param password
     *            - Password of the user which logs into the application
     * @throws TransactionException
     *             TODO document
     */
    public void authenticate(String username, String password)
            throws TransactionException {

        HttpSession session = Main.getCurrent().getSession();

        userId = userFacade.getUserIdByLogin(username, password);

        if (userFacade.isRegistered(userId)) {
            userFacade = new UserFacade(new UserRole(userId));

            if (session.getAttribute("tenantId") == null) {
                tenantId = userFacade.getCurrentTenantId(userId);
            } else {
                tenantId = (Long) session.getAttribute("tenantId");
                userFacade.setCurrentTenantId(userId, tenantId);
            }

            if (tenantId == null) {
                tenant = DecidrGlobals.getDefaultTenant();
                tenantId = tenant.getId();
                userFacade.setCurrentTenantId(userId, tenantId);
            }

            GetTenantSettingsCommand cmd = new GetTenantSettingsCommand(
                    new SuperAdminRole(DecidrGlobals.getSettings()
                            .getSuperAdmin().getId()), tenantId);
            HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
            Tenant tenant = cmd.getTenantSettings();
            tenantName = tenant.getName();

            role = userFacade.getUserRoleForTenant(userId, tenantId);
            Role roleInstance = null;
            try {
                roleInstance = role.getConstructor(Long.class).newInstance(
                        userId);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }

            session.setAttribute("userId", userId);
            session.setAttribute("tenantId", tenantId);
            session.setAttribute("role", roleInstance);

            loadProtectedResources();
        } else {
            Main
                    .getCurrent()
                    .getMainWindow()
                    .addWindow(
                            new InformationDialogComponent(
                                    "The user isn't registered yet. Please confirm your registration first",
                                    "Information"));
        }

    }

    /**
     * Provides the same functionality as authenticate(..) but requires a user
     * id and an authentification key. Should be used if login is executed by
     * clicking i.e. an email link.
     * 
     * @param userId
     *            ID of the user to be logged in
     * @param authentificationKey
     *            auth key of the user to be logged in. If is not accepted by
     *            the facade, login fails
     */
    public void loginById(Long userId, String authentificationKey)
            throws EntityNotFoundException, TransactionException {
        userFacade = new UserFacade(new UserRole(userId));

        if (userFacade.authKeyMatches(userId, authentificationKey)) {
            HttpSession session = Main.getCurrent().getSession();

            if (session.getAttribute("tenantId") == null) {
                tenantId = userFacade.getCurrentTenantId(userId);
            } else {
                tenantId = (Long) session.getAttribute("tenantId");
                userFacade.setCurrentTenantId(userId, tenantId);
            }

            if (tenantId == null) {
                tenant = DecidrGlobals.getDefaultTenant();
                tenantId = tenant.getId();
                userFacade.setCurrentTenantId(userId, tenantId);
            }

            GetTenantSettingsCommand cmd = new GetTenantSettingsCommand(
                    new SuperAdminRole(DecidrGlobals.getSettings()
                            .getSuperAdmin().getId()), tenantId);
            HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
            Tenant tenant = cmd.getTenantSettings();
            tenantName = tenant.getName();

            role = userFacade.getUserRoleForTenant(userId, tenantId);
            Role roleInstance = null;
            try {
                roleInstance = role.getConstructor(Long.class).newInstance(
                        userId);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }

            session.setAttribute("userId", userId);
            session.setAttribute("tenantId", tenantId);
            session.setAttribute("role", roleInstance);

            loadProtectedResources();
        }
    }

    /**
     * Loads the protected resources if and only if the user is logged in.
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
            // GH, Aleks: please provide a decent error message ~rr
                    "Role class not found");
        }

        uiDirector.constructView();

        tenantView = new TenantView();
        tenantView.synchronize();
        Main.getCurrent().setTheme(tenantName);
    }
}
