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
 * related to tenant user management.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2377", currentReviewState = State.PassedWithComments)
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
                // Aleks, GH: check whether this can be done with proper HTML
                // ~rr
                "1) Log into DecidR and navigate to &quot;Edit user list&quot; in the navigation bar.<br/>"
                        + "2) Navigate to the invitation sub-page.<br/>"
                        + "3) Select &quot;invite user&quot; and enter a number of email addresses and/or user names.<br/>"
                        + "4) If you are done, click on the &quot;Send&quot; button.<br/><br/>",
                Label.CONTENT_XHTML);
        inviteUserLabel.setVisible(false);
        inviteUserButton = new Button("How do I invite users to a tenant?",
                new ToggleLabelAction(inviteUserLabel));
        inviteUserButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(inviteUserButton);
        this.addComponent(inviteUserLabel);

        removeUserLabel = new Label(
                // Aleks, GH: check whether this can be done with proper HTML
                // ~rr
                "1) Log into DecidR and navigate to &quot;Edit users&quot; in the navigation bar.<br/>"
                        + "2) Select one or more tenant members to remove.<br/>"
                        + "3) Click on the &quot;Remove&quot; button and confirm the removal.<br/><br/>",
                Label.CONTENT_XHTML);
        removeUserLabel.setVisible(false);
        removeUserButton = new Button("How do I remove users from a tenant?",
                new ToggleLabelAction(removeUserLabel));
        removeUserButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(removeUserButton);
        this.addComponent(removeUserLabel);

        transferAdminLabel = new Label(
                // Aleks, GH: check whether this can be done with proper HTML
                // ~rr
                "1) Log into DecidR and navigate to the user administration by clicking on"
                        + " &quot;User&quot; in the navigation bar.<br/>"
                        + "2) Select the user you want to promote and click on the "
                        + "&quot;Promote to tenant admin&quot; button.<br/>"
                        + "3) The system sends a notification email to the super admin "
                        // Aleks, GH: Not sure here: doesn't the new tenant
                        // admin have a say in this? No confirmation
                        // "yes I want to administrate this tenant"? ~rr
                        + "and the designated tenant admin, stating that the tenant "
                        + "admin role has been transferred.<br/><br/>",
                Label.CONTENT_XHTML);
        transferAdminLabel.setVisible(false);
        transferAdminButton = new Button(
                "How do I transfer my admin role to another user?",
                new ToggleLabelAction(transferAdminLabel));
        transferAdminButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(transferAdminButton);
        this.addComponent(transferAdminLabel);
    }
}
