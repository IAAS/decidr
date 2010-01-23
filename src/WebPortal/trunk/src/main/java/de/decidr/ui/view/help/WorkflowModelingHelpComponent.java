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
 * related to workflow modeling.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2377", currentReviewState = State.PassedWithComments)
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
                "<ol>"
                        + "<li>Log into DecidR and navigate to the workflow modeling section by clicking"
                        + " on the &quot;Create/edit workflow models&quot; navigation link.</li>"
                        + "<li>Click on the &quot;Create new model&quot; button.</li>"
                        + "<li>Fill out the input box named &quot;Create new model&quot; and confirm.</li>"
                        + "<li>Now the new workflow model will be displayed in the workflow model editor for further editing.</li>"
                        + "</ol><br/>", Label.CONTENT_XHTML);
        createNewWorkflowModelLabel.setVisible(false);
        createNewWorkflowModelButton = new Button(
                "How do I create a new workflow model?", new ToggleLabelAction(
                        createNewWorkflowModelLabel));
        createNewWorkflowModelButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(createNewWorkflowModelButton);
        this.addComponent(createNewWorkflowModelLabel);

        deleteWorkflowModelLabel = new Label(
                "<ol>"
                        + "<li>Log into DecidR and navigate to the workflow modeling section by clicking"
                        + " on the &quot;Create/edit workflow models&quot; navigation link.</li>"
                        + "<li>Select one or more models you want to delete and click on the &quot;Remove&quot; button.</li>"
                        + "<li>Confirm the deletion.</li>" + "</ol><br/>",
                Label.CONTENT_XHTML);
        deleteWorkflowModelLabel.setVisible(false);
        deleteWorkflowModelButton = new Button(
                "How do I delete workflow models?", new ToggleLabelAction(
                        deleteWorkflowModelLabel));
        deleteWorkflowModelButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(deleteWorkflowModelButton);
        this.addComponent(deleteWorkflowModelLabel);

        lockWorkflowModelLabel = new Label(
                "<ol>"
                        + "<li>Log into DecidR and navigate to the workflow modeling section by clicking"
                        + " on the &quot;Create/edit workflow models&quot; navigation link.</li>"
                        + "<li>Select one or more models you want to lock/unlock and press the &quot;Lock/unlock&quot; button.</li>"
                        + "<li>The system locks/unlock every selected workflow model.</li>"
                        + "</ol><br/>", Label.CONTENT_XHTML);
        lockWorkflowModelLabel.setVisible(false);
        lockWorkflowModelButton = new Button(
                "How do I lock/unlock workflow models?", new ToggleLabelAction(
                        lockWorkflowModelLabel));
        lockWorkflowModelButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(lockWorkflowModelButton);
        this.addComponent(lockWorkflowModelLabel);

        importWorkflowModelLabel = new Label(
                "<ol>"
                        + "<li>Log into DecidR as tenant admin and navigate to the workflow modeling section by clicking"
                        + " on the &quot;Create/edit workflow models&quot; navigation link.</li>"
                        + "<li>Click on the &quot;Import from file&quot; link.</li>"
                        + "<li>Choose the file you want to upload.</li>"
                        + "<li>If the uploaded file is a valid workflow model, the system adds the uploaded"
                        + " workflow model to the tenant.</li>" + "</ol><br/>",
                Label.CONTENT_XHTML);
        importWorkflowModelLabel.setVisible(false);
        importWorkflowModelButton = new Button(
                "How do I import a workflow model from a file?",
                new ToggleLabelAction(importWorkflowModelLabel));
        importWorkflowModelButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(importWorkflowModelButton);
        this.addComponent(importWorkflowModelLabel);

        exportWorkflowModelLabel = new Label(
                "<ol>"
                        + "<li>Log into DecidR as tenant admin and navigate to the workflow modeling section by clicking"
                        + " on the &quot;Create/edit workflow models&quot; navigation link.</li>"
                        + "<li>Select the workflow model you want to download and click on the &quot;Download&quot; link.</li>"
                        + "</ol><br/>", Label.CONTENT_XHTML);
        exportWorkflowModelLabel.setVisible(false);
        exportWorkflowModelButton = new Button(
                "How do I export a workflow model to a file?",
                new ToggleLabelAction(exportWorkflowModelLabel));
        exportWorkflowModelButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(exportWorkflowModelButton);
        this.addComponent(exportWorkflowModelLabel);

        publishWorkflowModelLabel = new Label(
                "<ol>"
                        + "<li>Log into DecidR as tenant admin and navigate to the workflow modeling section by clicking"
                        + " on the &quot;Create/edit workflow models&quot; navigation link.</li>"
                        + "<li>Select the workflow model you want to publish and click on the &quot;Publish&quot; button.</li>"
                        + "</ol><br/>", Label.CONTENT_XHTML);
        publishWorkflowModelLabel.setVisible(false);
        publishWorkflowModelButton = new Button(
                "How do I publish workflow models?", new ToggleLabelAction(
                        publishWorkflowModelLabel));
        publishWorkflowModelButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(publishWorkflowModelButton);
        this.addComponent(publishWorkflowModelLabel);

        unpublishWorkflowModelLabel = new Label(
                "<ol>"
                        + "<li>Log into DecidR as tenant admin and navigate to the workflow modeling section by clicking"
                        + " on the &quot;Create/edit workflow models&quot; navigation link.</li>"
                        + "<li>Remove the &quot;published&quot; flags from workflow models you want to unpublish.</li>"
                        + "</ol><br/>", Label.CONTENT_XHTML);
        unpublishWorkflowModelLabel.setVisible(false);
        unpublishWorkflowModelButton = new Button(
                "How do I unpublish workflow models?", new ToggleLabelAction(
                        unpublishWorkflowModelLabel));
        unpublishWorkflowModelButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(unpublishWorkflowModelButton);
        this.addComponent(unpublishWorkflowModelLabel);

        appointWorkflowAdminLabel = new Label(
                "<ol>"
                        + "<li>Log into DecidR as tenant admin and navigate to the workflow modeling section by clicking"
                        + " on the &quot;Models&quot; navigation link.</li>"
                        + "<li>Select a workflow model and then choose &quot;Appoint workflow admins&quot;.</li>"
                        + "<li>Choose one of the following options from the selection field:<ol>"
                        + "<li>Appoint yourself as the workflow admin.</li>"
                        + "<li>Enter the usernames of registered DecidR users.</li>"
                        + "<li>Enter the email addresses of the desired workflow admins.</li></ol><li>"
                        + "<li>Confirm it be pressing the &quot;OK&quot; button.</li>"
                        + "</ol><br/>", Label.CONTENT_XHTML);
        appointWorkflowAdminLabel.setVisible(false);
        appointWorkflowAdminButton = new Button(
                "How do I appoint workflow admins?", new ToggleLabelAction(
                        appointWorkflowAdminLabel));
        appointWorkflowAdminButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(appointWorkflowAdminButton);
        this.addComponent(appointWorkflowAdminLabel);
    }
}
