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

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.ui.controller.show.ShowRegisterTenantAction;
import de.decidr.ui.controller.show.ShowRegisterUserAction;

/**
 * Here the user can choose if he wants to register a new user or a new tenant
 * and user.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2355", currentReviewState = State.PassedWithComments)
public class RegistrationComponent extends CustomComponent {
    private static final long serialVersionUID = 1L;
    private VerticalLayout verticalLayout = null;
    private HorizontalLayout horizontalLayout = null;

    private Label descriptionLabel = null;

    private Button registerUser = null;
    private Button registerTenant = null;

    /**
     * TODO document
     */
    public RegistrationComponent() {
        init();
    }

    /**
     * This method initializes the components of the
     * {@link RegistrationComponent}.
     */
    private void init() {
        verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setSpacing(true);
        this.setCompositionRoot(verticalLayout);

        descriptionLabel = new Label("Please select what you want to do:",
                Label.CONTENT_XHTML);

        registerTenant = new Button("Create both a new tenant and user",
                new ShowRegisterTenantAction());
        registerTenant.setStyleName(Button.STYLE_LINK);

        registerUser = new Button("Create a new user",
                new ShowRegisterUserAction());
        registerUser.setStyleName(Button.STYLE_LINK);

        horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        horizontalLayout.addComponent(registerUser);
        horizontalLayout.addComponent(registerTenant);
        // Aleks, GH: these buttons should be one above the other ~rr
        // RR nope, see spec and ui doc (aren't there bigger problems than the layout? oO)
        horizontalLayout.setComponentAlignment(registerUser, "center bottom");
        horizontalLayout.setComponentAlignment(registerTenant, "center bottom");

        verticalLayout.addComponent(descriptionLabel);
        verticalLayout.addComponent(horizontalLayout);
    }
}
