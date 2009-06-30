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

package de.decidr.modelingtool.client.ui.dialogs.variableeditor;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.ToolBarEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.CellSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

import de.decidr.modelingtool.client.ModelingTool;
import de.decidr.modelingtool.client.command.ChangeVariablesCommand;
import de.decidr.modelingtool.client.command.CommandStack;
import de.decidr.modelingtool.client.model.Variable;
import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.dialogs.Dialog;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;
import de.decidr.modelingtool.client.ui.dialogs.valueeditor.ValueEditor;
import de.decidr.modelingtool.client.ui.dialogs.valueeditor.ValueEditorInvoker;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class VariableEditor extends Dialog {

    private ContentPanel editorPanel;
    private ToolBar toolBar;

    private ListStore<Variable> variables = new ListStore<Variable>();
    private VariableEditorStoreListener listener = new VariableEditorStoreListener();
    private List<ColumnConfig> columns;
    private ColumnModel columnModel;
    private CellSelectionModel<Variable> csm;
    private EditorGrid<Variable> grid;

    public VariableEditor() {
        super();
        this.setLayout(new FitLayout());
        this.setSize(500, 500);
        this.setResizable(true);
        createEditorPanel();
        createButtons();
        variables.addStoreListener(listener);
    }

    private void createEditorPanel() {
        editorPanel = new ContentPanel();

        /* Do the layout of the panel which holds the grid */
        editorPanel.setHeading(ModelingTool.messages.editorHeading());
        editorPanel.setLayout(new FitLayout());

        /* Creating the columns and the Columns model */
        columns = new ArrayList<ColumnConfig>();
        NameColumn nameColumn = new NameColumn(Variable.NAME,
                ModelingTool.messages.nameColumn());
        columns.add(nameColumn);
        TypeColumn typeColumn = new TypeColumn(Variable.TYPE,
                ModelingTool.messages.typeColumn());
        columns.add(typeColumn);
        ValueColumn valueColumn = new ValueColumn(Variable.VALUE,
                ModelingTool.messages.valueColumn());
        columns.add(valueColumn);
        ArrayVarColumn arrayVarColumn = new ArrayVarColumn(Variable.ARRAYVAR,
                ModelingTool.messages.arrayVarColumn());
        columns.add(arrayVarColumn);
        ConfigVarColumn configVarColumn = new ConfigVarColumn(
                Variable.CONFIGVAR, ModelingTool.messages.configVarColumn());
        columns.add(configVarColumn);

        columnModel = new ColumnModel(columns);

        /* Create grid */
        grid = new EditorGrid<Variable>(variables, columnModel);
        csm = new CellSelectionModel<Variable>();
        grid.setSelectionModel(csm);
        grid.setAutoExpandColumn(Variable.VALUE);
        grid.addPlugin(arrayVarColumn);
        grid.addPlugin(configVarColumn);

        editorPanel.add(grid);
        createToolBar();
        this.add(editorPanel);
    }

    private void createToolBar() {
        toolBar = new ToolBar();

        TextToolItem addVar = new TextToolItem(ModelingTool.messages
                .addVariable()); //$NON-NLS-1$
        addVar.addSelectionListener(new SelectionListener<ToolBarEvent>() {
            @Override
            public void componentSelected(ToolBarEvent ce) {
                Variable var = new Variable();
                grid.stopEditing();
                variables.insert(var, 0);
                grid.startEditing(0, 0);
            }
        });
        toolBar.add(addVar);

        TextToolItem delVar = new TextToolItem(ModelingTool.messages
                .delVariable()); //$NON-NLS-1$
        delVar.addSelectionListener(new SelectionListener<ToolBarEvent>() {
            @Override
            public void componentSelected(ToolBarEvent ce) {
                grid.stopEditing();
                variables.remove(csm.getSelectCell().model);
            }
        });
        toolBar.add(delVar);

        TextToolItem editVar = new TextToolItem(ModelingTool.messages
                .editVariable());
        editVar.addSelectionListener(new SelectionListener<ToolBarEvent>() {
            @Override
            public void componentSelected(ToolBarEvent ce) {
                grid.stopEditing();
                ValueEditorInvoker.invoke(csm.getSelectCell().model);
                DialogRegistry.getInstance().showDialog(
                        ValueEditor.class.getName());
            }
        });
        toolBar.add(editVar);

        editorPanel.setBottomComponent(toolBar);
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
                        if (listener.hasDataChanged()) {
                            variables.commitChanges();
                            putVariablesToModel();
                        }
                        DialogRegistry.getInstance().hideDialog(
                                VariableEditor.class.getName());
                    }
                }));
        addButton(new Button(ModelingTool.messages.cancelButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        variables.rejectChanges();
                        DialogRegistry.getInstance().hideDialog(
                                VariableEditor.class.getName());
                    }
                }));
    }

    /**
     * 
     * TODO: add comment
     * 
     */
    private void getVariablesFromModel() {
        variables.removeAll();
        List<Variable> variablesModel = Workflow.getInstance().getModel()
                .getVariables();
        for (Variable v : variablesModel) {
            Variable targetVar = new Variable();
            targetVar.setId(v.getId());
            targetVar.setName(v.getName());
            targetVar.setType(v.getType());
            targetVar.setValues(v.getValues());
            targetVar.setConfig(v.isConfig());
            variables.add(targetVar);
        }
    }

    /**
     * 
     * TODO: add comment
     * 
     */
    private void putVariablesToModel() {
        List<Variable> variablesModel = new ArrayList<Variable>();
        for (int i = 0; i < variables.getCount(); i++) {
            Variable v = variables.getAt(i);
            Variable targetVar = new Variable();
            targetVar.setId(v.getId());
            targetVar.setName(v.getName());
            targetVar.setType(v.getType());
            targetVar.setValues(v.getValues());
            targetVar.setConfig(v.isConfig());
            variablesModel.add(targetVar);
        }
        CommandStack.getInstance().executeCommand(
                new ChangeVariablesCommand(variablesModel));
    }

    /**
     * 
     * TODO: add comment
     * 
     * @return
     */
    public ListStore<Variable> getVariables() {
        return variables;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.ui.dialogs.Dialog#initialize()
     */
    @Override
    public void initialize() {
        getVariablesFromModel();
        listener.setDataChanged(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.ui.dialogs.Dialog#reset()
     */
    @Override
    public void reset() {
        // TODO: write method
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.ui.dialogs.Dialog#refresh()
     */
    @Override
    public void refresh() {
        if (grid != null && csm != null && csm.getSelectCell() != null
                && csm.getSelectCell().model != null) {
            grid.getStore().update(csm.getSelectCell().model);
            grid.getView().refresh(false);
        }
    }

}
