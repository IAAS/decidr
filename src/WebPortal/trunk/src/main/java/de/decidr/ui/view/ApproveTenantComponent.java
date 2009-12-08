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

import de.decidr.ui.controller.tenant.ApproveTenantsAction;
import de.decidr.ui.controller.tenant.DeclineTenantsAction;
import de.decidr.ui.data.ApproveTenantContainer;
import de.decidr.ui.view.tables.ApproveTenantTable;

/**
 * This class provides the tenants which are to be approved in a component.
 * The super admin can approve and decline tenants.
 * 
 * @author AT
 */
public class ApproveTenantComponent extends CustomComponent {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;
	
	private ApproveTenantContainer approveTenantContainer = null;
	
	private VerticalLayout verticalLayout = null;
    
    private Label approveTenantLabel = null;
    
    private SearchPanel searchPanel = null;
    private ButtonPanel buttonPanel = null;
    
    private ApproveTenantTable approveTenantTable = null;
    
    private Button approveButton = null;
    private Button declineButton = null;
    
    private List<Button> buttonList = null;
    
    /**
	 * Default constructor
	 *
	 */
	public ApproveTenantComponent() {
		init();
	}

	/**
	 * This method initializes the components for the approve tenant component.
	 *
	 */
	private void init() {
		approveTenantContainer = new ApproveTenantContainer();

        verticalLayout = new VerticalLayout();

        approveTenantLabel = new Label("<h2> Edit tenant </h2>");
        approveTenantLabel.setContentMode(Label.CONTENT_XHTML);

        approveTenantTable = new ApproveTenantTable(approveTenantContainer);

        searchPanel = new SearchPanel(approveTenantTable);
        
        fillButtonList();
        
        setCompositionRoot(verticalLayout);
        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(approveTenantLabel);
        verticalLayout.addComponent(searchPanel);
        verticalLayout.addComponent(approveTenantTable);
        verticalLayout.addComponent(buttonPanel);
		
	}
	
	private void fillButtonList(){
		buttonList = new LinkedList<Button>();
		
		approveButton = new Button("Approve", new ApproveTenantsAction(
                approveTenantTable));
        declineButton = new Button("Decline", new DeclineTenantsAction(
                approveTenantTable));
        
        
        
        buttonList.add(approveButton);
        buttonList.add(declineButton);
		
		buttonPanel = new ButtonPanel(buttonList);
	}

}
