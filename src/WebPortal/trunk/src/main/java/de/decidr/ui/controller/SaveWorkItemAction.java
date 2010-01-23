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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.jdom.JDOMException;
import org.w3c.dom.DOMException;

import com.vaadin.ui.DateField;
import com.vaadin.ui.Form;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.WorkItemFacade;
import de.decidr.model.workflowmodel.dwdl.transformation.TransformUtil;
import de.decidr.model.workflowmodel.humantask.DWDLSimpleVariableType;
import de.decidr.model.workflowmodel.humantask.THumanTaskData;
import de.decidr.model.workflowmodel.humantask.TTaskItem;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This action saves the entered information to the user's work item.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2348", currentReviewState = State.PassedWithComments)
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
                org.jdom.Element element = new org.jdom.Element("value");
                // If the taskItem isn't of any uri type then the value from the
                // field is stored in the value
                // variable. If the taskItem is a uri type then the data, which
                // is the file id from the file
                // which is uploaded by the user, is received and stored in the
                // value variable.
                Object value = null;
                if (tTaskItem.getType().compareTo(
                        DWDLSimpleVariableType.ANY_URI) != 0) {
                    value = form.getField(tTaskItem.getName()).getValue();
                } else {
                    value = Main.getCurrent().getMainWindow().getData();
                    if (value == null) {
                        value = "";
                    }
                }

                // If the value equals to a string representation of a boolean
                // value it is mapped
                // to the bpel conform values yes and no.
                // Also, the date is parsed to specific format (yyyy-MM-dd) to
                // fit in
                // the bpel specification.
                if (value.toString().equals("true")) {
                    value = "yes";
                } else if (value.toString().equals("false")) {
                    value = "no";
                } else if (form.getField(tTaskItem.getName()) instanceof DateField) {
                    SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date = (Date) value;
                        value = dfs.format(date);
                    } catch (DOMException e) {
                        Main.getCurrent().getMainWindow().addWindow(
                                new TransactionErrorDialogComponent(e));
                    }
                }

                // The values are stored in an element and set as value to the
                // task item.
                element.setText(value.toString());
                try {
                    tTaskItem.setValue(TransformUtil.jdomToW3c(element));
                } catch (JDOMException e) {
                    throw new RuntimeException("Transformation not successful");
                }
            }
        }

        try {
            // TODO: can't this be done through some kind of action
            // assigned to the button (like in AWT/Swing)?
            if (event.getButton().getCaption().equals("OK")) {
                workItemFacade.setData(workItemId, tHumanTaskData);
                Main
                        .getCurrent()
                        .getMainWindow()
                        .addWindow(
                                new InformationDialogComponent(
                                        "Work item edited and marked as \"in progress\"",
                                        "Edit success"));
            } else if (event.getButton().getCaption().equals("Mark as done")) {
                workItemFacade.setDataAndMarkAsDone(workItemId, tHumanTaskData);
                Main.getCurrent().getMainWindow().addWindow(
                        new InformationDialogComponent(
                                "Work item edited and marked as \"done\"",
                                "Done"));
            }
            Main.getCurrent().getMainWindow().removeWindow(
                    event.getButton().getWindow());
        } catch (TransactionException exception) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(exception));
            Main.getCurrent().getMainWindow().removeWindow(
                    event.getButton().getWindow());
        }
    }
}
