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

package de.decidr.ui.view;

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

import de.decidr.model.workflowmodel.humantask.DWDLSimpleVariableType;
import de.decidr.model.workflowmodel.humantask.THumanTaskData;
import de.decidr.model.workflowmodel.humantask.TTaskItem;
import de.decidr.ui.controller.HideDialogWindowAction;
import de.decidr.ui.controller.SaveWorkItemAction;

/**
 * This window represents a form where the work items from the user are
 * represented. Here he can enter his values for the given fields and save them.
 * 
 * @author AT
 */
public class WorkItemWindow extends Window {

    private VerticalLayout verticalLayout = null;

    private HorizontalLayout horizontalLayout = null;

    private Label label = null;

    private Form itemForm = null;

    private Button okButton = null;

    private Button cancelButton = null;

    /**
     * Constructor with a given THumanTaskData object which calls the init
     * method.
     * 
     */
    public WorkItemWindow(THumanTaskData tHumanTaskData) {
        init(tHumanTaskData);
    }

    /**
     * Initializes the components for the window and fills the form with the
     * information from the THumanTaskData.
     * 
     * @param tHumanTaskData
     */
    private void init(THumanTaskData tHumanTaskData) {
        verticalLayout = new VerticalLayout();

        horizontalLayout = new HorizontalLayout();

        label = new Label();

        itemForm = new Form();
        
        fillForm(tHumanTaskData);

        okButton = new Button("OK", new SaveWorkItemAction(itemForm, tHumanTaskData));
        cancelButton = new Button("Cancel", new HideDialogWindowAction());

        verticalLayout.addComponent(label);

        verticalLayout.addComponent(itemForm);

        horizontalLayout.addComponent(okButton);
        horizontalLayout.addComponent(cancelButton);

        verticalLayout.addComponent(horizontalLayout);

        
    }

    /**
     * Returns the item form
     * 
     * @return itemForm
     */
    public Form getItemForm() {
        return itemForm;
    }

    /**
     * Fills the form with the specific settings from the THumanTaskData. For
     * every simple type an other Field is added to the form and if necessary a
     * validator is added, so the user can't enter invalid values. Also a
     * tooltip shows the user what he has to enter in the fields. And if a value
     * already exists this value is shown.
     * 
     * @param tHumanTaskData
     */
    private void fillForm(THumanTaskData tHumanTaskData) {
        for (int i = 0; i < tHumanTaskData.getTaskItemOrInformation().size(); i++) {
            if (tHumanTaskData.getTaskItemOrInformation().get(i).getClass() == TTaskItem.class) {
                TTaskItem taskItem = (TTaskItem) tHumanTaskData
                        .getTaskItemOrInformation().get(i);
                if (taskItem.getType().compareTo(DWDLSimpleVariableType.STRING) == 0) {
                    getItemForm().addField(taskItem.getLabel(),
                            new TextField(taskItem.getLabel()));
                    getItemForm().getField(taskItem.getLabel()).addValidator(
                            new RegexpValidator("[a-zA-Z]",
                                    "Please enter a String!"));
                } else if (taskItem.getType().compareTo(
                        DWDLSimpleVariableType.BOOLEAN) == 0) {
                    getItemForm().addField(taskItem.getLabel(),
                            new CheckBox(taskItem.getLabel()));
                } else if (taskItem.getType().compareTo(
                        DWDLSimpleVariableType.DATE) == 0) {
                    getItemForm().addField(taskItem.getLabel(),
                            new DateField(taskItem.getLabel()));
                } else if (taskItem.getType().compareTo(
                        DWDLSimpleVariableType.FLOAT) == 0) {
                    getItemForm().addField(taskItem.getLabel(),
                            new TextField(taskItem.getLabel()));
                    getItemForm().getField(taskItem.getLabel()).addValidator(
                            new RegexpValidator("[+]?[0-9]*\\p{.}?[0-9]*",
                                    "Please enter a valid float number!"));
                } else if (taskItem.getType().compareTo(
                        DWDLSimpleVariableType.INTEGER) == 0) {
                    getItemForm().addField(taskItem.getLabel(),
                            new TextField(taskItem.getLabel()));
                    getItemForm().getField(taskItem.getLabel()).addValidator(
                            new IntegerValidator("Please enter an Integer!"));
                } else if (taskItem.getType().compareTo(
                        DWDLSimpleVariableType.ANY_URI) == 0) {
                    getItemForm().addField(taskItem.getLabel(),
                            new TextField(taskItem.getLabel()));
                    getItemForm()
                            .getField(taskItem.getLabel())
                            .addValidator(
                                    new RegexpValidator(
                                            "^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?",
                                            "Please enter a valid URI!"));
                }
                getItemForm().getField(taskItem.getLabel()).setDescription(
                        taskItem.getHint());
                if (taskItem.getValue() != null) {
                    getItemForm().getField(taskItem.getLabel()).setValue(
                            taskItem.getValue());
                }
            }
        }
    }

}
