package de.decidr.ui.view;

import java.util.Observable;
import java.util.Observer;

import com.vaadin.data.Container;
import com.vaadin.ui.Table;
import de.decidr.ui.data.WorkItemContainer;

public class WorkItemTable extends Table implements Observer{

    /**
     * TODO: add comment
     */
    private static final long serialVersionUID = 24861377458898625L;
    
   private Container workItemContainer = null;
   private Observable observable = null;

    public WorkItemTable(Observable observable, Container container){
        this.workItemContainer = container;
        this.observable = observable;
        observable.addObserver(this);
        init(observable, container);
    }
    
    private void init(Observable observable, Container container){
       
        //workItemContainer = new WorkItemContainer();
        setSizeFull();
        setContainerDataSource(container);
       
        workItemContainer.addContainerProperty("Name", String.class, null);
        workItemContainer.addContainerProperty("Tenant", String.class, null);
        workItemContainer.addContainerProperty("Data received", String.class, null);
        workItemContainer.addContainerProperty("Status", String.class, null);
        //setVisibleColumns(workItemContainer.getContainerPropertyIds().toArray());
        
        
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof WorkItemContainer)
        refreshCurrentPage();
    }

}
