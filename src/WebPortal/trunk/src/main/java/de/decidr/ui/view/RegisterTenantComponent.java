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
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.controller.RegisterTenantAction;

/**
 * A tenant can be registered by inserting information about the tenant. These
 * information contain the tenant name, the username, the password, the email
 * address, the first name, the last name, the street, the postal code and the
 * city.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class RegisterTenantComponent extends CustomComponent {

    private VerticalLayout verticalLayout = null;

    private Label descriptionLabel = null;

    private Button completeRegistration = null;

    private TextField tenantName = null;
    private TextField userName = null;
    private TextField password = null;
    private TextField passwordConfirm = null;
    private TextField email = null;
    private TextField firstName = null;
    private TextField lastName = null;
    private TextField street = null;
    private TextField postalCode = null;
    private TextField city = null;

    private Form registrationForm = null;

    /**
     * Default constructor
     * 
     */
    public RegisterTenantComponent() {
        init();
    }

    /**
     * Saves the information which are entered into the form.
     * 
     */
    public void saveRegistrationForm() {
        registrationForm.commit();
    }

    /**
     * Returns the registration form.
     * 
     * @return registrationForm
     */
    public Item getRegistrationForm() {
        return registrationForm;
    }

    /**
     * This method initializes the components of the register tenant component
     * 
     */
    private void init() {
        registrationForm = new Form();
        registrationForm.setWriteThrough(false);

        descriptionLabel = new Label(
                "Please fill out all fields to register as a new tenant:",
                Label.CONTENT_XHTML);

        tenantName = new TextField();
        tenantName.setCaption("Tenant Name:");
        tenantName
                .addValidator(new RegexpValidator(
                        "\\w{2,50}",
                        "Der Tenant name darf 2-50 Zeichen lang sein und darf keine Sonderzeichen enthalten"));
        userName = new TextField();
        userName.setCaption("User Name:");
        userName
                .addValidator(new RegexpValidator(
                        "\\w{3,20}",
                        "Der Username darf 3-20 Zeichen lang sein und darf keine Sonderzeichen enthalten"));
        password = new TextField();
        password.setCaption("Password:");
        password.setSecret(true);
        passwordConfirm = new TextField();
        passwordConfirm.setCaption("Confirm Password:");
        passwordConfirm.setSecret(true);
        email = new TextField();
        email.setCaption("E-Mail:");
        email.addValidator(new EmailValidator(
                "Bitte geben sie eine valide Emailadresse an"));
        firstName = new TextField();
        firstName.setCaption("First Name:");
        firstName
                .addValidator(new StringLengthValidator(
                        "Bitte geben Sie ihren Vorname ein. Dieser darf maximal 50 Zeichen betragen",
                        0, 50, false));
        lastName = new TextField();
        lastName.setCaption("Last Name:");
        lastName.addValidator(new StringLengthValidator(
                        "Bitte geben Sie ihren Vorname ein. Dieser darf maximal 50 Zeichen betragen",
                        0, 50, false));
        street = new TextField();
        street.setCaption("Street:");
        postalCode = new TextField();
        postalCode.setCaption("PostalCode:");
        postalCode.addValidator(new RegexpValidator("[1-9][0-9]{4,15}", "Bitte geben sie nur Zahlen ein"));
        city = new TextField();
        city.setCaption("City");

        completeRegistration = new Button("Complete Registration",
                new RegisterTenantAction());

        registrationForm.addField("tenantName", tenantName);
        registrationForm.getField("tenantName").setRequired(true);
        registrationForm.addField("userName", userName);
        registrationForm.getField("userName").setRequired(true);
        registrationForm.addField("password", password);
        registrationForm.getField("password").setRequired(true);
        registrationForm.addField("passwordConfirm", passwordConfirm);
        registrationForm.getField("passwordConfirm").setRequired(true);
        registrationForm.addField("email", email);
        registrationForm.getField("email").setRequired(true);
        registrationForm.addField("firstName", firstName);
        registrationForm.addField("lastName", lastName);
        registrationForm.addField("street", street);
        registrationForm.addField("postalCode", postalCode);
        registrationForm.addField("city", city);
        registrationForm.setSizeFull();

        verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);

        verticalLayout.addComponent(descriptionLabel);
        verticalLayout.addComponent(registrationForm);
        verticalLayout.addComponent(completeRegistration);
        verticalLayout.setComponentAlignment(completeRegistration,
                "right bottom");

        this.setCompositionRoot(verticalLayout);
    }
}
