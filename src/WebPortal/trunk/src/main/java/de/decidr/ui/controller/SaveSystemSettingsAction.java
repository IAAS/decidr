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

/**
 * This action saves changes of the system settings
 *
 * @author Geoffrey-Alexeij Heinze
 * @reviewed ~tk, ~dh
 */

import javax.servlet.http.HttpSession;

import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.SystemFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.SystemSettingComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

public class SaveSystemSettingsAction implements ClickListener {

    private HttpSession session = Main.getCurrent().getSession();

    private Long userId = (Long) session.getAttribute("userId");
    private SystemFacade systemFacade = new SystemFacade(new SuperAdminRole(userId));

    private SystemSettingComponent content = null;
    private Item item = null;

    /*
     * (non-Javadoc)
     * 
     * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        SystemSettings settings = DecidrGlobals.getSettings();

        content = (SystemSettingComponent) Main.getCurrent().getUIDirector()
                .getTemplateView().getContent();
        content.saveSettingsItem();
        item = content.getSettingsItem();

        settings
                .setAutoAcceptNewTenants(Boolean.parseBoolean(item
                        .getItemProperty("autoAcceptNewTenants").getValue()
                        .toString()));
        settings.setLogLevel(item.getItemProperty("logLevel").getValue()
                .toString());
        settings.setChangeEmailRequestLifetimeSeconds(Integer.parseInt(item
                .getItemProperty("changeEmailRequestLifetimeSeconds")
                .getValue().toString()));
        settings
                .setDomain(item.getItemProperty("domain").getValue().toString());
        settings.setInvitationLifetimeSeconds(Integer.parseInt(item
                .getItemProperty("invitationLifetimeSeconds").getValue()
                .toString()));
        settings.setMaxAttachmentsPerEmail(Integer.parseInt(item
                .getItemProperty("maxAttachmentsPerEmail").getValue()
                .toString()));
        settings.setMaxUploadFileSizeBytes(Long.parseLong(item.getItemProperty(
                "maxUploadFileSizeByte").getValue().toString()));
        settings.setMtaHostname(item.getItemProperty("mtaHostname").getValue()
                .toString());
        settings.setMtaPassword(item.getItemProperty("mtaPassword").getValue()
                .toString());
        settings.setMtaPort(Integer.parseInt(item.getItemProperty("mtaPort")
                .getValue().toString()));
        settings.setMtaUsername(item.getItemProperty("mtaUsername").getValue()
                .toString());
        settings.setMtaUseTls(Boolean.parseBoolean(item.getItemProperty(
                "mtaUseTls").getValue().toString()));
        settings.setPasswordResetRequestLifetimeSeconds(Integer.parseInt(item
                .getItemProperty("passwordResetRequestLifeTimeSeconds")
                .getValue().toString()));
        settings.setRegistrationRequestLifetimeSeconds(Integer.parseInt(item
                .getItemProperty("registrationRequestLifetimeSecond")
                .getValue().toString()));
        // Aleks, GH: weitere settings speichern
        try {
            systemFacade.setSettings(settings);
        } catch (TransactionException e) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(e));
        }
    }
}
