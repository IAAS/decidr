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
import de.decidr.ui.controller.MarkWorkItemAsDoneAction;
import de.decidr.ui.controller.show.ShowWorkItemWindowAction;
import de.decidr.ui.data.WorkItemContainer;
import de.decidr.ui.view.tables.WorkItemTable;

/**
 * This component represents the work items in a table. The user can choose if
 * he wants to see the work items of all tenants or just from the current
 * tenant.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "TK", "JS" }, lastRevision = "2377", currentReviewState = State.Passed)
public class WorkItemComponent extends CustomComponent {

    private static final long serialVersionUID = -2110748321898265548L;

    private WorkItemContainer workItemContainer = null;

    private VerticalLayout verticalLayout = null;

    private SearchPanel searchPanel = null;
    private Panel tablePanel = null;

    private Label headerLabel = null;
    private Label showWorkItemLabel = null;

    private WorkItemTable workItemTable = null;

    private Button markAsDoneButton = null;

    private Button editWorkItemButton = null;

    private ButtonPanel buttonPanel = null;

    private List<Button> buttonList = new LinkedList<Button>();

    /**
     * Default constructor.
     * 
     */
    public WorkItemComponent() {
        init();
    }

    /**
     * Returns the work item table.
     * 
     * @return workItemTable
     */
    public WorkItemTable getWorkItemTable() {
        return workItemTable;
    }

    /**
     * This method initializes the components of the work item component.
     * 
     */
    private void init() {

        workItemContainer = new WorkItemContainer();

        verticalLayout = new VerticalLayout();

        tablePanel = new Panel();

        headerLabel = new Label("<h2>My Work Items</h2>");
        headerLabel.setContentMode(Label.CONTENT_XHTML);
        showWorkItemLabel = new Label("Show Work Items from: ");

        workItemTable = new WorkItemTable(workItemContainer);

        searchPanel = new SearchPanel(workItemTable);

        initButtonList();

        setCompositionRoot(verticalLayout);

        verticalLayout.addComponent(headerLabel);
        verticalLayout.addComponent(searchPanel);
        verticalLayout.setSpacing(true);

        searchPanel.getSearchHorizontalLayout().addComponent(showWorkItemLabel);

        verticalLayout.addComponent(tablePanel);
        tablePanel.addComponent(workItemTable);

        verticalLayout.addComponent(buttonPanel);

    }

    private void initButtonList() {
        markAsDoneButton = new Button("Mark as done",
                new MarkWorkItemAsDoneAction(workItemTable));

        editWorkItemButton = new Button("Edit Work Item",
                new ShowWorkItemWindowAction(workItemTable));

        buttonList.add(markAsDoneButton);
        buttonList.add(editWorkItemButton);

        buttonPanel = new ButtonPanel(buttonList);
        buttonPanel.setCaption("Edit Work Item");
    }

}
