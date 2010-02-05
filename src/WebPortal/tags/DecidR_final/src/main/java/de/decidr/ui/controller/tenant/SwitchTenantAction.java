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

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.acl.roles.TenantAdminRole;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.acl.roles.WorkflowAdminRole;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.beans.TenantBean;
import de.decidr.ui.beans.TenantSummaryViewBean;
import de.decidr.ui.controller.UIDirector;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.navigationmenus.HorizontalNavigationMenu;
import de.decidr.ui.view.uibuilder.SuperAdminViewBuilder;
import de.decidr.ui.view.uibuilder.TenantAdminViewBuilder;
import de.decidr.ui.view.uibuilder.UIBuilder;
import de.decidr.ui.view.uibuilder.UserViewBuilder;
import de.decidr.ui.view.uibuilder.WorkflowAdminViewBuilder;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * If a user switches the tenant, this action is called. The session attributes
 * &quot;role&quot; and &quot;tenant name&quot; will be overridden by the new
 * tenant name and the new role. The new tenant's theme is activated.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2358", currentReviewState = State.PassedWithComments)
public class SwitchTenantAction implements ClickListener {

    private static final long serialVersionUID = 1L;

    private String tenantName = null;

    private HttpSession session = null;
    private Class<? extends Role> role = null;
    private Role oldRole = null;

    private Long userId = null;
    private UserFacade userFacade = null;

    private Long tenantId = null;

    private Table table = null;

    private UIBuilder uiBuilder = null;
    private UIDirector uiDirector = Main.getCurrent().getUIDirector();

    /**
     * Constructor which is passed a tenant name to determine to which tenant
     * the user wants to switch to.
     */
    public SwitchTenantAction(Table table) {
        this.table = table;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        session = Main.getCurrent().getSession();
        userId = (Long) session.getAttribute("userId");
        oldRole = (Role) session.getAttribute("role");

        userFacade = new UserFacade(oldRole);

        try {
            Set<?> set = null;
            TenantSummaryViewBean tsvb = null;
            if (oldRole instanceof SuperAdminRole) {
                tsvb = (TenantSummaryViewBean) table.getValue();
                tenantName = tsvb.getTenantName();
                tenantId = tsvb.getId();

                role = userFacade.getUserRoleForTenant(userId, tenantId);
                Role roleInstance = role.getConstructor(Long.class)
                        .newInstance(userId);

                userFacade.setCurrentTenantId(userId, tenantId);

                session.setAttribute("tenantId", tenantId);
                session.setAttribute("role", roleInstance);

                loadProtectedResources();

                Main.getCurrent().getMainWindow().addWindow(
                        new InformationDialogComponent("Switched to "
                                + tenantName, "Success"));
            } else {
                set = (Set<?>) table.getValue();
                if ((table.getValue() != null) && (set.size() <= 1)) {
                    Iterator<?> iter = set.iterator();
                    while (iter.hasNext()) {
                        TenantBean tenantBean = (TenantBean) iter.next();
                        tenantName = tenantBean.getName();
                        tenantId = tenantBean.getId();

                        role = userFacade
                                .getUserRoleForTenant(userId, tenantId);
                        Role roleInstance = role.getConstructor(Long.class)
                                .newInstance(userId);

                        userFacade.setCurrentTenantId(userId, tenantId);

                        session.setAttribute("tenantId", tenantId);
                        session.setAttribute("role", roleInstance);

                        loadProtectedResources();

                        Main.getCurrent().getMainWindow().addWindow(
                                new InformationDialogComponent("Switched to "
                                        + tenantName, "Success"));
                    }
                } else {
                    Main.getCurrent().getMainWindow().addWindow(
                            new InformationDialogComponent(
                                    "Please select an item", "Information"));
                }
            }

        } catch (TransactionException exception) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(exception));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the protected resources if and only if the user is logged in.
     */
    private void loadProtectedResources() {
        TenantView tenantView = new TenantView();
        if (!oldRole.getClass().equals(role)) {
            if (UserRole.class.equals(role) || (role == null)) {
                uiBuilder = new UserViewBuilder();
            } else if (WorkflowAdminRole.class.equals(role)) {
                uiBuilder = new WorkflowAdminViewBuilder();
            } else if (TenantAdminRole.class.equals(role)) {
                uiBuilder = new TenantAdminViewBuilder();
            } else if (SuperAdminRole.class.equals(role)) {
                uiBuilder = new SuperAdminViewBuilder();
            } else {
                Main
                        .getCurrent()
                        .getMainWindow()
                        .addWindow(
                                new InformationDialogComponent(
                                        "Failed to load your resources due to a unknown role class.<br/>"
                                                + "Please inform the systems' administrator if this error occurs repeatedly.",
                                        "Login Failure"));
            }

            uiDirector.switchView(uiBuilder);
            ((HorizontalNavigationMenu) uiDirector.getTemplateView()
                    .getHNavigation()).getLogoutButton().setVisible(true);

        }
        tenantView.synchronize();
        Main.getCurrent().setTheme(tenantName);
    }
}
