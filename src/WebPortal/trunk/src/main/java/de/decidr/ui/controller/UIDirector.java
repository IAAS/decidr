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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.security.MessageDigest;

import org.apache.log4j.Logger;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.Password;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.ui.view.SiteFrame;
import de.decidr.ui.view.help.HelpDialogComponent;
import de.decidr.ui.view.uibuilder.UIBuilder;

/**
 * This class is the director for building the ui. In the DecidR application
 * there are different roles. Every role has an other authority and other items
 * are displayed. The user with most less authority is the unregistered user. He
 * only can browse through the application. The next level is the registered
 * user. He is able to edit his personal settings and to log in. The worklfow
 * administrator is able to administrate a workflow instance which is started
 * within the DecidR application. The tenant administrator administrates an
 * amount of user which belongs to him. He can make his sepcifi look and feel
 * from the application with CSS. And he is able to leave a user and to invite a
 * user. The super administrator is the one with full functionality. He can do
 * evereything. He is able to delete user from the application and to manage
 * server specific adjustments.
 * 
 * @author AT
 */
public class UIDirector {

    private static Logger logger = DefaultLogger.getLogger(UIDirector.class);

    private static String hash = null;

    private UIBuilder uiBuilder = null;

    private static ThreadLocal<UIDirector> uiDirector;

    private SiteFrame siteFrame = null;

    private HelpDialogComponent helpDialog = null;

    public static synchronized UIDirector getInstance() {
        if (uiDirector == null) {
            uiDirector = new ThreadLocal<UIDirector>() {
				@Override
				protected UIDirector initialValue() {
					return new UIDirector();
				}
            };
        }
        logger.debug("Getting UIDirector singleton: " + hash);
        return uiDirector.get();
    }

    /**
     * The default constructor
     * 
     */
    private UIDirector() {
        // Aleks, GH: document: why is this empty
        // Generate hash to verify singleton object identity
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(out);
        try {
            stream.writeLong(DecidrGlobals.getTime().getTimeInMillis());
            stream.write(hashCode());
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            hash = Password.getDigestNotation(digest.digest(out.toByteArray()));
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Constructs the view which is shown to the user. Here the header, the
     * specific content and the specific vertical navigation menu is build
     * depending on which role the user has.
     * 
     */
    public void constructView() {
        uiBuilder.buildHeader();
        uiBuilder.buildContent();
        uiBuilder.buildNavigation();
    }

    /**
     * Sets the ui builder which determines how the user interface is built.
     * 
     * @param uiBuilder
     */
    public void setUiBuilder(UIBuilder uiBuilder) {
        this.uiBuilder = uiBuilder;
    }

    /**
     * This method creates a new SiteFrame object, where the header, content and
     * navigation is set.
     * 
     */
    public void createNewView() {
        siteFrame = new SiteFrame();
    }

    /**
     * This method returns the SiteFrame object.
     * 
     * @return templateView
     */
    public SiteFrame getTemplateView() {
        return siteFrame;
    }

    /**
     * Sets the given uiBuilder and constructs the belonging user interface
     * 
     * @param uiBuilder
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
