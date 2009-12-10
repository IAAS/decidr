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

import java.util.LinkedList;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.ui.controller.TerminateWorkflowInstanceAction;
import de.decidr.ui.data.CompletedInstancesContainer;
import de.decidr.ui.data.RunningInstanceContainer;
import de.decidr.ui.view.tables.CompletedInstanceTable;
import de.decidr.ui.view.tables.RunningInstanceTable;

/**
 * In this component all workflow instances are represented. These instances are
 * the running workflow instances and the completed workflow instance. They are
 * represented in tables.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "TK", "JS" }, lastRevision = "2377", currentReviewState = State.Passed)
public class WorkflowInstanceComponent extends CustomComponent {

    private static final long serialVersionUID = -8769293137331802152L;

    private RunningInstanceContainer runningInstanceContainer = null;

    private CompletedInstancesContainer completedInstanceContainer = null;

    private VerticalLayout verticalLayout = null;

    private VerticalLayout tableVerticalLayout = null;

    private SearchPanel searchPanel = null;

    private Panel tablePanel = null;

    private Label workflowInstanceLabel = null;
    private Label runningWorkflowInstanceLabel = null;
    private Label completedWorkflowInstanceLabel = null;

    private RunningInstanceTable runningInstanceTable = null;
    private CompletedInstanceTable completedInstanceTable = null;

    private Button terminateButton = null;

    private ButtonPanel buttonPanel = null;

    private List<Button> buttonList = new LinkedList<Button>();

    /**
     * Default constructor.
     */
    public WorkflowInstanceComponent() {
        init();
    }

    /**
     * This method initializes the components of the workflow instance component.
     */
    private void init() {
        runningInstanceContainer = new RunningInstanceContainer();

        completedInstanceContainer = new CompletedInstancesContainer();

        verticalLayout = new VerticalLayout();

        tableVerticalLayout = new VerticalLayout();

        workflowInstanceLabel = new Label("<h2> Workflow instances </h2>");
        workflowInstanceLabel.setContentMode(Label.CONTENT_XHTML);
        runningWorkflowInstanceLabel = new Label(
                "<h3> Running workflow instances </h3>");
        runningWorkflowInstanceLabel.setContentMode(Label.CONTENT_XHTML);
        completedWorkflowInstanceLabel = new Label(
                "<h3> Completed workflow instances </h3>");
        completedWorkflowInstanceLabel.setContentMode(Label.CONTENT_XHTML);

        runningInstanceTable = new RunningInstanceTable(
                runningInstanceContainer);

        searchPanel = new SearchPanel(runningInstanceTable);

        tablePanel = new Panel();

        completedInstanceTable = new CompletedInstanceTable(
                completedInstanceContainer);

        setCompositionRoot(verticalLayout);

        initButtonPanel();

        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(workflowInstanceLabel);
        verticalLayout.addComponent(searchPanel);

        tablePanel.setContent(tableVerticalLayout);
        tablePanel.setScrollable(true);

        tableVerticalLayout.setSpacing(true);
        tableVerticalLayout.addComponent(runningWorkflowInstanceLabel);
        tableVerticalLayout.addComponent(runningInstanceTable);
        tableVerticalLayout.addComponent(completedWorkflowInstanceLabel);
        tableVerticalLayout.addComponent(completedInstanceTable);

        verticalLayout.addComponent(tablePanel);
        verticalLayout.addComponent(buttonPanel);
    }

    private void initButtonPanel() {
        terminateButton = new Button("Terminate instance",
                new TerminateWorkflowInstanceAction(runningInstanceTable));

        buttonList.add(terminateButton);

        buttonPanel = new ButtonPanel(buttonList);
        buttonPanel.setCaption("Edit instance");
    }

}
