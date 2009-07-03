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

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.ToolBarEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;

import de.decidr.modelingtool.client.ModelingTool;
import de.decidr.modelingtool.client.command.ChangeVariablesCommand;
import de.decidr.modelingtool.client.command.CommandStack;
import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.dialogs.Dialog;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;
import de.decidr.modelingtool.client.ui.dialogs.variableeditor.VariableEditor;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class ValueEditor extends Dialog {

    private ContentPanel contentPanel;
    private FlexTable table;
    private ScrollPanel scrollPanel;

    private Variable variable;
    private List<TextField<String>> fields = new ArrayList<TextField<String>>();

    /**
     * TODO: add comment
     * 
     */
    public ValueEditor() {
        super();
        this.setLayout(new FitLayout());
        this.setSize(400, 200);
        this.setResizable(true);
        createContentPanel();
        createButtons();
    }

    /**
     * 
     * TODO: add comment
     * 
     */
    private void createContentPanel() {
        contentPanel = new ContentPanel();
        contentPanel.setHeading(ModelingTool.messages.editVariable());
        contentPanel.setLayout(new FitLayout());

        // TODO: fix layout
        table = new FlexTable();
        table.setBorderWidth(0);
        table.setWidth("100%");
        scrollPanel = new ScrollPanel(table);
        contentPanel.add(scrollPanel);

        createToolBar();
        this.add(contentPanel);
    }

    /**
     * TODO: add comment
     * 
     */
    private void createToolBar() {
        ToolBar toolBar = new ToolBar();

        TextToolItem addVar = new TextToolItem(ModelingTool.messages.addValue()); //$NON-NLS-1$
        addVar.addSelectionListener(new SelectionListener<ToolBarEvent>() {
            @Override
            public void componentSelected(ToolBarEvent ce) {
                addEntry(ModelingTool.messages.newStringValue());
            }
        });
        toolBar.add(addVar);

        TextToolItem delVar = new TextToolItem(ModelingTool.messages.delValue()); //$NON-NLS-1$
        delVar.addSelectionListener(new SelectionListener<ToolBarEvent>() {
            @Override
            public void componentSelected(ToolBarEvent ce) {
                removeEntry();
            }
        });
        toolBar.add(delVar);

        contentPanel.setBottomComponent(toolBar);
    }

    /**
     * TODO: add comment
     * 
     */
    private void createButtons() {
        setButtonAlign(HorizontalAlignment.CENTER);
        addButton(new Button(ModelingTool.messages.okButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        changeWorkflowModel();
                        /*
                         * Refresh of the variable editor needed so that the
                         * displayed values are updated
                         */
                        DialogRegistry.getInstance().getDialog(
                                VariableEditor.class.getName()).refresh();
                        DialogRegistry.getInstance().hideDialog(
                                ValueEditor.class.getName());
                    }

                }));
        addButton(new Button(ModelingTool.messages.cancelButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        DialogRegistry.getInstance().hideDialog(
                                ValueEditor.class.getName());
                    }
                }));
    }

    private void changeWorkflowModel() {
        List<String> newValues = new ArrayList<String>();
        for (TextField<String> field : fields) {
            newValues.add(field.getValue());
        }
        /*
         * Check if the variable is already in the workflow model. If that is
         * the case, it means the value editor was called outside of the
         * variable editor (for example, from an email activity. Therefore any
         * changes have to be pushed in to the command stack. If the variable is
         * not in the workflow model, it means that the variable is a reference
         * to an element in the list store of the variable editor.
         */
        // JS check if changed
        if (Workflow.getInstance().getModel().getVariables().contains(variable)) {
            Variable newVariable = variable.copy();
            newVariable.setValues(newValues);
            CommandStack.getInstance().executeCommand(
                    new ChangeVariablesCommand(newVariable));
        } else {
            variable.setValues(newValues);
        }
    }

    private void addEntry(String fieldContent) {
        TextField<String> text = new TextField<String>();
        text.setValue(fieldContent);
        text.setAutoWidth(true);
        fields.add(text);
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, text);
    }

    private void removeEntry() {
        table.removeRow(table.getRowCount() - 1);
        fields.remove(table.getRowCount());
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

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.ui.dialogs.Dialog#initialize()
     */
    @Override
    public void initialize() {
        for (String s : variable.getValues()) {
            addEntry(new String(s));
        }
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
        // TODO Auto-generated method stub
    }
}
