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
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.controller.RegisterUserAction;

/**
 * TODO: add comment
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
    public RegisterUserComponent(){
        init();
    }
    
    public void saveRegistrationForm(){
        //TODO: validation
        registrationForm.commit();
    }
    
    public Item getRegistrationForm(){
        return registrationForm;
    }
    
    /**
     * This method initializes the components of the register user component
     *
     */
    private void init(){
        registrationForm = new Form();
        registrationForm.setWriteThrough(false);
        
        descriptionLabel = new Label("Please fill out all fields to register as a new user:", Label.CONTENT_XHTML);
        
        userName = new TextField();
        userName.setCaption("User Name:");
        password = new TextField();
        password.setCaption("Password:");
        password.setSecret(true);
        passwordConfirm = new TextField();
        passwordConfirm.setCaption("Confirm Password:");
        passwordConfirm.setSecret(true);
        email = new TextField();
        email.setCaption("E-Mail:");
        firstName = new TextField();
        firstName.setCaption("First Name:");
        lastName = new TextField();
        lastName.setCaption("Last Name:");
        street = new TextField();
        street.setCaption("Street:");
        postalCode = new TextField();
        postalCode.setCaption("PostalCode:");
        city = new TextField();
        city.setCaption("City");
        
        completeRegistration = new Button("Complete Registration", new RegisterUserAction());
        
        registrationForm.addField("userName", userName);
        registrationForm.addField("password", password);
        registrationForm.addField("passwordConfirm", passwordConfirm);
        registrationForm.addField("email", email);
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
        verticalLayout.setComponentAlignment(completeRegistration, "right bottom");
        
        this.setCompositionRoot(verticalLayout);
    }
}
