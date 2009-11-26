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
import com.vaadin.ui.Table;

/**
 * This class represents the workflow model ui component. It will be connected
 * with data from the database.
 * 
 * @author AT
 */
public class WorkflowModelTable extends Table {

	private Container container = null;

	public static final Object[] NAT_COL_ORDER = new Object[] { "id", "name",
			"creationDate", "published" };

	public static final String[] COL_HEADERS = new String[] { "ID", "Name",
			"Creation Date", "Published" };

	/**
	 * Default constructor.
	 * 
	 */
	public WorkflowModelTable(Container container) {
		this.container = container;
		init();
	}

	/**
	 * This method initializes the components for the workflow instance table.
	 * 
	 */
	private void init() {
		setSizeFull();
		setContainerDataSource(container);

		addContainerProperty("id", Long.class, null);
		addContainerProperty("name", String.class, null);
		addContainerProperty("creationDate", String.class, null);
		addContainerProperty("published", Boolean.class, null);
		// TODO: Add two more properties

		setVisibleColumns(NAT_COL_ORDER);
		setColumnHeaders(COL_HEADERS);
		setSelectable(true);
		setMultiSelect(true);
		setPageLength(8);

	}

}
