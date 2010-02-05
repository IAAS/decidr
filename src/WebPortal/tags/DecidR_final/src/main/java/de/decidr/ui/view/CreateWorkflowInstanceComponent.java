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
import com.vaadin.ui.VerticalLayout;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.ui.controller.show.ShowStartConfigurationWindowAction;
import de.decidr.ui.data.WorkflowInstanceContainer;
import de.decidr.ui.view.tables.WorkflowInstanceTable;

/**
 * In this component the user can create a workflow instance from a workflow
 * model. The user has to choose a workflow model from a table and push the
 * &quot;Create&quot; button.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2151", currentReviewState = State.PassedWithComments)
public class CreateWorkflowInstanceComponent extends CustomComponent {

    private static final long serialVersionUID = -2283442464298218331L;

    private WorkflowInstanceContainer workflowInstanceContainer = null;

    private VerticalLayout verticalLayout = null;

    private SearchPanel searchPanel = null;

    private Label createWorkflowInstanceLabel = null;

    private WorkflowInstanceTable instanceTable = null;

    private List<Button> buttonList = new LinkedList<Button>();

    private Button createInstanceButton = null;

    private ButtonPanel buttonPanel = null;

    /**
     * TODO document
     */
    public CreateWorkflowInstanceComponent() {
        init();
    }

    /**
     * @return instanceTable the workflow {@link WorkflowInstanceTable}.
     */
    public WorkflowInstanceTable getInstanceTable() {
        return instanceTable;
    }

    /**
     * This method initializes the components of the
     * {@link CreateWorkflowInstanceComponent}.
     */
    private void init() {
        verticalLayout = new VerticalLayout();

        workflowInstanceContainer = new WorkflowInstanceContainer();

        createWorkflowInstanceLabel = new Label(
                "<h2>Create Workflow Instance</h2>");
        createWorkflowInstanceLabel.setContentMode(Label.CONTENT_XHTML);

        instanceTable = new WorkflowInstanceTable(workflowInstanceContainer);

        searchPanel = new SearchPanel(instanceTable);

        setCompositionRoot(verticalLayout);

        initButtonPanel();

        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(createWorkflowInstanceLabel);
        verticalLayout.addComponent(searchPanel);

        verticalLayout.addComponent(instanceTable);
        verticalLayout.addComponent(buttonPanel);
    }

    private void initButtonPanel() {
        createInstanceButton = new Button("Create instance",
                new ShowStartConfigurationWindowAction());

        buttonList.add(createInstanceButton);

        buttonPanel = new ButtonPanel(buttonList);
        buttonPanel.setCaption("Edit instance");
    }
}
