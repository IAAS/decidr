package de.decidr.ui.view;

import java.util.Observable;
import java.util.Observer;

import com.vaadin.data.Container;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;

import de.decidr.ui.data.RunningInstanceContainer;

public class RunningInstanceTable extends Table implements Observer{

    /**
     * TODO: add comment
     */
    private static final long serialVersionUID = 49258596599726066L;
    
    private Observable observable = null;
    private Container runningInstanceContainer = null;

    public RunningInstanceTable(Observable observable, Container container){
        this.observable = observable;
        runningInstanceContainer = container;
        observable.addObserver(this);
        init(observable, container);
    }
    
    private void init(Observable observable, Container container){
        setSizeFull();
        setContainerDataSource(container);
        addContainerProperty("Name", String.class, null);
        addContainerProperty("Model", String.class, null);
        addContainerProperty("Start date", String.class, null);
        addContainerProperty("Status", String.class, null);
        addContainerProperty("Terminate", Button.class, null);
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof RunningInstanceContainer){
            refreshCurrentPage();
        }
        
    }
}
