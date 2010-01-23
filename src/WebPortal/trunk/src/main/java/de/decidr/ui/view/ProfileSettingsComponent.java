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
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.entities.User;
import de.decidr.ui.controller.SaveProfileAction;
import de.decidr.ui.controller.show.ShowChangeEmailAction;
import de.decidr.ui.controller.show.ShowChangePasswordAction;
import de.decidr.ui.controller.show.ShowLeaveTenantDialogAction;
import de.decidr.ui.controller.user.ChangeStatusAction;

/**
 * The user can change his profile by inserting his personal information.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2355", currentReviewState = State.PassedWithComments)
public class ProfileSettingsComponent extends CustomComponent {

    /**
     * This FieldFactory creates the input fields of the form in this component.<br>
     * 
     * @author Geoffrey-Alexeij Heinze
     */
    private class SettingsFieldFactory extends DefaultFieldFactory {
        private static final long serialVersionUID = 1L;

        @Override
        public Field createField(Item item, Object propertyId,
                Component uiContext) {
            Field field = super.createField(item, propertyId, uiContext);

            if ("firstName".equals(propertyId)) {
                TextField tf = (TextField) field;
                tf
                        .addValidator(new StringLengthValidator(
                                "Please enter your first name."
                                        + " Your first name may contain up to 50 characters.",
                                0, 50, false));
                tf.setCaption("First Name");
                tf.setColumns(30);
            } else if ("lastName".equals(propertyId)) {
                TextField tf = (TextField) field;
                tf
                        .addValidator(new StringLengthValidator(
                                "Please enter your surname."
                                        + " Your surname may contain up to 50 characters.",
                                0, 50, false));
                tf.setCaption("Last Name");
                tf.setColumns(30);
            } else if ("street".equals(propertyId)) {
                TextField tf = (TextField) field;
                tf.setCaption("Street");
                tf.setColumns(30);
            } else if ("postalCode".equals(propertyId)) {
                TextField tf = (TextField) field;
                // TODO: this only works for german codes, an example of an
                // english one might be "SS12 9BP"
                tf.addValidator(new RegexpValidator("[1-9][0-9]{4,15}",
                        "Please enter only numbers"));
                tf.setCaption("Postal Code");
                tf.setColumns(30);
            } else if ("city".equals(propertyId)) {
                TextField tf = (TextField) field;
                tf.setCaption("City");
                tf.setColumns(30);
            }

            return field;
        }
    }

    private User user = null;

    private Form settingsForm = new Form();

    private static final long serialVersionUID = 1L;
    private Panel addressPanel = null;
    private Panel usernamePanel = null;

    private Panel profileButtonPanel = null;
    private VerticalLayout verticalLayout = null;
    private GridLayout usernameGridLayout = null;
    private VerticalLayout addressVerticalLayout = null;

    private HorizontalLayout profileButtonHorizontalLayout = null;
    private Label myProfileLabel = null;
    private Label usernameLabel = null;
    private Label emailLabel = null;
    private Label usernameNameLabel = null;
    private Label emailNameLabel = null;

    private Button changeEmailLink = null;
    private Button changePasswordLink = null;
    private Button leaveTenantLink = null;

    private Button saveButton = null;

    private CheckBox statusCheckBox = null;

    private Item settingsItem = null;

    private String[] properties = { "firstName", "lastName", "street",
            "postalCode", "city" };

    /**
     * TODO document
     */
    public ProfileSettingsComponent(User user) {
        this.user = user;
        init();
    }

    /**
     * Gets the settings {@link Form}.
     * 
     * @return the settingsForm TODO document
     */
    public Form getSettingsForm() {
        return settingsForm;
    }

    /**
     * Returns the settings {@link Item} where the information is stored.
     * 
     * @return settingsItem - the settings item to be returned
     */
    public Item getSettingsItem() {
        return settingsItem;
    }

    /**
     * Returns the status {@link CheckBox}.
     * 
     * @return statusCheckBox TODO document
     */
    public CheckBox getStatus() {
        return statusCheckBox;
    }

    /**
     * This method initializes the components of the
     * {@link ProfileSettingsComponent}.
     */
    private void init() {
        settingsForm.setWriteThrough(false);
        settingsItem = new BeanItem(user.getUserProfile(), properties);
        settingsForm.setItemDataSource(settingsItem);
        settingsForm.setFormFieldFactory(new SettingsFieldFactory());

        settingsForm.setVisibleItemProperties(properties);

        addressPanel = new Panel();
        usernamePanel = new Panel();
        profileButtonPanel = new Panel();

        usernameGridLayout = new GridLayout(2, 2);
        verticalLayout = new VerticalLayout();
        addressVerticalLayout = new VerticalLayout();
        profileButtonHorizontalLayout = new HorizontalLayout();

        myProfileLabel = new Label("<h2> My Profile </h2>");
        myProfileLabel.setContentMode(Label.CONTENT_XHTML);
        usernameLabel = new Label("Username: ");
        emailLabel = new Label("Email address: ");
        usernameNameLabel = new Label(user.getUserProfile().getUsername());
        emailNameLabel = new Label(user.getEmail());

        changeEmailLink = new Button("Change email",
                new ShowChangeEmailAction());
        changeEmailLink.setStyleName(Button.STYLE_LINK);
        changePasswordLink = new Button("Change password",
                new ShowChangePasswordAction());
        changePasswordLink.setStyleName(Button.STYLE_LINK);
        leaveTenantLink = new Button("Leave tenant",
                new ShowLeaveTenantDialogAction());
        leaveTenantLink.setStyleName(Button.STYLE_LINK);

        saveButton = new Button("Save", new SaveProfileAction());
        settingsForm.addField("button", saveButton);

        statusCheckBox = new CheckBox();
        statusCheckBox.setValue(user.getUnavailableSince() != null ? true
                : false);
        statusCheckBox.addListener(new ChangeStatusAction(user.getId()));
        statusCheckBox.setImmediate(true);

        this.setCompositionRoot(verticalLayout);

        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(myProfileLabel);
        verticalLayout.addComponent(usernamePanel);

        usernamePanel.addComponent(usernameGridLayout);
        usernameGridLayout.setSpacing(true);
        usernameGridLayout.addComponent(usernameLabel, 0, 0);
        usernameGridLayout.addComponent(usernameNameLabel, 1, 0);
        usernameGridLayout.addComponent(emailLabel, 0, 1);
        usernameGridLayout.addComponent(emailNameLabel, 1, 1);

        verticalLayout.addComponent(profileButtonPanel);

        profileButtonPanel.addComponent(profileButtonHorizontalLayout);
        profileButtonHorizontalLayout.setSpacing(true);
        profileButtonHorizontalLayout.addComponent(changeEmailLink);
        profileButtonHorizontalLayout.addComponent(changePasswordLink);
        profileButtonHorizontalLayout.addComponent(leaveTenantLink);
        profileButtonHorizontalLayout.addComponent(statusCheckBox);
        statusCheckBox.setCaption("Set my status to unavailable");
        profileButtonHorizontalLayout.setComponentAlignment(statusCheckBox,
                Alignment.TOP_RIGHT);

        verticalLayout.addComponent(addressPanel);

        addressPanel.addComponent(addressVerticalLayout);

        addressVerticalLayout.setSpacing(true);
        addressVerticalLayout.addComponent(settingsForm);
    }

    public void saveSettingsItem() {
        try {
            settingsForm.commit();
        } catch (Exception e) {
            Main.getCurrent().getMainWindow().showNotification(e.getMessage());
        }
    }

}