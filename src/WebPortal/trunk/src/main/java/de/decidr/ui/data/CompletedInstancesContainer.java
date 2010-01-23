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

import java.util.List;

import javax.servlet.http.HttpSession;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.beans.WorkflowInstanceBean;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This container holds all completed workflow instances. These {@link Item
 * items} will be shown in a table.<br>
 * TODO: update comment: not an Item
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2499", currentReviewState = State.PassedWithComments)
public class CompletedInstancesContainer extends
        BeanItemContainer<WorkflowInstanceBean> {

    private static final long serialVersionUID = 1L;

    private HttpSession session = Main.getCurrent().getSession();

    private Role role = (Role) session.getAttribute("role");

    private Long userId = (Long) session.getAttribute("userId");

    UserFacade userFacade = new UserFacade(role);

    private List<WorkflowInstance> workflowInstanceList = null;

    public static final Object[] NAT_COL_ORDER = new Object[] { "model",
            "startedDate", "completedDate" };

    public static final String[] COL_HEADERS = new String[] { "Model",
            "Started Date", "Completed Date" };

    /**
     * Adds completed instance {@link Item items} to the container.<br>
     * TODO: update comment: not an Item
     */
    public CompletedInstancesContainer() {
        super(WorkflowInstanceBean.class);
        try {
            workflowInstanceList = userFacade
                    .getAdministratedWorkflowInstances(userId);
            WorkflowInstanceBean workflowInstanceBean;
            for (WorkflowInstance workflowInstance : workflowInstanceList) {
                if (workflowInstance.getCompletedDate() != null) {
                    workflowInstanceBean = new WorkflowInstanceBean(
                            workflowInstance);
                    addBean(workflowInstanceBean);
                }
            }
        } catch (TransactionException exception) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(exception));
        }
    }
}
