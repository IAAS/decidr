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

/**
 * TODO: add comment
 *
 * @author AT
 */
public class WorkflowModelsComponent extends CustomComponent {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -8284535233079548635L;
    
    private static WorkflowModelsComponent workflowModelsComponent = null;
    
    private VerticalLayout verticalLayout = null;
    private HorizontalLayout buttonHorizontalLayout = null;
    
    private Label workflowModelLabel = null;
    private Label showModelsFromLabel = null;
    
    private SearchPanel searchPanel = null;
    private Panel buttonPanel = null;
    
    private CurrentTenantModelTable currentTenantTable = null;
    private PublicModelTable publicModelTable = null;
    
    private NativeSelect nativeSelect = null;
    
    private Button createNewModelButton = null;
    private Button removeModelButton = null;
    private Button lockModelButton = null;
    private Button unlockModelButton = null;
    private Button publishModelButton = null;
    private Button unpublishModelButton = null;
    private Button appointWorkflowAdminButton = null;
    private Button importModelButton = null;
    
    private String[] models = new String[]{"Current tenant", "Public models"};

    /**
     * Default constructor
     *
     */
    private WorkflowModelsComponent() {
        init();
    }

    /**
     * This method initializes the components for the workflow model component.
     *
     */
    private void init() {
       verticalLayout = new VerticalLayout();
       buttonHorizontalLayout = new HorizontalLayout();
       
       workflowModelLabel = new Label("<h2> Workflow models </h2>");
       workflowModelLabel.setContentMode(Label.CONTENT_XHTML);
       showModelsFromLabel = new Label("Show models from: ");
       
       searchPanel = new SearchPanel();
       buttonPanel = new Panel();
       
       currentTenantTable = new CurrentTenantModelTable();
       
       nativeSelect = new NativeSelect();
       for(int i = 0; i < models.length; i++){
           nativeSelect.addItem(models[i]);
       }
       nativeSelect.setNullSelectionAllowed(false);
       nativeSelect.setValue("Current tenant");
       nativeSelect.setImmediate(true);
       //TODO: muss nacher rausgenommen werden, und in action registry eingef�hrt werden
       nativeSelect.addListener(new Property.ValueChangeListener(){

        @Override
        public void valueChange(ValueChangeEvent event) {
            changeToPublic();
            
        }
           
       });
       
       createNewModelButton = new Button("Create new model");
       removeModelButton = new Button("Remove");
       lockModelButton = new Button("Lock");
       unlockModelButton = new Button("Unlock");
       publishModelButton = new Button("Publish");
       unpublishModelButton = new Button("Un-publish");
       appointWorkflowAdminButton = new Button("Appoint workflow admin");
       
       setCompositionRoot(verticalLayout);
       
       verticalLayout.setSpacing(true);
       verticalLayout.addComponent(workflowModelLabel);
       verticalLayout.addComponent(searchPanel);
       
       searchPanel.getSearchHorizontalLayout().addComponent(showModelsFromLabel);
       searchPanel.getSearchHorizontalLayout().addComponent(nativeSelect);
       
       verticalLayout.addComponent(createNewModelButton);
       verticalLayout.addComponent(currentTenantTable);
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
    private void changeToPublic(){
        publicModelTable = new PublicModelTable();
        importModelButton = new Button("Import");
        if(!getNativeSelect().isSelected("Public models")){
            init();
        }else{
            getVerticalLayout().removeComponent(getCreateNewModelButton());
            getVerticalLayout().replaceComponent(getCurrentTenantTable(), publicModelTable);
            getButtonPanel().removeAllComponents();
            getButtonPanel().addComponent(importModelButton);
        } 
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
     * Sets the native select component.
     *
     * @param nativeSelect
     */
    public void setNativeSelect(NativeSelect nativeSelect) {
        this.nativeSelect = nativeSelect;
    }

    /**
     * Gets the vertical layout from the component.
     *
     * @return verticalLayout
     */
    public VerticalLayout getVerticalLayout() {
        return verticalLayout;
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
     * Sets the button panel for the component.
     *
     * @param buttonPanel
     */
    public void setButtonPanel(Panel buttonPanel) {
        this.buttonPanel = buttonPanel;
    }

    /**
     * Gets the current tenant table from the component.
     *
     * @return currentTenantTable
     */
    public CurrentTenantModelTable getCurrentTenantTable() {
        return currentTenantTable;
    }

    /**
     * Sets the current tenant table for the component.
     *
     * @param currentTenantTable
     */
    public void setCurrentTenantTable(CurrentTenantModelTable currentTenantTable) {
        this.currentTenantTable = currentTenantTable;
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
     * Sets the create new model button for the component.
     *
     * @param createNewModelButton
     */
    public void setCreateNewModelButton(Button createNewModelButton) {
        this.createNewModelButton = createNewModelButton;
    }

    /**
     * This method return the single instance of the workflow models component.
     *
     * @return workflowModelsComponent - instance of object
     */
    public static WorkflowModelsComponent getInstance(){
        if(workflowModelsComponent == null){
            workflowModelsComponent = new WorkflowModelsComponent();
        }
        return workflowModelsComponent;
    }
}