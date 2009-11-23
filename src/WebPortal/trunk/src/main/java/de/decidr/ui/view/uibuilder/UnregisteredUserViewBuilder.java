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

import de.decidr.ui.view.Header;
import de.decidr.ui.view.LoginComponent;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.WelcomePageComponent;
import de.decidr.ui.view.navigationmenus.HorizontalNavigationMenu;

/**
 * One concrete builder class which extends the UIBuilder and builds the unregistered
 * user specific header, content and navigation for the DecidR site.
 *
 * @author AT
 */
public class UnregisteredUserViewBuilder extends UIBuilder {
	
	/**
	 * Default constructor
	 * 
	 */
	public UnregisteredUserViewBuilder() {
		siteFrame = Main.getCurrent().getUIDirector().getTemplateView();
	}
	

	/* (non-Javadoc)
	 * @see de.decidr.ui.view.UIBuilder#buildContent()
	 */
	@Override
	public void buildContent() {
		siteFrame.setContent(new WelcomePageComponent());

	}

	/* (non-Javadoc)
	 * @see de.decidr.ui.view.UIBuilder#buildHeader()
	 */
	@Override
	public void buildHeader() {
		siteFrame.setHeader(new Header());
		siteFrame.setHorizontalNavigation(new HorizontalNavigationMenu());

	}

	/* (non-Javadoc)
	 * @see de.decidr.ui.view.UIBuilder#buildNavigation()
	 */
	@Override
	public void buildNavigation() {
		siteFrame.setVerticalNavigation(new LoginComponent());

	}

}
