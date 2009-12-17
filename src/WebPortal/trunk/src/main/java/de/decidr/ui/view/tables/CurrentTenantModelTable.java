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
import de.decidr.ui.data.CurrentTenantContainer;

/**
 * This table holds the current tenant items. It is integrated into a component.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2048", currentReviewState = State.PassedWithComments)
public class CurrentTenantModelTable extends Table {

    private static final long serialVersionUID = -3378507042364075268L;
    private Container currentTenantContainer = null;

    private final int TABLE_PAGE_LENGTH = 8;
    
    
    /**
     * Adds this table as an observer to the depending container.<br>
     * Aleks, GH: obviously incorrect usage of the expression "depending" -
     * don't know what you're trying to say ~rr
     */
    public CurrentTenantModelTable(Container container) {
        currentTenantContainer = container;
        init();
    }

    /**
     * Initializes the table and sets the container.
     */
    private void init() {
        setSizeFull();
        setContainerDataSource(currentTenantContainer);

        setVisibleColumns(CurrentTenantContainer.NAT_COL_ORDER);
        setColumnHeaders(CurrentTenantContainer.COL_HEADERS);
        setSelectable(true);
        setMultiSelect(true);
        setPageLength(TABLE_PAGE_LENGTH);
    }
}
