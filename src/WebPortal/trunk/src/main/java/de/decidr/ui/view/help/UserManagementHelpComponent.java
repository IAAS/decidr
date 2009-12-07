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

package de.decidr.ui.view.help;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * This component is a part of the integrated manual
 * and contains information related to user management
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class UserManagementHelpComponent extends VerticalLayout {

    /**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;
	private Button userListButton = null;
    private Label userListLabel = null;

    private Button disableAccountButton = null;
    private Label disableAccountLabel = null;
    
    public UserManagementHelpComponent(){
        setMargin(false, true, true, true);

        userListLabel = new Label("1) Login into DecidR as tenant admin and navigate to the workflow modeling section by clicking on the 'Users' navigation link.<br/>"
                +"2) The system displays a list of all users in the web browser.<br/><br/>",
                Label.CONTENT_XHTML);
        userListLabel.setVisible(false);
        userListButton = new Button("How to view the list of users?", new ToggleLabelAction(userListLabel));
        userListButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(userListButton);
        this.addComponent(userListLabel);

        disableAccountLabel = new Label("1) Login into DecidR as tenant admin and navigate to the workflow modeling section by clicking on the 'Users' navigation link.<br/>"
                +"2) Select a number of user accounts to disable.<br/>"
                +"3) The system flags each selected user as disabled unless the user is the super user admin himself.<br/><br/>",
                Label.CONTENT_XHTML);
        disableAccountLabel.setVisible(false);
        disableAccountButton = new Button("How to disable user accounts?", new ToggleLabelAction(disableAccountLabel));
        disableAccountButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(disableAccountButton);
        this.addComponent(disableAccountLabel);

    }
    
}
