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

package de.decidr.ui.controller.tenant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * Gets the CSS file and the logos of the current tenant from the database and
 * saves it to the VAADIN folder, where all themes are stored.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2374", currentReviewState = State.Rejected)
public class TenantView {

    private HttpSession session = Main.getCurrent().getSession();

    private Role role = (Role) session.getAttribute("role");
    private TenantFacade tenantFacade = new TenantFacade(role);

    private String tenantName = null;
    private Long tenantId = (Long) Main.getCurrent().getSession().getAttribute(
            "tenantId");

    private InputStream css = null;
    private InputStream logo = null;

    private File cssFile = null;
    private File logoFile = null;

    /**
     * This method gets the CSS and the logo and stores it in variables. If CSS
     * or logo files already exist, they will be overwritten with the data from
     * the database.
     */
    public void synchronize() {

        try {
            tenantName = tenantFacade.getTenant(tenantId).getName();

            cssFile = new File(Main.getCurrent().getContext()
                    .getBaseDirectory().getPath()
                    + File.separator
                    + "VAADIN"
                    + File.separator
                    + "themes"
                    + File.separator
                    + tenantName
                    + File.separator
                    + "styles.css");
            logoFile = new File(Main.getCurrent().getContext()
                    .getBaseDirectory().getPath()
                    + File.separator
                    + "VAADIN"
                    + File.separator
                    + "themes"
                    + File.separator
                    + tenantName
                    + File.separator
                    + "img"
                    + File.separator + "logo.png");

            css = tenantFacade.getCurrentColorScheme(tenantId);
            if (css == null) {
                css = tenantFacade
                        .getCurrentColorScheme(DecidrGlobals.DEFAULT_TENANT_ID);
            }

            if (css == null) {
                throw new RuntimeException(
                        "No style information found in the database.");
            }

            logo = tenantFacade.getLogo(tenantId);
            if (logo == null) {
                logo = tenantFacade.getLogo(DecidrGlobals.DEFAULT_TENANT_ID);
            }

            if (logo == null) {
                throw new RuntimeException(
                        "No logo file found in the database.");
            }

            if (cssFile.exists()) {
                cssFile.delete();
            }
            if (!cssFile.getParentFile().exists()) {
                if (!cssFile.getParentFile().mkdirs()) {
                    throw new IOException("Cannot create your tenants' layout directories.");
                }
            }
            if (logoFile.exists()) {
                logoFile.delete();
            }
            if (!logoFile.getParentFile().exists()) {
                if (!logoFile.getParentFile().mkdirs()) {
                    throw new IOException("Cannot create your tenants' layout directories.");
                }
            }

            OutputStream cssOut = null;
            OutputStream logoOut = null;
            try {
                cssOut = new FileOutputStream(cssFile);
                logoOut = new FileOutputStream(logoFile);

                IOUtils.copy(css, cssOut);
                IOUtils.copy(logo, logoOut);

            } finally {
                if (cssOut != null) {
                    cssOut.close();
                }
                css.close();
                if (logoOut != null) {
                    logoOut.close();
                }
                logo.close();
            }
        } catch (IOException exception) {
            Main.getCurrent().getMainWindow().addWindow(
                    new InformationDialogComponent(
                            "An error occured while getting your tenant specific settings.<br/>"
                            +"If this error occurs repeatedly please inform the systems' administrator.<br/><br/>"
                            +"Error Description:<br/>" + exception.getMessage(),
                            "Synchronization Error"));
        } catch (TransactionException e) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(e));
        }
    }
}
