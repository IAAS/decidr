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

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.ToolBarEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.model.humantask.FormElement;
import de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel;
import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.model.variable.VariableType;
import de.decidr.modelingtool.client.model.variable.VariablesFilter;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class HumanTaskActivityWindowContentPanel extends ContentPanel {

    /*
     * This constant holds die number of fields which are not to be removed by
     * the removeEntry method.
     */
    private static final int STATICFIELDS = 3;

    private ComboBox<Variable> userField;
    private ComboBox<Variable> formContainerField;
    private CheckBox notifyCheckBox;

    private ScrollPanel taskScrollPanel;
    private FlexTable taskTable;
    private List<FormElementFieldSet> formElementFields;

    public HumanTaskActivityWindowContentPanel() {
        super();

        this.setHeading(ModelingToolWidget.messages.humanTaskActivity());
        this.setLayout(new FitLayout());

        taskTable = new FlexTable();
        taskTable.setBorderWidth(0);
        taskTable.setWidth("100%");
        taskTable.setCellPadding(2);
        taskTable.setCellSpacing(2);
        taskScrollPanel = new ScrollPanel(taskTable);
        this.add(taskScrollPanel);

        formElementFields = new ArrayList<FormElementFieldSet>();

        createToolBar();
    }

    public void createFields(HumanTaskInvokeNodeModel model) {
        taskTable.insertRow(taskTable.getRowCount());

        userField = new ComboBox<Variable>();
        userField.setDisplayField(Variable.NAME);
        userField.setStore(VariablesFilter
                .getVariablesOfType(VariableType.ROLE));
        userField.setValue(VariablesFilter.getVariableById(model
                .getUserVariableId()));
        userField.setTypeAhead(true);
        userField.setWidth("200px");
        taskTable.setWidget(taskTable.getRowCount() - 1, 0, new Label(
                ModelingToolWidget.messages.userLabel()));
        taskTable.setWidget(taskTable.getRowCount() - 1, 1, userField);

        formContainerField = new ComboBox<Variable>();
        formContainerField.setDisplayField(Variable.NAME);
        formContainerField.setStore(VariablesFilter
                .getVariablesOfType(VariableType.FORM));
        formContainerField.setValue(VariablesFilter.getVariableById(model
                .getFormVariableId()));
        formContainerField.setTypeAhead(true);
        formContainerField.setWidth("200px");
        taskTable.insertRow(taskTable.getRowCount());
        taskTable.setWidget(taskTable.getRowCount() - 1, 0, new Label(
                ModelingToolWidget.messages.formLabel()));
        taskTable.setWidget(taskTable.getRowCount() - 1, 1, formContainerField);

        notifyCheckBox = new CheckBox();
        notifyCheckBox.setValue(model.getNotify());
        taskTable.insertRow(taskTable.getRowCount());
        taskTable.setWidget(taskTable.getRowCount() - 1, 0, new Label(
                ModelingToolWidget.messages.notifyLabel()));
        taskTable.setWidget(taskTable.getRowCount() - 1, 1, notifyCheckBox);

        createFormFields(model);
    }

    private void createFormFields(HumanTaskInvokeNodeModel model) {
        if (model.getFormElements() != null) {
            for (FormElement fe : model.getFormElements()) {
                addEntry(fe.getLabel(), fe.getVariableId());
            }
        }
    }

    /**
     * TODO: add comment
     * 
     */
    private void createToolBar() {
        ToolBar toolBar = new ToolBar();

        TextToolItem addVar = new TextToolItem(ModelingToolWidget.messages
                .addValue()); //$NON-NLS-1$
        addVar.addSelectionListener(new SelectionListener<ToolBarEvent>() {
            @Override
            public void componentSelected(ToolBarEvent ce) {
                addEntry(ModelingToolWidget.messages.newStringValue(), null);
            }
        });
        toolBar.add(addVar);

        TextToolItem delVar = new TextToolItem(ModelingToolWidget.messages
                .delValue()); //$NON-NLS-1$
        delVar.addSelectionListener(new SelectionListener<ToolBarEvent>() {
            @Override
            public void componentSelected(ToolBarEvent ce) {
                removeEntry();
            }
        });
        toolBar.add(delVar);

        this.setBottomComponent(toolBar);
    }

    private void addEntry(String label, Long variableId) {
        taskTable.insertRow(taskTable.getRowCount());

        TextField<String> labelField = new TextField<String>();
        labelField.setValue(label);
        labelField.setAutoWidth(true);
        taskTable.setWidget(taskTable.getRowCount() - 1, 0, new Label(
                ModelingToolWidget.messages.workItemLabel()));
        taskTable.setWidget(taskTable.getRowCount() - 1, 1, labelField);

        ComboBox<Variable> variableField = new ComboBox<Variable>();
        variableField.setDisplayField(Variable.NAME);
        variableField.setStore(VariablesFilter.getAllVariables());
        if (variableId != null) {
            variableField.setValue(VariablesFilter.getVariableById(variableId));
        }
        variableField.setTypeAhead(true);
        taskTable.setWidget(taskTable.getRowCount() - 1, 2, new Label(
                ModelingToolWidget.messages.workItemOutputVar()));
        taskTable.setWidget(taskTable.getRowCount() - 1, 3, variableField);

        formElementFields
                .add(new FormElementFieldSet(labelField, variableField));
    }

    private void removeEntry() {
        if (taskTable.getRowCount() > STATICFIELDS) {
            taskTable.removeRow(taskTable.getRowCount() - 1);
            formElementFields.remove(taskTable.getRowCount() - STATICFIELDS);
        }
    }

    public void clearAllEntries() {
        if (taskTable.getRowCount() > 0) {
            int start = taskTable.getRowCount();
            for (int i = start; i > 0; i--) {
                taskTable.removeRow(i - 1);
            }
        }
        if (formElementFields.size() > 0) {
            formElementFields.clear();
        }
    }

    public ComboBox<Variable> getUserField() {
        return userField;
    }

    public ComboBox<Variable> getFormContainerField() {
        return formContainerField;
    }

    public CheckBox getNotifyCheckBox() {
        return notifyCheckBox;
    }

    public List<FormElementFieldSet> getFormElementFields() {
        return formElementFields;
    }

}
