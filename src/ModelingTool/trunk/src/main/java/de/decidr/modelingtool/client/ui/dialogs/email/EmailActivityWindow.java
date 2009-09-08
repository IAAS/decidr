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
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.command.ChangeNodePropertiesCommand;
import de.decidr.modelingtool.client.command.CommandStack;
import de.decidr.modelingtool.client.model.EmailInvokeNodeModel;
import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.model.variable.VariableType;
import de.decidr.modelingtool.client.model.variable.VariablesFilter;
import de.decidr.modelingtool.client.ui.EmailInvokeNode;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;
import de.decidr.modelingtool.client.ui.dialogs.ModelingToolDialog;

/**
 * The property window for a {@link EmailInvokeNode}. It consists of six
 * comboboxes, each enabling to select the variable for the property. The
 * properties are: to, cc, bcc, subject, message and attachments.
 * 
 * @author Jonas Schlaak
 */
public class EmailActivityWindow extends ModelingToolDialog {

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
        this.setSize(450, 250);
        this.setResizable(true);
        createContentPanel();
        createButtons();
    }

    /**
     * Creates a {@link ContentPanel} which holds a {@link FlexTable} to which
     * the comboboxes are added.
     */
    private void createContentPanel() {
        contentPanel = new ContentPanel();

        contentPanel.setHeading(ModelingToolWidget.messages.emailActivity());
        contentPanel.setLayout(new FitLayout());

        table = new FlexTable();
        table.setBorderWidth(0);
        table.setWidth("100%");
        table.setCellPadding(2);
        table.setCellSpacing(2);
        scrollPanel = new ScrollPanel(table);
        contentPanel.add(scrollPanel);

        this.add(contentPanel);
    }

    /**
     * Creates the ok and cancel button.
     */
    private void createButtons() {
        setButtonAlign(HorizontalAlignment.CENTER);
        addButton(new Button(ModelingToolWidget.messages.okButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        /*
                         * check if the inputs are valid. If not, display a
                         * warning message, else change the workflow model.
                         */
                        if (validateInputs()) {
                            changeWorkflowModel();
                            DialogRegistry.getInstance().hideDialog(
                                    EmailActivityWindow.class.getName());
                        } else {
                            MessageBox.alert(ModelingToolWidget.messages
                                    .warningTitle(),
                                    ModelingToolWidget.messages
                                            .emailActivityWarning(), null);
                        }
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

    /**
     * Sets the {@link EmailInvokeNode} whose properties are to be modeled with
     * this window.
     * 
     * @param node
     *            the EmailInvokeNode
     */
    public void setNode(EmailInvokeNode node) {
        this.node = node;
        model = (EmailInvokeNodeModel) node.getModel();
    }

    private boolean validateInputs() {
        Boolean result = false;
        /* only the to and the subject fields are not allowed to be null. */
        if (toField.getValue() != null && subjectField.getValue() != null) {
            result = true;
        }
        return result;
    }

    /**
     * Retrieves the values from all fields. Creates and executes a
     * {@link ChangeNodePropertiesCommand} with the new values.
     */
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
        /* only push to command stack if changes where made */
        if (newModel.getProperties().equals(model.getProperties()) == false) {
            CommandStack.getInstance().executeCommand(
                    new ChangeNodePropertiesCommand(node, newModel
                            .getProperties()));
        }
    }

    /**
     * Adds a combobox to the window's table. The combobox is initialized which
     * the value from the workflow model.
     * 
     * @param field
     *            the combobox to be added
     * @param label
     *            the label of the combobox
     * @param type
     *            the variable type of the variables that the combobox displays
     * @param variableId
     *            the id of the variable to which the combobox is set to
     */
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
        table.setWidget(table.getRowCount() - 1, 2, new Button(
                ModelingToolWidget.messages.changeValueButton(),
                new ChangeValueButtonListener(field)));
    }

    /**
     * Creates the comboboxes and adds them to the window's table.
     */
    private void createFields() {
        toField = new ComboBox<Variable>();
        addComboField(toField, ModelingToolWidget.messages.toFieldLabel(),
                VariableType.ROLE, model.getToVariableId());
        ccField = new ComboBox<Variable>();
        addComboField(ccField, ModelingToolWidget.messages.ccFieldLabel(),
                VariableType.ROLE, model.getCcVariableId());
        bccField = new ComboBox<Variable>();
        addComboField(bccField, ModelingToolWidget.messages.bccFieldLabel(),
                VariableType.ROLE, model.getBccVariableId());
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

    /**
     * Removes the comboboxes from the table so that they can be readded on the
     * next call of the EmailActivityWindow.
     */
    private void clearAllEntries() {
        if (table.getRowCount() > 0) {
            int start = table.getRowCount();
            for (int i = start; i > 0; i--) {
                table.removeRow(i - 1);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.modelingtool.client.ui.dialogs.ModelingToolDialog#initialize()
     */
    @Override
    public void initialize() {
        createFields();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.ui.dialogs.ModelingToolDialog#reset()
     */
    @Override
    public void reset() {
        clearAllEntries();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.modelingtool.client.ui.dialogs.ModelingToolDialog#refresh()
     */
    @Override
    public void refresh() {

    }
}
