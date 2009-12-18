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

package de.decidr.ui.controller.show;

import java.util.Iterator;
import java.util.Set;

import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.ui.beans.WorkflowModelsBean;
import de.decidr.ui.controller.UIDirector;
import de.decidr.ui.view.AppointWorkflowAdminComponent;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.SiteFrame;
import de.decidr.ui.view.windows.InformationDialogComponent;

/**
 * Opens the {@link AppointWorkflowAdminComponent}, allowing the tenant admin to
 * appoint one or more workflow admins.
 * 
 * @author AT
 */
@Reviewed(reviewers = "RR", lastRevision = "2354", currentReviewState = State.Rejected)
public class ShowAppointWorkflowAdminAction implements ClickListener {

    private static final long serialVersionUID = 1L;
    private UIDirector uiDirector = Main.getCurrent().getUIDirector();
    private SiteFrame siteFrame = uiDirector.getTemplateView();

    private Table table = null;

    /**
     * Constructor which gets a {@link Table} as parameter to determine which
     * item is selected from which table.
     */
    public ShowAppointWorkflowAdminAction(Table table) {
        this.table = table;
    }

    /*
     * (non-Javadoc)
     * 
     * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        Long wfmId = null;
        Set<?> items = (Set<?>) table.getValue();

        if (items.size() == 1) {
            for (Iterator<?> iter = items.iterator(); iter.hasNext();) {
                WorkflowModelsBean workflowModelsBean = (WorkflowModelsBean) iter
                        .next();
                wfmId = workflowModelsBean.getId();
            }
            if (wfmId != null) {
                siteFrame.setContent(new AppointWorkflowAdminComponent(wfmId));
            }

        } else {
            Main.getCurrent().getMainWindow().addWindow(
                    new InformationDialogComponent(
                            "Please select exactly one workflow model.",
                            "Action Failed"));
        }
    }
}
