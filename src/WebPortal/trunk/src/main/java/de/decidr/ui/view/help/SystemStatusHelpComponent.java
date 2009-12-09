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
 * related to system status
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class SystemStatusHelpComponent extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    private Button allWorkflowModelsButton = null;
    private Label allWorkflowModelsLabel = null;

    private Button runningInstancesButton = null;
    private Label runningInstancesLabel = null;

    public SystemStatusHelpComponent() {
        setMargin(false, true, true, true);

        allWorkflowModelsLabel = new Label(
                "1) Login into DecidR as tenant admin and navigate to the workflow modeling section by clicking on the 'Models' navigation link.<br/>"
                        + "2) The system displays a list of all workflow models coming from all tenants in the system, including the default tenant.<br/><br/>",
                Label.CONTENT_XHTML);
        allWorkflowModelsLabel.setVisible(false);
        allWorkflowModelsButton = new Button(
                "How to view a list of allw workflow models?",
                new ToggleLabelAction(allWorkflowModelsLabel));
        allWorkflowModelsButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(allWorkflowModelsButton);
        this.addComponent(allWorkflowModelsLabel);

        runningInstancesLabel = new Label(
                "1) Login into DecidR as tenant admin and navigate to the workflow modeling section by clicking on the 'Instances' navigation link.<br/>"
                        + "2) The system displays a list of all running workflow instances coming from all tenants in the system, including the default tenant.<br/><br/>",
                Label.CONTENT_XHTML);
        runningInstancesLabel.setVisible(false);
        runningInstancesButton = new Button(
                "How to view a list of all running instances?",
                new ToggleLabelAction(runningInstancesLabel));
        runningInstancesButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(runningInstancesButton);
        this.addComponent(runningInstancesLabel);

    }

}
