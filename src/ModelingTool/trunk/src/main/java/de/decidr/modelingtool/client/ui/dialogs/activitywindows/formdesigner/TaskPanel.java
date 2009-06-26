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

package de.decidr.modelingtool.client.ui.dialogs.activitywindows.formdesigner;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

import de.decidr.modelingtool.client.ModelingTool;
import de.decidr.modelingtool.client.model.Variable;
import de.decidr.modelingtool.client.model.VariableType;
import de.decidr.modelingtool.client.model.VariablesFilter;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class TaskPanel extends ContentPanel {

    private ScrollPanel taskScrollPanel;
    private FlexTable taskTable;

    public TaskPanel() {
        super();

        this.setHeading(ModelingTool.messages.taskData());
        this.setLayout(new FitLayout());

        taskTable = new FlexTable();
        taskTable.setBorderWidth(0);
        taskTable.setWidth("100%");
        taskTable.setCellPadding(2);
        taskTable.setCellSpacing(2);
        taskScrollPanel = new ScrollPanel(taskTable);
        this.add(taskScrollPanel);
    }

    public void createFields() {
        ComboBox<Variable> userField = new ComboBox<Variable>();
        userField.setDisplayField(Variable.NAME);
        userField.setStore(VariablesFilter
                .getVariablesOfType(VariableType.ROLE));
        userField.setTypeAhead(true);
        userField.setWidth("200px");
        taskTable.insertRow(taskTable.getRowCount());
        taskTable.setWidget(taskTable.getRowCount() - 1, 0, new Label(
                ModelingTool.messages.userLabel()));
        taskTable.setWidget(taskTable.getRowCount() - 1, 1, userField);

        ComboBox<Variable> formField = new ComboBox<Variable>();
        formField.setDisplayField(Variable.NAME);
        formField.setStore(VariablesFilter
                .getVariablesOfType(VariableType.FORM));
        formField.setTypeAhead(true);
        formField.setWidth("200px");
        taskTable.insertRow(taskTable.getRowCount());
        taskTable.setWidget(taskTable.getRowCount() - 1, 0, new Label(
                ModelingTool.messages.formLabel()));
        taskTable.setWidget(taskTable.getRowCount() - 1, 1, formField);

        CheckBox notifyCheckBox = new CheckBox();
        taskTable.insertRow(taskTable.getRowCount());
        taskTable.setWidget(taskTable.getRowCount() - 1, 0, new Label(
                ModelingTool.messages.notifyLabel()));
        taskTable.setWidget(taskTable.getRowCount() - 1, 1, notifyCheckBox);
    }

    public void clearAllEntries() {
        if (taskTable.getRowCount() > 0) {
            int start = taskTable.getRowCount();
            for (int i = start; i > 0; i--) {
                taskTable.removeRow(i - 1);
            }
        }
    }
}
