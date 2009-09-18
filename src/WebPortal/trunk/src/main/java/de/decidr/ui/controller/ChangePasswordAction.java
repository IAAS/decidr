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

import javax.servlet.http.HttpSession;

import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.view.ChangePasswordComponent;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * This actions changes password of a user.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class ChangePasswordAction implements ClickListener {

        private HttpSession session = Main.getCurrent().getSession();

	private Long userId = (Long) session.getAttribute("userId");
	private UserFacade userFacade = new UserFacade(new UserRole(userId));

	private Item passwords = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
	 * ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		passwords = ((ChangePasswordComponent) event.getButton().getWindow())
				.getPasswords();

		
		String passwd = passwords.getItemProperty("newPassword").getValue()
				.toString();
		if (!passwd.equals(passwords.getItemProperty("newPasswordConfirm")
				.getValue().toString())) {
			Main.getCurrent().getMainWindow().showNotification(
					"new passwords don't match!");
		} else {
			try {
				userFacade.setPassword(userId, passwords.getItemProperty(
						"oldPassword").getValue().toString(), passwd);
			} catch (TransactionException e) {
			    Main.getCurrent().getMainWindow().addWindow(new TransactionErrorDialogComponent());
			}
			Main.getCurrent().getMainWindow().showNotification(
					"new password: " + passwd);
			Main.getCurrent().getMainWindow().removeWindow(
					event.getButton().getWindow());
		}
	}
}
