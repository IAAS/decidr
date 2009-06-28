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
 * This action changes the status of a user, either available or unavailable
 *
 * @author Geoffrey-Alexeij Heinze
 */

import java.util.Date;

import javax.servlet.http.HttpSession;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.gwt.server.WebApplicationContext;

import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.model.permissions.UserRole;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.ProfileSettingsComponent;
import de.decidr.ui.view.TransactionErrorDialogComponent;

public class ChangeStatusAction implements ValueChangeListener {
    
    private HttpSession session = Main.getCurrent().getSession();
    
    private Long userId = (Long)session.getAttribute("userId");
    private UserFacade userFacade = new UserFacade(new UserRole(userId));
	
    private ProfileSettingsComponent content = null;
    
    /* (non-Javadoc)
     * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
     */
    @Override
    public void valueChange(ValueChangeEvent event) {
    	content = (ProfileSettingsComponent) UIDirector.getInstance().getTemplateView().getContent();
                    
        if(content.getStatus().booleanValue()){
            try {
                userFacade.setUnavailableSince(userId, new Date());
            } catch (TransactionException e) {
                Main.getCurrent().getMainWindow().addWindow(new TransactionErrorDialogComponent());
            }
        } else {
            try {
                userFacade.setUnavailableSince(userId, null);
            } catch (TransactionException e) {
                Main.getCurrent().getMainWindow().addWindow(new TransactionErrorDialogComponent());
            }
        }
        
        
    }
}
