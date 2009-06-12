package de.decidr.ui.view;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.service.ApplicationContext.TransactionListener;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Window;
import de.decidr.ui.controller.UIDirector;

/**
 * TODO: add comment
 * 
 * @author AT
 */
public class Main extends Application implements TransactionListener {

    /**
     * TODO: add comment
     */
    private static final long serialVersionUID = 2668887930201158755L;

    private static ThreadLocal<Main> currentApplication = new ThreadLocal<Main>();

    Window main = new Window();
    UIBuilder ui = new SuperAdminViewBuilder();
    UIDirector director = UIDirector.getInstance();

    @Override
    public void init() {
        setMainWindow(main);
        //setTheme("test");
        director.setUiBuilder(ui);
        director.createNewView();
        director.constructView();
        main.addComponent(director.getTemplateView());
        if (getContext() != null) {
            getContext().addTransactionListener(this);
    }       

        
    }
    
    

    /**
     * @return the current application instance
     */
    public static Main getCurrent() {
        return currentApplication.get();
    }

    /**
     * Set the current application instance
     */
    public static void setCurrent(Main application) {
        if (getCurrent() == null) {
            currentApplication.set(application);
        }
    }

    /**
     * Remove the current application instance
     */
    public static void removeCurrent() {
        currentApplication.remove();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vaadin.service.ApplicationContext.TransactionListener#transactionEnd
     * (com.vaadin.Application, java.lang.Object)
     */
    @Override
    public void transactionEnd(Application application, Object transactionData) {
        Main.removeCurrent();

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vaadin.service.ApplicationContext.TransactionListener#transactionStart
     * (com.vaadin.Application, java.lang.Object)
     */
    @Override
    public void transactionStart(Application application, Object transactionData) {
        Main.setCurrent(this);

    }

}
