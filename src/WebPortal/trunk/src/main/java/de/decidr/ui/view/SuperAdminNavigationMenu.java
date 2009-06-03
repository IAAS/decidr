package de.decidr.ui.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.controller.CreateTenantAction;
import de.decidr.ui.controller.EditTenantAction;
import de.decidr.ui.controller.ProfileSettingsAction;
import de.decidr.ui.controller.ShowUserListAction;
import de.decidr.ui.controller.SystemSettingsAction;
import de.decidr.ui.controller.TenantSettingsAction;

public class SuperAdminNavigationMenu extends CustomComponent {
    
    /**
     * TODO: add comment
     */
    private static final long serialVersionUID = 6470214674053630344L;

    private static SuperAdminNavigationMenu superAdminNavigationMenu = null;
    
    private VerticalLayout verticalLayout;
    
    private Button myWorkItemLink = null;
    private Button changeTenantLink = null;
    private Button createWorkflowModelLink = null;
    private Button createWorkflowInstanceLink = null;
    private Button showWorkflowInstancesLink = null;
    private Button showUserListLink = null;
    private Button createTenantLink = null;
    private Button editTenantLink = null;
    private Button profileSettingsLink = null;
    private Button tenantSettingsLink = null;
    private Button systemSettingsLink = null;
    
    private Label workflowParticipationLabel = null;
    private Label workflowModelLabel = null;
    private Label workflowInstancesLabel = null;
    private Label usersLabel = null;
    private Label tenantsLabel = null;
    private Label settingsLabel = null;
    
    private SuperAdminNavigationMenu(){
        init();
    }
    
    private void init(){
        verticalLayout = new VerticalLayout();
        this.setCompositionRoot(verticalLayout);
        
        myWorkItemLink = new Button("My Workitems");
        myWorkItemLink.setStyleName(Button.STYLE_LINK);
        changeTenantLink = new Button("Change Tenant");
        changeTenantLink.setStyleName(Button.STYLE_LINK);
        createWorkflowModelLink = new Button("Create/Edit Workflow Model");
        createWorkflowModelLink.setStyleName(Button.STYLE_LINK);
        createWorkflowInstanceLink = new Button("Create Workflow Instance");
        createWorkflowInstanceLink.setStyleName(Button.STYLE_LINK);
        showWorkflowInstancesLink = new Button("Show Workflow Instances");
        showWorkflowInstancesLink.setStyleName(Button.STYLE_LINK);
        showUserListLink = new Button("Show/Edit User List", new ShowUserListAction());
        showUserListLink.setStyleName(Button.STYLE_LINK);
        createTenantLink = new Button("Create Tenant", new CreateTenantAction());
        createTenantLink.setStyleName(Button.STYLE_LINK);
        editTenantLink = new Button("Edit Tenant", new EditTenantAction());
        editTenantLink.setStyleName(Button.STYLE_LINK);
        profileSettingsLink = new Button("Profile Settings", new ProfileSettingsAction());
        profileSettingsLink.setStyleName(Button.STYLE_LINK);
        tenantSettingsLink = new Button("Tenant Settings", new TenantSettingsAction());
        tenantSettingsLink.setStyleName(Button.STYLE_LINK);
        systemSettingsLink = new Button("System Settings", new SystemSettingsAction());
        systemSettingsLink.setStyleName(Button.STYLE_LINK);
        
        workflowParticipationLabel = new Label("Workflow participation");
        workflowModelLabel = new Label("Workflow model");
        workflowInstancesLabel = new Label("Workflow Instances");
        usersLabel = new Label("Users");
        tenantsLabel = new Label("Tenants");
        settingsLabel = new Label("Settings");
    
        verticalLayout.setSpacing(true);
        
        verticalLayout.addComponent(workflowParticipationLabel);
        verticalLayout.addComponent(myWorkItemLink);
        verticalLayout.addComponent(changeTenantLink);
        
        verticalLayout.addComponent(workflowModelLabel);
        verticalLayout.addComponent(createWorkflowModelLink);
        
        verticalLayout.addComponent(workflowInstancesLabel);
        verticalLayout.addComponent(createWorkflowInstanceLink);
        verticalLayout.addComponent(showWorkflowInstancesLink);
        
        verticalLayout.addComponent(usersLabel);
        verticalLayout.addComponent(showUserListLink);
        
        verticalLayout.addComponent(tenantsLabel);
        verticalLayout.addComponent(createTenantLink);
        verticalLayout.addComponent(editTenantLink);
        
        verticalLayout.addComponent(settingsLabel);
        verticalLayout.addComponent(profileSettingsLink);
        verticalLayout.addComponent(tenantSettingsLink);
        verticalLayout.addComponent(systemSettingsLink);
    }
    
    public static SuperAdminNavigationMenu getInstance(){
        if(superAdminNavigationMenu == null){
            superAdminNavigationMenu = new SuperAdminNavigationMenu();
        }
        return superAdminNavigationMenu;
    }

}
