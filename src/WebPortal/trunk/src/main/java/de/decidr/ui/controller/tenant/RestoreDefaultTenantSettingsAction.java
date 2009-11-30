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

package de.decidr.ui.controller.tenant;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.ui.controller.UIDirector;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.SiteFrame;
import de.decidr.ui.view.TenantSettingsComponent;

/**
 * Restores the default settings from the the tenant settings. The css settings
 * and other settings are restored and set to a decidr global setting.
 * 
 * @author AT
 */
public class RestoreDefaultTenantSettingsAction implements ClickListener {
	
	UIDirector uiDirector = Main.getCurrent().getUIDirector();
	SiteFrame siteFrame = uiDirector.getTemplateView();

    /*
     * (non-Javadoc)
     * 
     * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        TenantSettingsComponent content = (TenantSettingsComponent)siteFrame.getContent();
        content.changeToBasic();
        content.getBackgroundSelect().setValue("aqua");
        content.getForegroundSelect().setValue("aqua");
        content.getFontSelect().setValue("Arial");
        content.getFontSizeSelect().setValue("12");
        Main.getCurrent().getMainWindow().setTheme("decidr");

    }

}
