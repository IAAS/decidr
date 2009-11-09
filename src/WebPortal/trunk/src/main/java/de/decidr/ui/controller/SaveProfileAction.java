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
 * This action saves changes of the user profile
 *
 * @author Geoffrey-Alexeij Heinze
 */

import javax.servlet.http.HttpSession;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.commands.user.GetUserWithProfileCommand;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.ProfileSettingsComponent;
import de.decidr.ui.view.TransactionErrorDialogComponent;

public class SaveProfileAction implements ClickListener {

    private HttpSession session = Main.getCurrent().getSession();

    private Long userId = (Long) session.getAttribute("userId");
    private UserFacade userFacade = new UserFacade(new UserRole(userId));

    private ProfileSettingsComponent content = null;

    private UserProfile userProfile = null;

    /*
     * (non-Javadoc)
     * 
     * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        content = (ProfileSettingsComponent) UIDirector.getInstance()
                .getTemplateView().getContent();
        content.saveSettingsItem();
        try {
        	UserProfile uP = fillUserProfile();
            userFacade.setProfile(userId, uP);
        } catch (EntityNotFoundException e) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(e));
            e.printStackTrace();
        } catch (NullPointerException e) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(e));
            e.printStackTrace();
        } catch (TransactionException e) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(e));
        }

    }

    private UserProfile fillUserProfile() {
    	UserRole role = new UserRole(userId);
    	GetUserWithProfileCommand cmd = new GetUserWithProfileCommand(role, userId);
    	try {
			HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
			userProfile = cmd.getResult().getUserProfile();
	        userProfile.setFirstName(content.getSettingsItem().getItemProperty(
	                "firstName").getValue().toString());
	        userProfile.setLastName(content.getSettingsItem().getItemProperty(
	                "lastName").getValue().toString());
	        userProfile.setCity(content.getSettingsItem().getItemProperty("city")
	                .getValue().toString());
	        userProfile.setPostalCode(content.getSettingsItem().getItemProperty(
	                "postalCode").getValue().toString());
	        userProfile.setStreet(content.getSettingsItem().getItemProperty(
	                "street").getValue().toString());
	        return userProfile;
		} catch (TransactionException e) {
			Main.getCurrent().getMainWindow().addWindow(new TransactionErrorDialogComponent(e));
			return null;
		}
        
    }
}
