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
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.controller.SwitchTenantAction;
import de.decidr.ui.data.CurrentTenantContainer;

public class ChangeTenantComponent extends CustomComponent {

    /**
     * The user can change the tenant if he wants. He has to choose one tenant
     * from the table and push the switch tenant button.
     * 
     * @author AT
     */
    private static final long serialVersionUID = 5599429204495615788L;

    private CurrentTenantContainer tenantContainer = null;

    private VerticalLayout verticalLayout = null;

    private Label changeTenantLabel = null;
    private Label waitingForApprovalLabel = null;
    
    private ButtonPanel buttonPanel = null;
    
    private Button changeTenantButton = null;
    
    private List<Button> buttonList = new LinkedList<Button>();

    private TenantTable tenantTable = null;
    private Table approvalTable = null;

    /**
     * Default constructor
     * 
     */
    public ChangeTenantComponent() {
        init();
    }

    /**
     * This method initializes the components of the change tenant component
     * 
     */
    private void init() {
        tenantContainer = new CurrentTenantContainer();

        verticalLayout = new VerticalLayout();

        changeTenantLabel = new Label("<h2> Change Tenant </h2>");
        changeTenantLabel.setContentMode(Label.CONTENT_XHTML);
        //waitingForApprovalLabel = new Label("<h2> Waiting for approval </h2>");
        //waitingForApprovalLabel.setContentMode(Label.CONTENT_XHTML);

        tenantTable = new TenantTable(tenantContainer);
        //approvalTable = new Table(); TODO: table mit approval anlegen
        
        initButtonPanel();

        setCompositionRoot(verticalLayout);

        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(changeTenantLabel);
        verticalLayout.addComponent(tenantTable);
        //verticalLayout.addComponent(waitingForApprovalLabel);
        //verticalLayout.addComponent(approvalTable);
        verticalLayout.addComponent(buttonPanel);
    }
    
    private void initButtonPanel(){
    	changeTenantButton = new Button("Change tenant", new SwitchTenantAction(tenantTable));
    	
    	buttonList.add(changeTenantButton);
    	
    	buttonPanel = new ButtonPanel(buttonList);
    }

}
