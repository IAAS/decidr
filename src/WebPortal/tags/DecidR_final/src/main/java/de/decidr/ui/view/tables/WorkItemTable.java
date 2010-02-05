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

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.ui.data.WorkItemContainer;

/**
 * The table represents the work items as items.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "TK", "JS" }, lastRevision = "2377", currentReviewState = State.Passed)
public class WorkItemTable extends Table {

    private static final long serialVersionUID = 24861377458898625L;

    private Container workItemContainer = null;

    /**
     * Default constructor.
     * 
     * @param container
     *            Container which holds the work items
     */
    public WorkItemTable(Container container) {
        this.workItemContainer = container;
        init();
    }

    /**
     * Initializes the table and sets the container data source.
     */
    private void init() {
        setSizeFull();
        setContainerDataSource(workItemContainer);

        setVisibleColumns(WorkItemContainer.NAT_COL_ORDER);
        setColumnHeaders(WorkItemContainer.COL_HEADERS);
        setSelectable(true);
        setMultiSelect(true);
    }
}
