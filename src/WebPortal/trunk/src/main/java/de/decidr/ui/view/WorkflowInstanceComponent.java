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
package de.decidr.ui.view;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.data.CompletedInstancesContainer;
import de.decidr.ui.data.RunningInstanceContainer;

/**
 * In this component all workflow instances are represent. These instances are
 * the running workflow instances and the completed workflow instance. They
 * represented in tables.
 * 
 * @author AT
 */
public class WorkflowInstanceComponent extends CustomComponent {

    /**
     * 
     */
    private static final long serialVersionUID = -8769293137331802152L;

    private RunningInstanceContainer runningInstanceContainer = null;

    private CompletedInstancesContainer completedInstanceContainer = null;

    private VerticalLayout verticalLayout = null;

    private SearchPanel searchPanel = null;

    private Label workflowInstanceLabel = null;
    private Label runningWorkflowInstanceLabel = null;
    private Label completedWorkflowInstanceLabel = null;

    private RunningInstanceTable runningInstanceTable = null;
    private CompletedInstanceTable completedInstanceTable = null;

    /**
     * Default constructor
     * 
     */
    public WorkflowInstanceComponent() {
        init();
    }

    /**
     * 
     * This method initializes the components of the workflow instance component
     * 
     */
    private void init() {
        runningInstanceContainer = new RunningInstanceContainer();

        completedInstanceContainer = new CompletedInstancesContainer();

        verticalLayout = new VerticalLayout();

        workflowInstanceLabel = new Label("<h2> Workflow instances </h2>");
        workflowInstanceLabel.setContentMode(Label.CONTENT_XHTML);
        runningWorkflowInstanceLabel = new Label(
                "<h3> Running workflow instances </h3>");
        runningWorkflowInstanceLabel.setContentMode(Label.CONTENT_XHTML);
        completedWorkflowInstanceLabel = new Label(
                "<h3> Completed workflow instances </h3>");
        completedWorkflowInstanceLabel.setContentMode(Label.CONTENT_XHTML);

        runningInstanceTable = new RunningInstanceTable(runningInstanceContainer);

        searchPanel = new SearchPanel(runningInstanceTable);

        completedInstanceTable = new CompletedInstanceTable(completedInstanceContainer);

        setCompositionRoot(verticalLayout);

        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(workflowInstanceLabel);
        verticalLayout.addComponent(searchPanel);

        verticalLayout.addComponent(runningWorkflowInstanceLabel);
        verticalLayout.addComponent(runningInstanceTable);
        verticalLayout.addComponent(completedWorkflowInstanceLabel);
        verticalLayout.addComponent(completedInstanceTable);
    }

}
