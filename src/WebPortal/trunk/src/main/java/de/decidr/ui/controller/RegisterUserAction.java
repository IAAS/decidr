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

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.RegisterUserComponent;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * This action creates a new user.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class RegisterUserAction implements ClickListener {

    private UserFacade userFacade = new UserFacade(new UserRole());

    private RegisterUserComponent content = null;
    
    private UserProfile userProfile = null;

    @Override
    public void buttonClick(ClickEvent event) {
        
        
        content = (RegisterUserComponent) UIDirector.getInstance()
                .getTemplateView().getContent();
        content.saveRegistrationForm();
        userProfile.setFirstName(content.getRegistrationForm().getItemProperty("userName").getValue().toString());
        
        try {
            userFacade.registerUser(content.getRegistrationForm()
                    .getItemProperty("email").getValue().toString(), content
                    .getRegistrationForm().getItemProperty("password")
                    .getValue().toString(), fillUserProfile());
        } catch (NullPointerException e) {
            Main.getCurrent().getMainWindow().addWindow(new TransactionErrorDialogComponent());
            e.printStackTrace();
        } catch (TransactionException e) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent());
        }

    }
    
    private UserProfile fillUserProfile(){
        userProfile = new UserProfile();
        userProfile.setFirstName(content.getRegistrationForm().getItemProperty("firstName").getValue().toString());
        userProfile.setLastName(content.getRegistrationForm().getItemProperty("lastName").getValue().toString());
        userProfile.setCity(content.getRegistrationForm().getItemProperty("city").getValue().toString());
        userProfile.setPostalCode(content.getRegistrationForm().getItemProperty("postalCode").getValue().toString());
        userProfile.setStreet(content.getRegistrationForm().getItemProperty("street").getValue().toString());
        userProfile.setUsername(content.getRegistrationForm().getItemProperty("userName").getValue().toString());
        return userProfile;
    }
}