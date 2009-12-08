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

import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.FileFacade;
import de.decidr.model.facades.TenantFacade;
import de.decidr.ui.controller.CssHandler;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TenantSettingsComponent;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This action saves the current tenant settings.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2350", currentReviewState = State.Passed)
public class SaveTenantSettingsAction implements ClickListener {

    private static final long serialVersionUID = 1L;

    private HttpSession session = null;

    private Role role = null;
    private Long fileId = null;
    private TenantFacade tenantFacade = null;
    private FileFacade fileFacade = null;
    private TenantSettingsComponent content = null;

    /*
     * (non-Javadoc)
     * 
     * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        session = Main.getCurrent().getSession();
        role = (Role) session.getAttribute("role");
        tenantFacade = new TenantFacade(role);
        fileFacade = new FileFacade(role);
        content = (TenantSettingsComponent) Main.getCurrent().getUIDirector()
                .getTemplateView().getContent();
        Long tenantId = (Long) Main.getCurrent().getSession().getAttribute(
                "tenantId");
        try {
            tenantFacade.setDescription(tenantId, content
                    .getTenantDescription().getValue().toString());

            fileId = (Long) Main.getCurrent().getMainWindow().getData();
            if (fileId != null) {
                Main.getCurrent().getMainWindow().showNotification(
                        fileId.toString(),
                        Window.Notification.TYPE_ERROR_MESSAGE);
                tenantFacade.setLogo(tenantId, fileId);
            } else {
                Main.getCurrent().getMainWindow().showNotification(
                        "file id is null",
                        Window.Notification.TYPE_ERROR_MESSAGE);
            }

            CssHandler cssHandler = new CssHandler(content);
            cssHandler.saveCss(tenantFacade, content
                    .getShowAdvancedOptionsButton().booleanValue(), fileFacade);
            Main.getCurrent().getMainWindow().addWindow(
                    new InformationDialogComponent(
                            "Tenant settings successfully saved!", "Success"));
            Main.getCurrent().getMainWindow().requestRepaint();
        } catch (TransactionException e) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(e));
        }
    }
}
