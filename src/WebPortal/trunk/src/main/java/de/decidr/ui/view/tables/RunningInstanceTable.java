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
import de.decidr.ui.data.RunningInstanceContainer;

/**
 * This table holds the running workflow instances as items.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "TK", "JS" }, lastRevision = "2377", currentReviewState = State.PassedWithComments)
public class RunningInstanceTable extends Table {

    private static final long serialVersionUID = 49258596599726066L;

    private final int TABLE_PAGE_LENGTH = 7;
    
    private Container runningInstanceContainer = null;

    

    /**
     * Default constructor.
     * 
     * @param container
     *            Container which holds the running workflow instances
     */
    public RunningInstanceTable(Container container) {
        runningInstanceContainer = container;
        init();
    }

    /**
     * Initializes the table and sets the container.
     */
    private void init() {
        setSizeFull();
        setContainerDataSource(runningInstanceContainer);

        setVisibleColumns(RunningInstanceContainer.NAT_COL_ORDER);
        setColumnHeaders(RunningInstanceContainer.COL_HEADERS);
        setSelectable(true);
        setMultiSelect(false);
        setPageLength(TABLE_PAGE_LENGTH);
    }
}
