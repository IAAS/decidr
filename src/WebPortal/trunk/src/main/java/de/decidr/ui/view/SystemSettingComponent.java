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

import java.util.Arrays;

import com.vaadin.data.Item;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.controller.SaveSystemSettingsAction;
import de.decidr.ui.data.SystemSettingsItem;

public class SystemSettingComponent extends CustomComponent {

    /**
     * The user can change the system settings. He can change the log level and
     * automtically approve all tenants.
     * 
     * @author AT
     */
    private static final long serialVersionUID = 3389525551936631625L;

    private SystemSettingsItem settingsItem = new SystemSettingsItem();

    private VerticalLayout verticalLayout = null;

    private Form settingsForm = null;

    private static final String[] logLevel = new String[] { "INFO", "TRACE",
            "DEBUG", "WARN", "ERROR", "FATAL" };

    private NativeSelect nativeSelect = null;

    private CheckBox checkBox = null;
    private CheckBox mtaUseTlsCheckBox = null;

    private TextField systemNameTextField = null;
    private TextField domainTextField = null;
    private TextField systemEmailAddressTextField = null;
    private TextField mtaHostnameTextField = null;
    private TextField mtaPortTextField = null;
    private TextField mtaUsernameTextField = null;
    private TextField mtaPasswordTextField = null;
    private TextField maxUploadSizeTextField = null;

    private Slider passwordResetSlider = null;
    private Slider registrationSlider = null;
    private Slider changeEmailSlider = null;
    private Slider invitationSlider = null;
    private Slider maxAttachmentSlider = null;
    // Aleks, GH: weitere settings hinzufügen
    private Button saveButton = null;

    /**
     * Default constructor
     * 
     */
    public SystemSettingComponent() {
        init();
    }

    private void fillForm() {
        settingsForm.addField(settingsItem.getItemProperty("logLevel"),
                nativeSelect);
        settingsForm.addField(settingsItem
                .getItemProperty("autoAcceptNewTenants"), checkBox);
        settingsForm.addField(settingsItem.getItemProperty("systemName"),
                systemNameTextField);
        settingsForm.getField(settingsItem.getItemProperty("systemName"))
                .setValue("DecidR");
        settingsForm.addField(settingsItem.getItemProperty("domain"),
                domainTextField);
        settingsForm.addField(settingsItem
                .getItemProperty("systemEmailAddress"),
                systemEmailAddressTextField);
        settingsForm.getField(
                settingsItem.getItemProperty("systemEmailAddress"))
                .addValidator(
                        new EmailValidator(
                                "Please enter a valid email address!"));
        settingsForm.addField(settingsItem
                .getItemProperty("passwordResetRequestLifeTimeSeconds"),
                passwordResetSlider);
        settingsForm.addField(settingsItem
                .getItemProperty("registrationRequestLifetimeSeconds"),
                registrationSlider);
        settingsForm.addField(settingsItem
                .getItemProperty("changeEmailRequestLifetimeSeconds"),
                changeEmailSlider);
        settingsForm
                .addField(settingsItem
                        .getItemProperty("invitationLifetimeSeconds"),
                        invitationSlider);
        settingsForm.addField(settingsItem.getItemProperty("mtaHostname"),
                mtaHostnameTextField);
        settingsForm.addField(settingsItem.getItemProperty("mtaPort"),
                mtaPortTextField);
        settingsForm.getField(settingsItem.getItemProperty("mtaPort"))
                .addValidator(
                        new IntegerValidator("Please enter a valid integer!"));
        settingsForm.addField(settingsItem.getItemProperty("mtaUseTls"),
                mtaUseTlsCheckBox);
        settingsForm.addField(settingsItem.getItemProperty("mtaUsername"),
                mtaUsernameTextField);
        settingsForm.addField(settingsItem.getItemProperty("mtaPassword"),
                mtaPasswordTextField);
        settingsForm.addField(settingsItem
                .getItemProperty("maxUploadFileSizeByte"),
                maxUploadSizeTextField);
        settingsForm.addField(settingsItem
                .getItemProperty("maxAttachmentSlider"), maxAttachmentSlider);
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
        settingsForm.setWriteThrough(false);
        settingsForm.setItemDataSource(settingsItem);
        settingsForm.setVisibleItemProperties(Arrays.asList(new String[] {
                "logLevel", "autoAcceptNewTenants" }));

        verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);

        nativeSelect = new NativeSelect("Log Level");
        for (int i = 0; i < logLevel.length; i++) {
            nativeSelect.addItem(logLevel[i]);
        }
        nativeSelect.setNullSelectionAllowed(false);
        nativeSelect.setValue(logLevel[0]);

        checkBox = new CheckBox("Automatically approve all new tenants");
        mtaUseTlsCheckBox = new CheckBox("Use MTA transport layer");

        systemNameTextField = new TextField("System Name");
        domainTextField = new TextField("Domain Name");
        systemEmailAddressTextField = new TextField("System email address");
        mtaHostnameTextField = new TextField("MTA hostname");
        mtaPortTextField = new TextField("MTA port");
        mtaUsernameTextField = new TextField("MTA username");
        mtaPasswordTextField = new TextField("MTA password");
        mtaPasswordTextField.setSecret(true);
        maxUploadSizeTextField = new TextField("Max upload size");

        passwordResetSlider = new Slider("Password reset request lifetime");
        registrationSlider = new Slider("Registration request lifetime");
        changeEmailSlider = new Slider("Change email request lifetime");
        invitationSlider = new Slider("Invitation lifetime");
        maxAttachmentSlider = new Slider("Max attachments per email");

        saveButton = new Button("Save", new SaveSystemSettingsAction());

        setCompositionRoot(verticalLayout);
        setCaption("System Setting");

        fillForm();

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
