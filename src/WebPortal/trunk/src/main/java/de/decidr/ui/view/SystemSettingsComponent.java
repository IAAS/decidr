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

import com.vaadin.data.Item;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.controller.SaveSystemSettingsAction;

/**
 * The user can change the system settings.
 * 
 * @author AT
 */
public class SystemSettingsComponent extends CustomComponent {

    private static final long serialVersionUID = 3389525551936631625L;

    private Item settingsItem = null;

    private VerticalLayout verticalLayout = null;

    private Form settingsForm = null;

    private Button saveButton = null;

    /**
     * Default constructor
     */
    public SystemSettingsComponent(Item settingsItem) {
        this.settingsItem = settingsItem;
        init();
    }

    public Form getSettingsForm() {
        return settingsForm;
    }

    /**
     * Returns the settings item.
     * 
     * @return settingsItem
     */
    public Item getSettingsItem() {
        return settingsItem;
    }

    /**
     * This method initializes the components of the system settings component
     * 
     */

    private void init() {
        settingsForm = new Form();
        settingsForm.setWriteThrough(true);
        settingsForm.setImmediate(true);
        settingsForm.setItemDataSource(settingsItem);
        Object[] visiblePropertyIds = new Object[] { "autoAcceptNewTenants",
                "logLevel", "changeEmailRequestLifetimeSeconds", "domain",
                "invitationLifetimeSeconds", "maxAttachmentsPerEmail",
                "maxUploadFileSizeBytes", "maxUploadFileSizeBytes",
                "mtaHostname", "mtaPassword", "mtaPort", "mtaUsername",
                "mtaUseTls", "passwordResetRequestLifetimeSeconds",
                "registrationRequestLifetimeSeconds", "systemName",
                "systemEmailAddress", "monitorUpdateIntervalSeconds",
                "monitorAveragingPeriodSeconds", "serverPoolInstances",
                "minServerLoadForLock", "maxServerLoadForUnlock",
                "maxServerLoadForShutdown", "minUnlockedServers",
                "minWorkflowInstancesForLock", "maxWorkflowInstancesForUnlock",
                "maxWorkflowInstancesForShutdown" };
        settingsForm.setVisibleItemProperties(visiblePropertyIds);

        verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);

        saveButton = new Button("Save", new SaveSystemSettingsAction());

        setCompositionRoot(verticalLayout);
        setCaption("System Setting");

        verticalLayout.addComponent(settingsForm);
        verticalLayout.addComponent(saveButton);
        verticalLayout.setComponentAlignment(saveButton, Alignment.BOTTOM_LEFT);
    }

    /**
     * Updates all changes since the previous commit to the data source.
     * 
     */
    public void saveSettingsItem() {
        try {
            settingsForm.commit();

        } catch (Exception e) {
            Main.getCurrent().getMainWindow().showNotification(e.getMessage());
        }
    }

}
