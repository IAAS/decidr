package de.decidr.ui.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.controller.ProfileSettingsAction;

public class WorkflowAdminNavigationMenu extends CustomComponent {
    
    /**
     * TODO: add comment
     */
    private static final long serialVersionUID = 3301289362151635416L;

    private static WorkflowAdminNavigationMenu workflowAdminNavigationMenu = null;
    
    private VerticalLayout verticalLayout;
    
    private Button myWorkItemLink = null;
    private Button changeTenantLink = null;
    private Button createWorkflowInstanceLink = null;
    private Button showWorkflowInstancesLink = null;
    private Button showUserListLink = null;
    private Button profileSettingsLink = null;
    
    private Label workflowParticipationLabel = null;
    private Label workflowInstancesLabel = null;
    private Label usersLabel = null;
    private Label settingsLabel = null;
    
    private WorkflowAdminNavigationMenu(){
        init();
    }
    
    private void init(){
        verticalLayout = new VerticalLayout();
        this.setCompositionRoot(verticalLayout);
        
        myWorkItemLink = new Button("My Workitems");
        myWorkItemLink.setStyleName(Button.STYLE_LINK);
        changeTenantLink = new Button("Change Tenant");
        changeTenantLink.setStyleName(Button.STYLE_LINK);
        createWorkflowInstanceLink = new Button("Create Workflow Instance");
        createWorkflowInstanceLink.setStyleName(Button.STYLE_LINK);
        showWorkflowInstancesLink = new Button("Show Workflow Instances");
        showWorkflowInstancesLink.setStyleName(Button.STYLE_LINK);
        showUserListLink = new Button("Show User List");
        showUserListLink.setStyleName(Button.STYLE_LINK);
        profileSettingsLink = new Button("Profile Settings", new ProfileSettingsAction());
        profileSettingsLink.setStyleName(Button.STYLE_LINK);
        
        workflowParticipationLabel = new Label("Workflow participation");
        workflowInstancesLabel = new Label("Workflow Instances");
        usersLabel = new Label("Users");
        settingsLabel = new Label("Settings");
    
        verticalLayout.setSpacing(true);
        
        verticalLayout.addComponent(workflowParticipationLabel);
        verticalLayout.addComponent(myWorkItemLink);
        verticalLayout.addComponent(changeTenantLink);
        
        verticalLayout.addComponent(workflowInstancesLabel);
        verticalLayout.addComponent(createWorkflowInstanceLink);
        verticalLayout.addComponent(showWorkflowInstancesLink);
        
        verticalLayout.addComponent(usersLabel);
        verticalLayout.addComponent(showUserListLink);

        verticalLayout.addComponent(settingsLabel);
        verticalLayout.addComponent(profileSettingsLink);
    }
    
    public static WorkflowAdminNavigationMenu getInstance(){
        if(workflowAdminNavigationMenu == null){
            workflowAdminNavigationMenu = new WorkflowAdminNavigationMenu();
        }
        return workflowAdminNavigationMenu;
    }

}
