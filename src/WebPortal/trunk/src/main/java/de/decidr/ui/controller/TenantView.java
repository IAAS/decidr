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

    private String winWebInf = "..\\..\\..\\..\\..\\webapp\\VAADIN\\themes\\";

    private String unixWebInf = "../../../../../webapp/VAADIN/themes/";

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
        try {
            tenantId = tenantFacade.getTenantId(tenantName);
            css = tenantFacade.getCurrentColorScheme(tenantId);
            logo = tenantFacade.getLogo(tenantId);
        } catch (TransactionException exception) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent());
        }

        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            cssFile = new File(winWebInf + tenantName + "\\styles.css");
            logoFile = new File(winWebInf + tenantName + "\\img\\logo.png");
        } else {
            cssFile = new File(unixWebInf + tenantName + "/styles.css");
            logoFile = new File(unixWebInf + tenantName + "/img/logo.png");
        }

        if (cssFile.exists() || logoFile.exists()) {
            cssFile.delete();
            logoFile.delete();
        } else {
            try {
                OutputStream cssOut = new FileOutputStream(cssFile);
                OutputStream logoOut = new FileOutputStream(logoFile);
                byte cssbuf[] = new byte[102400];
                byte logobuf[] = new byte[10485760];
                int csslen;
                int logolen;
                while ((csslen = css.read(cssbuf)) > 0)
                    cssOut.write(cssbuf, 0, csslen);
                cssOut.close();
                css.close();
                while ((logolen = logo.read(logobuf)) > 0)
                    logoOut.write(logobuf, 0, logolen);
                logoOut.close();
                logo.close();
            } catch (IOException exception) {
                Main.getCurrent().getMainWindow().addWindow(
                        new TransactionErrorDialogComponent());
            }
        }
    }

}
