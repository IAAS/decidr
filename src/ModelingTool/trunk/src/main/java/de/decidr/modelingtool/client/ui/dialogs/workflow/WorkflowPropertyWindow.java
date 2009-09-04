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

package de.decidr.modelingtool.client.ui.dialogs.workflow;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.command.ChangeWorkflowPropertiesCommand;
import de.decidr.modelingtool.client.command.CommandStack;
import de.decidr.modelingtool.client.model.WorkflowModel;
import de.decidr.modelingtool.client.model.WorkflowProperties;
import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.model.variable.VariableType;
import de.decidr.modelingtool.client.model.variable.VariablesFilter;
import de.decidr.modelingtool.client.ui.dialogs.Dialog;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class WorkflowPropertyWindow extends Dialog {

    private WorkflowModel model;

    private ContentPanel contentPanel;
    private ScrollPanel scrollPanel;
    private FlexTable table;

    private ComboBox<Variable> recipientField;
    private ComboBox<Variable> faultMessageField;
    private ComboBox<Variable> successMessageField;
    private CheckBox notifyBox;

    /**
     * 
     * TODO: add comment
     * 
     */
    public WorkflowPropertyWindow() {
        super();
        this.setLayout(new FitLayout());
        this.setSize(400, 200);
        this.setResizable(true);
        createcontentPanel();
        createButtons();
    }

    private void createcontentPanel() {
        contentPanel = new ContentPanel();

        contentPanel.setHeading(ModelingToolWidget.messages.workflowProperty());
        contentPanel.setLayout(new FitLayout());

        // TODO: fix layout
        table = new FlexTable();
        table.setBorderWidth(0);
        table.setWidth("100%");
        table.setCellPadding(2);
        table.setCellSpacing(2);
        scrollPanel = new ScrollPanel(table);
        contentPanel.add(scrollPanel);

        this.add(contentPanel);
    }

    private void createButtons() {
        setButtonAlign(HorizontalAlignment.CENTER);
        addButton(new Button(ModelingToolWidget.messages.okButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        changeWorkflowModel();
                        DialogRegistry.getInstance().hideDialog(
                                WorkflowPropertyWindow.class.getName());
                    }
                }));
        addButton(new Button(ModelingToolWidget.messages.cancelButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        DialogRegistry.getInstance().hideDialog(
                                WorkflowPropertyWindow.class.getName());
                    }
                }));
    }

    public void setModel(WorkflowModel workflow) {
        model = workflow;
    }

    private void changeWorkflowModel() {
        WorkflowProperties newProperties = new WorkflowProperties();
        newProperties.setRecipientVariableId(recipientField.getValue().getId());
        newProperties.setFaultMessageVariableId(faultMessageField.getValue()
                .getId());
        if (successMessageField.getValue() != null) {
            newProperties.setSuccessMessageVariableId(successMessageField
                    .getValue().getId());
        }
        newProperties.setNotifyOnSuccess(notifyBox.getValue());
        // JS check if changed
        CommandStack.getInstance().executeCommand(
                new ChangeWorkflowPropertiesCommand(newProperties));
    }

    private void addComboField(ComboBox<Variable> field, String label,
            VariableType type, Long variableId) {
        field.setDisplayField(Variable.LABEL);
        field.setStore(VariablesFilter.getVariablesOfType(type));
        field.setValue(VariablesFilter.getVariableById(variableId));
        field.setTypeAhead(true);
        field.setWidth("200px");
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, new Label(label));
        table.setWidget(table.getRowCount() - 1, 1, field);
    }

    private void createFields() {
        recipientField = new ComboBox<Variable>();
        addComboField(recipientField, ModelingToolWidget.messages
                .recipientFieldLabel(), VariableType.ROLE, model
                .getProperties().getRecipientVariableId());
        faultMessageField = new ComboBox<Variable>();
        addComboField(faultMessageField, ModelingToolWidget.messages
                .faultMessageFieldLabel(), VariableType.STRING, model
                .getProperties().getFaultMessageVariableId());
        successMessageField = new ComboBox<Variable>();
        addComboField(successMessageField, ModelingToolWidget.messages
                .successMessageFieldLabel(), VariableType.STRING, model
                .getProperties().getSuccessMessageVariableId());
        notifyBox = new CheckBox();
        notifyBox.setValue(model.getProperties().getNotifyOnSuccess());
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, new Label(
                ModelingToolWidget.messages.notifyCheckBox()));
        table.setWidget(table.getRowCount() - 1, 1, notifyBox);
    }

    private void clearAllEntries() {
        if (table.getRowCount() > 0) {
            int start = table.getRowCount();
            for (int i = start; i > 0; i--) {
                table.removeRow(i - 1);
            }
        }
    }

    @Override
    public void initialize() {
        createFields();
    }

    @Override
    public void reset() {
        clearAllEntries();
    }

    @Override
    public void refresh() {
        // TODO Auto-generated method stub

    }
}