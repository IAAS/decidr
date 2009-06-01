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
import com.vaadin.ui.Component;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.VerticalLayout;

public class MenuSplitPanel extends CustomComponent {

        private static MenuSplitPanel menuSplitPanel = null;
        
        private VerticalLayout verticalLayout = null;
        private SplitPanel splitPanel = null;
        
        private MenuSplitPanel(){
                init();
        }
        
        private void init(){
                verticalLayout = new VerticalLayout();
                this.setCompositionRoot(verticalLayout);
                
                splitPanel = new SplitPanel();
                
                splitPanel.setOrientation(SplitPanel.ORIENTATION_HORIZONTAL);
                splitPanel.setWidth("800px");
                splitPanel.setHeight("600px");
                splitPanel.setSplitPosition(200 ,SplitPanel.UNITS_PIXELS);
                splitPanel.setLocked(true);
                
                verticalLayout.addComponent(splitPanel);
                
                
        }
        
        public static MenuSplitPanel getInstance(){
        if(menuSplitPanel == null){
                menuSplitPanel = new MenuSplitPanel();
        }
        return menuSplitPanel;
    }

        public void addMenu(Component m){
                splitPanel.setFirstComponent(m);
        }

        public void addContent(Component m){
                splitPanel.setSecondComponent(m);
        }
}