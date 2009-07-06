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

package de.decidr.modelingtool.client.ui.dialogs.email;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.command.ChangeNodeModelCommand;
import de.decidr.modelingtool.client.command.CommandStack;
import de.decidr.modelingtool.client.model.EmailInvokeNodeModel;
import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.model.variable.VariableType;
import de.decidr.modelingtool.client.model.variable.VariablesFilter;
import de.decidr.modelingtool.client.ui.EmailInvokeNode;
import de.decidr.modelingtool.client.ui.dialogs.Dialog;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class EmailActivityWindow extends Dialog {

    private EmailInvokeNode node;
    private EmailInvokeNodeModel model;

    private ContentPanel contentPanel;
    private ScrollPanel scrollPanel;
    private FlexTable table;

    private ComboBox<Variable> toField;
    private ComboBox<Variable> ccField;
    private ComboBox<Variable> bccField;
    private ComboBox<Variable> subjectField;
    private ComboBox<Variable> messageField;
    private ComboBox<Variable> attachmentField;

    public EmailActivityWindow() {
        super();
        this.setLayout(new FitLayout());
        this.setSize(400, 200);
        this.setResizable(true);
        createcontentPanel();
        createButtons();
    }

    private void createcontentPanel() {
        contentPanel = new ContentPanel();

        contentPanel.setHeading(ModelingToolWidget.messages.emailActivity());
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
                        // JS implement change listener
                        changeWorkflowModel();
                        DialogRegistry.getInstance().hideDialog(
                                EmailActivityWindow.class.getName());
                    }
                }));
        addButton(new Button(ModelingToolWidget.messages.cancelButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        DialogRegistry.getInstance().hideDialog(
                                EmailActivityWindow.class.getName());
                    }
                }));
    }

    public void setNode(EmailInvokeNode node) {
        this.node = node;
        model = (EmailInvokeNodeModel) node.getModel();
    }

    private void changeWorkflowModel() {
        EmailInvokeNodeModel newModel = new EmailInvokeNodeModel(node
                .getModel().getParentModel());
        newModel.setToVariableId(toField.getValue().getId());
        if (ccField.getValue() != null) {
            newModel.setCcVariableId(ccField.getValue().getId());
        }
        if (bccField.getValue() != null) {
            newModel.setBccVariableId(bccField.getValue().getId());
        }
        newModel.setSubjectVariableId(subjectField.getValue().getId());
        if (messageField.getValue() != null) {
            newModel.setMessageVariableId(messageField.getValue().getId());
        }
        if (attachmentField.getValue() != null) {
            newModel
                    .setAttachmentVariableId(attachmentField.getValue().getId());
        }
        // JS check if changed
        CommandStack
                .getInstance()
                .executeCommand(
                        new ChangeNodeModelCommand<EmailInvokeNode, EmailInvokeNodeModel>(
                                node, newModel));
    }

    private void addComboField(ComboBox<Variable> field, String label,
            VariableType type, Long variableId) {
        field.setDisplayField(Variable.NAME);
        field.setStore(VariablesFilter.getVariablesOfType(type));
        field.setValue(VariablesFilter.getVariableById(variableId));
        field.setTypeAhead(true);
        field.setWidth("200px");
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, new Label(label));
        table.setWidget(table.getRowCount() - 1, 1, field);
        table.setWidget(table.getRowCount() - 1, 2, new Button(
                ModelingToolWidget.messages.changeValueButton(),
                new ChangeValueButtonListener(field)));
    }

    private void createFields() {
        toField = new ComboBox<Variable>();
        addComboField(toField, ModelingToolWidget.messages.toFieldLabel(),
                VariableType.STRING, model.getToVariableId());
        ccField = new ComboBox<Variable>();
        addComboField(ccField, ModelingToolWidget.messages.ccFieldLabel(),
                VariableType.STRING, model.getCcVariableId());
        bccField = new ComboBox<Variable>();
        addComboField(bccField, ModelingToolWidget.messages.bccFieldLabel(),
                VariableType.STRING, model.getBccVariableId());
        subjectField = new ComboBox<Variable>();
        addComboField(subjectField, ModelingToolWidget.messages
                .subjectFieldLabel(), VariableType.STRING, model
                .getSubjectVariableId());
        messageField = new ComboBox<Variable>();
        addComboField(messageField, ModelingToolWidget.messages
                .messageFieldLabel(), VariableType.STRING, model
                .getMessageVariableId());
        attachmentField = new ComboBox<Variable>();
        addComboField(attachmentField, ModelingToolWidget.messages
                .attachmentFieldLabel(), VariableType.FILE, model
                .getAttachmentVariableId());
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
