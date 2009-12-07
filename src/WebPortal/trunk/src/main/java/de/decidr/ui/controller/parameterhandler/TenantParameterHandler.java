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

package de.decidr.ui.controller.parameterhandler;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.vaadin.terminal.ParameterHandler;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This class handles URL parameters for tenants
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class TenantParameterHandler implements ParameterHandler {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;

	private HttpSession session = null;

	private String tenantName = null;

	private Long tenantId = null;

	String key = null;
	String value = null;

	private TenantFacade tenantFacade = new TenantFacade(new UserRole());

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.terminal.ParameterHandler#handleParameters(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void handleParameters(Map parameters) {
		tenantName = "";
		for (Iterator<String> it = parameters.keySet().iterator(); it.hasNext();) {
			key = it.next();
			value = ((String[]) parameters.get(key))[0];
			try {
				if (key.equals("tenant")) {
					tenantName = value;
				}

			} catch (NumberFormatException e) {
				Main
						.getCurrent()
						.getMainWindow()
						.addWindow(
								new InformationDialogComponent(
										"An error occured while handling your invitation.<br/>Please try again and do not modify the provided link!",
										"Invitation Error"));
			}
		}
		session = Main.getCurrent().getSession();
		if (session.getAttribute("userId") == null) {
			if (tenantName != "") {
				// specific tenant selected

				try {
					if (session != null) {

						tenantId = tenantFacade.getTenant(tenantName).getId();
						session.setAttribute("tenantId", tenantId);

					}

					Main.getCurrent().setTheme(tenantName);
				} catch (TransactionException exception) {
					Main.getCurrent().getMainWindow().addWindow(
							new TransactionErrorDialogComponent(exception));
				}

			} else {
				Main.getCurrent().setTheme("decidr");
			}
		}

	}

}
