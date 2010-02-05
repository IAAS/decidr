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
import de.decidr.ui.data.WorkflowModelContainer;

/**
 * This class represents the workflow model UI component. It will be connected
 * to data from the database.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2474", currentReviewState = State.Passed)
public class WorkflowModelTable extends Table {

    private static final long serialVersionUID = 1L;

    private final int TABLE_PAGE_LENGTH = 8;

    private Container container = null;

    /**
     * @param container
     *            Container which holds the workflow models
     */
    public WorkflowModelTable(Container container) {
        this.container = container;
        init();
    }

    /**
     * This method initializes the components for the workflow instance table.
     */
    private void init() {
        setSizeFull();
        setContainerDataSource(container);

        setVisibleColumns(WorkflowModelContainer.NAT_COL_ORDER);
        setColumnHeaders(WorkflowModelContainer.COL_HEADERS);
        setSelectable(true);
        setMultiSelect(true);
        setPageLength(TABLE_PAGE_LENGTH);
    }
}
