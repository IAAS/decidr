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

package de.decidr.ui.controller.workflowmodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.WorkflowModelFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This class removes workflow models from a UI table.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2352", currentReviewState = State.PassedWithComments)
public class RemoveWorkflowModelsAction implements ClickListener {

    private static final long serialVersionUID = 1L;

    private WorkflowModelFacade workflowModelFacade = new WorkflowModelFacade(
            (Role) Main.getCurrent().getSession().getAttribute("role"));

    private Table table = null;

    /**
     * Stores the passed table to get the selected item.<br>
     * Aleks, GH: you *do* know, that this description doesn't actually make any
     * sense, right? ~rr
     */
    public RemoveWorkflowModelsAction(Table table) {
        this.table = table;
    }

    /*
     * (non-Javadoc)
     * 
     * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void buttonClick(ClickEvent event) {
        List<Long> wfms = new LinkedList<Long>();
        Set<WorkflowModel> value = (Set<WorkflowModel>) table.getValue();
        List<WorkflowModel> items = new ArrayList<WorkflowModel>(value);
        if ((value != null) && (value.size() != 0)) {
            for (Iterator<?> iter = value.iterator(); iter.hasNext();) {
                WorkflowModel workflowModel = (WorkflowModel) iter.next();
                wfms.add(workflowModel.getId());
            }
            try {
                workflowModelFacade.deleteWorkflowModels(wfms);
                for (WorkflowModel workflowModel : items) {
                    table.removeItem(workflowModel);
                }
                Main.getCurrent().getMainWindow().addWindow(
                        new InformationDialogComponent(
                                "Selected model(s) successfully removed!",
                                "Success"));
            } catch (TransactionException e) {
                Main.getCurrent().getMainWindow().addWindow(
                        new TransactionErrorDialogComponent(e));
            }
        } else {
            Main.getCurrent().getMainWindow().addWindow(
                    new InformationDialogComponent(
                            "Please select a workflow model from the table.",
                            "Information"));
        }
    }
}
