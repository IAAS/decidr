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

import de.decidr.ui.view.SiteFrame;
import de.decidr.ui.view.UIBuilder;

/**
 * This class is the director for building the ui. In the DecidR application
 * there are different roles. Every role has an other authority and other items
 * are displayed. The user with most less authority is the unregistered user. He
 * only can browse through the application. The next level is the registered user.
 * He is able to edit his personal settings and to log in. The worklfow administrator
 * is able to administrate a workflow instance which is started within the DecidR 
 * application. The tenant administrator administrates an amount of user which belongs
 * to him. He can make his sepcifi look and feel from the application with CSS. And he 
 * is able to leave a user and to invite a user. The super administrator is the one with 
 * full functionality. He can do evereything. He is able to delete user from the application
 * and to manage server specific adjustments.
 *
 * @author AT
 */
public class UIDirector {
    
    private UIBuilder uiBuilder = null;
    private static UIDirector uiDirector = null;
    
    /**
     * The default constructor
     *
     */
    private UIDirector() {
        
    }

    /**
     * Returns the template view, which is the basic of all sites
     * generated in the DecidR application.
     *
     * @return
     */
    public SiteFrame getTemplateView() {
        return uiBuilder.getView();
    }

    /**
     * Sets the ui builder which determines how the user interface is built.
     *
     * @param uiBuilder
     */
    public void setUiBuilder(UIBuilder uiBuilder) {
        this.uiBuilder = uiBuilder;
    }
    
    public void createNewView(){
        uiBuilder.createNewView();
    }
    
    /**
     * Constructs the view which is shown to the user. Here the header, the specific content
     * and the specific vertical navigation menu is build depending on which role the user has.
     *
     */
    public void constructView(){
        uiBuilder.buildHeader();
        uiBuilder.buildContent();
        uiBuilder.buildNavigation();
    }
    
    public static UIDirector getInstance(){
       if(uiDirector == null){
           uiDirector = new UIDirector();
       }
       return uiDirector;
        
    }
   
}