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
 * related to workflow model editor.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2377", currentReviewState = State.PassedWithComments)
public class WorkflowModelEditorHelpComponent extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    private Button openModelButton = null;
    private Label openModelLabel = null;

    private Button clearModelButton = null;
    private Label clearModelLabel = null;

    private Button addActivityButton = null;
    private Label addActivityLabel = null;

    private Button addContainerButton = null;
    private Label addContainerLabel = null;

    private Button connectionButton = null;
    private Label connectionLabel = null;

    public WorkflowModelEditorHelpComponent() {
        setMargin(false, true, true, true);

        openModelLabel = new Label(
                "<ol>"
                        + "<li>Log into DecidR as tenant admin and navigate to the workflow"
                        + " modeling section by clicking on the &quot;Create/edit"
                        + " workflow models&quot; navigation link.</li>"
                        + "<li>Click on the &quot;Edit&quot; link next to the"
                        + " workflow model you want to edit.</li>"
                        + "</ol><br/>", Label.CONTENT_XHTML);
        openModelLabel.setVisible(false);
        openModelButton = new Button(
                "How do I open an existing model for editing?",
                new ToggleLabelAction(openModelLabel));
        openModelButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(openModelButton);
        this.addComponent(openModelLabel);

        clearModelLabel = new Label(
                "<ol>"
                        + "<li>Log into DecidR as tenant admin and navigate to the"
                        + " workflow modeling section by clicking on the"
                        + " &quot;Create/edit workflow models&quot; navigation link.</li>"
                        + "<li>Click on the &quot;Edit&quot; link next to workflow model you want to clear.</li>"
                        + "<li>The system shows the workflow in the modeling tool editor.</li>"
                        + "<li>Press the &quot;Clear&quot; button and confirm it.</li>"
                        + "</ol><br/>", Label.CONTENT_XHTML);
        clearModelLabel.setVisible(false);
        clearModelButton = new Button(
                "How do I remove all activities, containers,"
                        + " connections and variables from the workflow model?",
                new ToggleLabelAction(clearModelLabel));
        clearModelButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(clearModelButton);
        this.addComponent(clearModelLabel);

        addActivityLabel = new Label(
                "<ol>"
                        + "<li>You are the tenant admin. You have a workflow model"
                        + " and want to insert one of the available activities.</li>"
                        + "<li>Drag an activity icon from the palette onto the canvas.</li>"
                        + "<li>The editor adds the new activity to the workflow model"
                        + " and opens its properties for editing.</li>"
                        + "</ol><br/>", Label.CONTENT_XHTML);
        addActivityLabel.setVisible(false);
        addActivityButton = new Button("How do I insert a new activity?",
                new ToggleLabelAction(addActivityLabel));
        addActivityButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(addActivityButton);
        this.addComponent(addActivityLabel);

        addContainerLabel = new Label(
                "<ol>"
                        + "<li>You are the tenant admin. You have a workflow model"
                        + " and want to insert one of the available containers.</li>"
                        + "<li>Drag a container icon from the palette on the canvas.</li>"
                        + "<li>The editor adds the new container to the workflow model"
                        + " and opens its properties for editing.</li>"
                        + "</ol><br/>", Label.CONTENT_XHTML);
        addContainerLabel.setVisible(false);
        addContainerButton = new Button("How do I insert a new container?",
                new ToggleLabelAction(addContainerLabel));
        addContainerButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(addContainerButton);
        this.addComponent(addContainerLabel);

        connectionLabel = new Label(
                "<ol>"
                        + "<li>You are the tenant admin and you opened an existing workflow for editing.</li>"
                        + "<li>Click on the origin port and the destination port.</li>"
                        + "<li>The editor creates a connection between the selected ports.</li>"
                        + "</ol><br/>", Label.CONTENT_XHTML);
        connectionLabel.setVisible(false);
        connectionButton = new Button(
                "How do I connect activites and containers?",
                new ToggleLabelAction(connectionLabel));
        connectionButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(connectionButton);
        this.addComponent(connectionLabel);
    }
}
