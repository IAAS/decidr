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

package de.decidr.modelingtool.client.ui.dialogs.valueeditor;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.ToolBarEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.command.ChangeVariablesCommand;
import de.decidr.modelingtool.client.command.CommandStack;
import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.model.variable.VariableType;
import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.dialogs.ModelingToolDialog;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;
import de.decidr.modelingtool.client.ui.dialogs.variableeditor.VariableEditor;

/**
 * A dialog that is for editing the values of a {@link Variable} of all types,
 * except for role (see {@link RoleEditor}).
 * 
 * @author Jonas Schlaak
 */
public class ValueEditor extends ModelingToolDialog {

    private ContentPanel contentPanel;
    private FlexTable table;
    private ScrollPanel scrollPanel;

    private Variable variable;
    private List<TextField<String>> fields = new ArrayList<TextField<String>>();

    /**
     * Default constructor that sets the layout.
     */
    public ValueEditor() {
        super();
        this.setLayout(new FitLayout());
        this.setSize(500, 300);
        this.setResizable(true);
        createContentPanel();
        createButtons();
    }

    /**
     * Creates the content panel for the dialog. The content panel holds the
     * text fields for the values and a toolbar.
     */
    private void createContentPanel() {
        contentPanel = new ContentPanel();
        contentPanel.setHeading(ModelingToolWidget.messages.editVariable());
        contentPanel.setLayout(new FitLayout());

        table = new FlexTable();
        table.setBorderWidth(0);
        table.setWidth("100%");
        scrollPanel = new ScrollPanel(table);
        contentPanel.add(scrollPanel);

        createToolBar();
        this.add(contentPanel);
    }

    /**
     * Creates a toolbar that has two buttons to add or delete a value.
     */
    private void createToolBar() {
        ToolBar toolBar = new ToolBar();

        TextToolItem addValue = new TextToolItem(ModelingToolWidget.messages
                .addValue()); //$NON-NLS-1$
        addValue.addSelectionListener(new SelectionListener<ToolBarEvent>() {
            @Override
            public void componentSelected(ToolBarEvent ce) {
                addEntry(variable.getType().getDefaultValue());
            }
        });
        toolBar.add(addValue);

        contentPanel.setBottomComponent(toolBar);
    }

    /**
     * Creates ok and cancel button
     */
    private void createButtons() {
        setButtonAlign(HorizontalAlignment.CENTER);
        addButton(new Button(ModelingToolWidget.messages.okButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        List<String> newValues = new ArrayList<String>();
                        for (TextField<String> field : fields) {
                            newValues.add(field.getValue());
                        }
                        ValueValidatorCallback call = new ValueValidatorCallback();
                        ValueValidator validator = new ValueValidator(
                                newValues, variable.getType());
                        if (validator.validate(call)) {
                            changeWorkflowModel(newValues);
                            /*
                             * Refresh of the variable editor needed so that the
                             * displayed values are updated
                             */
                            DialogRegistry.getInstance().getDialog(
                                    VariableEditor.class.getName()).refresh();
                            DialogRegistry.getInstance().hideDialog(
                                    ValueEditor.class.getName());
                        } else {
                            MessageBox.alert(ModelingToolWidget.messages
                                    .warningTitle(), call.getMessage(), null);
                        }
                    }

                }));
        addButton(new Button(ModelingToolWidget.messages.cancelButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        DialogRegistry.getInstance().hideDialog(
                                ValueEditor.class.getName());
                    }
                }));
    }

    private void changeWorkflowModel(List<String> newValues) {
        /*
         * Check if the variable is already in the workflow model. If that is
         * the case, it means the value editor was called outside of the
         * variable editor (for example, from an email activity. Therefore any
         * changes have to be pushed in to the command stack. If the variable is
         * not in the workflow model, it means that the variable is a reference
         * to an element in the list store of the variable editor.
         */
        if (Workflow.getInstance().getModel().getVariables().contains(variable)) {
            if (variable.getValues().equals(newValues)) {
                Variable newVariable = variable.copy();
                newVariable.setValues(newValues);
                CommandStack.getInstance().executeCommand(
                        new ChangeVariablesCommand(newVariable));
            }
        } else {
            variable.setValues(newValues);
        }
    }

    private void addEntry(String fieldContent) {
        final TextField<String> text = new TextField<String>();
        text.setValue(fieldContent);
        text.setAutoWidth(true);
        text.setValidator(new FieldValidator(variable.getType()));

        /* Add key listener to start the validator whenever a key is pressed */
        text.addKeyListener(new KeyListener() {
            /*
             * (non-Javadoc)
             * 
             * @see
             * com.extjs.gxt.ui.client.event.KeyListener#componentKeyUp(com.
             * extjs.gxt.ui.client.event.ComponentEvent)
             */
            @Override
            public void componentKeyUp(ComponentEvent event) {
                text.clearInvalid();
                text.validate();
            }
        });

        addInputHelpers(text, variable.getType());

        fields.add(text);
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, new ValueFieldWidget(table, text,
                fields));
    }

    /**
     * Adds an input helper to the text fields that is displayed whenever the
     * text field is focused. An input helper is, for example, a date picker
     * widget.
     * 
     * @param text
     *            the text field the input helper should be added to
     * @param type
     *            the type of the values to be entered in the text field
     */
    private void addInputHelpers(final TextField<String> text, VariableType type) {
        if (type == VariableType.DATE) {
            text.addListener(Events.Focus, new Listener<ComponentEvent>() {

                @Override
                public void handleEvent(ComponentEvent be) {
                    DatePickerWindow dpw = new DatePickerWindow(text);
                    dpw.showAt(text.getAbsoluteLeft(), text.getAbsoluteTop());
                }

            });
        }
    }

    private void clearAllEntries() {
        if (table.getRowCount() > 0) {
            int start = table.getRowCount();
            for (int i = start; i > 0; i--) {
                table.removeRow(i - 1);
            }
        }
        if (fields.size() > 0) {
            fields.clear();
        }
    }

    /**
     * Sets the variable which values are to be edited.
     * 
     * @param variable
     *            the variable
     */
    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.ui.dialogs.Dialog#initialize()
     */
    @Override
    public Boolean initialize() {
        for (String value : variable.getValues()) {
            addEntry(new String(value));
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.ui.dialogs.Dialog#reset()
     */
    @Override
    public void reset() {
        clearAllEntries();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.ui.dialogs.Dialog#refresh()
     */
    @Override
    public void refresh() {

    }
}
