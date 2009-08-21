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

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

import de.decidr.ui.controller.RestoreDefaultTenantSettingsAction;
import de.decidr.ui.controller.SaveTenantSettingsAction;
import de.decidr.ui.controller.UploadTenantLogoAction;

/**
 * The tenant can change his settings. He change his theme by choosing
 * a given background or font or he can enter his own CSS and upload it.
 *
 * @author AT
 */
public class TenantSettingsComponent extends CustomComponent {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -7841061147447361631L;
    
    
    private VerticalLayout verticalLayout = null;
    private HorizontalLayout browseHorizontalLayout = null;
    private HorizontalLayout buttonHorizontalLayout = null;
    private HorizontalLayout schemeHorizontalLayout = null;
    
    private Panel browsePanel = null;
    private Panel schemePanel = null;
    private Panel buttonPanel = null;
    
    private Label tenantSettingsLabel = null;
   
    private Upload logoUpload = null;
    
    private TextField textArea = null;
    private TextField browseTextField = null;
    private TextField cssTextField = null;
    
    private Button browseButton = null;
    private Button saveButton = null;
    private Button cancelButton = null;
    private Button restoreDefaultSettingsButton = null;
    private Button showAdvancedOptionsButton = null;
    private Button showBasicOptionsButton = null;
    private Button backgroundButton = null;
    private Button foregroundButton = null;
    private Button fontButton = null;
    String name = "logo.png";
    
    private Embedded logoEmbedded = null;
    
    /**
     * Default constructor.
     *
     */
    public TenantSettingsComponent() {
        init();
    }

    /**
     * This method initializes the components for the tenant settings component.
     *
     */
    private void init() {
        verticalLayout = new VerticalLayout();
        browseHorizontalLayout = new HorizontalLayout();
        buttonHorizontalLayout = new HorizontalLayout();
        schemeHorizontalLayout = new HorizontalLayout();
        
        browsePanel = new Panel();
        schemePanel = new Panel();
        schemePanel.setCaption("Color Scheme");
        buttonPanel = new Panel();
        
        tenantSettingsLabel = new Label("<h2>Tenant Settings</h2>");
        tenantSettingsLabel.setContentMode(Label.CONTENT_XHTML);
        
        textArea = new TextField();
        textArea.setRows(10);
        textArea.setColumns(30);
        textArea.setCaption("Description");
        
        logoUpload = new Upload("Upload Logo", new UploadTenantLogoAction());
        logoUpload.setButtonCaption("Upload Logo");
        
        
        browseTextField = new TextField();
        browseTextField.setColumns(30);
        browseTextField.setValue("img/logo.png");
        
        browseButton = new Button("Browse");
        
        saveButton = new Button("Save", new SaveTenantSettingsAction());
        cancelButton = new Button("Cancel");
        restoreDefaultSettingsButton = new Button("Restore default settings", new RestoreDefaultTenantSettingsAction());
        
        
        logoEmbedded = new Embedded("", new ThemeResource(browseTextField.getValue().toString()));
        logoEmbedded.setCaption("Logo");
        logoEmbedded.setImmediate(true);
        
        setCompositionRoot(verticalLayout);
        
        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(tenantSettingsLabel);
        verticalLayout.addComponent(textArea);
        verticalLayout.addComponent(logoEmbedded);
        verticalLayout.addComponent(browsePanel);
        
        browsePanel.addComponent(browseHorizontalLayout);
        browseHorizontalLayout.setSpacing(true);
        //browseHorizontalLayout.addComponent(browseTextField);
        //browseHorizontalLayout.addComponent(browseButton);
        browseHorizontalLayout.addComponent(logoUpload);
        
        verticalLayout.addComponent(schemePanel);
        
        schemePanel.addComponent(schemeHorizontalLayout);
        schemeHorizontalLayout.setSpacing(true);
        
        changeToBasic();
        
        verticalLayout.addComponent(buttonPanel);
        
        buttonPanel.addComponent(buttonHorizontalLayout);
        buttonHorizontalLayout.setSpacing(true);
        buttonHorizontalLayout.addComponent(saveButton);
        buttonHorizontalLayout.addComponent(cancelButton);
        buttonHorizontalLayout.addComponent(restoreDefaultSettingsButton);
        
       
    }
    
    /**
     * Returns the text area.
     *
     * @return textArea
     */
    public TextField getTenantDescription(){
    	return textArea;
    }
    
   
    
    /**
     * This method changes the view from advanced to basic. So only the
     * basic information are visible and the tenant can choose a given background 
     * or font.
     *
     */
    private void changeToBasic(){
        backgroundButton = new Button("Background");
        backgroundButton.setStyleName(Button.STYLE_LINK);
        foregroundButton = new Button("Foreground");
        foregroundButton.setStyleName(Button.STYLE_LINK);
        fontButton = new Button("Font");
        fontButton.setStyleName(Button.STYLE_LINK);
        showAdvancedOptionsButton = new Button("Show advanced options");
        showAdvancedOptionsButton.addListener(new Button.ClickListener(){

            @Override
            public void buttonClick(ClickEvent event) {
                changeToAdvanced();                
            }
            
        });
        
        getSchemeHorizontalLayout().removeAllComponents();
        getSchemeHorizontalLayout().addComponent(backgroundButton);
        getSchemeHorizontalLayout().addComponent(foregroundButton);
        getSchemeHorizontalLayout().addComponent(fontButton);
        getSchemeHorizontalLayout().addComponent(showAdvancedOptionsButton);
        getSchemeHorizontalLayout().setComponentAlignment(showAdvancedOptionsButton, Alignment.BOTTOM_RIGHT);
    }
    
    /**
     * This method changes the view from basic to advanced. So the 
     * text area for the CSS is visible.
     *
     */
    private void changeToAdvanced(){
        showBasicOptionsButton = new Button("Show basic options");
        showBasicOptionsButton.addListener(new Button.ClickListener(){

            @Override
            public void buttonClick(ClickEvent event) {
               changeToBasic();                
            }
            
        });
        cssTextField = new TextField();
        cssTextField.setRows(10);
        cssTextField.setColumns(30);
        getSchemeHorizontalLayout().removeAllComponents();
        getSchemeHorizontalLayout().addComponent(cssTextField);
        getSchemeHorizontalLayout().addComponent(showBasicOptionsButton);
    }
    
 
    /**
     * Returns the horizontal layout for the CSS panel.
     *
     * @return schemeHorizontalLayout
     */
    public HorizontalLayout getSchemeHorizontalLayout() {
        return schemeHorizontalLayout;
    }

    

}
