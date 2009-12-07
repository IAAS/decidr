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

package de.decidr.ui.view.help;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * This component is a part of the integrated manual
 * and contains information related to account management
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class AccountManagementHelpComponent extends VerticalLayout {

    /**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;
	private Button registerAsTenantAdminButton = null;
    private Label registerAsTenantAdminLabel = null;
    
    private Button registerAsUserButton = null;
    private Label registerAsUserLabel = null;

    private Button changeEmailButton = null;
    private Label changeEmailLabel = null;
    
    private Button changePasswordButton = null;
    private Label changePasswordLabel = null;

    private Button changeAddressButton = null;
    private Label changeAddressLabel = null;

    private Button createNewTenantButton = null;
    private Label createNewTenantLabel = null;

    private Button switchTenantButton = null;
    private Label switchTenantLabel = null;

    private Button leaveTenantButton = null;
    private Label leaveTenantLabel = null;

    private Button changeStatusButton = null;
    private Label changeStatusLabel = null;

    public AccountManagementHelpComponent(){
        setMargin(false, true, true, true);
        
        registerAsTenantAdminLabel = new Label("1) Follow the registration link to see the registration form in your browser <br/>"
                                               +"2) Choose to create a new tenant along with your account<br/>"
                                               +"3)      Fill out the data needed to proceed (e.g. email and additional address data)<br/>"
                                               +"4)      Please be sure, that your email is valid, because your need it for the activation of your account.<br/>"
                                               +"5)      Press the 'Continue' button.<br/>"
                                               +"6)      You get a confirmation mail<br/><br/>"
                                               +"Problems:<br/>"
                                               +"You can’t register, because your data is invalid. This can have many reasons:<br/>"
                                               +"-       Your username/tenant name/mail address has been already taken. Please check your data and try again.<br/>"
                                               +"-       Your username or tenant name is not long enough. Please make sure that your username or tenant name are longer than 3 characters.<br/><br/>",
                Label.CONTENT_XHTML);
        registerAsTenantAdminLabel.setVisible(false);
        registerAsTenantAdminButton = new Button("How to register as tenant admin?", new ToggleLabelAction(registerAsTenantAdminLabel));
        registerAsTenantAdminButton.setStyleName(Button.STYLE_LINK);
        
        this.addComponent(registerAsTenantAdminButton);
        this.addComponent(registerAsTenantAdminLabel);
        

        registerAsUserLabel = new Label("1) Follow the registration link to see the registration form in your browser<br/>"
                +"2) Fill out the data needed to proceed (e.g. email and additional address data)<br/>"
                +"3) Please be sure, that your email is valid, because your need it for the activation of your account.<br/>"
                +"4) Press the 'Continue' button.<br/>"
                +"5) You get a confirmation mail<br/>"
                +"<br/>"	
                +"Problems:<br/>"
                +"You can’t register, because your data is invalid. This can have many reasons:<br/>"
                +"- Your username/ mail address has been already taken. Please check your data and try again.<br/>"
                +"- Your username or tenant name is not long enough. Please make sure that your username is longer than 3 characters.<br/><br/>",
                Label.CONTENT_XHTML);
        registerAsUserLabel.setVisible(false);
        registerAsUserButton = new Button("How to register as normal user?", new ToggleLabelAction(registerAsUserLabel));
        registerAsUserButton.setStyleName(Button.STYLE_LINK);
        
        this.addComponent(registerAsUserButton);
        this.addComponent(registerAsUserLabel);
        

        changeEmailLabel = new Label("1) Login into DecidR and navigate to 'Profile Page' through the click in the navigation bar.<br/>"
                +"2) The profile page shows your data.<br/>" 
                +"3) Click on the 'Change mail' link<br/>" 
                +"4) Type your new mail address and confirm it.<br/><br/>",
                Label.CONTENT_XHTML);
        changeEmailLabel.setVisible(false);
        changeEmailButton = new Button("How to change my email address?", new ToggleLabelAction(changeEmailLabel));
        changeEmailButton.setStyleName(Button.STYLE_LINK);
        
        this.addComponent(changeEmailButton);
        this.addComponent(changeEmailLabel);

        changePasswordLabel = new Label("1) Login into DecidR and navigate to the 'Profile Page' through the click in the navigation bar<br/>"
                +"2) The profile page shows your data.<br/>"
                +"3) Click on the 'Change password' link.<br/>"
                +"4) The page shows a new window where you have to enter your old and your new desired password<br/><br/>",
                Label.CONTENT_XHTML);
        changePasswordLabel.setVisible(false);
        changePasswordButton = new Button("How to change my password?", new ToggleLabelAction(changePasswordLabel));
        changePasswordButton.setStyleName(Button.STYLE_LINK);
        
        this.addComponent(changePasswordButton);
        this.addComponent(changePasswordLabel);

        changeAddressLabel = new Label("1) Login into DecidR and navigate to the 'Profile Page' through the click in the navigation bar<br/>"
                +"2) The profile page shows your data.<br/>"
                +"3) Change the entries in 'First name', 'Lat name', 'Street', 'Postal code' and 'City'<br/>"
                +"4) Click on the 'Save' button<br/><br/>",
                Label.CONTENT_XHTML);
        changeAddressLabel.setVisible(false);
        changeAddressButton = new Button("How to change my address data?", new ToggleLabelAction(changeAddressLabel));
        changeAddressButton.setStyleName(Button.STYLE_LINK);
        
        this.addComponent(changeAddressButton);
        this.addComponent(changeAddressLabel);

        createNewTenantLabel = new Label("1) Login into DecidR and navigate to 'Create Tenant' in the navigation bar.<br/>"
                +"2) Enter the tenant information<br/>"
                +"3) Click on the save button<br/>"
                +"4) If the tenant isn’t already registered you are not the tenant admin of the newly created tenant<br/><br/>",
                Label.CONTENT_XHTML);
        createNewTenantLabel.setVisible(false);
        createNewTenantButton = new Button("How to create a new tenant?", new ToggleLabelAction(createNewTenantLabel));
        createNewTenantButton.setStyleName(Button.STYLE_LINK);
        
        this.addComponent(createNewTenantButton);
        this.addComponent(createNewTenantLabel);

        switchTenantLabel = new Label("1) Login into DecidR and navigate to the 'Change tenant' in the navigation bar.<br/>"
                +"2) The page shows you the list of your tenants.<br/>" 
                +"3) Select the tenant you want to switch to and click on the 'switch' button<br/><br/>",
                Label.CONTENT_XHTML);
        switchTenantLabel.setVisible(false);
        switchTenantButton = new Button("How to switch tenant?", new ToggleLabelAction(switchTenantLabel));
        switchTenantButton.setStyleName(Button.STYLE_LINK);
        
        this.addComponent(switchTenantButton);
        this.addComponent(switchTenantLabel);

        leaveTenantLabel = new Label("Please make that you are not the tenant admin and/or you are not participating in a particular workflow. Otherwise you cannot leave tenant.<br/><br/>"
                +"1) Login into DecidR and navigate to the 'Profile Page' in the navigation bar.<br/>"
                +"2) The page shows you your profile page. Click on the 'leave tenant' link.<br/>"
                +"3) The page shows you’re the list of your tenants. Choose a tenant your want to leave and confirm it with the 'ok' button.<br/><br/>",
                Label.CONTENT_XHTML);
        leaveTenantLabel.setVisible(false);
        leaveTenantButton = new Button("How to leave a tenant?", new ToggleLabelAction(leaveTenantLabel));
        leaveTenantButton.setStyleName(Button.STYLE_LINK);
        
        this.addComponent(leaveTenantButton);
        this.addComponent(leaveTenantLabel);

        changeStatusLabel = new Label("1) Login into DecidR and navigate to the 'Profile Page' in the navigation bar.<br/>"
                +"2) Check or uncheck the checkbox 'Set my status to unavailable'<br/>"
                +"3) Click on the save button<br/><br/>",
                Label.CONTENT_XHTML);
        changeStatusLabel.setVisible(false);
        changeStatusButton = new Button("How to change my status to un/available?", new ToggleLabelAction(changeStatusLabel));
        changeStatusButton.setStyleName(Button.STYLE_LINK);
        
        this.addComponent(changeStatusButton);
        this.addComponent(changeStatusLabel);
    }
}
