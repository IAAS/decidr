package de.decidr.ui.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.controller.ShowChangeTenantAction;
import de.decidr.ui.controller.ShowMyWorkitemsAction;
import de.decidr.ui.controller.ShowProfileSettingsAction;

public class UserNavigationMenu extends CustomComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3304973028559451364L;
	
	private VerticalLayout verticalLayout;
	
	private Button myWorkItemLink = null;
	private Button changeTenantLink = null;
	private Button profileSettingsLink = null;
	
	private Label workflowParticipationLabel = null;
	private Label settingsLabel = null;
	
	/**
	 * Default constructor
	 *
	 */
	public UserNavigationMenu(){
		init();
	}
	
	/**
	 * This method initializes the components of the user navigation menu component
	 *
	 */
	private void init(){	
		verticalLayout = new VerticalLayout();
		this.setCompositionRoot(verticalLayout);
		
		myWorkItemLink = new Button("My Workitems", new ShowMyWorkitemsAction());
		myWorkItemLink.setStyleName(Button.STYLE_LINK);
		changeTenantLink = new Button("Change Tenant", new ShowChangeTenantAction());
		changeTenantLink.setStyleName(Button.STYLE_LINK);		
		profileSettingsLink = new Button("Profile Settings", new ShowProfileSettingsAction());
		profileSettingsLink.setStyleName(Button.STYLE_LINK);
		
		workflowParticipationLabel = new Label("Workflow participation");
		settingsLabel = new Label("Settings");
	    
		verticalLayout.setSpacing(true);
		
		verticalLayout.addComponent(workflowParticipationLabel);
		verticalLayout.addComponent(myWorkItemLink);
		verticalLayout.addComponent(changeTenantLink);
	
		verticalLayout.addComponent(settingsLabel);
		verticalLayout.addComponent(profileSettingsLink);
	}
	

}
