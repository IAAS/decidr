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
 * This site frame is a template for the DecidR application. In this 
 * site frame several components can be added. It is the structure of
 * the DecidR application. Components like the header the horizontal 
 * navigation menu, the vertical navigation menu and the content
 * can be placed in this site frame. 
 * 
 * So only the specific component has to be changed and not the whole
 * page.
 *
 * @author Geoffrey-Alexeij Heinze
 */
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.SplitPanel;

@SuppressWarnings("serial")
public class SiteFrame extends CustomComponent {

    private GridLayout gridFrame = null;
    private SplitPanel splitPanel = null;
    private Component content = null;
    private Component header = null;
    private Component navigation = null;
    private Component hNavigation = null;

    /**
     * Default constructor.
     * 
     */
    public SiteFrame() {
        init();
    }

    /**
     * Returns the current content.
     * 
     * @return content
     */
    public Component getContent() {
        return content;
    }

    /**
     * Returns the current header.
     * 
     * @return header
     */
    public Component getHeader() {
        return header;
    }

    /**
     * Returns the current horizontal navigation menu.
     * 
     * @return
     */
    public Component getHNavigation() {
        return hNavigation;
    }

    /**
     * Returns the current vertical navigation menu.
     * 
     * @return navigation
     */
    public Component getNavigation() {
        return navigation;
    }

    /**
     * Initializes the components for the site frame.
     * 
     */
    private void init() {
        gridFrame = new GridLayout(3, 5);
        this.setCompositionRoot(gridFrame);

        gridFrame.setSizeFull();
        gridFrame.setMargin(false);
        gridFrame.setSpacing(false);

        splitPanel = new SplitPanel();

        splitPanel.setOrientation(SplitPanel.ORIENTATION_HORIZONTAL);
        splitPanel.setWidth("1000px");
        splitPanel.setHeight("600px");
        splitPanel.setSplitPosition(200, Sizeable.UNITS_PIXELS);
        splitPanel.setLocked(true);

        gridFrame.setColumnExpandRatio(0, 1);
        gridFrame.setColumnExpandRatio(2, 1);

        gridFrame.addComponent(splitPanel, 1, 3);

    }

    /**
     * Sets the content into the site frame and stores the current content in a
     * varibale.
     * 
     * @param content
     */
    public void setContent(Component content) {
        splitPanel.setSecondComponent(content);
        this.content = content;
        this.content.addStyleName("dcdr_content");
        this.content.setWidth("775px");
    }

    /**
     * Sets the header into the site frame and stores the current header in a
     * variable.
     * 
     * @param header
     */
    public void setHeader(Component header) {
    	init();
        gridFrame.addComponent(header, 1, 1);
        this.header = header;
        this.header.addStyleName("dcdr_header");
    }

    /**
     * Sets the horizontal navigation menu into the site frame.
     * 
     * @param navigation
     */
    public void setHorizontalNavigation(Component navigation) {
        gridFrame.addComponent(navigation, 1, 2);
        this.hNavigation = navigation;
        this.hNavigation.addStyleName("dcdr_hnav");
    }

    /**
     * Sets the vertical navigation menu into the site frame and stores the
     * current vertical navigation menu in a variable.
     * 
     * @param navigation
     */
    public void setVerticalNavigation(Component navigation) {
        splitPanel.setFirstComponent(navigation);
        this.navigation = navigation;
        this.navigation.addStyleName("dcdr_vnav");
    }

}
