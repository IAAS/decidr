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

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.data.WorkflowInstanceContainer;

public class CreateWorkflowInstanceComponent extends CustomComponent {
    
    /**
     * In this component the user can create a workflow instance from
     * a workflow model. The user has to choose a workflow model from 
     * table and push the create button.
     * 
     * @author AT
     */
    private static final long serialVersionUID = -2283442464298218331L;
    
    private WorkflowInstanceContainer workflowInstanceContainer = null;
    
    private VerticalLayout verticalLayout = null;
    
    private SearchPanel searchPanel = null;
    
    private Label createWorkflowInstanceLabel = null;
    
    private WorkflowInstanceTable instanceTable = null;
    
    
    /**
     * Default constructor
     *
     */
    public CreateWorkflowInstanceComponent(){
        init();
    }
    
    /**
     * This method initializes the components of the create workflow instance component
     *
     */
    private void init(){
        workflowInstanceContainer = new WorkflowInstanceContainer();
        
        verticalLayout = new VerticalLayout();
        
        searchPanel = new SearchPanel();
        
        createWorkflowInstanceLabel = new Label("<h2> Create Workflow Instance </h2>");
        createWorkflowInstanceLabel.setContentMode(Label.CONTENT_XHTML);
    
        instanceTable = new WorkflowInstanceTable(workflowInstanceContainer, workflowInstanceContainer);
        
        setCompositionRoot(verticalLayout);
        
        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(createWorkflowInstanceLabel);
        verticalLayout.addComponent(searchPanel);
        
        verticalLayout.addComponent(instanceTable);
        verticalLayout.setComponentAlignment(instanceTable, Alignment.MIDDLE_CENTER);
        
        
    }
    

}
