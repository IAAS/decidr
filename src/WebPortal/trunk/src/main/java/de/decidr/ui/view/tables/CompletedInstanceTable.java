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

import java.util.Date;

import com.vaadin.data.Container;
import com.vaadin.ui.Table;

import de.decidr.ui.data.CompletedInstancesContainer;

/**
 * This table represents the completed workflow instances. The instances are
 * stored in items. The table is connected to the
 * {@link CompletedInstancesContainer}.
 * 
 * @author AT
 */
public class CompletedInstanceTable extends Table {

    private static final long serialVersionUID = -4341477724807479177L;

    private Container workflowInstanceContainer = null;

    public static final Object[] NAT_COL_ORDER = new Object[] { "model",
            "startedDate", "completedDate" };

    public static final String[] COL_HEADERS = new String[] { "Model",
            "Started date", "Completed date" };

    /**
     * The table is added as an observer to the container.
     * 
     * @param observable
     * @param container
     */
    public CompletedInstanceTable(Container container) {
        workflowInstanceContainer = container;
        init();
    }

    /**
     * Initializes the properties for the table like the data source and the
     * container property.
     * 
     * @param observable
     * @param container
     */
    private void init() {
        setSizeFull();
        setContainerDataSource(workflowInstanceContainer);
        addContainerProperty("model", String.class, null);
        addContainerProperty("startedDate", Date.class, null);
        addContainerProperty("completedDate", Date.class, null);

        setVisibleColumns(NAT_COL_ORDER);
        setColumnHeaders(COL_HEADERS);
        setSelectable(true);
        setMultiSelect(true);
        setPageLength(7);
    }

}
