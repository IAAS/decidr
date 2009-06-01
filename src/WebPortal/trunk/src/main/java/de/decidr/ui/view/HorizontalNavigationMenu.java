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

/**
 * TODO: add comment
 *
 * @author GH
 */

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;


@SuppressWarnings("serial")
public class HorizontalNavigationMenu extends CustomComponent {
        
        private static HorizontalNavigationMenu horizontalNavigationMenu = null;
        
        private HorizontalLayout horizontalLayout = null;
        
        private Button btnHomeLink = null;
        private Button btnHelpLink = null;
        private Button btnImpressumLink = null;
        private Button btnTermsOfServiceLink = null;
        
        private HorizontalNavigationMenu(){
                init();
        }
        
        private void init(){
                horizontalLayout = new HorizontalLayout();
                this.setCompositionRoot(horizontalLayout);
                
                btnHomeLink = new Button("Home");
                btnHomeLink.setStyleName(Button.STYLE_LINK);
                btnHelpLink = new Button("Help");
                btnHelpLink.setStyleName(Button.STYLE_LINK);
                btnImpressumLink = new Button("Impressum");
                btnImpressumLink.setStyleName(Button.STYLE_LINK);
                btnTermsOfServiceLink = new Button("Terms of Service");
                btnTermsOfServiceLink.setStyleName(Button.STYLE_LINK);
                
                horizontalLayout.setWidth(800,HorizontalLayout.UNITS_PIXELS);
                horizontalLayout.setSpacing(true);
                
                horizontalLayout.addComponent(btnHomeLink);
                horizontalLayout.addComponent(btnHelpLink);
                horizontalLayout.addComponent(btnTermsOfServiceLink);
                horizontalLayout.addComponent(btnImpressumLink);
                
        }
        
        public static HorizontalNavigationMenu getInstance(){
        if(horizontalNavigationMenu == null){
                horizontalNavigationMenu = new HorizontalNavigationMenu();
        }
        return horizontalNavigationMenu;
    }
}
