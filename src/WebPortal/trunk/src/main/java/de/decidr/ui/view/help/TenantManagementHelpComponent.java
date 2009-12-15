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
 * related to tenant management.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2377", currentReviewState = State.PassedWithComments)
public class TenantManagementHelpComponent extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    private Button approvedTenantsButton = null;
    private Label approvedTenantsLabel = null;

    private Button deleteTenantButton = null;
    private Label deleteTenantLabel = null;

    private Button unapprovedTenantsButton = null;
    private Label unapprovedTenantsLabel = null;

    private Button approveTenantButton = null;
    private Label approveTenantLabel = null;

    private Button declineTenantButton = null;
    private Label declineTenantLabel = null;

    public TenantManagementHelpComponent() {
        setMargin(false, true, true, true);

        approvedTenantsLabel = new Label(
                // Aleks, GH: check whether this can be done with proper HTML
                // ~rr
                "1) Log in as super admin and navigate to the list of approved tenants by selecting"
                        + " &quot;Approved tenants&quot; in the administration navigation menu.<br/>"
                        + "2) The system displays a list of all approved tenants in the web browser.<br/><br/>",
                Label.CONTENT_XHTML);
        approvedTenantsLabel.setVisible(false);
        approvedTenantsButton = new Button(
                "How do I view a list of all approved tenants?",
                new ToggleLabelAction(approvedTenantsLabel));
        approvedTenantsButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(approvedTenantsButton);
        this.addComponent(approvedTenantsLabel);

        deleteTenantLabel = new Label(
                // Aleks, GH: check whether this can be done with proper HTML
                // ~rr
                "1) Log in as super admin and navigate to the list of approved tenants by selecting"
                        + " &quot;Approved tenants&quot; in the administration navigation menu.<br/>"
                        + "2) Select tenants you want to remove.<br/>"
                        + "3) Press the delete button and confirm the deletion.<br/>"
                        + "4) The system terminates all running workflow instances of the selected tenants,"
                        + " and then removes the selected tenants and all workflow models and workflow instances"
                        + " of the selected tenants.<br/><br/>",
                Label.CONTENT_XHTML);
        deleteTenantLabel.setVisible(false);
        deleteTenantButton = new Button("How do I delete tenants?",
                new ToggleLabelAction(deleteTenantLabel));
        deleteTenantButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(deleteTenantButton);
        this.addComponent(deleteTenantLabel);

        unapprovedTenantsLabel = new Label(
                // Aleks, GH: check whether this can be done with proper HTML
                // ~rr
                "1) You are the super admin. Navigate to the list of tenants that require approval "
                        + "by selecting &quot;Tenants to approve&quot; in the administration navigation menu.<br/>"
                        + "2) The system displays a list of all tenants that need approval"
                        + " and the user that is requesting to become tenant administrator.<br/><br/>",
                Label.CONTENT_XHTML);
        unapprovedTenantsLabel.setVisible(false);
        unapprovedTenantsButton = new Button(
                "How do I view a list of tenants to be approved?",
                new ToggleLabelAction(unapprovedTenantsLabel));
        unapprovedTenantsButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(unapprovedTenantsButton);
        this.addComponent(unapprovedTenantsLabel);

        approveTenantLabel = new Label(
                // Aleks, GH: check whether this can be done with proper HTML
                // ~rr
                "1) Log in as a super admin and navigate to the list of approved tenants by selecting"
                        + " &quot;Approved tenants&quot; in the administration navigation menu.<br/>"
                        + "2) The system displays a list of all tenants that need approval.<br/>"
                        + "3) Press the &quot;Approve&quot; button. The system will unlock the selected tenants.<br/>"
                        + "4) The system also sends an email to each tenant admin,"
                        + " informing them about the approval.<br/><br/>",
                Label.CONTENT_XHTML);
        approveTenantLabel.setVisible(false);
        approveTenantButton = new Button("How do I approve new tenants?",
                new ToggleLabelAction(approveTenantLabel));
        approveTenantButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(approveTenantButton);
        this.addComponent(approveTenantLabel);

        declineTenantLabel = new Label(
                // Aleks, GH: check whether this can be done with proper HTML
                // ~rr
                "1) Log in as super admin and navigate to the list of approved tenants by selecting"
                        + " &quot;Approved tenants&quot; in the administration navigation menu.<br/>"
                        + "2) The system displays a list of all tenants that need approval.<br/>"
                        + "3) Press the decline button. The system deletes the selected tenants.<br/>"
                        + "4) The system sends an email to each tenant admin informing them about"
                        + " the refusal to approve the new tenant.<br/><br/>",
                Label.CONTENT_XHTML);
        declineTenantLabel.setVisible(false);
        declineTenantButton = new Button(
                "How do I decline new tenant applications?",
                new ToggleLabelAction(declineTenantLabel));
        declineTenantButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(declineTenantButton);
        this.addComponent(declineTenantLabel);
    }
}
