package de.decidr.ui.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class ChangeTenantComponent extends CustomComponent {
    
    /**
     * TODO: add comment
     */
    private static final long serialVersionUID = 5599429204495615788L;

    private static ChangeTenantComponent changeTenantComponent = null;
    
    private VerticalLayout verticalLayout = null;
    
    private Label changeTenantLabel = null;
    private Label waitingForApprovalLabel = null;
    
    private Table tenantTable = null;
    private Table approvalTable = null;
    
    private ChangeTenantComponent(){
        init();
    }
    
    private void init(){
        verticalLayout = new VerticalLayout();
        
        changeTenantLabel = new Label("<h2> Change Tenant </h2>");
        changeTenantLabel.setContentMode(Label.CONTENT_XHTML);
        waitingForApprovalLabel = new Label("<h2> Waiting for approval </h2>");
        waitingForApprovalLabel.setContentMode(Label.CONTENT_XHTML);
        
        tenantTable = new Table();
        tenantTable.addContainerProperty("Tenant", String.class, null);
        tenantTable.addContainerProperty("Status", Button.class, null);
        approvalTable = new Table();
        approvalTable.addContainerProperty("Tenant", String.class, null);
        
        setCompositionRoot(verticalLayout);
        
        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(changeTenantLabel);
        verticalLayout.addComponent(tenantTable);
        verticalLayout.addComponent(waitingForApprovalLabel);
        verticalLayout.addComponent(approvalTable);
    }
    
    public static ChangeTenantComponent getInstance(){
        if(changeTenantComponent == null){
            changeTenantComponent = new ChangeTenantComponent();
        }
        return changeTenantComponent;
    }

}
