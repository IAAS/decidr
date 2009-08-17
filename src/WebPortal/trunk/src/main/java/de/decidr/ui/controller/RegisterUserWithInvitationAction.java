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

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.view.InvitationDialogComponent;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * This action creates a new user and performs the given invitation.
 *
 * @author Geoffrey-Alexeij Heinze
 */
public class RegisterUserWithInvitationAction implements ClickListener  {

    private UserFacade userFacade = new UserFacade(new UserRole());
    
    private Form settingsForm = null;
    private Long invitationId = null;
    private Long userId = null;
    
    public RegisterUserWithInvitationAction(Form form, Long invId){
    	settingsForm = form;
    	invitationId = invId;
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
        	String invDescription = "invitation";
			// GH:       	Item invitationItem = userFacade.getInvitation(invitationId);
			//        	 invDescription = "Please confirm this invitation from " + 
			//        							invitationItem.getItemProperty("senderFirstName").getValue().toString() +
			//        							" " +
			//        							invitationItem.getItemProperty("senderLastName").getValue().toString();
			Main.getCurrent().getMainWindow().addWindow(new InvitationDialogComponent(invDescription,invitationId, userId));
			//userFacade.confirmInvitation(invitationId);
        }
    }
}