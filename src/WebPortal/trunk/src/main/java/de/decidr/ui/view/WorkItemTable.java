package de.decidr.ui.view;

import java.util.Observable;
import java.util.Observer;
import com.vaadin.ui.Table;
import de.decidr.ui.data.WorkItemContainer;

public class WorkItemTable extends Table implements Observer{

    /**
     * TODO: add comment
     */
    private static final long serialVersionUID = 24861377458898625L;
    
   private WorkItemContainer workItemContainer = null;
   String aleks = "Aleks";

    public WorkItemTable(){
        init();
    }
    
    private void init(){
       
        workItemContainer = new WorkItemContainer();
        setSizeFull();
        setContainerDataSource(workItemContainer);
       
        workItemContainer.addContainerProperty("Name", String.class, null);
        workItemContainer.addContainerProperty("Tenant", String.class, null);
        workItemContainer.addContainerProperty("Data received", String.class, null);
        workItemContainer.addContainerProperty("Status", String.class, null);
        setVisibleColumns(workItemContainer.getContainerPropertyIds().toArray());
        
        
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub
        
    }

}
