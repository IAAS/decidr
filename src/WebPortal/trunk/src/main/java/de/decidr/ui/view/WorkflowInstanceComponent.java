package de.decidr.ui.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class WorkflowInstanceComponent extends CustomComponent {

    /**
     * TODO: add comment
     */
    private static final long serialVersionUID = -8769293137331802152L;
    
    private static WorkflowInstanceComponent workflowInstanceComponent = null;
    
    private VerticalLayout verticalLayout = null;
    
    private SearchPanel searchPanel = null;
    
    private Label workflowInstanceLabel = null;
    private Label runningWorkflowInstanceLabel = null;
    private Label completedWorkflowInstanceLabel = null;
    
    private Table runningInstanceTable = null;
    private Table completedInstanceTable = null;
    
    private WorkflowInstanceComponent(){
        init();
    }
    
    private void init(){
        verticalLayout = new VerticalLayout();

        searchPanel = new SearchPanel();
        
        workflowInstanceLabel = new Label("<h2> Workflow instances </h2>");
        workflowInstanceLabel.setContentMode(Label.CONTENT_XHTML);
        runningWorkflowInstanceLabel = new Label("<h3> Running workflow instances </h3>");
        runningWorkflowInstanceLabel.setContentMode(Label.CONTENT_XHTML);
        completedWorkflowInstanceLabel = new Label("<h3> Completed workflow instances </h3>");
        completedWorkflowInstanceLabel.setContentMode(Label.CONTENT_XHTML);
        
        runningInstanceTable = new Table();
        runningInstanceTable.setSizeFull();
        runningInstanceTable.addContainerProperty("Name", String.class, null);
        runningInstanceTable.addContainerProperty("Model", String.class, null);
        runningInstanceTable.addContainerProperty("Start date", String.class, null);
        runningInstanceTable.addContainerProperty("Status", String.class, null);
        runningInstanceTable.addContainerProperty("Terminate", Button.class, null);
        
        completedInstanceTable = new Table();
        completedInstanceTable.setSizeFull();
        completedInstanceTable.addContainerProperty("Name", String.class, null);
        completedInstanceTable.addContainerProperty("Model", String.class, null);
        completedInstanceTable.addContainerProperty("Start date", String.class, null);
        completedInstanceTable.addContainerProperty("Completion date", String.class, null);
        
        setCompositionRoot(verticalLayout);
        
        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(workflowInstanceLabel);
        verticalLayout.addComponent(searchPanel);
        
        verticalLayout.addComponent(runningWorkflowInstanceLabel);
        verticalLayout.addComponent(runningInstanceTable);
        verticalLayout.addComponent(completedWorkflowInstanceLabel);
        verticalLayout.addComponent(completedInstanceTable);
    }
    
    public static WorkflowInstanceComponent getInstance(){
        if(workflowInstanceComponent == null){
            workflowInstanceComponent = new WorkflowInstanceComponent();
        }
        return workflowInstanceComponent;
    }

}
