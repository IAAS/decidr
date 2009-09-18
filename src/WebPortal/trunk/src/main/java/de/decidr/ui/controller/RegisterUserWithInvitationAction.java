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
import de.decidr.model.entities.UserProfile;
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
public class RegisterUserWithInvitationAction implements ClickListener {

    private UserFacade userFacade = new UserFacade(new UserRole());

    private Form settingsForm = null;
    private Long invitationId = null;
    private Long userId = null;
    
    private UserProfile userProfile = null;

    public RegisterUserWithInvitationAction(Form form, Long invId) {
        settingsForm = form;
        invitationId = invId;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        settingsForm.commit();

        try {
            userId = userFacade.registerUser(settingsForm.getItemProperty(
                    "email").getValue().toString(), settingsForm
                    .getItemProperty("password").getValue().toString(),
                    fillUserProfile());
        } catch (NullPointerException e) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent());
            e.printStackTrace();
        } catch (TransactionException e) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent());
        }

        if (userId != null) {
            String invDescription = "invitation";
            // GH: Item invitationItem = userFacade.getInvitation(invitationId);
            // invDescription = "Please confirm this invitation from " +
            // invitationItem.getItemProperty("senderFirstName").getValue().toString()
            // +
            // " " +
            // invitationItem.getItemProperty("senderLastName").getValue().toString();
            Main.getCurrent().getMainWindow().addWindow(
                    new InvitationDialogComponent(invDescription, invitationId,
                            userId));
            // userFacade.confirmInvitation(invitationId);
        }
    }
    
    private UserProfile fillUserProfile(){
        userProfile = new UserProfile();
        userProfile.setFirstName(settingsForm.getItemProperty("firstName").getValue().toString());
        userProfile.setLastName(settingsForm.getItemProperty("lastName").getValue().toString());
        userProfile.setCity(settingsForm.getItemProperty("city").getValue().toString());
        userProfile.setPostalCode(settingsForm.getItemProperty("postalCode").getValue().toString());
        userProfile.setStreet(settingsForm.getItemProperty("street").getValue().toString());
        userProfile.setUsername(settingsForm.getItemProperty("userName").getValue().toString());
        return userProfile;
    }
}