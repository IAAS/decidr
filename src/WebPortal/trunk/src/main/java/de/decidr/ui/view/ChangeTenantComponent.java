package de.decidr.ui.view;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.data.TenantContainer;

public class ChangeTenantComponent extends CustomComponent {
    
    /**
     * TODO: add comment
     */
    private static final long serialVersionUID = 5599429204495615788L;

    private static ChangeTenantComponent changeTenantComponent = null;
    
    private TenantContainer tenantContainer = null;
    
    private VerticalLayout verticalLayout = null;
    
    private Label changeTenantLabel = null;
    private Label waitingForApprovalLabel = null;
    
    private TenantTable tenantTable = null;
    private Table approvalTable = null;
    
    private ChangeTenantComponent(){
        init();
    }
    
    private void init(){
        tenantContainer = new TenantContainer();
        
        verticalLayout = new VerticalLayout();
        
        changeTenantLabel = new Label("<h2> Change Tenant </h2>");
        changeTenantLabel.setContentMode(Label.CONTENT_XHTML);
        waitingForApprovalLabel = new Label("<h2> Waiting for approval </h2>");
        waitingForApprovalLabel.setContentMode(Label.CONTENT_XHTML);
        
        tenantTable = new TenantTable(tenantContainer, tenantContainer);
        tenantTable.setVisibleColumns(new Object[]{"Tenant", "Status"});
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
