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

import javax.management.Descriptor;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.controller.ShowRegisterTenantAction;
import de.decidr.ui.controller.ShowRegisterUserAction;

/**
 * TODO: add comment
 *
 * @author GH
 */
public class RegistrationComponent extends CustomComponent {
    //FIXME: currently non singleton
    private VerticalLayout verticalLayout = null;
    private HorizontalLayout horizontalLayout = null;
    
    private Label descriptionLabel = null;
    
    private Button registerUser = null;
    private Button registerTenant = null;
    
    public RegistrationComponent(){
        init();
    }
    
    private void init(){
        verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setSpacing(true);
        this.setCompositionRoot(verticalLayout);
        
        descriptionLabel = new Label("Please select what you want to do:",Label.CONTENT_XHTML);
        
        registerTenant = new Button ("create new tenant", new ShowRegisterTenantAction());
        registerTenant.setStyle(Button.STYLE_LINK);
        
        registerUser = new Button("create new user", new ShowRegisterUserAction());
        registerUser.setStyle(Button.STYLE_LINK);
        
        horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        horizontalLayout.addComponent(registerUser);
        horizontalLayout.addComponent(registerTenant);
        horizontalLayout.setComponentAlignment(registerUser, "center bottom");
        horizontalLayout.setComponentAlignment(registerTenant, "center bottom");
        
        verticalLayout.addComponent(descriptionLabel);
        verticalLayout.addComponent(horizontalLayout);
    }
}