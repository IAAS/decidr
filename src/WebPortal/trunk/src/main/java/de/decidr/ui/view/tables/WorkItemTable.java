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
package de.decidr.ui.view.tables;

import com.vaadin.data.Container;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;

import de.decidr.ui.controller.MarkWorkItemAsDoneAction;

public class WorkItemTable extends Table {

	/**
	 * The table represents the work items as items.
	 * 
	 * @author AT
	 */
	private static final long serialVersionUID = 24861377458898625L;

	private Container workItemContainer = null;

	public static final Object[] NAT_COL_ORDER = new Object[] { "workItemName",
			"workflowInstanceId", "creationDate", "workItemStatus" };

	public static final String[] COL_HEADERS = new String[] { "Name", "WfI#",
			"Creation date", "Status" };

	/**
	 * Default constructor.
	 * 
	 * @param container
	 */
	public WorkItemTable(Container container) {
		this.workItemContainer = container;
		init();
	}
	
	public WorkItemTable(){
		init();
	}

	/**
	 * Initializes the table and sets the container data source.
	 */
	private void init() {
		setSizeFull();
		setContainerDataSource(workItemContainer);

		addContainerProperty("workItemName", String.class, null);
		addContainerProperty("workflowInstanceId", Long.class, null);
		addContainerProperty("creationDate", String.class, null);
		addContainerProperty("workItemStatus", String.class, null);

		setVisibleColumns(NAT_COL_ORDER);
		setColumnHeaders(COL_HEADERS);
		setSelectable(true);
		setMultiSelect(true);
	}
}
