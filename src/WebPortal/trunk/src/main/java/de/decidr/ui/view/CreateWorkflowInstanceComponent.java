package de.decidr.ui.view;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class CreateWorkflowInstanceComponent extends CustomComponent {
    
    /**
     * TODO: add comment
     */
    private static final long serialVersionUID = -2283442464298218331L;
    private static CreateWorkflowInstanceComponent createWorkflowInstanceComponent = null;
    
    private VerticalLayout verticalLayout = null;
    
    private SearchPanel searchPanel = null;
    
    private Label createWorkflowInstanceLabel = null;
    
    private Table instanceTable = null;
    
    
    private CreateWorkflowInstanceComponent(){
        init();
    }
    
    private void init(){
        verticalLayout = new VerticalLayout();
        
        searchPanel = new SearchPanel();
        
        createWorkflowInstanceLabel = new Label("<h2> Create Workflow Instance </h2>");
        createWorkflowInstanceLabel.setContentMode(Label.CONTENT_XHTML);
    
        instanceTable = new Table();
        instanceTable.setSizeFull();
        instanceTable.addContainerProperty("Name", String.class, null);
        instanceTable.addContainerProperty("Create", Button.class, null);
        
        setCompositionRoot(verticalLayout);
        
        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(createWorkflowInstanceLabel);
        verticalLayout.addComponent(searchPanel);
        
        verticalLayout.addComponent(instanceTable);
        verticalLayout.setComponentAlignment(instanceTable, Alignment.MIDDLE_CENTER);
        
        
    }
    
    public static CreateWorkflowInstanceComponent getInstance(){
        if(createWorkflowInstanceComponent == null){
            createWorkflowInstanceComponent = new CreateWorkflowInstanceComponent();
        }
        return createWorkflowInstanceComponent;
    }

}
