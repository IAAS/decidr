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

package de.decidr.ui.view;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;

/**
 * This component represents the default welcome page of the DecidR application
 * 
 * @author AT
 */
public class DefaultWelcomeComponent extends CustomComponent {

	private VerticalLayout verticalLayout = null;
	private Label labelDesc = null;

	private TenantFacade tenantFacade = new TenantFacade(new UserRole());

	public DefaultWelcomeComponent() {
		init();
	}

	private void init() {
		verticalLayout = new VerticalLayout();

		try {
			String description = tenantFacade.getTenantSettings(
					DecidrGlobals.DEFAULT_TENANT_ID).getItemProperty(
					"description").getValue().toString();

			labelDesc = new Label("<h2>Welcome</h2><br/>" + description,
					Label.CONTENT_XHTML);

			this.setCompositionRoot(verticalLayout);

			verticalLayout.addComponent(labelDesc);
		} catch (TransactionException e) {
			Main.getCurrent().getMainWindow().addWindow(
					new TransactionErrorDialogComponent(e));
		}
	}

}
