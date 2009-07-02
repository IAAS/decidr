package de.decidr.ui.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.controller.ShowChangeTenantAction;
import de.decidr.ui.controller.ShowCreateWorkflowInstanceAction;
import de.decidr.ui.controller.ShowMyWorkitemsAction;
import de.decidr.ui.controller.ShowProfileSettingsAction;
import de.decidr.ui.controller.ShowUserListAction;
import de.decidr.ui.controller.ShowWorkflowInstancesAction;

public class WorkflowAdminNavigationMenu extends CustomComponent {
    
    /**
     * TODO: add comment
     */
    private static final long serialVersionUID = 3301289362151635416L;
    
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
    
    /**
     * Default constructor
     *
     */
    public WorkflowAdminNavigationMenu(){
        init();
    }
    
    /**
     * This method initializes the components of the workflow admin navigation menu component
     *
     */
    private void init(){
        verticalLayout = new VerticalLayout();
        this.setCompositionRoot(verticalLayout);
        
        myWorkItemLink = new Button("My Workitems", new ShowMyWorkitemsAction());
        myWorkItemLink.setStyleName(Button.STYLE_LINK);
        changeTenantLink = new Button("Change Tenant", new ShowChangeTenantAction());
        changeTenantLink.setStyleName(Button.STYLE_LINK);
        createWorkflowInstanceLink = new Button("Create Workflow Instance", new ShowCreateWorkflowInstanceAction());
        createWorkflowInstanceLink.setStyleName(Button.STYLE_LINK);
        showWorkflowInstancesLink = new Button("Show Workflow Instances", new ShowWorkflowInstancesAction());
        showWorkflowInstancesLink.setStyleName(Button.STYLE_LINK);
        showUserListLink = new Button("Show User List", new ShowUserListAction());
        showUserListLink.setStyleName(Button.STYLE_LINK);
        profileSettingsLink = new Button("Profile Settings", new ShowProfileSettingsAction());
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
    
 
}
