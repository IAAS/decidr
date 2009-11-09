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
import com.vaadin.data.validator.NullValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.controller.RegisterUserAction;
import de.decidr.ui.controller.RegisterUserWithInvitationAction;

/**
 * A new user can be registered by entering some information. This information
 * contains the username, the password, the email address, the first name, the
 * last name, the street, the postal code and the city.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class RegisterUserComponent extends CustomComponent {
	private VerticalLayout verticalLayout = null;

	private Label descriptionLabel = null;

	private Button completeRegistration = null;

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
	public RegisterUserComponent() {
		init();
		setCompleteListener();
	}

	/**
	 * This constructor should be used for invitations from unregistered users,
	 * who should automatically accept the invitation after registration
	 * 
	 * @param invId
	 *            : invitation id
	 */
	public RegisterUserComponent(Long invId) {
		init();
		setCompleteListener(invId);
	}

	/**
	 * Returns the registration form.
	 * 
	 * @return registrationForm
	 */
	public Form getRegistrationForm() {
		return registrationForm;
	}

	/**
	 * This method initializes the components of the register user component
	 * 
	 */
	private void init() {
		registrationForm = new Form();
		registrationForm.setWriteThrough(true);
		registrationForm.setImmediate(true);

		descriptionLabel = new Label(
				"Please fill out all fields to register as a new user:",
				Label.CONTENT_XHTML);

		userName = new TextField();
		userName.setCaption("User Name:");
		userName
				.addValidator(new RegexpValidator(
						"\\w{3,20}",
						"The username must have a length from 3 - 20 characters and mustn't contain additional characters"));
		password = new TextField();
		password.setCaption("Password:");
		password.setSecret(true);
		passwordConfirm = new TextField();
		passwordConfirm.setCaption("Confirm Password:");
		passwordConfirm.setSecret(true);
		email = new TextField();
		email.setCaption("E-Mail:");
		email.addValidator(new EmailValidator(
				"Please enter a valid email address"));
		firstName = new TextField();
		firstName.setCaption("First Name:");
		firstName.addValidator(new StringLengthValidator(
				"Please enter your forname. It can be up to 50 characters", 0,
				50, false));
		lastName = new TextField();
		lastName.setCaption("Last Name:");
		lastName.addValidator(new StringLengthValidator(
				"Please enter your surename. It can be up to 50 characters", 0,
				50, false));
		street = new TextField();
		street.setCaption("Street:");
		postalCode = new TextField();
		postalCode.setCaption("PostalCode:");
		postalCode.addValidator(new RegexpValidator("[1-9][0-9]{4,15}",
				"Please enter a valid postal code"));
		city = new TextField();
		city.setCaption("City");

		completeRegistration = new Button("Complete Registration");

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
	 * Saves the entered data from the form.
	 * 
	 */
	public void saveRegistrationForm() {
		boolean notEmpty = true;
		for (Object propertyId : registrationForm.getItemPropertyIds()) {
			if (registrationForm.getField(propertyId).getValue().equals(null)) {
				notEmpty = false;
			}
		}
		if (notEmpty) {
			registrationForm.commit();
		} else {
			Main.getCurrent().getMainWindow().addWindow(
					new InformationDialogComponent(
							"Please enter the required information",
							"Empty fields"));
		}

	}

	/**
	 * Sets a listener to the complete button.
	 * 
	 */
	private void setCompleteListener() {
		completeRegistration.addListener(new RegisterUserAction());
	}

	/**
	 * Sets a listener to the complete button with the given invitation id.
	 * 
	 * @param invId
	 */
	private void setCompleteListener(Long invId) {
		completeRegistration.addListener(new RegisterUserWithInvitationAction(
				registrationForm, invId));
	}
}
