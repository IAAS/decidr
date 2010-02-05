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

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.ui.controller.show.ShowAppointWorkflowAdminAction;
import de.decidr.ui.controller.show.ShowModelDescription;
import de.decidr.ui.controller.show.ShowModelingToolAction;
import de.decidr.ui.controller.workflowmodel.ImportWorkflowModelAction;
import de.decidr.ui.controller.workflowmodel.LockWorkflowModelAction;
import de.decidr.ui.controller.workflowmodel.PublishWorkflowModelAction;
import de.decidr.ui.controller.workflowmodel.RemoveWorkflowModelsAction;
import de.decidr.ui.controller.workflowmodel.UnlockWorkflowModelsAction;
import de.decidr.ui.controller.workflowmodel.UnpublishWorkflowModelsAction;
import de.decidr.ui.data.PublicModelContainer;
import de.decidr.ui.data.WorkflowModelContainer;
import de.decidr.ui.view.tables.PublicModelTable;
import de.decidr.ui.view.tables.WorkflowModelTable;

/**
 * This component represents the workflow models. They are stored in a table.
 * Also the user is able to choose if he wants to show the public workflow
 * models or all workflow models. A tenant table is included, too. If the user
 * selects a model he can create, remove, lock, publish, unpublish, appoint or
 * import a model.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "TK", "JS" }, lastRevision = "2377", currentReviewState = State.PassedWithComments)
public class WorkflowModelsComponent extends CustomComponent {

    private static final long serialVersionUID = -8284535233079548635L;

    private PublicModelContainer publicModelContainer = null;

    private WorkflowModelContainer workflowModelContainer = null;

    private VerticalLayout verticalLayout = null;
    private HorizontalLayout buttonHorizontalLayout = null;

    private Label workflowModelLabel = null;
    private Label showModelsFromLabel = null;

    private SearchPanel searchPanel = null;
    private Panel buttonPanel = null;

    private WorkflowModelTable workflowModelTable = null;
    private PublicModelTable publicModelTable = null;

    private NativeSelect nativeSelect = null;

    private Button createNewModelButton = null;
    private Button editWorkflowModelButton = null;
    private Button removeModelButton = null;
    private Button lockModelButton = null;
    private Button unlockModelButton = null;
    private Button publishModelButton = null;
    private Button unpublishModelButton = null;
    private Button appointWorkflowAdminButton = null;
    private Button importModelButton = null;

    private String[] models = new String[] { "Current Tenant", "Public models" };

    /**
     * Default constructor.
     */
    public WorkflowModelsComponent() {
        init();
    }

    /**
     * Changes the view if public models are selected.
     * 
     */
    private void changeToPublic() {
        publicModelContainer = new PublicModelContainer();
        publicModelTable = new PublicModelTable(publicModelContainer);
        Button editWorkflowButton = new Button("Edit workflow",
                new ShowModelingToolAction(publicModelTable));
        importModelButton = new Button("Import", new ImportWorkflowModelAction(
                publicModelTable));
        unpublishModelButton = new Button("Un-publish",
                new UnpublishWorkflowModelsAction(publicModelTable));
        if (!getNativeSelect().isSelected("Public models")) {
            init();
        } else {
            getVerticalLayout().replaceComponent(getWorkflowModelTable(),
                    publicModelTable);
            getVerticalLayout().replaceComponent(editWorkflowModelButton,
                    editWorkflowButton);
            getButtonHorizontalLayout().removeAllComponents();
            getButtonHorizontalLayout().addComponent(importModelButton);
            getButtonHorizontalLayout().addComponent(unpublishModelButton);
        }
    }

    /**
     * Returns the button button
     * 
     * @return buttonHorizontalLayout HorizontalLayout that contains the buttons
     *         for remove, publish, unpublish, lock, unlock, appoint wfm admin
     */
    public HorizontalLayout getButtonHorizontalLayout() {
        return buttonHorizontalLayout;
    }

    /**
     * Gets the create new model button from the component.
     * 
     * @return createNewModelButton The button to create a new workflow model
     */
    public Button getCreateNewModelButton() {
        return createNewModelButton;
    }

    /**
     * Gets the button for editing a workflow model
     * 
     * @return Vaadin Button
     */
    public Button getEditWorkflowModelButton() {
        return editWorkflowModelButton;
    }

    /**
     * Gets the native select component.
     * 
     * @return nativeSelect The dropdown list to select which models are
     *         displayed
     */
    public NativeSelect getNativeSelect() {
        return nativeSelect;
    }

    /**
     * Gets the vertical layout from the component.
     * 
     * @return verticalLayout VerticalLayout of the component
     */
    public VerticalLayout getVerticalLayout() {
        return verticalLayout;
    }

    /**
     * Gets the current tenant table from the component.
     * 
     * @return workflowModelTable The table with the current workflow models
     */
    public WorkflowModelTable getWorkflowModelTable() {
        return workflowModelTable;
    }

    /**
     * This method initializes the components for the workflow model component.
     */
    private void init() {

        workflowModelContainer = new WorkflowModelContainer();

        verticalLayout = new VerticalLayout();
        buttonHorizontalLayout = new HorizontalLayout();

        workflowModelLabel = new Label("<h2> Workflow models </h2>");
        workflowModelLabel.setContentMode(Label.CONTENT_XHTML);
        showModelsFromLabel = new Label("Show Models from: ");

        buttonPanel = new Panel();

        workflowModelTable = new WorkflowModelTable(workflowModelContainer);

        searchPanel = new SearchPanel(workflowModelTable);

        nativeSelect = new NativeSelect();
        for (int i = 0; i < models.length; i++) {
            nativeSelect.addItem(models[i]);
        }
        nativeSelect.setNullSelectionAllowed(false);
        nativeSelect.setValue("Current Tenant");
        nativeSelect.setImmediate(true);
        nativeSelect.addListener(new Property.ValueChangeListener() {

            /**
             * Serial version uid
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                changeToPublic();

            }

        });

        createNewModelButton = new Button("Create New Model",
                new ShowModelDescription(workflowModelTable));
        editWorkflowModelButton = new Button("Edit Model",
                new ShowModelingToolAction(workflowModelTable));
        removeModelButton = new Button("Remove",
                new RemoveWorkflowModelsAction(workflowModelTable));
        lockModelButton = new Button("Make non-executable",
                new LockWorkflowModelAction(workflowModelTable));
        unlockModelButton = new Button("Make executable",
                new UnlockWorkflowModelsAction(workflowModelTable));
        publishModelButton = new Button("Publish",
                new PublishWorkflowModelAction(workflowModelTable));
        appointWorkflowAdminButton = new Button("Appoint workflow admin",
                new ShowAppointWorkflowAdminAction(workflowModelTable));

        setCompositionRoot(verticalLayout);

        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(workflowModelLabel);
        verticalLayout.addComponent(searchPanel);

        searchPanel.getSearchHorizontalLayout().addComponent(
                showModelsFromLabel);
        searchPanel.getSearchHorizontalLayout().addComponent(nativeSelect);

        verticalLayout.addComponent(createNewModelButton);
        verticalLayout.addComponent(editWorkflowModelButton);
        verticalLayout.addComponent(workflowModelTable);
        verticalLayout.addComponent(buttonPanel);

        buttonPanel.addComponent(buttonHorizontalLayout);
        buttonPanel.setCaption("Selected models: ");
        buttonHorizontalLayout.setSpacing(true);
        buttonHorizontalLayout.addComponent(removeModelButton);
        buttonHorizontalLayout.addComponent(lockModelButton);
        buttonHorizontalLayout.addComponent(unlockModelButton);
        buttonHorizontalLayout.addComponent(publishModelButton);
        buttonHorizontalLayout.addComponent(appointWorkflowAdminButton);
    }

    /**
     * Sets the button panel for the component.
     * 
     * @param buttonPanel
     *            Vaadin panel, contains the buttons for remove, lock, unlock,
     *            publish, unpublish, appoint wfm admin
     */
    public void setButtonPanel(Panel buttonPanel) {
        this.buttonPanel = buttonPanel;
    }

    /**
     * Sets the create new model button for the component.
     * 
     * @param createNewModelButton
     *            Vaadin button
     */
    public void setCreateNewModelButton(Button createNewModelButton) {
        this.createNewModelButton = createNewModelButton;
    }

    /**
     * Sets the native select component.
     * 
     * @param nativeSelect
     *            The drop down box to select which models are displayed
     */
    public void setNativeSelect(NativeSelect nativeSelect) {
        this.nativeSelect = nativeSelect;
    }

    /**
     * Sets the vertical layout for the component.
     * 
     * @param verticalLayout
     *            Vaadin VerticalLayout
     */
    public void setVerticalLayout(VerticalLayout verticalLayout) {
        this.verticalLayout = verticalLayout;
    }

    /**
     * Sets the current tenant table for the component.
     * 
     * @param workflowModelTable
     *            The table with the list of workflow models
     */
    public void setWorkflowModelTable(WorkflowModelTable workflowModelTable) {
        this.workflowModelTable = workflowModelTable;
    }

}
