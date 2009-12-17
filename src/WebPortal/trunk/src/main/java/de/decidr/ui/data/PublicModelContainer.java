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
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.WorkflowModelFacade;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.KeywordFilter;
import de.decidr.ui.beans.WorkflowModelBean;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This container holds the public workflow models. The models are represented
 * as {@link Item items} in a table.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2353", currentReviewState = State.NeedsReview)
public class PublicModelContainer extends BeanItemContainer<WorkflowModelBean> {

    private static final long serialVersionUID = 1L;

    private HttpSession session = Main.getCurrent().getSession();

    private Role role = (Role) session.getAttribute("role");

    WorkflowModelFacade workflowModelFacade = new WorkflowModelFacade(role);

    List<WorkflowModel> publishedModelList = null;

    private KeywordFilter filter = new KeywordFilter();

    private List<Filter> filterList = new LinkedList<Filter>();

    public static final Object[] NAT_COL_ORDER = new Object[] { "name",
            "modifiedDate", "tenantName" };

    public static final String[] COL_HEADERS = new String[] { "Name",
            "Modified Date", "Tenant Name" };

    /**
     * The public model {@link Item items} are added to the container.
     */
    public PublicModelContainer() {
        super(WorkflowModelBean.class);
        filterList.add(filter);
        try {
            publishedModelList = workflowModelFacade
                    .getAllPublishedWorkflowModels(null, null);
            WorkflowModelBean workflowModelBean;
            for (WorkflowModel workflowModel : publishedModelList) {
                workflowModelBean = new WorkflowModelBean(workflowModel);
                addBean(workflowModelBean);
            }
        } catch (TransactionException exception) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(exception));
        }
    }
}
