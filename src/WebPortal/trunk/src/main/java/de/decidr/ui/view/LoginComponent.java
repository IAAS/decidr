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

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.decidr.model.workflowmodel.wsc.TConfiguration;
import de.decidr.ui.controller.LoginAction;
import de.decidr.ui.controller.LoginWithInvitationAction;
import de.decidr.ui.controller.ShowRegistrationAction;
import de.decidr.ui.controller.ShowResetPasswordAction;
import de.decidr.ui.controller.ShowStartConfigurationWindowAction;

/**
 * With the login component the user is able to authenticate himself
 * to the application. He has to insert his username and his password.
 *
 * @author AT
 */
public class LoginComponent extends CustomComponent {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 6622328174696591882L;

    
    private VerticalLayout verticalLayout = null;
    
    private Label loginLabel = null;
    
    private TextField usernameTextField = null;
    private TextField passwordTextField = null;
    
    private Button loginButton = null;
    private Button forgotPasswordButton = null;
    private Button registerButton = null;
    
    /**
     * Default constructor.
     *
     */
    public LoginComponent() {
        init(null);
    }


    /**
     * Default constructor.
     *
     */
    public LoginComponent(InvitationDialogComponent invD) {
        init(invD);
    }
    /**
     * This method initializes the components for the login component.
     *
     */
    private void init(InvitationDialogComponent invD) {
        verticalLayout = new VerticalLayout();
        
        loginLabel = new Label("<h3>Login:</h3>");
        loginLabel.setContentMode(Label.CONTENT_XHTML);
       
        usernameTextField = new TextField();
        usernameTextField.setCaption("Username");
        passwordTextField = new TextField();
        passwordTextField.setCaption("Password");
        passwordTextField.setSecret(true);
        
        if (invD == null)
        {
        	loginButton = new Button("Login", new LoginAction());
        }else{
        	loginButton = new Button("Login", new LoginWithInvitationAction(invD));
        }
        forgotPasswordButton = new Button("Forgot your password?", new ShowResetPasswordAction());
        forgotPasswordButton.setStyleName(Button.STYLE_LINK);
        registerButton = new Button("Register", new ShowRegistrationAction());
        registerButton.setStyleName(Button.STYLE_LINK);
        
       setCompositionRoot(verticalLayout);
       
       verticalLayout.setSpacing(true);
       verticalLayout.setMargin(true);
       verticalLayout.addComponent(loginLabel);
       verticalLayout.addComponent(usernameTextField);
       verticalLayout.addComponent(passwordTextField);
       verticalLayout.addComponent(loginButton);
       verticalLayout.addComponent(forgotPasswordButton);
       verticalLayout.addComponent(registerButton);
    }
    

    /**
     * Returns the username text field.
     *
     * @return usernameTextField
     */
    public TextField getUsernameTextField() {
        return usernameTextField;
    }

    /**
     * Returns the password text field.
     *
     * @return passwordTextField
     */
    public TextField getPasswordTextField() {
        return passwordTextField;
    }

}
