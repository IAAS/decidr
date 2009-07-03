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

import com.vaadin.ui.Form;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.model.permissions.UserRole;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.RegisterUserComponent;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * This action creates a new user with already set tenant membership.
 *
 * @author Geoffrey-Alexeij Heinze
 */
public class RegisterUserAsTenantMemberAction implements ClickListener  {

    private UserFacade userFacade = new UserFacade(new UserRole());
    
    private Form settingsForm = null;
    private Long invitationId = null;
    private Long userId = null;
    
    public RegisterUserAsTenantMemberAction(Form form, Long invitationId){
    	settingsForm = form;
    	this.invitationId = invitationId;
    }
        
    @Override
    public void buttonClick(ClickEvent event) {
        settingsForm.commit();
        
        try {
            userId = userFacade.registerUser(settingsForm.getItemProperty("email").getValue().toString(), settingsForm.getItemProperty("password").getValue().toString(), settingsForm);
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransactionException e) {
            Main.getCurrent().getMainWindow().addWindow(new TransactionErrorDialogComponent());
        }
        
        if(userId != null){
        	try {
				userFacade.confirmInvitation(invitationId);
			} catch (TransactionException e) {
				Main.getCurrent().getMainWindow().addWindow(new TransactionErrorDialogComponent());
			}
        }
    }
}