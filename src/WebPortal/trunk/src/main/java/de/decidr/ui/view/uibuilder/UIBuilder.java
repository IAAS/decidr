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

package de.decidr.ui.view.uibuilder;

import de.decidr.ui.controller.UIDirector;
import de.decidr.ui.view.SiteFrame;

/**
 * The UIBuilder is a component of the builder pattern. This abstract class
 * offers three methods for building the header, content and the navigation bar
 * for the DecidR site. The concrete builder classes extend this class and build
 * their specific header, content and navigation.
 * 
 * @author AT
 */
public abstract class UIBuilder {

    protected SiteFrame siteFrame = UIDirector.getInstance().getTemplateView();
    
    

    /**
     * This method builds the content of the DecidR site.
     * 
     */
    public abstract void buildContent();

    /**
     * This method builds the header of the DecidR site.
     * 
     */
    public abstract void buildHeader();

    /**
     * This method builds the navigation of the DecidR site.
     * 
     */
    public abstract void buildNavigation();

    

}
