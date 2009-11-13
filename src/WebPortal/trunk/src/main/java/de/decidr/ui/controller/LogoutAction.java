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

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.DecidrGlobals;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.navigationmenus.HorizontalNavigationMenu;

/**
 * This action logs a user out, invalidates the session, makes the logout button
 * invisible and returns to the main page of the DecidR application.
 * 
 * @author AT
 */
public class LogoutAction implements ClickListener {

    private UIDirector uiDirector = UIDirector.getInstance();

    /*
     * (non-Javadoc)
     * 
     * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        Main.getCurrent().getSession().invalidate();
        ((HorizontalNavigationMenu) uiDirector.getTemplateView()
                .getHNavigation()).getLogoutButton().setVisible(false);
        Main.getCurrent().getMainWindow().open(
                new ExternalResource("http://"
                        + DecidrGlobals.getSettings().getDomain()));
    }

}
