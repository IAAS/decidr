package de.decidr.ui.view;

import com.vaadin.Application;
import com.vaadin.ui.Window;

import de.decidr.ui.controller.UIDirector;


/**
 * TODO: add comment
 *
 * @author AT
 */
public class Main extends Application {

    /**
     * TODO: add comment
     */
    private static final long serialVersionUID = 2668887930201158755L;
    Window main = new Window();
    UIBuilder ui = new SuperAdminViewBuilder();
    UIDirector director = UIDirector.getInstance();
	
	@Override
	public void init() {
		setMainWindow(main);
		setTheme("test");
		//director.setUiBuilder(ui);
		//director.constructView();
		main.addComponent(TenantSettingsComponent.getInstance());

	}

}
