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

package de.decidr.ui.data;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.entities.WorkItemSummaryView;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.KeywordFilter;
import de.decidr.ui.beans.WorkItemSummaryViewBean;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * The container holds work items. The work items are represented as
 * {@link Item items} in a table.<br>
 * Aleks: update comment: not an Item; "work item" vs. "workitem"
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2499", currentReviewState = State.PassedWithComments)
public class WorkItemContainer extends
        BeanItemContainer<WorkItemSummaryViewBean> {

    private static final long serialVersionUID = 1L;

    private HttpSession session = Main.getCurrent().getSession();

    private Long userId = (Long) session.getAttribute("userId");
    private Role role = (Role) session.getAttribute("role");

    private UserFacade userFacade = new UserFacade(role);

    private List<WorkItemSummaryView> workItemList = null;

    private KeywordFilter filter = new KeywordFilter();

    private List<Filter> filterList = new LinkedList<Filter>();

    public static final Object[] NAT_COL_ORDER = new Object[] { "workItemName",
            "workflowInstanceId", "creationDate", "workItemStatus" };

    public static final String[] COL_HEADERS = new String[] { "Name", "WfI#",
            "Creation Date", "State" };

    /**
     * The work item items are added to the container.<br>
     * Aleks: update comment: not an Item
     */
    public WorkItemContainer() {
        super(WorkItemSummaryViewBean.class);
        filterList.add(filter);
        try {
            workItemList = userFacade.getWorkItems(userId, filterList, null);
            WorkItemSummaryViewBean workItemSummaryViewBean;
            for (WorkItemSummaryView workItemSummaryView : workItemList) {
                workItemSummaryViewBean = new WorkItemSummaryViewBean(
                        workItemSummaryView);
                addBean(workItemSummaryViewBean);
            }
        } catch (TransactionException exception) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(exception));
        }
    }
}
