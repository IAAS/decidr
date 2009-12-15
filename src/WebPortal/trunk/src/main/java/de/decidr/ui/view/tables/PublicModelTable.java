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

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;

/**
 * This table contains the public workflow models as items.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "TK", "JS" }, lastRevision = "2377", currentReviewState = State.PassedWithComments)
public class PublicModelTable extends Table {

    private static final long serialVersionUID = -8901605163680575150L;

    private Container publicModelContainer = null;

    private final int TABLE_PAGE_LENGTH = 7;
    
    public static final Object[] NAT_COL_ORDER = new Object[] { "name",
            "modifiedDate", "tenantName" };

    public static final String[] COL_HEADERS = new String[] { "Name",
            "Modified Date", "Tenant Name" };

    /**
     * Default constructor. Adds this table as an observer to the container
     * which notifies the table if there are any changes in the data.
     * 
     * @param container
     *            Container which holds the workflow models
     */
    public PublicModelTable(Container container) {
        publicModelContainer = container;
        init();
    }

    /**
     * Initializes the table and sets the container and the properties.
     */
    private void init() {
        setSizeFull();
        setContainerDataSource(publicModelContainer);

        addContainerProperty("name", String.class, null);
        addContainerProperty("modifiedDate", Date.class, null);
        addContainerProperty("tenantName", String.class, null);

        setVisibleColumns(NAT_COL_ORDER);
        setColumnHeaders(COL_HEADERS);
        setSelectable(true);
        setMultiSelect(true);
        setPageLength(TABLE_PAGE_LENGTH);
    }

}
