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
import com.vaadin.data.Item;
import com.vaadin.ui.Table;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.ui.data.TenantContainer;

/**
 * This table holds the tenants as {@link Item items}.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2332", currentReviewState = State.PassedWithComments)
public class TenantTable extends Table {

    private static final long serialVersionUID = -4777680020350752428L;

    private Container tenantContainer = null;

    private final int TABLE_PAGE_LENGTH = 10;
    
    

    /**
     * Default constructor with container which is set as data source.<br>
     * Aleks, GH: *not* a default constructor ~rr
     */
    public TenantTable(Container container) {
        tenantContainer = container;
        init();
    }

    /**
     * Initializes the table and sets the container data source.
     */
    private void init() {
        setSizeFull();
        setContainerDataSource(tenantContainer);

        setVisibleColumns(TenantContainer.NAT_COL_ORDER);
        setColumnHeaders(TenantContainer.COL_HEADERS);
        setSelectable(true);
        setMultiSelect(false);
        setPageLength(TABLE_PAGE_LENGTH);
    }
}
