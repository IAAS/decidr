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

package de.decidr.ui.controller;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.ui.view.SiteFrame;
import de.decidr.ui.view.help.HelpDialogComponent;
import de.decidr.ui.view.uibuilder.UIBuilder;

/**
 * This class is the director for building the UI. In the DecidR application
 * there are different roles. Every role has an other authority and other items
 * are displayed. The user with lowest authority is the unregistered user. He
 * only can browse through the application. The next level is the registered
 * user. He is able to edit his personal settings and to log in. The workflow
 * administrator is able to administrate a workflow instance which is started
 * within the DecidR application. The tenant administrator administrates an
 * amount of users which belong to him. He can create/adapt a tenant-specific
 * look and feel within the application using CSS. He is also able to remove or
 * invite a user. The super administrator can access the full functionality. He
 * is able to make server specific adjustments.
 * 
 * @author AT
 */
@Reviewed(reviewers = "RR", lastRevision = "2343", currentReviewState = State.PassedWithComments)
public class UIDirector {

    private UIBuilder uiBuilder = null;

    private SiteFrame siteFrame = null;

    private HelpDialogComponent helpDialog = null;

    /**
     * Constructs the view which is shown to the user. Here the header, the
     * specific content and the specific vertical navigation menu is built
     * depending on which role the user has.
     */
    public void constructView() {
        uiBuilder.buildHeader();
        uiBuilder.buildContent();
        uiBuilder.buildNavigation();
    }

    /**
     * Sets the {@link UIBuilder} which determines how the user interface is
     * built.
     * 
     * @param uiBuilder
     *            TODO document
     */
    public void setUiBuilder(UIBuilder uiBuilder) {
        this.uiBuilder = uiBuilder;
    }

    /**
     * This method creates a new {@link SiteFrame} object, where the header,
     * content and navigation are set.
     */
    public void createNewView() {
        siteFrame = new SiteFrame();
    }

    /**
     * This method returns the {@link SiteFrame} object.
     * 
     * @return templateView TODO document
     */
    public SiteFrame getTemplateView() {
        return siteFrame;
    }

    /**
     * Sets the given {@link UIBuilder} and constructs the belonging user
     * interface
     * 
     * @param uiBuilder
     *            TODO document
     */
    public void switchView(UIBuilder uiBuilder) {
        setUiBuilder(uiBuilder);
        constructView();
    }

    public void setHelpDialog(HelpDialogComponent dialog) {
        helpDialog = dialog;
    }

    public HelpDialogComponent getHelpDialog() {
        return helpDialog;
    }
}
