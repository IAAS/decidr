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
 * related to workflow instance management.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2377", currentReviewState = State.PassedWithComments)
public class WorkflowInstanceManagementHelpComponent extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    private Button workflowModelsButton = null;
    private Label workflowModelsLabel = null;

    private Button workflowInstancesButton = null;
    private Label workflowInstancesLabel = null;

    private Button workflowInstanceConfigurationButton = null;
    private Label workflowInstanceConfigurationLabel = null;

    private Button createWofkflowInstanceButton = null;
    private Label createWorkflowInstanceLabel = null;

    private Button addUserToConfigurationButton = null;
    private Label addUserToConfigurationLabel = null;

    private Button removeUserFromConfigurationButton = null;
    private Label removeUserFromConfigurationLabel = null;

    private Button completeWorkItemForUserButton = null;
    private Label completeWorkItemForUserLabel = null;

    private Button terminateWorkflowInstanceButton = null;
    private Label terminateWorkflowInstanceLabel = null;

    public WorkflowInstanceManagementHelpComponent() {
        setMargin(false, true, true, true);

        workflowModelsLabel = new Label(
                "Please make sure that you are a workflow admin!<br/><ol>"
                        + "<li>Log into DecidR and navigate to &quot;Create"
                        + " workflow instance&quot; in the navigation bar.</li>"
                        + "<li>If you are a workflow admin, the page will list all"
                        + " your workflow models for the current tenant.</li>"
                        + "</ol><br/>", Label.CONTENT_XHTML);
        workflowModelsLabel.setVisible(false);
        workflowModelsButton = new Button("Where are my workflow models?",
                new ToggleLabelAction(workflowModelsLabel));
        workflowModelsButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(workflowModelsButton);
        this.addComponent(workflowModelsLabel);

        workflowInstancesLabel = new Label(
                "<ol>"
                        + "<li>Log into DecidR and navigate to &quot;Show workflow instances&quot;.</li>"
                        + "<li>The page lists all running and completed workflow instances"
                        + " started by the workflow admin.</li>" + "</ol><br/>",
                Label.CONTENT_XHTML);
        workflowInstancesLabel.setVisible(false);
        workflowInstancesButton = new Button(
                "Where are my workflow instances?", new ToggleLabelAction(
                        workflowInstancesLabel));
        workflowInstancesButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(workflowInstancesButton);
        this.addComponent(workflowInstancesLabel);

        workflowInstanceConfigurationLabel = new Label(
                "<ol>"
                        + "<li>Log into DecidR and navigate to &quot;Create workflow instance&quot;.</li>"
                        + "<li>Select a workflow model you want to prepare a workflow instance"
                        + " for and click the &quot;Prepare&quot; button and then the"
                        + " &quot;Save&quot; button.</li>" + "</ol><br/>",
                Label.CONTENT_XHTML);
        workflowInstanceConfigurationLabel.setVisible(false);
        workflowInstanceConfigurationButton = new Button(
                "How do I prepare a workflow instance configuration?",
                new ToggleLabelAction(workflowInstanceConfigurationLabel));
        workflowInstanceConfigurationButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(workflowInstanceConfigurationButton);
        this.addComponent(workflowInstanceConfigurationLabel);

        createWorkflowInstanceLabel = new Label(
                "<ol>"
                        + "<li>Log into DecidR and navigate to the &quot;Workflow model overview&quot; page.</li>"
                        + "<li>Select the desired model and click on &quot;Start instance&quot;.</li>"
                        + "<li>Now you can configure the workflow instance through the configuration interface.</li>"
                        + "<li>Set the startup options of the new workflow instance and press on the &quot;Start&quot; button.</li>"
                        + "<li>If all workflow participants are available and you selected &quot;Start immediately&quot;,"
                        + " the system sends invitation emails to all workflow participants that aren’t already members of the tenant,"
                        + " including unregistered users. The invitation emails contain a link that lets the receivers participate"
                        + " without creating a user profile.</li>"
                        + "</ol><br/>", Label.CONTENT_XHTML);
        createWorkflowInstanceLabel.setVisible(false);
        createWofkflowInstanceButton = new Button(
                "How do I create a workflow instance?", new ToggleLabelAction(
                        createWorkflowInstanceLabel));
        createWofkflowInstanceButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(createWofkflowInstanceButton);
        this.addComponent(createWorkflowInstanceLabel);

        addUserToConfigurationLabel = new Label(
                "<ol>"
                        + "<li>Log into DecidR and navigate to the editing page of an instance.</li>"
                        + "<li>Click on the &quot;Add user&quot; button and fill in the required fields.</li>"
                        + "</ol><br/>", Label.CONTENT_XHTML);
        addUserToConfigurationLabel.setVisible(false);
        addUserToConfigurationButton = new Button(
                "How do I add a user to a prepared workflow instance configuration?",
                new ToggleLabelAction(addUserToConfigurationLabel));
        addUserToConfigurationButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(addUserToConfigurationButton);
        this.addComponent(addUserToConfigurationLabel);

        removeUserFromConfigurationLabel = new Label(
                "<ol>"
                        + "<li>Log into DecidR and navigate to the editing page of an instance.</li>"
                        + "<li>Select a user to be deleted and click on the &quot;Delete user&quot; button.</li>"
                        + "</ol><br/>", Label.CONTENT_XHTML);
        removeUserFromConfigurationLabel.setVisible(false);
        removeUserFromConfigurationButton = new Button(
                "How do I remove a user from a prepared workflow instance configuration?",
                new ToggleLabelAction(removeUserFromConfigurationLabel));
        removeUserFromConfigurationButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(removeUserFromConfigurationButton);
        this.addComponent(removeUserFromConfigurationLabel);

        completeWorkItemForUserLabel = new Label(
                "<ol>"
                        + "<li>Log into DecidR and navigate to the workflow instance overview page.</li>"
                        + "<li>Select a running workflow instance.</li>"
                        + "<li>Click on the &quot;Show workitems&quot; button.</li>"
                        + "<li>For each workitem in the list, the system displays the user to whom the workitem was assigned.</li>"
                        + "<li>Now (if you are a workflow admin) you can start and resume working on a workitem.</li>"
                        + "<li>Mark any workitem as done.</li>" + "</ol><br/>",
                Label.CONTENT_XHTML);
        completeWorkItemForUserLabel.setVisible(false);
        completeWorkItemForUserButton = new Button(
                "How to complete a workitem for a user?",
                new ToggleLabelAction(completeWorkItemForUserLabel));
        completeWorkItemForUserButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(completeWorkItemForUserButton);
        this.addComponent(completeWorkItemForUserLabel);

        terminateWorkflowInstanceLabel = new Label(
                "<ol>"
                        + "<li>Log into DecidR and navigate to the workflow instance overview page.</li>"
                        + "<li>Select a running workflow instance.</li>"
                        + "<li>Click on the &quot;Terminate&quot; button and confirm the termination.</li>"
                        + "</ol><br/>", Label.CONTENT_XHTML);
        terminateWorkflowInstanceLabel.setVisible(false);
        terminateWorkflowInstanceButton = new Button(
                "How do I terminate a workflow instance?",
                new ToggleLabelAction(terminateWorkflowInstanceLabel));
        terminateWorkflowInstanceButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(terminateWorkflowInstanceButton);
        this.addComponent(terminateWorkflowInstanceLabel);
    }
}
