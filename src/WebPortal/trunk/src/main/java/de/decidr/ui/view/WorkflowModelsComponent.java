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
        lockModelButton = new Button("Lock", new LockWorkflowModelAction(
                workflowModelTable));
        unlockModelButton = new Button("Unlock",
                new UnlockWorkflowModelsAction(workflowModelTable));
        publishModelButton = new Button("Publish",
                new PublishWorkflowModelAction(workflowModelTable));
        unpublishModelButton = new Button("Un-publish",
                new UnpublishWorkflowModelsAction(workflowModelTable));
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
        buttonHorizontalLayout.addComponent(unpublishModelButton);
        buttonHorizontalLayout.addComponent(appointWorkflowAdminButton);
    }

    /**
     * Changes the view if public models are selected.
     * 
     */
    private void changeToPublic() {
        publicModelContainer = new PublicModelContainer();
        publicModelTable = new PublicModelTable(publicModelContainer);
        importModelButton = new Button("Import");
        if (!getNativeSelect().isSelected("Public models")) {
            init();
        } else {
            // getVerticalLayout().removeComponent(getCreateNewModelButton());
            // getVerticalLayout().removeComponent(getEditWorkflowModelButton());
            getVerticalLayout().replaceComponent(getWorkflowModelTable(),
                    publicModelTable);
            getButtonPanel().removeAllComponents();
            getButtonPanel().addComponent(importModelButton);
        }
    }

    // GH, Aleks: Please comment parameters and return values

    /**
     * Sets the button panel for the component.
     * 
     * @param buttonPanel
     */
    public void setButtonPanel(Panel buttonPanel) {
        this.buttonPanel = buttonPanel;
    }

    /**
     * Sets the create new model button for the component.
     * 
     * @param createNewModelButton
     */
    public void setCreateNewModelButton(Button createNewModelButton) {
        this.createNewModelButton = createNewModelButton;
    }

    /**
     * Sets the current tenant table for the component.
     * 
     * @param currentTenantTable
     */
    public void setWorkflowModelTable(WorkflowModelTable workflowModelTable) {
        this.workflowModelTable = workflowModelTable;
    }

    /**
     * Sets the native select component.
     * 
     * @param nativeSelect
     */
    public void setNativeSelect(NativeSelect nativeSelect) {
        this.nativeSelect = nativeSelect;
    }

    /**
     * Sets the vertical layout for the component.
     * 
     * @param verticalLayout
     */
    public void setVerticalLayout(VerticalLayout verticalLayout) {
        this.verticalLayout = verticalLayout;
    }

    /**
     * Gets the button panel from the component.
     * 
     * @return buttonPanel
     */
    public Panel getButtonPanel() {
        return buttonPanel;
    }

    /**
     * Gets the create new model button from the component.
     * 
     * @return createNewModelButton
     */
    public Button getCreateNewModelButton() {
        return createNewModelButton;
    }

    /**
     * Gets the current tenant table from the component.
     * 
     * @return currentTenantTable
     */
    public WorkflowModelTable getWorkflowModelTable() {
        return workflowModelTable;
    }

    public Button getEditWorkflowModelButton() {
        return editWorkflowModelButton;
    }

    /**
     * Gets the native select component.
     * 
     * @return nativeSelect
     */
    public NativeSelect getNativeSelect() {
        return nativeSelect;
    }

    /**
     * Gets the vertical layout from the component.
     * 
     * @return verticalLayout
     */
    public VerticalLayout getVerticalLayout() {
        return verticalLayout;
    }

}
