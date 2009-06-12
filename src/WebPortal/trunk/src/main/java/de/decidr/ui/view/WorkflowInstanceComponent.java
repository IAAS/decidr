
package de.decidr.ui.view;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.data.CompletedInstancesContainer;

public class WorkflowInstanceComponent extends CustomComponent {

    /**
     * 
     */
    private static final long serialVersionUID = -8769293137331802152L;
    
    private static WorkflowInstanceComponent workflowInstanceComponent = null;
    
    private CompletedInstancesContainer completedInstanceContainer = null;
    
    private VerticalLayout verticalLayout = null;
    
    private SearchPanel searchPanel = null;
    
    private Label workflowInstanceLabel = null;
    private Label runningWorkflowInstanceLabel = null;
    private Label completedWorkflowInstanceLabel = null;
    
    private RunningInstanceTable runningInstanceTable = null;
    private CompletedInstanceTable completedInstanceTable = null;
    
    private WorkflowInstanceComponent(){
        init();
    }
    
    /**
     * 
     * TODO: add comment
     *
     */
    private void init(){
        completedInstanceContainer = new CompletedInstancesContainer();
        
        verticalLayout = new VerticalLayout();

        searchPanel = new SearchPanel();
        
        workflowInstanceLabel = new Label("<h2> Workflow instances </h2>");
        workflowInstanceLabel.setContentMode(Label.CONTENT_XHTML);
        runningWorkflowInstanceLabel = new Label("<h3> Running workflow instances </h3>");
        runningWorkflowInstanceLabel.setContentMode(Label.CONTENT_XHTML);
        completedWorkflowInstanceLabel = new Label("<h3> Completed workflow instances </h3>");
        completedWorkflowInstanceLabel.setContentMode(Label.CONTENT_XHTML);
        
        runningInstanceTable = new RunningInstanceTable();        
        
        completedInstanceTable = new CompletedInstanceTable(completedInstanceContainer, completedInstanceContainer);        
        
        setCompositionRoot(verticalLayout);
        
        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(workflowInstanceLabel);
        verticalLayout.addComponent(searchPanel);
        
        verticalLayout.addComponent(runningWorkflowInstanceLabel);
        verticalLayout.addComponent(runningInstanceTable);
        verticalLayout.addComponent(completedWorkflowInstanceLabel);
        verticalLayout.addComponent(completedInstanceTable);
    }
    
    /**
     * TODO: add comment
     *
     * @return
     */
    public static WorkflowInstanceComponent getInstance(){
        if(workflowInstanceComponent == null){
            workflowInstanceComponent = new WorkflowInstanceComponent();
        }
        return workflowInstanceComponent;
    }

}
