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
import de.decidr.ui.data.ApproveTenantContainer;

/**
 * This table holds the tenants which are to be approved.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2332", currentReviewState = State.Passed)
public class ApproveTenantTable extends Table {

    private static final long serialVersionUID = 1L;

    Container container = null;
    
    private final int TABLE_PAGE_LENGTH = 10;

    /**
     * Constructor with a container which is set as data source.
     */
    public ApproveTenantTable(Container container) {
        this.container = container;
        init();
    }

    /**
     * Initializes the table and sets the
     * {@link Table#setContainerDataSource(Container) container data source}.
     */
    private void init() {
        setSizeFull();
        setContainerDataSource(container);

        setVisibleColumns(ApproveTenantContainer.NAT_COL_ORDER);
        setColumnHeaders(ApproveTenantContainer.COL_HEADERS);
        setSelectable(true);
        setMultiSelect(true);
        setPageLength(TABLE_PAGE_LENGTH);
    }
}
