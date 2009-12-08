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

package de.decidr.ui.controller;

import java.util.Iterator;

import com.vaadin.ui.Form;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.WorkItemFacade;
import de.decidr.model.workflowmodel.humantask.TContent;
import de.decidr.model.workflowmodel.humantask.THumanTaskData;
import de.decidr.model.workflowmodel.humantask.TInformation;
import de.decidr.model.workflowmodel.humantask.TTaskItem;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This action saves the entered information to the user's work item.
 * 
 * @author AT
 */
@Reviewed(reviewers={"RR"}, lastRevision="2348", currentReviewState=State.PassedWithComments)
public class SaveWorkItemAction implements ClickListener {

    private static final long serialVersionUID = 1L;

    private Form form = null;

    private THumanTaskData tHumanTaskData = null;

    private Long workItemId = null;

    private Role role = (Role) Main.getCurrent().getSession().getAttribute(
            "role");

    private WorkItemFacade workItemFacade = new WorkItemFacade(role);

    /**
     * Constructor which has a {@link Form} and a {@link THumanTaskData} as
     * parameter so that the entered information can be saved in the task items.
     */
    public SaveWorkItemAction(Form form, THumanTaskData tHumanTaskData,
            Long workItemId) {
        this.form = form;
        this.tHumanTaskData = tHumanTaskData;
        this.workItemId = workItemId;
    }

    /*
     * (non-Javadoc)
     * 
     * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        Iterator<?> iterator = tHumanTaskData.getTaskItemOrInformation()
                .iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object instanceof TTaskItem) {
                TTaskItem tTaskItem = (TTaskItem) object;
                tTaskItem.setValue(form.getField(tTaskItem.getName())
                        .getValue());
            } else {
                TInformation tInformation = (TInformation) object;
                TContent tContent = new TContent();
                tContent.setAny(form.getField(tInformation.getName())
                        .getValue());
                tInformation.setContent(tContent);
            }
        }

        try {
            // Aleks, GH: can't this be done through some kind of action
            // assigned to the button (like in AWT/Swing)? ~rr
            if (event.getButton().getCaption().equals("OK")) {
                workItemFacade.setData(workItemId, tHumanTaskData);
            } else if (event.getButton().getCaption().equals("Mark as done")) {
                workItemFacade.setData(workItemId, tHumanTaskData);
            }
            new HideDialogWindowAction();
        } catch (TransactionException exception) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(exception));
            new HideDialogWindowAction();
        }
    }
}
