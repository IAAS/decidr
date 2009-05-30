package de.decidr.ui.view;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ProfileSettings extends CustomComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static ProfileSettings profileSettings = null;
	
	private Panel profilePanel = null;
	private Panel addressPanel = null;
	private Panel formPanel = null;
	private Panel buttonPanel = null;
	private Panel usernamePanel = null;
	private Panel profileButtonPanel = null;
	
	private VerticalLayout verticalLayout = null;
	private GridLayout usernameGridLayout = null;
	private VerticalLayout addressVerticalLayout = null;
	private VerticalLayout profileVerticalLayout = null;
	private VerticalLayout formVerticalLayout = null;
	private HorizontalLayout buttonHorizontalLayout = null;
	private HorizontalLayout profileButtonHorizontalLayout = null;

	private Label myProfileLabel = null;
	private Label usernameLabel = null;
	private Label emailLabel = null;
	private Label usernameNameLabel = null;
	private Label emailNameLabel = null;
	private Label addressDataLabel = null;
	
	private TextField firstNameTextField = null;
	private TextField lastNameTextField = null;
	private TextField streetTextField = null;
	private TextField postalCodeTextField = null;
	private TextField cityTextField = null;
	
	private Button changeEmailLink = null;
	private Button changePasswordLink = null;
	private Button leaveTenantLink = null;
	private Button cancelMembershipLink = null;
	
	private Button saveButton = null;
	
	private CheckBox statusCheckBox = null;
	
	private ProfileSettings(){
		init();
	}
	
	private void init(){
		profilePanel = new Panel();
		addressPanel = new Panel();
		formPanel = new Panel();
		buttonPanel = new Panel();
		usernamePanel = new Panel();
		profileButtonPanel = new Panel();
		
		usernameGridLayout = new GridLayout(2,2);
		verticalLayout = new VerticalLayout();
		addressVerticalLayout = new VerticalLayout();
		profileVerticalLayout = new VerticalLayout();
		formVerticalLayout = new VerticalLayout();
		buttonHorizontalLayout = new HorizontalLayout();
		profileButtonHorizontalLayout = new HorizontalLayout();
		
		myProfileLabel = new Label("<h2> My Profile </h2>");
		myProfileLabel.setContentMode(Label.CONTENT_XHTML);
		usernameLabel = new Label("Username: ");
		emailLabel = new Label("Email address: ");
		usernameNameLabel = new Label("test1");
		emailNameLabel = new Label("test");
		addressDataLabel = new Label("Address Data (optional)");
		
		firstNameTextField = new TextField("First name");
		firstNameTextField.setColumns(30);
		lastNameTextField = new TextField("Last name");
		lastNameTextField.setColumns(30);
		streetTextField = new TextField("Street");
		streetTextField.setColumns(30);
		postalCodeTextField = new TextField("Postal code");
		postalCodeTextField.setColumns(30);
		cityTextField = new TextField("City");
		cityTextField.setColumns(30);
		
		changeEmailLink = new Button("Change email");
		changeEmailLink.setStyleName(Button.STYLE_LINK);
		changePasswordLink = new Button("Change password");
		changePasswordLink.setStyleName(Button.STYLE_LINK);
		leaveTenantLink = new Button("Leave tenant");
		leaveTenantLink.setStyleName(Button.STYLE_LINK);
		cancelMembershipLink = new Button("Cancel Membership");
		cancelMembershipLink.setStyleName(Button.STYLE_LINK);
		
		saveButton = new Button("Save");
		
		statusCheckBox = new CheckBox();
		
		this.setCompositionRoot(verticalLayout);
		
		verticalLayout.addComponent(profilePanel);
		verticalLayout.setComponentAlignment(profilePanel, Alignment.TOP_LEFT);
		
		profilePanel.setContent(profileVerticalLayout);
		profileVerticalLayout.setSpacing(true);
		profileVerticalLayout.addComponent(myProfileLabel);
		profileVerticalLayout.addComponent(usernamePanel);
		
		usernamePanel.addComponent(usernameGridLayout);
		usernameGridLayout.setSpacing(true);
		usernameGridLayout.addComponent(usernameLabel, 0, 0);
		usernameGridLayout.addComponent(usernameNameLabel, 1, 0);
		usernameGridLayout.addComponent(emailLabel, 0, 1);
		usernameGridLayout.addComponent(emailNameLabel, 1, 1);
		
		profileVerticalLayout.addComponent(profileButtonPanel);
		
		profileButtonPanel.addComponent(profileButtonHorizontalLayout);
		profileButtonHorizontalLayout.setSpacing(true);
		profileButtonHorizontalLayout.addComponent(changeEmailLink);
		profileButtonHorizontalLayout.addComponent(changePasswordLink);
		
		verticalLayout.addComponent(addressPanel);
		verticalLayout.setComponentAlignment(addressPanel, Alignment.MIDDLE_LEFT);
		
		addressPanel.addComponent(addressVerticalLayout);
		addressVerticalLayout.setSpacing(true);
		addressVerticalLayout.addComponent(addressDataLabel);
		addressVerticalLayout.addComponent(formPanel);
		
		formPanel.addComponent(formVerticalLayout);
		formVerticalLayout.setSpacing(true);
		formVerticalLayout.addComponent(firstNameTextField);
		formVerticalLayout.addComponent(lastNameTextField);
		formVerticalLayout.addComponent(streetTextField);
		formVerticalLayout.addComponent(postalCodeTextField);
		formVerticalLayout.addComponent(cityTextField);
		
		addressVerticalLayout.addComponent(statusCheckBox);
		statusCheckBox.setCaption("Set my status to unavailable");
		
		verticalLayout.addComponent(buttonPanel);
		verticalLayout.setComponentAlignment(buttonPanel, Alignment.BOTTOM_LEFT);
		
		buttonPanel.addComponent(buttonHorizontalLayout);
		buttonHorizontalLayout.setSpacing(true);
		buttonHorizontalLayout.addComponent(saveButton);
		buttonHorizontalLayout.addComponent(leaveTenantLink);
		buttonHorizontalLayout.addComponent(cancelMembershipLink);
	}

	//instance method singleton
	public static ProfileSettings getInstance(){
		if(profileSettings == null){
			profileSettings = new ProfileSettings();
		}
		return profileSettings;
	}

}
