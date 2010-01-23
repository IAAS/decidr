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

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.ui.controller.tenant.RegisterTenantAction;

/**
 * A tenant can be registered by inserting information about the tenant. This
 * information consists of the tenant name, the username, the password, the
 * email address, the first name, the last name, the street, the postal code and
 * the city.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2355", currentReviewState = State.PassedWithComments)
public class RegisterTenantComponent extends CustomComponent {

    private static final long serialVersionUID = 1L;

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
     * TODO document
     */
    public RegisterTenantComponent() {
        init();
    }

    /**
     * Returns the registration form.
     * 
     * @return registrationForm TODO document
     */
    public Form getRegistrationForm() {
        return registrationForm;
    }

    /**
     * This method initializes the components of the
     * {@link RegisterTenantComponent}.
     */
    private void init() {
        registrationForm = new Form();
        registrationForm.setWriteThrough(true);
        registrationForm.setImmediate(true);

        descriptionLabel = new Label(
                "Please fill out all fields to register as a new tenant:",
                Label.CONTENT_XHTML);

        tenantName = new TextField();
        tenantName.setCaption("Tenant Name:");
        tenantName.addValidator(new RegexpValidator("\\w{2,50}",
                "The tenant name must consist of 2 to 50 characters!"));
        userName = new TextField();
        userName.setCaption("User Name:");
        userName.addValidator(new RegexpValidator("\\w{3,20}",
                "The username must consist of 3 to 20 characters!"));
        password = new TextField();
        password.setCaption("Password:");
        password.setSecret(true);
        passwordConfirm = new TextField();
        passwordConfirm.setCaption("Confirm Password:");
        passwordConfirm.setSecret(true);
        email = new TextField();
        email.setCaption("Email:");
        email.addValidator(new EmailValidator(
                "Please enter a valid email address."));
        firstName = new TextField();
        firstName.setCaption("First Name:");
        firstName.addValidator(new StringLengthValidator(
                "Please enter your first name. "
                        + "It can consist of up to 50 characters.", 0, 50,
                false));
        lastName = new TextField();
        lastName.setCaption("Last Name:");
        lastName.addValidator(new StringLengthValidator(
                "Please enter your surname. "
                        + "It can consist of up to 50 characters.", 0, 50,
                false));
        street = new TextField();
        street.setCaption("Street:");
        postalCode = new TextField();
        postalCode.setCaption("PostalCode:");
        // TODO: see previous comment about postal codes
        postalCode.addValidator(new RegexpValidator("[1-9][0-9]{4,15}",
                "Please enter a valid postal code"));
        city = new TextField();
        city.setCaption("City");

        completeRegistration = new Button("Finish Registration",
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

    /**
     * Saves the information which is entered into the form.
     */
    public void saveRegistrationForm() {
        registrationForm.commit();
    }
}
