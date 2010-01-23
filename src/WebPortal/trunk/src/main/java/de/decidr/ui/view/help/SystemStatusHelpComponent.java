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
 * related to system status.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2377", currentReviewState = State.PassedWithComments)
public class SystemStatusHelpComponent extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    private Button allWorkflowModelsButton = null;
    private Label allWorkflowModelsLabel = null;

    private Button runningInstancesButton = null;
    private Label runningInstancesLabel = null;

    public SystemStatusHelpComponent() {
        setMargin(false, true, true, true);

        allWorkflowModelsLabel = new Label(
                "<ol>"
                        + "<li>Login into DecidR as tenant admin and navigate to the workflow modeling section by clicking on the &quot;Models&quot; navigation link.</li>"
                        + "<li>The system displays a list of all workflow models coming from all tenants in the system, including the default tenant.</li>"
                        + "</ol><br/>", Label.CONTENT_XHTML);
        allWorkflowModelsLabel.setVisible(false);
        allWorkflowModelsButton = new Button(
                "How do I display a list of all workflow models?",
                new ToggleLabelAction(allWorkflowModelsLabel));
        allWorkflowModelsButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(allWorkflowModelsButton);
        this.addComponent(allWorkflowModelsLabel);

        runningInstancesLabel = new Label(
                "<ol>"
                        + "<li>Login into DecidR as tenant admin and navigate to the workflow modeling section by clicking on the 'Instances' navigation link.</li>"
                        + "<li>The system displays a list of all running workflow instances coming from all tenants in the system, including the default tenant.</li>"
                        + "</ol><br/>", Label.CONTENT_XHTML);
        runningInstancesLabel.setVisible(false);
        runningInstancesButton = new Button(
                "How do I display a list of all running instances?",
                new ToggleLabelAction(runningInstancesLabel));
        runningInstancesButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(runningInstancesButton);
        this.addComponent(runningInstancesLabel);
    }
}
