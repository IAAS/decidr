package de.decidr.ui.view;

import com.vaadin.Application;
import com.vaadin.ui.Window;

public class Main extends Application {

	Window main = new Window();
	
	@Override
	public void init() {
		
		setMainWindow(main);
		main.addComponent(SystemSetting.getInstance());

	}

}
