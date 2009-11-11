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

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.vaadin.terminal.ParameterHandler;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.view.InformationDialogComponent;
import de.decidr.ui.view.Main;

/**
 * This class handles URL parameters for tenants
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class TenantParameterHandler implements ParameterHandler {

    private HttpSession session = null;
    private UserFacade userFacade = null;

    private String tenantName = null;

    String key = null;
    String value = null;

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.terminal.ParameterHandler#handleParameters(java.util.Map)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void handleParameters(Map parameters) {
        tenantName="";
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
        if (tenantName != "") {
        	//specific tenant selected
            
            if (session != null)
            {
            	session.setAttribute("tenant", tenantName);
            }

            Main.getCurrent().setTheme(tenantName);
        } else {
        	if (session != null)
            {
        		session.setAttribute("tenant",DecidrGlobals.getDefaultTenant().getName());
            }

            Main.getCurrent().setTheme("decidr");
        }

    }


}
