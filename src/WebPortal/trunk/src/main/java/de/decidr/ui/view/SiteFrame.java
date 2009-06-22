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
import javax.servlet.http.Cookie;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.SplitPanel;

@SuppressWarnings("serial")
public class SiteFrame extends CustomComponent{
        
        
        private GridLayout gridFrame = null;
        private SplitPanel splitPanel = null;
        private Component content = null;
        private Component header = null;
        private Component navigation = null;
        
        public SiteFrame(){
                init();
        }
        
        private void init(){
        gridFrame = new GridLayout(3,5);
        this.setCompositionRoot(gridFrame);
        
        
        gridFrame.setSizeFull();
        gridFrame.setMargin(true);
        gridFrame.setSpacing(true);
        
        splitPanel = new SplitPanel();
        
        splitPanel.setOrientation(SplitPanel.ORIENTATION_HORIZONTAL);
        splitPanel.setWidth("1000px");
        splitPanel.setHeight("600px");
        splitPanel.setSplitPosition(200 ,SplitPanel.UNITS_PIXELS);
        splitPanel.setLocked(true);
        
        gridFrame.setColumnExpandRatio(0, 1);
        gridFrame.setColumnExpandRatio(2, 1);
        
        gridFrame.addComponent(splitPanel, 1, 3);
        
        }
         
        
        public void setHeader(Component header){
                gridFrame.addComponent(header, 1, 1);
                this.header = header;
        }
        
        public void setHorizontalNavigation(Component navigation){
                gridFrame.addComponent(navigation, 1, 2);
        }
        
        public void setVerticalNavigation(Component navigation){
                splitPanel.setFirstComponent(navigation);
                this.navigation = navigation;
        }
        
        public void setContent(Component content){
                splitPanel.setSecondComponent(content);
                this.content = content;
        }
        
        public Component getContent(){
            return content;
        }
        
        public Component getHeader() {
            return header;
        }

        public Component getNavigation() {
            return navigation;
        }

}

