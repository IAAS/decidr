package de.decidr.ui.view;

import java.util.Arrays;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.BaseFieldFactory;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

//TODO:GH
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

import de.decidr.ui.controller.ChangeStatusAction;
import de.decidr.ui.controller.SaveProfileAction;
import de.decidr.ui.controller.ShowCancelMembershipAction;
import de.decidr.ui.controller.ShowChangeEmailAction;
import de.decidr.ui.controller.ShowChangePasswordAction;
import de.decidr.ui.controller.ShowLeaveTenantAction;
import de.decidr.ui.data.ProfileSettingsContainer;

/**
 * TODO: add comment
 *
 * @author GH
 */
public class ProfileSettingsComponent extends CustomComponent {
	
        //TODO:GH
        private ProfileSettingsContainer settingsContainer = new ProfileSettingsContainer();
        private BeanItem settingsItem = new BeanItem(settingsContainer); 
        
        private Form settingsForm = new Form();
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static ProfileSettingsComponent profileSettings = null;
	
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
		
	private Button changeEmailLink = null;
	private Button changePasswordLink = null;
	private Button leaveTenantLink = null;
	private Button cancelMembershipLink = null;
	
	private Button saveButton = null;
	
	private CheckBox statusCheckBox = null;
	
	
	public Item getSettingsItem(){
	    try{
                settingsForm.commit();
                
                //TODO: remove debug info
                Main.getCurrent().getMainWindow().showNotification("would save settings now");
            } catch (Exception e) {
                Main.getCurrent().getMainWindow().showNotification(e.getMessage());
            }

	    return settingsItem;
	}
	
	public CheckBox getStatus(){
	        return statusCheckBox;
	}
	
	private ProfileSettingsComponent(){
		init();
	}
	
	private void init(){
	    //TODO: GH
	        settingsForm.setWriteThrough(false);
	        settingsForm.setFieldFactory(new SettingsFieldFactory());
	        settingsForm.setItemDataSource(settingsItem);
	        settingsForm.setVisibleItemProperties(Arrays.asList(new String[] {
	                "firstNameText", "lastNameText", "streetText", "postalCode", "cityText" }));
	        
	    
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
		
		
		changeEmailLink = new Button("Change email", new ShowChangeEmailAction());
		changeEmailLink.setStyleName(Button.STYLE_LINK);
		changePasswordLink = new Button("Change password", new ShowChangePasswordAction());
		changePasswordLink.setStyleName(Button.STYLE_LINK);
		leaveTenantLink = new Button("Leave tenant", new ShowLeaveTenantAction());
		leaveTenantLink.setStyleName(Button.STYLE_LINK);
		cancelMembershipLink = new Button("Cancel Membership", new ShowCancelMembershipAction());
		cancelMembershipLink.setStyleName(Button.STYLE_LINK);
		
		saveButton = new Button("Save", new SaveProfileAction());
		
		statusCheckBox = new CheckBox();
		//statusCheckBox.addListener(new ChangeStatusAction());
		//TODO: Listener only reacts on other buttons
		
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
		formVerticalLayout.addComponent(settingsForm);
		
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
	public static ProfileSettingsComponent getInstance(){
		if(profileSettings == null){
			profileSettings = new ProfileSettingsComponent();
		}
		return profileSettings;
	}
	
	
	/**
	 * TODO: add comment
	 *
	 * @author GH
	 */
	private class SettingsFieldFactory extends BaseFieldFactory{
	    
	    public Field createField(Item item, Object propertyId, Component uiContext){
	        Field field = super.createField(item, propertyId, uiContext);
	        
	        if("firstNameText".equals(propertyId)){
	            TextField tf = (TextField) field;
	            tf.setCaption("First Name");
	            tf.setColumns(30);
	        } else if("lastNameText".equals(propertyId)){
                    TextField tf = (TextField) field;
                    tf.setCaption("Last Name");
                    tf.setColumns(30);
                } else if("streetText".equals(propertyId)){
                    TextField tf = (TextField) field;
                    tf.setCaption("Street");
                    tf.setColumns(30);
                } else if("postalCode".equals(propertyId)){
                    TextField tf = (TextField) field;
                    tf.setCaption("Postal Code");
                    tf.setColumns(30);
                } else if("cityText".equals(propertyId)){
                    TextField tf = (TextField) field;
                    tf.setCaption("City");
                    tf.setColumns(30);
                }
	        
	        return field;
	    }
	}
}