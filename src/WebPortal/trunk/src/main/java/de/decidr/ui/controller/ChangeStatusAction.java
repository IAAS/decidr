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

import java.util.Date;

import javax.servlet.http.HttpSession;

import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.ui.view.Main;
import de.decidr.ui.view.ProfileSettingsComponent;
import de.decidr.model.facades.UserFacade;
import de.decidr.model.permissions.UserRole;

public class ChangeStatusAction implements ValueChangeListener {
    
    //TODO: remove // below, code is disabled for testing, since the model causes errors
    
    //private ApplicationContext ctx = Main.getCurrent().getContext();
    //private WebApplicationContext webCtx = (WebApplicationContext)ctx;
    //private HttpSession session = webCtx.getHttpSession();
    
    //private Long userId = (Long)session.getAttribute("userId");
    //private UserFacade userFacade = new UserFacade(new UserRole(userId));
	private ProfileSettingsComponent content = null;
	
    @Override
    public void valueChange(ValueChangeEvent event) {
    	content = (ProfileSettingsComponent) UIDirector.getInstance().getTemplateView().getContent();
            
        //TODO: date format?
        //      How can it be set to available?
       
        //userFacade.setUnavailableSince(userId, Date date);
        if(content.getStatus().booleanValue()){
            Main.getCurrent().getMainWindow().showNotification("not available");
        } else {
            Main.getCurrent().getMainWindow().showNotification("available");
        }
        
        
    }
}
