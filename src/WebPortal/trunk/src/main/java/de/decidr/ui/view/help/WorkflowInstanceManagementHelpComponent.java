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
 * and contains information related to workflow instance management
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class WorkflowInstanceManagementHelpComponent extends VerticalLayout {

    /**
	 * Serial version uid
	 */
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

    
    public WorkflowInstanceManagementHelpComponent(){
        setMargin(false, true, true, true);

        workflowModelsLabel = new Label("Please make sure that are a workflow admin<br/>"
                +"1) Login into DecidR and navigate to 'Create workflow instance' in the navigation bar.<br/>"
                +"2) If you are a workflow admin, the page lists you all workflow models for the current tenant.<br/><br/>",
                Label.CONTENT_XHTML);
        workflowModelsLabel.setVisible(false);
        workflowModelsButton = new Button("Where are my workflow models?", new ToggleLabelAction(workflowModelsLabel));
        workflowModelsButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(workflowModelsButton);
        this.addComponent(workflowModelsLabel);

        workflowInstancesLabel = new Label("1) Login into DecidR and navigate to 'Show workflow instances'<br/>"
                +"2) The page lists all running and completed workflow instances the workflow admin has already started.<br/><br/>",
                Label.CONTENT_XHTML);
        workflowInstancesLabel.setVisible(false);
        workflowInstancesButton = new Button("Where are my workflow instances?", new ToggleLabelAction(workflowInstancesLabel));
        workflowInstancesButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(workflowInstancesButton);
        this.addComponent(workflowInstancesLabel);

        workflowInstanceConfigurationLabel = new Label("1) Login into DecidR and navigate to 'Create workflow instance'<br/>"
                +"2) Select a workflow model you want to create a sequence from and click on the 'prepare' button and then on the 'save' button<br/><br/>",
                Label.CONTENT_XHTML);
        workflowInstanceConfigurationLabel.setVisible(false);
        workflowInstanceConfigurationButton = new Button("How to prepare workflow instance configuration?", new ToggleLabelAction(workflowInstanceConfigurationLabel));
        workflowInstanceConfigurationButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(workflowInstanceConfigurationButton);
        this.addComponent(workflowInstanceConfigurationLabel);

        createWorkflowInstanceLabel = new Label("1) Login into DecidR and navigate to the workflow model overview page.<br/>"
                +"2) Select the desired model and click on 'start instance'<br/>"
                +"3) Now you can configure the workflow instance through the configuration interface.<br/>" 
                +"4) Set the startup options of the new workflow instance and press on the 'start' button.<br/>"
                +"5) If all workflow participants are available and you selected 'start immediately', the system sends invitation mails to all workflow participants that aren’t already a member of the tenant, including unregistered users. The invitation mails contain a link that lets the receivers to participate without creating a user profile.<br/><br/>",
                Label.CONTENT_XHTML);
        createWorkflowInstanceLabel.setVisible(false);
        createWofkflowInstanceButton = new Button("How to create a workflow instance?", new ToggleLabelAction(createWorkflowInstanceLabel));
        createWofkflowInstanceButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(createWofkflowInstanceButton);
        this.addComponent(createWorkflowInstanceLabel);

        addUserToConfigurationLabel = new Label("1) Login into DecidR and navigate to the editing page of an instance.<br/>"
                +"2) Click on the 'add user' button and fill out the required fields.<br/><br/>",
                Label.CONTENT_XHTML);
        addUserToConfigurationLabel.setVisible(false);
        addUserToConfigurationButton = new Button("How to add a user to a prepared workflow instance configuration?", new ToggleLabelAction(addUserToConfigurationLabel));
        addUserToConfigurationButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(addUserToConfigurationButton);
        this.addComponent(addUserToConfigurationLabel);

        removeUserFromConfigurationLabel = new Label("1) Login into DecidR and navigate to the editing page of an instance.<br/>"
                +"2) Select a user to be deleted and click on the 'delete user' button.<br/><br/>",
                Label.CONTENT_XHTML);
        removeUserFromConfigurationLabel.setVisible(false);
        removeUserFromConfigurationButton = new Button("How to remove a user from a prepared workflow instance configuration?", new ToggleLabelAction(removeUserFromConfigurationLabel));
        removeUserFromConfigurationButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(removeUserFromConfigurationButton);
        this.addComponent(removeUserFromConfigurationLabel);

        completeWorkItemForUserLabel = new Label("1) Login into DecidR and navigate to the workflow instance overview page.<br/>"
                +"2) Select a 'running' workflow instance.<br/>"
                +"3) Click on the 'show work items' button.<br/>"
                +"4) For each work item in the list, the system displays the user to whom the work item was assigned to.<br/>"
                +"5) Now (if you are in a role of workflow admin) you can start and resume working on a work item.<br/>"
                +"6) Mark the work item you want as done.<br/><br/>",
                Label.CONTENT_XHTML);
        completeWorkItemForUserLabel.setVisible(false);
        completeWorkItemForUserButton = new Button("How to complete a work item for a user?", new ToggleLabelAction(completeWorkItemForUserLabel));
        completeWorkItemForUserButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(completeWorkItemForUserButton);
        this.addComponent(completeWorkItemForUserLabel);

        terminateWorkflowInstanceLabel = new Label("1) Login into DecidR and navigate to the workflow instance overview page.<br/>"
                +"2) Select a 'running' workflow instance.<br/>"
                +"3) Click on 'terminate' button and confirm the termination<br/><br/>",
                Label.CONTENT_XHTML);
        terminateWorkflowInstanceLabel.setVisible(false);
        terminateWorkflowInstanceButton = new Button("How to terminate a workflow instance?", new ToggleLabelAction(terminateWorkflowInstanceLabel));
        terminateWorkflowInstanceButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(terminateWorkflowInstanceButton);
        this.addComponent(terminateWorkflowInstanceLabel);
        
    }
    
}
