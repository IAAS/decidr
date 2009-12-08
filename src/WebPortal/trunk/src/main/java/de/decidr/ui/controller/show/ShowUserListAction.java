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

package de.decidr.ui.controller.show;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.acl.roles.TenantAdminRole;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.controller.UIDirector;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.SiteFrame;
import de.decidr.ui.view.UserListComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This actions shows the {@link UserListComponent} in the content area.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2349", currentReviewState = State.Passed)
public class ShowUserListAction implements ClickListener {

    private static final long serialVersionUID = 8622917418547074320L;

    private UIDirector uiDirector = Main.getCurrent().getUIDirector();
    private SiteFrame siteFrame = uiDirector.getTemplateView();

    private UserFacade userFacade = null;
    private Long tenantId = (Long) Main.getCurrent().getSession().getAttribute(
            "tenantId");
    private Long userId = (Long) Main.getCurrent().getSession().getAttribute(
            "userId");
    private Role role = (Role) Main.getCurrent().getSession().getAttribute(
            "role");

    /*
     * (non-Javadoc)
     * 
     * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        userFacade = new UserFacade(role);
        try {
            siteFrame.setContent(new UserListComponent());
            if (userFacade.getUserRoleForTenant(userId, tenantId).equals(
                    SuperAdminRole.class)) {
                ((UserListComponent) siteFrame.getContent())
                        .changeToSuperAdmin();
            } else if (userFacade.getUserRoleForTenant(userId, tenantId)
                    .equals(TenantAdminRole.class)) {
                ((UserListComponent) siteFrame.getContent())
                        .changeToTenantAdmin();
            } else {
                ((UserListComponent) siteFrame.getContent()).getUserListTable()
                        .removeContainerProperty("Edit");
            }
        } catch (TransactionException exception) {
            Main.getCurrent().addWindow(
                    new TransactionErrorDialogComponent(exception));
        }
    }
}
