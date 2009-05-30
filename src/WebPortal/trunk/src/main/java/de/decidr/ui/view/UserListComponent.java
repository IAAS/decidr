package de.decidr.ui.view;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class UserListComponent extends CustomComponent {
    
    private static UserListComponent userListComponent = null;
    
    private VerticalLayout verticalLayout = null;
    
    private Label userListLabel = null;
    
    private SearchPanel searchPanel = null;
    
    private Table userListTable = null;
    
    private UserListComponent(){
        init();
    }

    private void init(){
        verticalLayout = new VerticalLayout();
        
        userListLabel = new Label("<h2> User list </h2>");
        userListLabel.setContentMode(Label.CONTENT_XHTML);
        
        searchPanel = new SearchPanel();
        
        userListTable = new Table();
        userListTable.setSizeFull();
        userListTable.addContainerProperty("Username", String.class, null);
        userListTable.addContainerProperty("Read Name", String.class, null);
        userListTable.addContainerProperty("E-Mail address", String.class, null);
        
        setCompositionRoot(verticalLayout);
        
        verticalLayout.addComponent(userListLabel);
        verticalLayout.addComponent(searchPanel);
        verticalLayout.addComponent(userListTable);
    }
    
    public static UserListComponent getInstance(){
        if(userListComponent == null){
            userListComponent = new UserListComponent();
        }
        return userListComponent;
    }
}
