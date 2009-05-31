package de.decidr.ui.view;

import com.vaadin.Application;
import com.vaadin.ui.Window;

public class Main extends Application {

	/**
     * TODO: add comment
     */
    private static final long serialVersionUID = 2668887930201158755L;
    Window main = new Window();
	
	@Override
	public void init() {
		
		setMainWindow(main);
		main.addComponent(UserListComponent.getInstance());

	}

}
