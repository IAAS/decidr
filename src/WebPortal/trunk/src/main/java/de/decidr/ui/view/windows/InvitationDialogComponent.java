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

package de.decidr.ui.view.windows;

/**
 * If a user is invited to a workflow process the user will see an invitation
 * dialog which welcomes the user to the invited workflow.
 *
 * @author Geoffrey-Alexeij Heinze
 */

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.decidr.ui.controller.ConfirmInvitationAction;
import de.decidr.ui.controller.user.RefuseInvitationAction;

public class InvitationDialogComponent extends Window {

    /**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;
	private Long invitationId = null;
    private Long userId = null;

    private VerticalLayout verticalLayout = null;
    private HorizontalLayout horizontalLayout = null;

    private Label infoLabel = null;

    private Button submitButton = null;
    private Button cancelButton = null;

    /**
     * Default constructor. Stores the invitation id and calls the
     * initialization with the given description.
     * 
     * @param description
     * @param invId
     */
    public InvitationDialogComponent(String description, Long invId, Long uId) {
        invitationId = invId;
        userId = uId;
        init(description);
    }

    /**
     * Initializes the components for the invitation dialog with the given
     * description.
     * 
     * @param description
     */
    private void init(String description) {

        verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(true);
        verticalLayout.setSizeUndefined();

        horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        horizontalLayout.setMargin(false);
        horizontalLayout.setSizeUndefined();

        infoLabel = new Label(description, Label.CONTENT_XHTML);
        infoLabel.setWidth(350, Sizeable.UNITS_PIXELS);

        submitButton = new Button("Accept Invitation",
                new ConfirmInvitationAction(invitationId, userId));
        cancelButton = new Button("Refuse Invitation",
                new RefuseInvitationAction(invitationId, userId));

        verticalLayout.addComponent(infoLabel);
        horizontalLayout.addComponent(submitButton);
        horizontalLayout.addComponent(cancelButton);
        verticalLayout.addComponent(horizontalLayout);

        horizontalLayout.setComponentAlignment(cancelButton, "right bottom");
        horizontalLayout.setComponentAlignment(submitButton, "right bottom");

        verticalLayout.setComponentAlignment(horizontalLayout, "right bottom");

        this.setModal(true);
        this.setName("InvitationDialog");
        this.setResizable(false);
        this.setCaption("Invitation");
        this.setContent(verticalLayout);
    }
}
