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

import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.Action.Handler;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.controller.authentication.LoginAction;
import de.decidr.ui.controller.authentication.LoginWithInvitationAction;
import de.decidr.ui.controller.show.ShowRegistrationAction;
import de.decidr.ui.controller.show.ShowResetPasswordAction;
import de.decidr.ui.view.windows.InvitationDialogComponent;

/**
 * With the login component the user is able to authenticate himself to the
 * application. He has to insert his username and his password.
 * 
 * @author AT
 */
public class LoginComponent extends CustomComponent implements Handler {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 6622328174696591882L;

    private VerticalLayout verticalLayout = null;

    private Label loginLabel = null;

    private TextField usernameTextField = null;
    private TextField passwordTextField = null;
    private Panel invisiblePanel = null;

    private Button loginButton = null;
    private Button forgotPasswordButton = null;
    private Button registerButton = null;
    private Button.ClickListener loginListener = null;
    
    private ShortcutAction loginAction = new ShortcutAction("Login", ShortcutAction.KeyCode.ENTER, null);

    /**
     * Default constructor.
     */
    public LoginComponent() {
        init(null);
    }

    /**
     * Default constructor.
     */
    public LoginComponent(InvitationDialogComponent invD) {
        init(invD);
    }

    /**
     * Returns the password text field.
     * 
     * @return passwordTextField
     */
    public TextField getPasswordTextField() {
        return passwordTextField;
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
     * This method initializes the components for the login component.
     */
    // Aleks, GH: use a form/add a listener/do whatever is necessary to allow
    // pressing enter to log in ~rr
    private void init(InvitationDialogComponent invD) {
        verticalLayout = new VerticalLayout();

        invisiblePanel = new Panel();
        invisiblePanel.setVisible(false);
        invisiblePanel.addActionHandler(this);
        
        loginLabel = new Label("<h3>Login:</h3>");
        loginLabel.setContentMode(Label.CONTENT_XHTML);

        usernameTextField = new TextField();
        usernameTextField.setCaption("Username");
        passwordTextField = new TextField();
        passwordTextField.setCaption("Password");
        passwordTextField.setSecret(true);

        if (invD == null) {
            loginListener = new LoginAction();
            loginButton = new Button("Login", loginListener);
            usernameTextField.focus();
        } else {
            loginListener = new LoginWithInvitationAction(invD);
            loginButton = new Button("Login", loginListener);
            loginButton.focus();
        }

        forgotPasswordButton = new Button("Forgot your password?",
                new ShowResetPasswordAction());
        forgotPasswordButton.setStyleName(Button.STYLE_LINK);
        registerButton = new Button("Register", new ShowRegistrationAction());
        registerButton.setStyleName(Button.STYLE_LINK);

        setCompositionRoot(verticalLayout);

        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(true);
        verticalLayout.addComponent(invisiblePanel);
        verticalLayout.addComponent(loginLabel);
        verticalLayout.addComponent(usernameTextField);
        verticalLayout.addComponent(passwordTextField);
        verticalLayout.addComponent(loginButton);
        verticalLayout.addComponent(forgotPasswordButton);
        verticalLayout.addComponent(registerButton);
        

    }

    /* (non-Javadoc)
     * @see com.vaadin.event.Action.Handler#getActions(java.lang.Object, java.lang.Object)
     */
    @Override
    public Action[] getActions(Object target, Object sender) {
        return new Action[]{ loginAction};
    }

    /* (non-Javadoc)
     * @see com.vaadin.event.Action.Handler#handleAction(com.vaadin.event.Action, java.lang.Object, java.lang.Object)
     */
    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if (action == loginAction ){
            loginListener.buttonClick(loginButton.new ClickEvent( loginButton ));
        }
        
    }
}
