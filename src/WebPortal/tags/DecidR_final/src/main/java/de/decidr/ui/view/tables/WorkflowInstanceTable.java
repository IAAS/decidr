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
import de.decidr.ui.data.WorkflowInstanceContainer;

/**
 * This class represents the workflow instance ui component. It will be filled
 * with data from the database.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "TK", "JS" }, lastRevision = "2377", currentReviewState = State.PassedWithComments)
public class WorkflowInstanceTable extends Table {

    private static final long serialVersionUID = -4395559171091884350L;

    private Container workflowInstanceContainer = null;

    /**
     * Default constructor.
     * 
     * @param container
     *            Container which holds the workflow instances
     */
    public WorkflowInstanceTable(Container container) {
        workflowInstanceContainer = container;
        init();
    }

    /**
     * This method initializes the components for the workflow instance table.
     */
    private void init() {
        setSizeFull();
        setContainerDataSource(workflowInstanceContainer);

        setVisibleColumns(WorkflowInstanceContainer.NAT_COL_ORDER);
        setColumnHeaders(WorkflowInstanceContainer.COL_HEADERS);
        setSelectable(true);
        setMultiSelect(false);

    }

}
