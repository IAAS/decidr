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
 * and contains information related to tenant settings
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class TenantSettingsHelpComponent extends VerticalLayout {

    /**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;
	private Button changeSettingsButton = null;
    private Label changeSettingsLabel = null;

    private Button changeLogoButton = null;
    private Label changeLogoLabel = null;

    private Button restoreDefaultButton = null;
    private Label restoreDefaultLabel = null;
    
    public TenantSettingsHelpComponent(){
        setMargin(false, true, true, true);

        changeSettingsLabel = new Label("1) Login into DecidR and navigate to 'Tenant Settings' in the navigation bar.<br/>"
                +"2) Enter new description text and change the tenant settings.<br/>"
                +"3) Click on the save button.<br/><br/>",
                Label.CONTENT_XHTML);
        changeSettingsLabel.setVisible(false);
        changeSettingsButton = new Button("How to change tenant settings?", new ToggleLabelAction(changeSettingsLabel));
        changeSettingsButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(changeSettingsButton);
        this.addComponent(changeSettingsLabel);

        changeLogoLabel = new Label("1) Login into DecidR and navigate to 'Tenant Settings' in the navigation bar.<br/>"
                +"2) Select a file on you system to use as the new tenant logo.<br/>"
                +"3) After the upload click on the 'save' button.<br/><br/>",
                Label.CONTENT_XHTML);
        changeLogoLabel.setVisible(false);
        changeLogoButton = new Button("How to change tenant logo?", new ToggleLabelAction(changeLogoLabel));
        changeLogoButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(changeLogoButton);
        this.addComponent(changeLogoLabel);

        restoreDefaultLabel = new Label("1) Login into DecidR and navigate to 'Tenant Settings' in the navigation bar.<br/>"
                +"2) The system displays the current tenant settings.<br/>"
                +"3) Click on the 'Restore default settings' button.<br/><br/>",
                Label.CONTENT_XHTML);
        restoreDefaultLabel.setVisible(false);
        restoreDefaultButton = new Button("How to restore default tenant settings?", new ToggleLabelAction(restoreDefaultLabel));
        restoreDefaultButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(restoreDefaultButton);
        this.addComponent(restoreDefaultLabel);
        
    }
    
}
