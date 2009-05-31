package de.decidr.ui.view;

import java.util.Observable;
import java.util.Observer;

import com.vaadin.ui.Table;

public class CompletedInstanceTable extends Table implements Observer{

    /**
     * TODO: add comment
     */
    private static final long serialVersionUID = -4341477724807479177L;

    public CompletedInstanceTable(){
        init();
    }
    
    private void init(){
        setSizeFull();
        addContainerProperty("Name", String.class, null);
        addContainerProperty("Model", String.class, null);
        addContainerProperty("Start date", String.class, null);
        addContainerProperty("Completion date", String.class, null);
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub
        
    }

}
