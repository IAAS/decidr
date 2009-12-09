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
 * This class represents the workflow instance ui component. It will be
 * connected with data from the database.
 * 
 * @author AT
 */
public class WorkflowInstanceTable extends Table {

    private static final long serialVersionUID = -4395559171091884350L;

    private Container workflowInstanceContainer = null;

    public static final Object[] NAT_COL_ORDER = new Object[] { "id", "name",
            "description" };

    public static final String[] COL_HEADERS = new String[] { "ID", "Name",
            "Description" };

    /**
     * Default constructor.
     * 
     */
    public WorkflowInstanceTable(Container container) {
        workflowInstanceContainer = container;
        init();
    }

    /**
     * This method initializes the components for the workflow instance table.
     * 
     */
    private void init() {
        setSizeFull();
        setContainerDataSource(workflowInstanceContainer);

        addContainerProperty("id", Long.class, null);
        addContainerProperty("name", String.class, null);
        addContainerProperty("description", String.class, null);

        setVisibleColumns(NAT_COL_ORDER);
        setColumnHeaders(COL_HEADERS);
        setSelectable(true);
        setMultiSelect(false);
    }

}
