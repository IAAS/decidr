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

package de.decidr.ui.view.windows;

import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.workflowmodel.humantask.DWDLSimpleVariableType;
import de.decidr.model.workflowmodel.humantask.THumanTaskData;
import de.decidr.model.workflowmodel.humantask.TInformation;
import de.decidr.model.workflowmodel.humantask.TTaskItem;
import de.decidr.ui.controller.HideDialogWindowAction;
import de.decidr.ui.controller.SaveWorkItemAction;

/**
 * This window represents a form where the work items of the user are displayed.
 * Here he can enter his data into the provided fields and save them.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2357", currentReviewState = State.PassedWithComments)
public class WorkItemWindow extends Window {

    private static final long serialVersionUID = 1L;

    private VerticalLayout verticalLayout = null;

    private HorizontalLayout horizontalLayout = null;

    private Label label = null;

    private Form itemForm = null;

    private Button okButton = null;

    private Button cancelButton = null;

    private Button markAsDoneButton = null;

    /**
     * Constructor with a given {@link THumanTaskData} object which calls the
     * init method.
     */
    public WorkItemWindow(THumanTaskData tHumanTaskData, Long workItemId) {
        init(tHumanTaskData, workItemId);
    }

    /**
     * Fills the form with the specific settings from the {@link THumanTaskData}
     * . For every simple type another Field is added to the form and if
     * necessary a validator is added, so that the user can't enter invalid
     * data. A tooltip shows the user some information on what to enter into the
     * fields. If a value already exists, this value is shown.
     * 
     * @param tHumanTaskData
     *            TODO document
     */
    private void fillForm(THumanTaskData tHumanTaskData) {
        for (int i = 0; i < tHumanTaskData.getTaskItemOrInformation().size(); i++) {
            if (tHumanTaskData.getTaskItemOrInformation().get(i) instanceof TTaskItem) {
                TTaskItem taskItem = (TTaskItem) tHumanTaskData
                        .getTaskItemOrInformation().get(i);
                if (taskItem.getType().compareTo(DWDLSimpleVariableType.STRING) == 0) {
                    getItemForm().addField(taskItem.getName(),
                            new TextField(taskItem.getLabel()));
                    getItemForm().getField(taskItem.getLabel()).addValidator(
                            new RegexpValidator("[a-zA-Z]",
                                    "Please enter a String!"));
                } else if (taskItem.getType().compareTo(
                        DWDLSimpleVariableType.BOOLEAN) == 0) {
                    getItemForm().addField(taskItem.getName(),
                            new CheckBox(taskItem.getLabel()));
                } else if (taskItem.getType().compareTo(
                        DWDLSimpleVariableType.DATE) == 0) {
                    getItemForm().addField(taskItem.getName(),
                            new DateField(taskItem.getLabel()));
                } else if (taskItem.getType().compareTo(
                        DWDLSimpleVariableType.FLOAT) == 0) {
                    getItemForm().addField(taskItem.getName(),
                            new TextField(taskItem.getLabel()));
                    getItemForm()
                            .getField(taskItem.getName())
                            .addValidator(
                                    new RegexpValidator(
                                            "[+]?[0-9]*\\p{.}?[0-9]*",
                                            "Please enter a valid floating point number!"));
                } else if (taskItem.getType().compareTo(
                        DWDLSimpleVariableType.INTEGER) == 0) {
                    getItemForm().addField(taskItem.getName(),
                            new TextField(taskItem.getLabel()));
                    getItemForm().getField(taskItem.getName()).addValidator(
                            new IntegerValidator("Please enter an Integer!"));
                } else if (taskItem.getType().compareTo(
                        DWDLSimpleVariableType.ANY_URI) == 0) {
                    getItemForm().addField(taskItem.getName(),
                            new TextField(taskItem.getLabel()));
                    getItemForm()
                            .getField(taskItem.getName())
                            .addValidator(
                                    new RegexpValidator(
                                            "^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?",
                                            "Please enter a valid URI!"));
                }
                getItemForm().getField(taskItem.getName()).setDescription(
                        taskItem.getHint());
                if (taskItem.getValue() != null) {
                    getItemForm().getField(taskItem.getName()).setValue(
                            taskItem.getValue());
                }
            } else {
                TInformation tInformation = (TInformation) tHumanTaskData
                        .getTaskItemOrInformation().get(i);
                getItemForm().addField(tInformation.getName(), new TextField());
                getItemForm().getField(tInformation.getName()).setValue(
                        tInformation.getContent().getAny());
            }
        }
    }

    /**
     * Returns the item {@link Form}.
     * 
     * @return itemForm TODO document
     */
    public Form getItemForm() {
        return itemForm;
    }

    /**
     * Initializes the components for the window and fills the form with the
     * information from the {@link THumanTaskData}.
     * 
     * @param tHumanTaskData
     *            TODO document
     */
    private void init(THumanTaskData tHumanTaskData, Long workItemId) {
        verticalLayout = new VerticalLayout();

        horizontalLayout = new HorizontalLayout();

        label = new Label();

        itemForm = new Form();

        fillForm(tHumanTaskData);

        okButton = new Button("OK", new SaveWorkItemAction(itemForm,
                tHumanTaskData, workItemId));
        okButton.focus();
        cancelButton = new Button("Cancel", new HideDialogWindowAction());

        markAsDoneButton = new Button("Mark as done", new SaveWorkItemAction(
                itemForm, tHumanTaskData, workItemId));

        verticalLayout.addComponent(label);

        verticalLayout.addComponent(itemForm);

        horizontalLayout.addComponent(okButton);
        horizontalLayout.addComponent(markAsDoneButton);
        horizontalLayout.addComponent(cancelButton);

        verticalLayout.addComponent(horizontalLayout);
    }
}
