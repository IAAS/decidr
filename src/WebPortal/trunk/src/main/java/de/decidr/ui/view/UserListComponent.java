package de.decidr.ui.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class UserListComponent extends CustomComponent {
    
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -9044763020637727581L;

    private static UserListComponent userListComponent = null;
    
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
    private UserListComponent(){
        init();
    }

    /**
     * This method initializes the components for the user list component.
     *
     */
    private void init(){
        verticalLayout = new VerticalLayout();
        buttonHorizontalLayout = new HorizontalLayout();
        
        userListLabel = new Label("<h2> User list </h2>");
        userListLabel.setContentMode(Label.CONTENT_XHTML);
        
        searchPanel = new SearchPanel();
        buttonPanel = new Panel();
        
        userListTable = new UserListTable();
        
        inviteUserButton = new Button("Invite user");
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
     * Changes the user list component view for the super admin.
     *
     */
    public void changeToSuperAdmin(){
        init();
        removeFromTenantButton = new Button("Remove from tenant");
        deactivateAccountButton = new Button("Deactivate account");
        activateAccountButton = new Button("Activate account");
        getInviteUserButton().setVisible(true);
        getButtonHorizontalLayout().addComponent(removeFromTenantButton);
        getButtonHorizontalLayout().addComponent(deactivateAccountButton);
        getButtonHorizontalLayout().addComponent(activateAccountButton);
        getVerticalLayout().addComponent(getButtonPanel());
    }
    
    /**
     * TODO: add comment
     *
     */
    public void changeToTenantAdmin(){
        init();
        removeUserButton = new Button("Remove user");
        promoteToTenantAdminButton = new Button("Promote to tenant admin");
        getButtonHorizontalLayout().addComponent(removeUserButton);
        getButtonHorizontalLayout().addComponent(promoteToTenantAdminButton);
        getVerticalLayout().addComponent(getButtonPanel());
    }
    
    /**
     * TODO: add comment
     *
     * @return
     */
    public Panel getButtonPanel() {
        return buttonPanel;
    }

    /**
     * TODO: add comment
     *
     * @param buttonPanel
     */
    public void setButtonPanel(Panel buttonPanel) {
        this.buttonPanel = buttonPanel;
    }

    
    /**
     * TODO: add comment
     *
     * @return
     */
    public HorizontalLayout getButtonHorizontalLayout() {
        return buttonHorizontalLayout;
    }

    /**
     * TODO: add comment
     *
     * @param buttonHorizontalLayout
     */
    public void setButtonHorizontalLayout(HorizontalLayout buttonHorizontalLayout) {
        this.buttonHorizontalLayout = buttonHorizontalLayout;
    }

    /**
     * This method return the single instance of the user list component.
     *
     * @return userListComponent
     */
    public static UserListComponent getInstance(){
        if(userListComponent == null){
            userListComponent = new UserListComponent();
        }
        return userListComponent;
    }

    /**
     * TODO: add comment
     *
     * @return
     */
    public VerticalLayout getVerticalLayout() {
        return verticalLayout;
    }

    /**
     * TODO: add comment
     *
     * @param verticalLayout
     */
    public void setVerticalLayout(VerticalLayout verticalLayout) {
        this.verticalLayout = verticalLayout;
    }


    /**
     * TODO: add comment
     *
     * @return
     */
    public Button getInviteUserButton() {
        return inviteUserButton;
    }

    /**
     * TODO: add comment
     *
     * @param inviteUserButton
     */
    public void setInviteUserButton(Button inviteUserButton) {
        this.inviteUserButton = inviteUserButton;
    }
}
