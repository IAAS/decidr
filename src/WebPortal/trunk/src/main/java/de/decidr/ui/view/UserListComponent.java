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

import de.decidr.ui.controller.ActivateAccountAction;
import de.decidr.ui.controller.DeactivateAccountAction;
import de.decidr.ui.controller.RemoveUserFromTenantAction;
import de.decidr.ui.controller.ShowInviteUserToTenantAction;
import de.decidr.ui.data.UserListContainer;

/**
 * The user are managed in a table and can be invited, removed, promoted to a
 * tenant, removed from a tenant, deactivated and activated.
 * 
 * @author AT
 */
public class UserListComponent extends CustomComponent {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -9044763020637727581L;

    private UserListContainer userListContainer = null;

    private VerticalLayout verticalLayout = null;
    private HorizontalLayout buttonHorizontalLayout = null;

    private Label userListLabel = null;

    private SearchPanel searchPanel = null;
    private Panel buttonPanel = null;

    private UserListTable userListTable = null;

    private Button inviteUserButton = null;
    private Button removeUserButton = null;
    private Button promoteToTenantAdminButton = null;
    private Button removeFromTenantButton = null;
    private Button deactivateAccountButton = null;
    private Button activateAccountButton = null;

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
        removeFromTenantButton = new Button("Remove from tenant",
                new RemoveUserFromTenantAction(userListTable));
        deactivateAccountButton = new Button("Deactivate account",
                new DeactivateAccountAction(userListTable));
        activateAccountButton = new Button("Activate account",
                new ActivateAccountAction(userListTable));
        getInviteUserButton().setVisible(true);
        getButtonHorizontalLayout().addComponent(removeFromTenantButton);
        getButtonHorizontalLayout().addComponent(deactivateAccountButton);
        getButtonHorizontalLayout().addComponent(activateAccountButton);
        getVerticalLayout().addComponent(getButtonPanel());
    }

    /**
     * Changes the user list component view for the tenant.
     * 
     */
    public void changeToTenantAdmin() {
        init();
        removeUserButton = new Button("Remove user",
                new RemoveUserFromTenantAction(userListTable));
        promoteToTenantAdminButton = new Button("Promote to tenant admin");
        getButtonHorizontalLayout().addComponent(removeUserButton);
        getButtonHorizontalLayout().addComponent(promoteToTenantAdminButton);
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

        userListTable = new UserListTable(userListContainer, userListContainer);

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
	 * TODO: add comment
	 *
	 * @return the userListTable
	 */
	public UserListTable getUserListTable() {
		return userListTable;
	}
    
    

}
