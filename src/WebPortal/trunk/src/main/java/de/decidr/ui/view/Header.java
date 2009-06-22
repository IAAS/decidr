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
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;


@SuppressWarnings("serial")
public class Header extends CustomComponent {
        
        private HorizontalLayout horizontalLayout = null;
        private Embedded decidrLogo = null;
        
        /**
         * Default constructor
         *
         */
        public Header(){
                init();
        }
        
        /**
         * This method initializes the components of the header component
         *
         */
        private void init(){
                horizontalLayout = new HorizontalLayout();
                this.setCompositionRoot(horizontalLayout);
                
                horizontalLayout.setWidth(800,HorizontalLayout.UNITS_PIXELS);
                
                decidrLogo = new Embedded("",Resources.getInstance().getDecidrLogo());
                
                horizontalLayout.addComponent(decidrLogo);
        }
        
       
}