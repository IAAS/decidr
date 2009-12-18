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

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;

/**
 * This component is part of the integrated manual and contains information
 * related to user management.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2377", currentReviewState = State.PassedWithComments)
public class UserManagementHelpComponent extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    private Button userListButton = null;
    private Label userListLabel = null;

    private Button disableAccountButton = null;
    private Label disableAccountLabel = null;

    public UserManagementHelpComponent() {
        setMargin(false, true, true, true);

        userListLabel = new Label("<ol>"
                + "<li>Log into DecidR as tenant admin and navigate to the workflow modeling section"
                + " by clicking on the &quot;Users&quot; navigation link.</li>"
                + "<li>The system displays a list of all users in the web browser.</li>"
                + "</ol><br/>",
                Label.CONTENT_XHTML);
        userListLabel.setVisible(false);
        userListButton = new Button("How do I view the list of users?",
                new ToggleLabelAction(userListLabel));
        userListButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(userListButton);
        this.addComponent(userListLabel);

        disableAccountLabel = new Label("<ol>"
                + "<li>Log into DecidR as tenant admin and navigate to the workflow"
                + " modeling section by clicking on the &quot;Users&quot;"
                + " navigation link.</li>"
                + "<li>Select a number of user accounts to disable.</li>"
                + "<li>The system flags each selected user as disabled"
                + " unless the user is the super admin himself.</li>"
                + "</ol><br/>",
                Label.CONTENT_XHTML);
        disableAccountLabel.setVisible(false);
        disableAccountButton = new Button("How do I disable user accounts?",
                new ToggleLabelAction(disableAccountLabel));
        disableAccountButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(disableAccountButton);
        this.addComponent(disableAccountLabel);
    }
}
