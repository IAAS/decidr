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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpSession;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * Gets the css file and the logos from the current tenant from the database and
 * saves it to the VAADIN folder, where all themes are stored.
 * 
 * @author AT
 */
public class TenantView {

	private HttpSession session = Main.getCurrent().getSession();

	private Long userId = (Long) session.getAttribute("userId");
	private TenantFacade tenantFacade = new TenantFacade(new UserRole(userId));

	private String tenantName = (String) session.getAttribute("tenant");
	private Long tenantId = null;

	private InputStream css = null;
	private InputStream logo = null;

	private File cssFile = null;
	private File logoFile = null;

	/**
	 * This method gets the css and the logo and stores it in variables. If css
	 * or logo files exist already they will be deleted and new css and logo
	 * files are genereated with the data from the database.
	 * 
	 */
	public void synchronize() {
		cssFile = new File("themes" + File.separator + tenantName
				+ File.separator + "styles.css");
		logoFile = new File("themes" + File.separator + tenantName
				+ File.separator + "img" + File.separator + "logo.png");
		try {
			tenantId = tenantFacade.getTenantId(tenantName);

			css = tenantFacade.getCurrentColorScheme(tenantId);
			if (css == null) {
				css = tenantFacade
						.getCurrentColorScheme(DecidrGlobals.DEFAULT_TENANT_ID);
			}
			logo = tenantFacade.getLogo(tenantId);
			if (logo == null) {
				logo = tenantFacade.getLogo(DecidrGlobals.DEFAULT_TENANT_ID);
			}

			if (cssFile.exists()) {
				cssFile.delete();
			}
			if (logoFile.exists()) {
				logoFile.delete();
			}

			OutputStream cssOut = new FileOutputStream(cssFile);
			OutputStream logoOut = new FileOutputStream(logoFile);
			byte cssbuf[] = new byte[102400];
			byte logobuf[] = new byte[10485760];
			int csslen;
			int logolen;
			while ((csslen = css.read(cssbuf)) > 0) {
				cssOut.write(cssbuf, 0, csslen);
			}
			cssOut.close();
			css.close();
			while ((logolen = logo.read(logobuf)) > 0) {
				logoOut.write(logobuf, 0, logolen);
			}
			logoOut.close();
			logo.close();
		} catch (IOException exception) {
			Main.getCurrent().getMainWindow().showNotification("IOException");
		} catch (TransactionException e) {
			Main.getCurrent().getMainWindow().addWindow(
					new TransactionErrorDialogComponent(e));
			e.printStackTrace();
		}
	}
}
