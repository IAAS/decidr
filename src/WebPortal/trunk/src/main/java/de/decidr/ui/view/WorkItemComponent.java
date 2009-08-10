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
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.data.WorkItemContainer;

public class WorkItemComponent extends CustomComponent {
    
    /**
     * This component represents the work items in a table. The user
     * can choose if he wants to see the work items of all tenants or
     * just from the current tenant.
     * 
     * @author AT
     */
    private static final long serialVersionUID = -2110748321898265548L;
    
    private WorkItemContainer workItemContainer = null;
    
    private VerticalLayout verticalLayout = null;
    
    private SearchPanel searchPanel = null;
    private Panel tablePanel = null;
    
    private Label headerLabel = null;
    private Label showWorkItemLabel = null;
    
    private NativeSelect tenantNativeSelect = null;
    
    private static final String[] tenants = new String[]{"All tenants", "Current tenants"};
    
    private WorkItemTable workItemTable = null;
    
    /**
     * Default constructor
     *
     */
    public WorkItemComponent(){
        init();
    }
    
    /**
     * This method initializes the components of the work item component
     *
     */
    private void init(){
        workItemContainer = new WorkItemContainer();
        
        verticalLayout = new VerticalLayout();

        tablePanel = new Panel();
        
        headerLabel = new Label(
                "<h2>My work items </h2>");
        headerLabel.setContentMode(Label.CONTENT_XHTML);
        showWorkItemLabel = new Label("Show work items from: ");
        
        tenantNativeSelect = new NativeSelect();
        for(int i = 0; i < tenants.length; i++){
            tenantNativeSelect.addItem(tenants[i]);
        }
        
        workItemTable = new WorkItemTable(workItemContainer, workItemContainer);
        
        searchPanel = new SearchPanel(workItemTable);
        
        setCompositionRoot(verticalLayout);
        
        verticalLayout.addComponent(headerLabel);        
        verticalLayout.addComponent(searchPanel);
        verticalLayout.setSpacing(true);
        
        searchPanel.getSearchHorizontalLayout().addComponent(showWorkItemLabel);
        searchPanel.getSearchHorizontalLayout().addComponent(tenantNativeSelect);
        
        verticalLayout.addComponent(tablePanel);
        tablePanel.addComponent(workItemTable);
        
    }
    

}
