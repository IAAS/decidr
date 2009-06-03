package de.decidr.ui.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.controller.ProfileSettingsAction;

public class UserNavigationMenu extends CustomComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3304973028559451364L;

	private static UserNavigationMenu userNavigationMenu = null;
	
	private VerticalLayout verticalLayout;
	
	private Button myWorkItemLink = null;
	private Button changeTenantLink = null;
	private Button profileSettingsLink = null;
	
	private Label workflowParticipationLabel = null;
	private Label settingsLabel = null;
	
	private UserNavigationMenu(){
		init();
	}
	
	private void init(){	
		verticalLayout = new VerticalLayout();
		this.setCompositionRoot(verticalLayout);
		
		myWorkItemLink = new Button("My Workitems");
		myWorkItemLink.setStyleName(Button.STYLE_LINK);
		changeTenantLink = new Button("Change Tenant");
		changeTenantLink.setStyleName(Button.STYLE_LINK);		
		profileSettingsLink = new Button("Profile Settings", new ProfileSettingsAction());
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
	
	public static UserNavigationMenu getInstance(){
		if(userNavigationMenu == null){
			userNavigationMenu = new UserNavigationMenu();
		}
		return userNavigationMenu;
	}
	

}
