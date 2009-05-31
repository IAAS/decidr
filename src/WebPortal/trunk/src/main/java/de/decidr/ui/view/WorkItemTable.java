package de.decidr.ui.view;

import java.util.Observable;
import java.util.Observer;

import com.vaadin.ui.Table;

public class WorkItemTable extends Table implements Observer{

    /**
     * TODO: add comment
     */
    private static final long serialVersionUID = 24861377458898625L;

    public WorkItemTable(){
        init();
    }
    
    private void init(){
        setSizeFull();
        addContainerProperty("Name", String.class, null);
        addContainerProperty("Tenant", String.class, null);
        addContainerProperty("Data received", String.class, null);
        addContainerProperty("Status", String.class, null);
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub
        
    }

}
