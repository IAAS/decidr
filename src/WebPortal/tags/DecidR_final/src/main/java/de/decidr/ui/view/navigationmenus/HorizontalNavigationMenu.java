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

package de.decidr.ui.view.navigationmenus;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.ui.controller.authentication.LogoutAction;
import de.decidr.ui.controller.show.ShowHelpDialogAction;
import de.decidr.ui.controller.show.ShowImpressumAction;
import de.decidr.ui.controller.show.ShowTermsOfServiceAction;
import de.decidr.ui.controller.show.ShowWelcomePageAction;

/**
 * This menu represents five button links. The home button, the help button,
 * logout button, the legal information button and the terms of service button.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "TK", "JS" }, lastRevision = "2377", currentReviewState = State.PassedWithComments)
public class HorizontalNavigationMenu extends CustomComponent {

    private static final long serialVersionUID = -85255929127752304L;

    private HorizontalLayout horizontalLayout = null;

    private Button btnLogoutLink = null;
    private Button btnHomeLink = null;
    private Button btnHelpLink = null;
    private Button btnImpressumLink = null;
    private Button btnTermsOfServiceLink = null;

    /**
     * Default constructor
     * 
     */
    public HorizontalNavigationMenu() {
        init();
    }

    /**
     * Returns the logout button.
     * 
     * @return btnLogoutLink - The logout button
     */
    public Button getLogoutButton() {
        return btnLogoutLink;
    }

    /**
     * This method initializes the components of the horizontal navigation menu
     * component
     * 
     */
    private void init() {
        horizontalLayout = new HorizontalLayout();
        this.setCompositionRoot(horizontalLayout);

        btnHomeLink = new Button("Home", new ShowWelcomePageAction());

        btnHelpLink = new Button("Help", new ShowHelpDialogAction());

        btnImpressumLink = new Button("Impressum", new ShowImpressumAction());

        btnTermsOfServiceLink = new Button("Terms of Service",
                new ShowTermsOfServiceAction());

        btnLogoutLink = new Button("Logout", new LogoutAction());

        btnLogoutLink.setVisible(false);

        horizontalLayout.setSpacing(false);

        horizontalLayout.addComponent(btnHomeLink);
        horizontalLayout.addComponent(btnHelpLink);
        horizontalLayout.addComponent(btnTermsOfServiceLink);
        horizontalLayout.addComponent(btnImpressumLink);
        horizontalLayout.addComponent(btnLogoutLink);

    }

}
