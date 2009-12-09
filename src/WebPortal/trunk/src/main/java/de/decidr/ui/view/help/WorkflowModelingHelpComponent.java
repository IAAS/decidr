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
 * related to workflow modeling
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class WorkflowModelingHelpComponent extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    private Button createNewWorkflowModelButton = null;
    private Label createNewWorkflowModelLabel = null;

    private Button deleteWorkflowModelButton = null;
    private Label deleteWorkflowModelLabel = null;

    private Button lockWorkflowModelButton = null;
    private Label lockWorkflowModelLabel = null;

    private Button importWorkflowModelButton = null;
    private Label importWorkflowModelLabel = null;

    private Button exportWorkflowModelButton = null;
    private Label exportWorkflowModelLabel = null;

    private Button publishWorkflowModelButton = null;
    private Label publishWorkflowModelLabel = null;

    private Button unpublishWorkflowModelButton = null;
    private Label unpublishWorkflowModelLabel = null;

    private Button appointWorkflowAdminButton = null;
    private Label appointWorkflowAdminLabel = null;

    public WorkflowModelingHelpComponent() {
        setMargin(false, true, true, true);

        createNewWorkflowModelLabel = new Label(
                "1) Login into DecidR and navigate to the workflow modeling section by clicking on the 'create/ edit workflow models' navigation link.<br/>"
                        + "2) Click on the 'Create new model' Button<br/>"
                        + "3) Fill out the input box named 'Create new model' and confirm it.<br/>"
                        + "4) Now you can see the new workflow model in the workflow model editor for further editing.<br/><br/>",
                Label.CONTENT_XHTML);
        createNewWorkflowModelLabel.setVisible(false);
        createNewWorkflowModelButton = new Button(
                "How to create a new workflow model?", new ToggleLabelAction(
                        createNewWorkflowModelLabel));
        createNewWorkflowModelButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(createNewWorkflowModelButton);
        this.addComponent(createNewWorkflowModelLabel);

        deleteWorkflowModelLabel = new Label(
                "1) Login into DecidR and navigate to the workflow modeling section by clicking on the 'create/edit workflow models' navigation link.<br/>"
                        + "2) Select one or more models you want to delete and click on the remove button.<br/>"
                        + "3) Confirm the deletion.<br/><br/>",
                Label.CONTENT_XHTML);
        deleteWorkflowModelLabel.setVisible(false);
        deleteWorkflowModelButton = new Button(
                "How to delete workflow models?", new ToggleLabelAction(
                        deleteWorkflowModelLabel));
        deleteWorkflowModelButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(deleteWorkflowModelButton);
        this.addComponent(deleteWorkflowModelLabel);

        lockWorkflowModelLabel = new Label(
                "1) Login into DecidR and navigate to the workflow modeling section by clicking on the 'create/edit workflow models' navigation link.<br/>"
                        + "2) Select one or more models you want to lock/unlock and press the lock/unlock button.<br/>"
                        + "3) The system locks/unlock each selected workflow model<br/><br/>",
                Label.CONTENT_XHTML);
        lockWorkflowModelLabel.setVisible(false);
        lockWorkflowModelButton = new Button(
                "How to lock/unlock workflow models?", new ToggleLabelAction(
                        lockWorkflowModelLabel));
        lockWorkflowModelButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(lockWorkflowModelButton);
        this.addComponent(lockWorkflowModelLabel);

        importWorkflowModelLabel = new Label(
                "1) Login into DecidR as tenant admin and navigate to the workflow modeling section by clicking on the 'create/edit workflow models' navigation link.<br/>"
                        + "2) Click on the 'Import from file' link.<br/>"
                        + "3) Choose the file from you local computer you want to upload.<br/>"
                        + "4) If the uploaded file is a valid workflow model the system adds the uploaded workflow model to the tenant.<br/><br/>",
                Label.CONTENT_XHTML);
        importWorkflowModelLabel.setVisible(false);
        importWorkflowModelButton = new Button(
                "How to import workflow model file?", new ToggleLabelAction(
                        importWorkflowModelLabel));
        importWorkflowModelButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(importWorkflowModelButton);
        this.addComponent(importWorkflowModelLabel);

        exportWorkflowModelLabel = new Label(
                "1) Login into DecidR as tenant admin and navigate to the workflow modeling section by clicking on the 'create/edit workflow models' navigation link.<br/>"
                        + "2) Select and the workflow model you want to download and click on the 'download' link.<br/><br/>",
                Label.CONTENT_XHTML);
        exportWorkflowModelLabel.setVisible(false);
        exportWorkflowModelButton = new Button(
                "How to export workflow model file?", new ToggleLabelAction(
                        exportWorkflowModelLabel));
        exportWorkflowModelButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(exportWorkflowModelButton);
        this.addComponent(exportWorkflowModelLabel);

        publishWorkflowModelLabel = new Label(
                "1) Login into DecidR as tenant admin and navigate to the workflow modeling section by clicking on the 'create/edit workflow models' navigation link.<br/>"
                        + "2) Select the workflow model you want to publish and click on the publish button.<br/><br/>",
                Label.CONTENT_XHTML);
        publishWorkflowModelLabel.setVisible(false);
        publishWorkflowModelButton = new Button(
                "How to publish workflow models?", new ToggleLabelAction(
                        publishWorkflowModelLabel));
        publishWorkflowModelButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(publishWorkflowModelButton);
        this.addComponent(publishWorkflowModelLabel);

        unpublishWorkflowModelLabel = new Label(
                "1) Login into DecidR as tenant admin and navigate to the workflow modeling section by clicking on the 'create/edit workflow models' navigation link.<br/>"
                        + "2) Remove the published flags from  workflow model s you want to unpublish.<br/><br/>",
                Label.CONTENT_XHTML);
        unpublishWorkflowModelLabel.setVisible(false);
        unpublishWorkflowModelButton = new Button(
                "How to unpublish workflow models?", new ToggleLabelAction(
                        unpublishWorkflowModelLabel));
        unpublishWorkflowModelButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(unpublishWorkflowModelButton);
        this.addComponent(unpublishWorkflowModelLabel);

        appointWorkflowAdminLabel = new Label(
                "1) Login into DecidR as tenant admin and navigate to the workflow modeling section by clicking on the 'Models' navigation link.<br/>"
                        + "2) Select a workflow model and then choose 'appoint workflow admins'.<br/>"
                        + "3) Choose in the input box one of the following options:<br/>"
                        + "3.a) Appoint yourself as the workflow admin<br/>"
                        + "3.b) Enter the usernames of registered DecidR users.<br/>"
                        + "3.c) Enter the email addresses of the desired workflow admins<br/>"
                        + "4) Confirm it be pressing the OK button.<br/><br/>",
                Label.CONTENT_XHTML);
        appointWorkflowAdminLabel.setVisible(false);
        appointWorkflowAdminButton = new Button(
                "How to appoint workflow admins?", new ToggleLabelAction(
                        appointWorkflowAdminLabel));
        appointWorkflowAdminButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(appointWorkflowAdminButton);
        this.addComponent(appointWorkflowAdminLabel);

    }
}
