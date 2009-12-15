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

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.SplitPanel;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;

/**
 * This site frame is a template for the DecidR application. Several components
 * can be added to this site frame. It is the structure of the DecidR
 * application. Components like the header, the horizontal navigation menu, the
 * vertical navigation menu and the content can be placed into this site frame.
 * <p>
 * So only a specific component needs to be changed and not the whole page.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2048", currentReviewState = State.PassedWithComments)
public class SiteFrame extends CustomComponent {

    private GridLayout gridFrame = null;
    private SplitPanel splitPanel = null;
    private Component content = null;
    private Component header = null;
    private Component navigation = null;
    private Component hNavigation = null;

    /**
     * TODO document
     */
    public SiteFrame() {
        init();
    }

    /**
     * Returns the current content.
     * 
     * @return content TODO document
     */
    public Component getContent() {
        return content;
    }

    /**
     * Returns the current header.
     * 
     * @return header TODO document
     */
    public Component getHeader() {
        return header;
    }

    /**
     * Returns the current horizontal navigation menu.
     * 
     * @return TODO document
     */
    public Component getHNavigation() {
        return hNavigation;
    }

    /**
     * Returns the current vertical navigation menu.
     * 
     * @return navigation TODO document
     */
    public Component getNavigation() {
        return navigation;
    }

    /**
     * Initializes the components of the site frame.
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
     * Puts the content into the site frame and stores the current content in a
     * variable.
     * Also sets the components style name which is used in the styles.css
     * 
     * @param content
     *            TODO document
     */
    public void setContent(Component content) {
        splitPanel.setSecondComponent(content);
        this.content = content;
        this.content.addStyleName("dcdr_content");
        this.content.setWidth("775px");
    }

    /**
     * Puts the header into the site frame and stores the current header in a
     * variable.
     * Also sets the components style name which is used in the styles.css
     * 
     * @param header
     *            TODO document
     */
    public void setHeader(Component header) {
        init();
        gridFrame.addComponent(header, 1, 1);
        this.header = header;
        this.header.addStyleName("dcdr_header");
    }

    /**
     * Puts the horizontal navigation menu into the site frame.
     * Also sets the components style name which is used in the styles.css
     * 
     * @param navigation
     *            TODO document
     */
    public void setHorizontalNavigation(Component navigation) {
        gridFrame.addComponent(navigation, 1, 2);
        this.hNavigation = navigation;
        this.hNavigation.addStyleName("dcdr_hnav");
    }

    /**
     * Puts the vertical navigation menu into the site frame and stores the
     * current vertical navigation menu in a variable.
     * Also sets the components style name which is used in the styles.css
     * 
     * @param navigation
     *            TODO document
     */
    public void setVerticalNavigation(Component navigation) {
        splitPanel.setFirstComponent(navigation);
        this.navigation = navigation;
        this.navigation.addStyleName("dcdr_vnav");
    }
}
