package de.decidr.ui.view;


import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.data.WorkItemContainer;

public class WorkItemComponent extends CustomComponent {
    
    /**
     * TODO: add comment
     */
    private static final long serialVersionUID = -2110748321898265548L;
    
    private WorkItemContainer workItemContainer = null;
    
    private VerticalLayout verticalLayout = null;
    
    private SearchPanel searchPanel = null;
    private Panel tablePanel = null;
    
    private Label headerLabel = null;
    private Label showWorkItemLabel = null;
    
    private NativeSelect tenantNativeSelect = null;
    
    private static final String[] tenants = new String[]{"All tenants", "Current tenants"};
    
    private WorkItemTable workItemTable = null;
    
    /**
     * Default constructor
     *
     */
    public WorkItemComponent(){
        init();
    }
    
    /**
     * This method initializes the components of the work item component
     *
     */
    private void init(){
        workItemContainer = new WorkItemContainer();
        
        verticalLayout = new VerticalLayout();

        searchPanel = new SearchPanel();
        tablePanel = new Panel();
        
        headerLabel = new Label(
                "<h2>My work items </h2>");
        headerLabel.setContentMode(Label.CONTENT_XHTML);
        showWorkItemLabel = new Label("Show work items from: ");
        
        tenantNativeSelect = new NativeSelect();
        for(int i = 0; i < tenants.length; i++){
            tenantNativeSelect.addItem(tenants[i]);
        }
        
        workItemTable = new WorkItemTable(workItemContainer, workItemContainer);
        
        setCompositionRoot(verticalLayout);
        
        verticalLayout.addComponent(headerLabel);        
        verticalLayout.addComponent(searchPanel);
        verticalLayout.setSpacing(true);
        
        searchPanel.getSearchHorizontalLayout().addComponent(showWorkItemLabel);
        searchPanel.getSearchHorizontalLayout().addComponent(tenantNativeSelect);
        
        verticalLayout.addComponent(tablePanel);
        tablePanel.addComponent(workItemTable);
        
    }
    

}
