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
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.ui.controller.AddMemberToTenantAction;
import de.decidr.ui.controller.show.ShowEditUserAction;
import de.decidr.ui.controller.show.ShowInviteUserToTenantAction;
import de.decidr.ui.controller.user.AccountActivationAction;
import de.decidr.ui.controller.user.RemoveUserFromTenantAction;
import de.decidr.ui.data.UserListContainer;
import de.decidr.ui.view.tables.UserListTable;

/**
 * The users are managed in a table and can be invited, removed, promoted to a
 * tenant, removed from a tenant, deactivated and activated.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "TK", "JS" }, lastRevision = "2377", currentReviewState = State.Passed)
public class UserListComponent extends CustomComponent {

    private static final long serialVersionUID = -9044763020637727581L;

    private UserListContainer userListContainer = null;

    private VerticalLayout verticalLayout = null;
    private HorizontalLayout buttonHorizontalLayout = null;

    private Label userListLabel = null;

    private SearchPanel searchPanel = null;
    private Panel buttonPanel = null;

    private UserListTable userListTable = null;

    private Button inviteUserButton = null;
    private Button removeFromTenantButton = null;
    private Button deactivateAccountButton = null;
    private Button activateAccountButton = null;
    private Button editUserButton = null;
    private Button addMemberToTenantButton = null;

    /**
     * Default constructor.
     * 
     */
    public UserListComponent() {
        init();
    }

    /**
     * Changes the user list component view for the super administrator.
     * 
     */
    public void changeToSuperAdmin() {
        init();
        removeFromTenantButton = new Button("Remove from Tenant",
                new RemoveUserFromTenantAction(userListTable));
        deactivateAccountButton = new Button("Deactivate Account",
                new AccountActivationAction(userListTable));
        activateAccountButton = new Button("Activate Account",
                new AccountActivationAction(userListTable));
        editUserButton = new Button("Edit user", new ShowEditUserAction());
        addMemberToTenantButton = new Button("Add user to tenant",
                new AddMemberToTenantAction(userListTable));
        getInviteUserButton().setVisible(true);
        getButtonHorizontalLayout().addComponent(removeFromTenantButton);
        getButtonHorizontalLayout().addComponent(deactivateAccountButton);
        getButtonHorizontalLayout().addComponent(activateAccountButton);
        getButtonHorizontalLayout().addComponent(editUserButton);
        getButtonHorizontalLayout().addComponent(addMemberToTenantButton);
        getVerticalLayout().addComponent(getButtonPanel());
    }

    /**
     * Changes the user list component view for the tenant.
     * 
     */
    public void changeToTenantAdmin() {
        init();
        removeFromTenantButton = new Button("Remove from Tenant",
                new RemoveUserFromTenantAction(userListTable));
        addMemberToTenantButton = new Button("Add user to tenant",
                new AddMemberToTenantAction(userListTable));
        getButtonHorizontalLayout().addComponent(removeFromTenantButton);
        getButtonHorizontalLayout().addComponent(addMemberToTenantButton);
        getVerticalLayout().addComponent(getButtonPanel());
    }

    /**
     * Returns the horizontal layout from the button panel.
     * 
     * @return buttonHorizontalLayout
     */
    public HorizontalLayout getButtonHorizontalLayout() {
        return buttonHorizontalLayout;
    }

    /**
     * Returns the button panel.
     * 
     * @return buttonPanel
     */
    public Panel getButtonPanel() {
        return buttonPanel;
    }

    /**
     * Returns the button for inviting user.
     * 
     * @return inviteUserButton
     */
    public Button getInviteUserButton() {
        return inviteUserButton;
    }

    /**
     * Returns the vertical layout.
     * 
     * @return verticalLayout
     */
    public VerticalLayout getVerticalLayout() {
        return verticalLayout;
    }

    /**
     * This method initializes the components for the user list component.
     * 
     */
    private void init() {
        userListContainer = new UserListContainer();

        verticalLayout = new VerticalLayout();
        buttonHorizontalLayout = new HorizontalLayout();

        userListLabel = new Label("<h2> User list </h2>");
        userListLabel.setContentMode(Label.CONTENT_XHTML);

        buttonPanel = new Panel();

        userListTable = new UserListTable(userListContainer);

        searchPanel = new SearchPanel(userListTable);

        inviteUserButton = new Button("Invite user",
                new ShowInviteUserToTenantAction());
        inviteUserButton.setVisible(false);

        setCompositionRoot(verticalLayout);

        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(userListLabel);
        verticalLayout.addComponent(searchPanel);
        verticalLayout.addComponent(inviteUserButton);
        verticalLayout.addComponent(userListTable);
        buttonPanel.addComponent(buttonHorizontalLayout);
        buttonHorizontalLayout.setSpacing(true);
    }

    /**
     * Gets the userlist table
     * 
     * @return the userListTable
     */
    public UserListTable getUserListTable() {
        return userListTable;
    }

}
