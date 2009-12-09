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

/**
 * This table holds the tenants as {@link Item items}.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2332", currentReviewState = State.PassedWithComments)
public class TenantTable extends Table {

    private static final long serialVersionUID = -4777680020350752428L;

    private Container tenantContainer = null;

    public static final Object[] NAT_COL_ORDER = new Object[] {
            "adminFirstName", "adminLastName", "numDeployedWorkflowModels",
            "numMembers", "numWorkflowInstances" };

    public static final String[] COL_HEADERS = new String[] { "First name",
            "Last name", "#Deployed Models", "#Members", "#Instances" };

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

        addContainerProperty("adminFirstName", String.class, null);
        addContainerProperty("adminLastName", String.class, null);
        addContainerProperty("numDeployedWorkflowModels", Long.class, null);
        addContainerProperty("numWorkflowInstances", Long.class, null);
        addContainerProperty("numMembers", Long.class, null);

        setVisibleColumns(NAT_COL_ORDER);
        setColumnHeaders(COL_HEADERS);
        setSelectable(true);
        setMultiSelect(true);
        setPageLength(10);
    }
}
