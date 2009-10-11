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
 * One concrete builder class which extends the UIBuilder and builds the super
 * administrator specific header, content and navigation for the DecidR site.
 * 
 * @author AT
 */
public class SuperAdminViewBuilder extends UIBuilder {

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.ui.view.UIBuilder#buildContent()
     */
    @Override
    public void buildContent() {
        getView().setContent(new LoginComponent());

    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.ui.view.UIBuilder#buildHeader()
     */
    @Override
    public void buildHeader() {
        getView().setHeader(new Header());
        getView().setHorizontalNavigation(new HorizontalNavigationMenu());
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.ui.view.UIBuilder#buildNavigation()
     */
    @Override
    public void buildNavigation() {
        getView().setVerticalNavigation(new SuperAdminNavigationMenu());

    }

}
