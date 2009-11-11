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

import java.util.Date;
import com.vaadin.data.Container;

import com.vaadin.ui.Button;
import com.vaadin.ui.Table;

public class RunningInstanceTable extends Table {

	/**
	 * This table holds the running workflow instances as items.
	 * 
	 * @author AT
	 */
	private static final long serialVersionUID = 49258596599726066L;

	private Container runningInstanceContainer = null;

	public static final Object[] NAT_COL_ORDER = new Object[] { "model",
			"startedDate", "completedDate" };

	public static final String[] COL_HEADERS = new String[] { "Model",
			"Started date", "Completed date" };

	/**
	 * Default construtctor.
	 * 
	 * @param container
	 */
	public RunningInstanceTable(Container container) {
		runningInstanceContainer = container;
		init();
	}

	/**
	 * Initializes the table and sets the container.
	 * 
	 * @param observable
	 * @param container
	 */
	private void init() {
		setSizeFull();
		setContainerDataSource(runningInstanceContainer);

		addContainerProperty("model", String.class, null);
		addContainerProperty("startedDate", Date.class, null);
		addContainerProperty("completedDate", Date.class, null);
		addContainerProperty("Terminate", Button.class, null);
		
		setVisibleColumns(NAT_COL_ORDER);
		setColumnHeaders(COL_HEADERS);
		setSelectable(true);
		setMultiSelect(true);
	}

}
