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

import java.util.Arrays;

import com.vaadin.data.Item;
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
import de.decidr.ui.controller.ChangeStatusAction;
import de.decidr.ui.controller.SaveProfileAction;
import de.decidr.ui.controller.ShowCancelMembershipAction;
import de.decidr.ui.controller.ShowChangeEmailAction;
import de.decidr.ui.controller.ShowChangePasswordAction;
import de.decidr.ui.controller.ShowLeaveTenantDialogAction;
import de.decidr.ui.data.ProfileSettingsItem;

/**
 * The user can change his profile by inserting his personnel 
 * information.
 *
 * @author Geoffrey-Alexeij Heinze
 */
public class ProfileSettingsComponent extends CustomComponent {
	
        private ProfileSettingsItem settingsItem = new ProfileSettingsItem(); 
        
        private Form settingsForm = new Form();
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
	
	//TODO: make private when no longer needed for testing
	public void saveSettingsItem(){
		try{
            settingsForm.commit();
            
        } catch (Exception e) {
            Main.getCurrent().getMainWindow().showNotification(e.getMessage());
        }

	}
	
	/**
	 * Returns the settings item where the information are stored.
	 *
	 * @return settingsItem
	 */
	public Item getSettingsItem(){
	    
	    return settingsItem;
	}
	
	/**
	 * Returns the status check box.
	 *
	 * @return statusCheckBox
	 */ 
	public CheckBox getStatus(){
	        return statusCheckBox;
	}
	
	/**
	 * Default constructor
	 *  
	 */
	public ProfileSettingsComponent(){
		init();
	}
	
	/**
	 * This method initializes the components of the profile settings component
	 *
	 */
	private void init(){
	        settingsForm.setWriteThrough(false);
	        settingsForm.setFieldFactory(new SettingsFieldFactory());
	        settingsForm.setItemDataSource(settingsItem);
	        settingsForm.setVisibleItemProperties(Arrays.asList(new String[] {
	                "firstName", "lastName", "street", "postalCode", "city" }));
	        
	    
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
		leaveTenantLink = new Button("Leave tenant", new ShowLeaveTenantDialogAction());
		leaveTenantLink.setStyleName(Button.STYLE_LINK);
		cancelMembershipLink = new Button("Cancel Membership", new ShowCancelMembershipAction());
		cancelMembershipLink.setStyleName(Button.STYLE_LINK);
		
		saveButton = new Button("Save", new SaveProfileAction());
		
		statusCheckBox = new CheckBox();
		statusCheckBox.addListener(new ChangeStatusAction());
		statusCheckBox.setImmediate(true);
		
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

	
	
	/**
	 * This FieldFactory creates the input fields of the Form used above
	 *
	 * @author Geoffrey-Alexeij Heinze
	 */
	private class SettingsFieldFactory extends BaseFieldFactory{
	    
	    public Field createField(Item item, Object propertyId, Component uiContext){
	        Field field = super.createField(item, propertyId, uiContext);
	        
	        if("firstName".equals(propertyId)){
	            TextField tf = (TextField) field;
	            tf.setCaption("First Name");
	            tf.setColumns(30);
	        } else if("lastName".equals(propertyId)){
                    TextField tf = (TextField) field;
                    tf.setCaption("Last Name");
                    tf.setColumns(30);
                } else if("street".equals(propertyId)){
                    TextField tf = (TextField) field;
                    tf.setCaption("Street");
                    tf.setColumns(30);
                } else if("postalCode".equals(propertyId)){
                    TextField tf = (TextField) field;
                    tf.setCaption("Postal Code");
                    tf.setColumns(30);
                } else if("city".equals(propertyId)){
                    TextField tf = (TextField) field;
                    tf.setCaption("City");
                    tf.setColumns(30);
                }
	        
	        return field;
	    }
	}
}