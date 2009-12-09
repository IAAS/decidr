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
package de.decidr.ui.view.navigationmenus;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.controller.show.ShowChangeTenantAction;
import de.decidr.ui.controller.show.ShowCreateTenantAction;
import de.decidr.ui.controller.show.ShowCreateWorkflowInstanceAction;
import de.decidr.ui.controller.show.ShowCreateWorkflowModelAction;
import de.decidr.ui.controller.show.ShowMyWorkitemsAction;
import de.decidr.ui.controller.show.ShowProfileSettingsAction;
import de.decidr.ui.controller.show.ShowTenantSettingsAction;
import de.decidr.ui.controller.show.ShowWorkflowInstancesAction;

/**
 * The navigation menu for the tenant. He has less links than the super
 * administrator.
 * 
 * @author AT
 */
public class TenantAdminNavigationMenu extends CustomComponent {

    private static final long serialVersionUID = -5463146748220047931L;

    private VerticalLayout verticalLayout;

    private Button myWorkItemLink = null;
    private Button changeTenantLink = null;
    private Button createWorkflowModelLink = null;
    private Button createWorkflowInstanceLink = null;
    private Button showWorkflowInstancesLink = null;
    private Button createTenantLink = null;
    private Button profileSettingsLink = null;
    private Button tenantSettingsLink = null;

    private Label workflowParticipationLabel = null;
    private Label workflowModelLabel = null;
    private Label workflowInstancesLabel = null;
    private Label usersLabel = null;
    private Label tenantsLabel = null;
    private Label settingsLabel = null;

    /**
     * Default constructor
     * 
     */
    public TenantAdminNavigationMenu() {
        init();
    }

    /**
     * This method initializes the components of the tenant admin navigation
     * component
     * 
     */
    private void init() {
        verticalLayout = new VerticalLayout();
        this.setCompositionRoot(verticalLayout);

        myWorkItemLink = new Button("My Workitems", new ShowMyWorkitemsAction());
        myWorkItemLink.setStyleName(Button.STYLE_LINK);
        changeTenantLink = new Button("Change Tenant",
                new ShowChangeTenantAction());
        changeTenantLink.setStyleName(Button.STYLE_LINK);
        createWorkflowModelLink = new Button("Create/Edit Workflow Models",
                new ShowCreateWorkflowModelAction());
        createWorkflowModelLink.setStyleName(Button.STYLE_LINK);
        createWorkflowInstanceLink = new Button("Create Workflow Instance",
                new ShowCreateWorkflowInstanceAction());
        createWorkflowInstanceLink.setStyleName(Button.STYLE_LINK);
        showWorkflowInstancesLink = new Button("Show Workflow Instances",
                new ShowWorkflowInstancesAction());
        showWorkflowInstancesLink.setStyleName(Button.STYLE_LINK);
        createTenantLink = new Button("Create Tenant",
                new ShowCreateTenantAction());
        createTenantLink.setStyleName(Button.STYLE_LINK);
        profileSettingsLink = new Button("Profile Settings",
                new ShowProfileSettingsAction());
        profileSettingsLink.setStyleName(Button.STYLE_LINK);
        tenantSettingsLink = new Button("Tenant Settings",
                new ShowTenantSettingsAction());
        tenantSettingsLink.setStyleName(Button.STYLE_LINK);

        workflowParticipationLabel = new Label(
                "<h5>Workflow participation</h5>");
        workflowParticipationLabel.setContentMode(Label.CONTENT_XHTML);
        workflowModelLabel = new Label("<h5>Workflow model</h5>");
        workflowModelLabel.setContentMode(Label.CONTENT_XHTML);
        workflowInstancesLabel = new Label("<h5>Workflow Instances</h5>");
        workflowInstancesLabel.setContentMode(Label.CONTENT_XHTML);
        usersLabel = new Label("<h5>Users</h5>");
        usersLabel.setContentMode(Label.CONTENT_XHTML);
        tenantsLabel = new Label("<h5>Tenants</h5>");
        tenantsLabel.setContentMode(Label.CONTENT_XHTML);
        settingsLabel = new Label("<h5>Settings</h5>");
        settingsLabel.setContentMode(Label.CONTENT_XHTML);

        verticalLayout.setSpacing(true);

        verticalLayout.addComponent(workflowParticipationLabel);
        verticalLayout.addComponent(myWorkItemLink);
        verticalLayout.addComponent(changeTenantLink);

        verticalLayout.addComponent(workflowModelLabel);
        verticalLayout.addComponent(createWorkflowModelLink);

        verticalLayout.addComponent(workflowInstancesLabel);
        verticalLayout.addComponent(createWorkflowInstanceLink);
        verticalLayout.addComponent(showWorkflowInstancesLink);

        verticalLayout.addComponent(tenantsLabel);
        verticalLayout.addComponent(createTenantLink);

        verticalLayout.addComponent(settingsLabel);
        verticalLayout.addComponent(profileSettingsLink);
        verticalLayout.addComponent(tenantSettingsLink);
    }

}
