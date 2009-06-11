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

/**
 * TODO: add comment
 *
 * @author GH
 */

import javax.servlet.http.HttpSession;

import com.vaadin.data.Item;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.ui.view.EditTenantComponent;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.ProfileSettingsComponent;
import de.decidr.model.facades.UserFacade;
import de.decidr.model.permissions.UserRole;



public class SaveProfileAction implements ClickListener  {

    //TODO: remove // below, code is disabled for testing, since the model causes errors
    
    //private ApplicationContext ctx = Main.getCurrent().getContext();
    //private WebApplicationContext webCtx = (WebApplicationContext)ctx;
    //private HttpSession session = webCtx.getHttpSession();
    
    //private Long userId = (Long)session.getAttribute("userId");
   // private UserFacade userFacade = new UserFacade(new UserRole(userId));
    
    @Override
    public void buttonClick(ClickEvent event) {
        //userFacade.setProfile(userId, ProfileSettingsComponent.getInstance().getSettingsItem());
        Main.getCurrent().getMainWindow().showNotification("Profile Settings Saved");
    }
}