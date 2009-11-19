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

package de.decidr.ui.view.help;

import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.decidr.ui.controller.HideDialogWindowAction;
import de.decidr.ui.controller.HideHelpDialogAction;

/**
 * The integrated help window
 *
 * @author Geoffrey-Alexeij Heinze
 */
public class HelpDialogComponent extends Window {
    
    private Accordion acc = null;
    private Button closeButton = null;
    
    public HelpDialogComponent(){
        super("DecidR Help");
        setTheme("decidr");
        
        VerticalLayout layout = (VerticalLayout) getContent();
        layout.setSpacing(true);
        
        closeButton = new Button("close", new HideHelpDialogAction());
        
        acc = new Accordion();
        acc.setSizeFull();
        
        acc.addTab(new AccountManagementHelpComponent(), "Account Management", null);
        acc.addTab(new WorkflowParticipationHelpComponent(), "Workflow Participation", null);
        acc.addTab(new WorkflowInstanceManagementHelpComponent(), "Workflow Instance Management", null);
        acc.addTab(new TenantSettingsHelpComponent(), "Tenant Settings", null);
        acc.addTab(new TenantUserManagementHelpComponent(), "Tenant User Management", null);
        acc.addTab(new WorkflowModelingHelpComponent(), "Workflow Modeling", null);
        acc.addTab(new WorkflowModelEditorHelpComponent(), "Workflow Model Editor", null);
        acc.addTab(new TenantMonitoringHelpComponent(), "Tenant Monitoring", null);
        acc.addTab(new SystemSettingsHelpComponent(), "System Settings", null);
        acc.addTab(new SystemStatusHelpComponent(), "System Status", null);
        acc.addTab(new UserManagementHelpComponent(), "User Management", null);
        acc.addTab(new TenantManagementHelpComponent(), "Tenant Management", null);
        
        layout.addComponent(closeButton);
        layout.setComponentAlignment(closeButton, Alignment.TOP_RIGHT);
        layout.addComponent(acc);
    }
    
    public void show(){
        setWidth(400, VerticalLayout.UNITS_PIXELS);
        setHeight(600, VerticalLayout.UNITS_PIXELS);
        center();
        setVisible(true);
        
    }
    
    public void hide(){
        setVisible(false);
    }
}
