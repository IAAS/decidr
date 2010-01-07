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
import de.decidr.ui.data.UserListContainer;

/**
 * This class represents the user list's ui component. It will be filled with
 * data from the database.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "TK", "JS" }, lastRevision = "2377", currentReviewState = State.PassedWithComments)
public class UserListTable extends Table {

    private static final long serialVersionUID = -4772118786130924736L;

    private Container userListContainer = null;

    /**
     * Default constructor.
     * 
     * @param container
     *            Container which holds the users.
     */
    public UserListTable(Container container) {
        userListContainer = container;
        init();
    }

    /**
     * This method initializes the components for the user list table.
     */
    private void init() {
        setSizeFull();
        setContainerDataSource(userListContainer);

        setVisibleColumns(UserListContainer.NAT_COL_ORDER);
        setColumnHeaders(UserListContainer.COL_HEADERS);
        setSelectable(true);
        setMultiSelect(false);

    }

}