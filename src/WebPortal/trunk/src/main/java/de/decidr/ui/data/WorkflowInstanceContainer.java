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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.model.facades.UserFacade;
import de.decidr.model.filters.EqualsFilter;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.StartableWorkflowModelFilter;
import de.decidr.ui.beans.WorkflowModelsBean;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * The container holds the workflow instances. The instances are represented as
 * {@link Item items} in a table.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2360", currentReviewState = State.NeedsReview)
public class WorkflowInstanceContainer extends BeanItemContainer<WorkflowModelsBean> {

    private static final long serialVersionUID = 1L;

    private HttpSession session = Main.getCurrent().getSession();

    private Long userId = (Long) session.getAttribute("userId");
    private Role role = (Role) session.getAttribute("role");

    TenantFacade tenantFacade = new TenantFacade(role);
    UserFacade userFacade = new UserFacade(role);
    Long tenantId = (Long) Main.getCurrent().getSession().getAttribute(
            "tenantId");

    List<WorkflowModel> workflowModelList = null;

    private List<Filter> filterList;

    public static final Object[] NAT_COL_ORDER = new Object[] { "id", "name",
            "description" };

    public static final String[] COL_HEADERS = new String[] { "ID", "Name",
            "Description" };

    /**
     * The instance items are added to the container.
     */
    public WorkflowInstanceContainer() {
        super(WorkflowModelsBean.class);
        try {
            // Aleks Please review my changes: ~dh
            filterList = new ArrayList<Filter>();
            filterList.add(new StartableWorkflowModelFilter());
            filterList.add(new EqualsFilter(true, "tenant.id", tenantId));

            workflowModelList = userFacade.getAdministratedWorkflowModels(
                    userId, null, null);
            WorkflowModelsBean workflowModelsBean;
            for (WorkflowModel workflowModel : workflowModelList) {
                workflowModelsBean = new WorkflowModelsBean(workflowModel);
                addBean(workflowModelsBean);
            }
        } catch (TransactionException exception) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(exception));
        }
    }
}
