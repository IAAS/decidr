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
 * This component is a part of the integrated manual and contains information
 * related to tenant user management
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class TenantUserManagementHelpComponent extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    private Button inviteUserButton = null;
    private Label inviteUserLabel = null;

    private Button removeUserButton = null;
    private Label removeUserLabel = null;

    private Button transferAdminButton = null;
    private Label transferAdminLabel = null;

    public TenantUserManagementHelpComponent() {
        setMargin(false, true, true, true);

        inviteUserLabel = new Label(
                "1) Login into DecidR and navigate to 'Edit user list' in the navigation bar.<br/>"
                        + "2) Navigate to the invitation sub-page.<br/>"
                        + "3) Select 'invite user' and enter a number of mail addresses and/or user names.<br/>"
                        + "4) If you are done, click on the send button.<br/><br/>",
                Label.CONTENT_XHTML);
        inviteUserLabel.setVisible(false);
        inviteUserButton = new Button("How to invite users to tenant?",
                new ToggleLabelAction(inviteUserLabel));
        inviteUserButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(inviteUserButton);
        this.addComponent(inviteUserLabel);

        removeUserLabel = new Label(
                "1) Login into DecidR and navigate to 'Edit users' in the navigation bar.<br/>"
                        + "2) Select one or more tenant members to remove.<br/>"
                        + "3) Click on the remove button and confirm the removal.<br/><br/>",
                Label.CONTENT_XHTML);
        removeUserLabel.setVisible(false);
        removeUserButton = new Button("How to remove users from tenant?",
                new ToggleLabelAction(removeUserLabel));
        removeUserButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(removeUserButton);
        this.addComponent(removeUserLabel);

        transferAdminLabel = new Label(
                "1) Login into DecidR and navigate to the user administration by clicking on 'User' in the navigation bar.<br/>"
                        + "2) Select the user you want to promote and click on 'promote to tenant admin' button.<br/>"
                        + "3 The system sends a notification email to the super admin and the designated tenant admin stating that the tenant admin role has been transferred.<br/><br/>",
                Label.CONTENT_XHTML);
        transferAdminLabel.setVisible(false);
        transferAdminButton = new Button(
                "How to transfer admin role to another user?",
                new ToggleLabelAction(transferAdminLabel));
        transferAdminButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(transferAdminButton);
        this.addComponent(transferAdminLabel);

    }

}
