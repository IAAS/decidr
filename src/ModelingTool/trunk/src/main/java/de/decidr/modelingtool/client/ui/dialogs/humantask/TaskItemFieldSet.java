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

package de.decidr.modelingtool.client.ui.dialogs.humantask;

import java.util.List;

import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.model.variable.Variable;

/**
 * This container holds the references to all fields of one row of a task item
 * part of the {@link TaskItemWindow}. When the workflow is being executed a
 * human task appears as work item to the user. This container then represents
 * an input field of a specific type.
 * 
 * @author Jonas Schlaak
 */
public class TaskItemFieldSet extends HorizontalPanel {

    private TextField<String> labelField;
    private TextField<String> hintField;
    private ComboBox<Variable> variableField;

    /**
     * Constructs a field set with the given parameters.
     * 
     * @param labelField
     *            this field is for the name of the input field which later
     *            appears to the user
     * @param hintField
     *            this field is for s short hint message for the user
     * @param variableField
     *            from this field the variable can be selected into which the
     *            input from the user shall be written
     */
    public TaskItemFieldSet(TextField<String> labelField,
            TextField<String> hintField, ComboBox<Variable> variableField,
            FlexTable table, List<TaskItemFieldSet> fieldsets) {
        this.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        this.setSpacing(3);

        this.labelField = labelField;
        this.add(new Label(ModelingToolWidget.getMessages().taskItemLabel()));
        this.add(labelField);

        this.hintField = hintField;
        this.add(new Label(ModelingToolWidget.getMessages().taskItemHint()));
        this.add(hintField);

        this.variableField = variableField;
        this
                .add(new Label(ModelingToolWidget.getMessages()
                        .taskItemInputVar()));
        this.add(variableField);

        TaskItemClickHandler handler = new TaskItemClickHandler(table, this,
                fieldsets);
        PushButton button = new PushButton(ModelingToolWidget.getMessages()
                .delTaskItem(), handler);
        this.add(button);

    }

    public TextField<String> getLabelField() {
        return labelField;
    }

    public TextField<String> getHintField() {
        return hintField;
    }

    public ComboBox<Variable> getVariableField() {
        return variableField;
    }

}
