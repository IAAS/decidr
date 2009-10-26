/*
 * The DecidR Development Team licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.decidr.ui.view;

import javax.servlet.http.HttpSession;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.service.ApplicationContext.TransactionListener;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Window;

import de.decidr.ui.controller.ConfirmationParameterHandler;
import de.decidr.ui.controller.InvitationParameterHandler;
import de.decidr.ui.controller.UIDirector;

/**
 * This is the main class. When calling the DecidR web site this class is called
 * first and initialized.
 * 
 * @author AT
 */
public class Main extends Application implements TransactionListener {

	private static final long serialVersionUID = 2668887930201158755L;

	private ApplicationContext ctx = null;

	private WebApplicationContext webCtx = null;

	private HttpSession session = null;

	private static ThreadLocal<Main> currentApplication = new ThreadLocal<Main>();

	Window main = new Window();

	UIBuilder ui = new UnregisteredUserViewBuilder();

	UIDirector director = UIDirector.getInstance();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.Application#init()
	 */
	@Override
	public void init() {
		setMainWindow(main);

		main.addParameterHandler(new InvitationParameterHandler());
		main.addParameterHandler(new ConfirmationParameterHandler());

		setTheme("decidr");

		director.setUiBuilder(ui);
		director.createNewView();
		director.constructView();
		
		main.addComponent(director.getTemplateView());
		
		if (getContext() != null) {
			getContext().addTransactionListener(this);
		}
	}

	/**
	 * Returns the current application instance.
	 * 
	 * @return the current application instance
	 */
	public static Main getCurrent() {
		return currentApplication.get();
	}

	/**
	 * Remove the current application instance
	 */
	public static void removeCurrent() {
		currentApplication.remove();
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
	 * Returns the session from the depending DecidR instance.
	 * 
	 * @return session
	 */
	public HttpSession getSession() {
		return session;
	}
	
	/**
	 * Sets the session from the depending DecidR instance.
	 *
	 * @param session
	 */
	public void setSession(HttpSession session){
		this.session = session;
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
