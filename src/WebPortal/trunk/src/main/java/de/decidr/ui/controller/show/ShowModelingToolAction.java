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
import de.decidr.ui.beans.WorkflowModelBean;
import de.decidr.ui.beans.WorkflowModelsBean;
import de.decidr.ui.controller.UIDirector;
import de.decidr.ui.data.ModelingTool;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.SiteFrame;
import de.decidr.ui.view.tables.PublicModelTable;
import de.decidr.ui.view.tables.WorkflowModelTable;
import de.decidr.ui.view.windows.InformationDialogComponent;

/**
 * Opens the modeling tool.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2348", currentReviewState = State.Passed)
public class ShowModelingToolAction implements ClickListener {

    private static final long serialVersionUID = 1L;
    UIDirector uiDirector = Main.getCurrent().getUIDirector();
    SiteFrame siteFrame = uiDirector.getTemplateView();

    private Table table = null;

    /**
     * Constructor which stores the table where the workflow model is selected.
     */
    public ShowModelingToolAction(Table table) {
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
        Main.getCurrent().getMainWindow().requestRepaint();
        if (table instanceof WorkflowModelTable) {
            Set<WorkflowModelsBean> set = (Set<WorkflowModelsBean>) table
                    .getValue();
            if (table.getValue() != null && set.size() == 1) {
                Iterator<WorkflowModelsBean> iter = set.iterator();
                while (iter.hasNext()) {
                    WorkflowModelsBean workflowModel = iter.next();
                    siteFrame
                            .setContent(new ModelingTool(workflowModel.getId()));
                }
            }else{
                Main.getCurrent().getMainWindow().addWindow(
                        new InformationDialogComponent(
                                "Please select exactly one workflow model",
                                "Information"));
            }
        } else if (table instanceof PublicModelTable) {
            Set<WorkflowModelBean> set = (Set<WorkflowModelBean>) table
                    .getValue();
            if (table.getValue() != null && set.size() <= 1) {
                Iterator<WorkflowModelBean> iter = set.iterator();
                while (iter.hasNext()) {
                    WorkflowModelBean workflowModel = iter.next();
                    siteFrame
                            .setContent(new ModelingTool(workflowModel.getId()));

                }

            } else {
                Main.getCurrent().getMainWindow().addWindow(
                        new InformationDialogComponent(
                                "Please select exactly one workflow model",
                                "Information"));
            }
        }
    }
}
