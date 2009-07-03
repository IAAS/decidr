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
 * @author Geoffrey-Alexeij Heinze
 */
import javax.servlet.http.HttpSession;

import com.vaadin.data.Item;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class WelcomePageComponent extends CustomComponent{

    private HttpSession session = Main.getCurrent().getSession();
    
        private VerticalLayout verticalLayout = null;
        private Label labelDesc = null;
        
        /**
         * Default constructor
         *
         */
        public WelcomePageComponent(){
                init();
        }
        
        /**
         * This method initializes the components of the help component
         *
         */
        private void init(){
                verticalLayout = new VerticalLayout();
        this.setCompositionRoot(verticalLayout);

        
        Item tenant = (Item) session.getAttribute("tenant");
        if(tenant != null){
        	//TODO: get tenant description
        	labelDesc = new Label("tenant description",
                    Label.CONTENT_XHTML);
        }else{
        	labelDesc = new Label("<h2>Welcome</h2><br/>Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
                    Label.CONTENT_XHTML);
        }
        
        verticalLayout.addComponent(labelDesc);
        }
        
        
}
