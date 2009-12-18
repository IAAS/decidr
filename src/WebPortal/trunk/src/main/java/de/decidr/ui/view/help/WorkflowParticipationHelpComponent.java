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
 * related to workflow participation.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2377", currentReviewState = State.PassedWithComments)
public class WorkflowParticipationHelpComponent extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    private Button currentTenantWorkItemsButton = null;
    private Label currentTenantWorkItemsLabel = null;

    private Button allTenantWorkItemsButton = null;
    private Label allTenantWorkItemsLabel = null;

    private Button startWorkOnItemButton = null;
    private Label startWorkOnItemLabel = null;

    private Button resumeWorkOnItemButton = null;
    private Label resumeWorkOnItemLabel = null;

    public WorkflowParticipationHelpComponent() {
        setMargin(false, true, true, true);

        currentTenantWorkItemsLabel = new Label("<ol>"
                + "<li>Log into DecidR and navigate to the &quot;My Workitems&quot;"
                + " in the navigation bar.</li>"
                + "<li>The page shows you a list of your workitems"
                + " for the current tenant.</li>"
                + "</ol><br/>",
                Label.CONTENT_XHTML);
        currentTenantWorkItemsLabel.setVisible(false);
        currentTenantWorkItemsButton = new Button(
                "How do I display workitems for the current tenant?",
                new ToggleLabelAction(currentTenantWorkItemsLabel));
        currentTenantWorkItemsButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(currentTenantWorkItemsButton);
        this.addComponent(currentTenantWorkItemsLabel);

        allTenantWorkItemsLabel = new Label("<ol>"
                + "<li>Log into DecidR and navigate to the &quot;My"
                + " Workitems&quot; in the navigation bar.</li>"
                + "<li>Select &quot;All tenants&quot; from the"
                + " combo box on the workitem page.</li>"
                + "<li>The page shows you a list of your"
                + " workitems for all tenants.</li>"
                + "</ol><br/>",
                Label.CONTENT_XHTML);
        allTenantWorkItemsLabel.setVisible(false);
        allTenantWorkItemsButton = new Button(
                "How do I display workitems for all tenants?",
                new ToggleLabelAction(allTenantWorkItemsLabel));
        allTenantWorkItemsButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(allTenantWorkItemsButton);
        this.addComponent(allTenantWorkItemsLabel);

        startWorkOnItemLabel = new Label("<ol>"
                + "<li>Log into DecidR and navigate to &quot;My Workitems&quot; in the navigation bar.</li>"
                + "<li>Select the item you want to work on and click on the &quot;Work on Item&quot; link.</li>"
                + "<li>DecidR opens a new window that contains one or more input fields.</li>"
                + "<li>Fill out the required fields and click on the &quot;Save&quot; button.</li>"
                + "</ol><br/>",
                Label.CONTENT_XHTML);
        startWorkOnItemLabel.setVisible(false);
        startWorkOnItemButton = new Button(
                "How do I start working on a workitem?", new ToggleLabelAction(
                        startWorkOnItemLabel));
        startWorkOnItemButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(startWorkOnItemButton);
        this.addComponent(startWorkOnItemLabel);

        resumeWorkOnItemLabel = new Label("<ol>"
                + "<li>Log into DecidR and navigate to &quot;My Workitems&quot; in the navigation bar.</li>"
                + "<li>All items you already started have the status &quot;In progress&quot;."
                + " Select the item you want to work on and click on the &quot;Work on Item&quot; link.</li>"
                + "</ol><br/>",
                Label.CONTENT_XHTML);
        resumeWorkOnItemLabel.setVisible(false);
        resumeWorkOnItemButton = new Button(
                "How do I register as tenant admin?", new ToggleLabelAction(
                        resumeWorkOnItemLabel));
        resumeWorkOnItemButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(resumeWorkOnItemButton);
        this.addComponent(resumeWorkOnItemLabel);
    }
}
