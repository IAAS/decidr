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
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
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
 * This action creates a new tenant.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2350", currentReviewState = State.Passed)
public class CreateTenantAction implements ClickListener {

    private static final long serialVersionUID = 1L;

    private HttpSession session = Main.getCurrent().getSession();

    private Long userId = (Long) session.getAttribute("userId");
    private Role role = (Role) session.getAttribute("role");
    private TenantFacade tenantFacade = new TenantFacade(role);
    private UserFacade userFacade = new UserFacade(role);

    private UIDirector uiDirector = Main.getCurrent().getUIDirector();
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

            String tenantName = createForm.getItemProperty("tenantName")
                    .getValue().toString();

            try {
                Long tenantId = tenantFacade.createTenant(tenantName,
                        createForm.getItemProperty("tenantDescription")
                                .getValue().toString(), userId);
                userFacade.setCurrentTenantId(userId, tenantId);

                Class<? extends Role> roleClass = userFacade
                        .getHighestUserRole(userId);
                Role roleInstance = roleClass.getConstructor(Long.class)
                        .newInstance(userId);

                Main.getCurrent().getSession().setAttribute("role",
                        roleInstance);
                Main.getCurrent().getSession().setAttribute("tenant",
                        tenantName);

                Main.getCurrent().getMainWindow().addWindow(
                        new InformationDialogComponent(
                                "Tenant successfully created!", "Success"));
                uiDirector.switchView(tenantAdminViewBuilder);
                siteFrame.setContent(new TenantSettingsComponent(tenantId));
                ((HorizontalNavigationMenu) uiDirector.getTemplateView()
                        .getHNavigation()).getLogoutButton().setVisible(true);
            } catch (TransactionException e) {
                Main.getCurrent().getMainWindow().addWindow(
                        new TransactionErrorDialogComponent(e));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            Main.getCurrent().getMainWindow().addWindow(
                    new InformationDialogComponent(
                            "Please enter the required information!",
                            "Missing Information"));
        }
    }
}
