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

/**
 * Represents the welcome page according to a specific tenant
 * which is stored in the session.
 *
 * @author Geoffrey-Alexeij Heinze
 */
// Aleks, GH: what if no tenant is stored, as the user isn't logged in yet?
// Also, do you mean "welcome page for a specific tenant"? If so, you also
// need a generic welcome page ~rr
import javax.servlet.http.HttpSession;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

public class WelcomePageComponent extends CustomComponent {

    private HttpSession session = null;

    private Long tenantId = null;
    private Long userId = null;
    private TenantFacade tenantFacade = null;

    private String tenantDescription = null;

    private VerticalLayout verticalLayout = null;
    private Label labelDesc = null;

    /**
     * Default constructor
     */
    public WelcomePageComponent() {
        init();
    }

    /**
     * This method initializes the components of the help component
     */
    private void init() {
        verticalLayout = new VerticalLayout();
        this.setCompositionRoot(verticalLayout);

        session = Main.getCurrent().getSession();
        userId = (Long) session.getAttribute("userId");
        tenantFacade = new TenantFacade(new UserRole(userId));
        tenantId = (Long) Main.getCurrent().getSession().getAttribute(
                "tenantId");
        try {
            if (tenantId != null) {
                tenantDescription = "";
                if (tenantFacade.getTenant(tenantId).getDescription() != null) {
                    tenantDescription = tenantFacade.getTenant(tenantId)
                            .getDescription();
                }
                labelDesc = new Label(tenantDescription, Label.CONTENT_XHTML);
            } else {
                labelDesc = new Label(
                        // Aleks, GH: please specify some error/warning
                        // message!!! ~rr
                        "<h2>Welcome</h2><br/>Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
                        Label.CONTENT_XHTML);
            }

            verticalLayout.addComponent(labelDesc);
        } catch (TransactionException exception) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(exception));
        }
    }
}
