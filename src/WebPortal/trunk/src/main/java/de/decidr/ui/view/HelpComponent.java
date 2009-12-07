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

/**
 * The user can call the help site, to get information about the 
 * DecidR application.
 *
 * @author Geoffrey-Alexeij Heinze
 */
import com.vaadin.ui.Accordion;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.view.help.AccountManagementHelpComponent;
import de.decidr.ui.view.help.GlossaryHelpComponent;
import de.decidr.ui.view.help.SystemSettingsHelpComponent;
import de.decidr.ui.view.help.SystemStatusHelpComponent;
import de.decidr.ui.view.help.TenantManagementHelpComponent;
import de.decidr.ui.view.help.TenantMonitoringHelpComponent;
import de.decidr.ui.view.help.TenantSettingsHelpComponent;
import de.decidr.ui.view.help.TenantUserManagementHelpComponent;
import de.decidr.ui.view.help.UserManagementHelpComponent;
import de.decidr.ui.view.help.WorkflowInstanceManagementHelpComponent;
import de.decidr.ui.view.help.WorkflowModelEditorHelpComponent;
import de.decidr.ui.view.help.WorkflowModelingHelpComponent;
import de.decidr.ui.view.help.WorkflowParticipationHelpComponent;

@SuppressWarnings("serial")
public class HelpComponent extends CustomComponent {

    private VerticalLayout verticalLayout = null;
    private Accordion acc = null;

    /**
     * Default constructor
     * 
     */
    public HelpComponent() {
        init();
    }

    /**
     * This method initializes the components of the help component
     * 
     */
    private void init() {
        verticalLayout = new VerticalLayout();
        this.setCompositionRoot(verticalLayout);

        acc = new Accordion();
        acc.setSizeFull();
        
        AccountManagementHelpComponent accMngHelp = new AccountManagementHelpComponent();
        WorkflowParticipationHelpComponent wfpHelp = new WorkflowParticipationHelpComponent();
        WorkflowInstanceManagementHelpComponent wfiMngHelp = new WorkflowInstanceManagementHelpComponent();
        TenantSettingsHelpComponent tsetHelp = new TenantSettingsHelpComponent();
        TenantUserManagementHelpComponent tuserHelp = new TenantUserManagementHelpComponent();
        WorkflowModelingHelpComponent wfmHelp = new WorkflowModelingHelpComponent();
        WorkflowModelEditorHelpComponent wfmeHelp = new WorkflowModelEditorHelpComponent();
        TenantMonitoringHelpComponent tmonHelp = new TenantMonitoringHelpComponent();
        SystemSettingsHelpComponent syssetHelp = new SystemSettingsHelpComponent();
        SystemStatusHelpComponent sysstatHelp = new SystemStatusHelpComponent();
        UserManagementHelpComponent userMngHelp = new UserManagementHelpComponent();
        TenantManagementHelpComponent tMngHelp = new TenantManagementHelpComponent();
        GlossaryHelpComponent glHelp = new GlossaryHelpComponent();
        
        acc.addTab(accMngHelp, "Account Management", null);
        acc.addTab(wfpHelp, "Workflow Participation", null);
        acc.addTab(wfiMngHelp, "Workflow Instance Management", null);
        acc.addTab(tsetHelp, "Tenant Settings", null);
        acc.addTab(tuserHelp, "Tenant User Management", null);
        acc.addTab(wfmHelp, "Workflow Modeling", null);
        acc.addTab(wfmeHelp, "Workflow Model Editor", null);
        acc.addTab(tmonHelp, "Tenant Monitoring", null);
        acc.addTab(syssetHelp, "System Settings", null);
        acc.addTab(sysstatHelp, "System Status", null);
        acc.addTab(userMngHelp, "User Management", null);
        acc.addTab(tMngHelp, "Tenant Management", null);
        acc.addTab(glHelp, "Glossary", null);

        acc.setSelectedTab(glHelp);
        
        verticalLayout.addComponent(acc);
    }

}
