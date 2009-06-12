package de.decidr.ui.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.controller.ShowProfileSettingsAction;
import de.decidr.ui.controller.ShowTenantSettingsAction;

public class TenantAdminNavigationMenu extends CustomComponent {
    
    /**
     * TODO: add comment
     */
    private static final long serialVersionUID = -5463146748220047931L;

    private static TenantAdminNavigationMenu tenantAdminNavigationMenu = null;
    
    private VerticalLayout verticalLayout;
    
    private Button myWorkItemLink = null;
    private Button changeTenantLink = null;
    private Button createWorkflowModelLink = null;
    private Button createWorkflowInstanceLink = null;
    private Button showWorkflowInstancesLink = null;
    private Button showUserListLink = null;
    private Button profileSettingsLink = null;
    private Button tenantSettingsLink = null;
    
    private Label workflowParticipationLabel = null;
    private Label workflowModelLabel = null;
    private Label workflowInstancesLabel = null;
    private Label usersLabel = null;
    private Label settingsLabel = null;
    
    private TenantAdminNavigationMenu(){
        init();
    }
    
    private void init(){
        verticalLayout = new VerticalLayout();
        this.setCompositionRoot(verticalLayout);
        
        myWorkItemLink = new Button("My Workitems");
        myWorkItemLink.setStyleName(Button.STYLE_LINK);
        changeTenantLink = new Button("Change Tenant");
        changeTenantLink.setStyleName(Button.STYLE_LINK);
        createWorkflowModelLink = new Button("Create/Edit Workflow Models");
        createWorkflowModelLink.setStyleName(Button.STYLE_LINK);
        createWorkflowInstanceLink = new Button("Create Workflow Instance");
        createWorkflowInstanceLink.setStyleName(Button.STYLE_LINK);
        showWorkflowInstancesLink = new Button("Show Workflow Instances");
        showWorkflowInstancesLink.setStyleName(Button.STYLE_LINK);
        showUserListLink = new Button("Show/Edit User List");
        showUserListLink.setStyleName(Button.STYLE_LINK);
        profileSettingsLink = new Button("Profile Settings", new ShowProfileSettingsAction());
        profileSettingsLink.setStyleName(Button.STYLE_LINK);
        tenantSettingsLink = new Button("Tenant Settings", new ShowTenantSettingsAction());
        tenantSettingsLink.setStyleName(Button.STYLE_LINK);
        
        workflowParticipationLabel = new Label("Workflow participation");
        workflowModelLabel = new Label("Workflow model");
        workflowInstancesLabel = new Label("Workflow Instances");
        usersLabel = new Label("Users");
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

        verticalLayout.addComponent(settingsLabel);
        verticalLayout.addComponent(profileSettingsLink);
        verticalLayout.addComponent(tenantSettingsLink);
    }
    
    public static TenantAdminNavigationMenu getInstance(){
        if(tenantAdminNavigationMenu == null){
            tenantAdminNavigationMenu = new TenantAdminNavigationMenu();
        }
        return tenantAdminNavigationMenu;
    }

}
