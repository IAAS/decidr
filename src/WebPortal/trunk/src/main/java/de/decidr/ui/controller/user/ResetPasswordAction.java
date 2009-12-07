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

package de.decidr.ui.controller.user;

/**
 * This action requests a password reset
 *
 * @author Geoffrey-Alexeij Heinze
 */

import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.controller.HideDialogWindowAction;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.ResetPasswordWindow;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

public class ResetPasswordAction implements ClickListener {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;

	// private Long userId = (Long) session.getAttribute("userId");
	private UserFacade userFacade = new UserFacade(new UserRole());

	private Item request = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
	 * ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		request = ((ResetPasswordWindow) event.getButton().getWindow())
				.getRequestForm();

		try {
			userFacade.requestPasswordReset(request.getItemProperty("email")
					.getValue().toString());
			Main.getCurrent().getMainWindow().addWindow(
					new InformationDialogComponent("New password sent to: "
							+ request.getItemProperty("email").getValue()
									.toString(), "Information"));
			new HideDialogWindowAction();
		} catch (TransactionException e) {
			Main.getCurrent().getMainWindow().addWindow(
					new TransactionErrorDialogComponent(e));
		}
	}
}