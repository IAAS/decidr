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
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.command.ChangeVariablesCommand;
import de.decidr.modelingtool.client.command.CommandStack;
import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.ui.dialogs.ModelingToolDialog;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;
import de.decidr.modelingtool.client.ui.dialogs.valueeditor.ValueEditorWindowInvoker;

/**
 * Editor for {@link Variable Variables}. Variables can be added, edited and
 * deleted. The Editor consists of a {@link Grid} with four columns: A
 * {@link NameColumn}, a {@link TypeColumn}, a {@link ValueColumn} and a
 * {@link ConfigVarColumn}.
 * 
 * @author Jonas Schlaak
 */
public class VariableEditorWindow extends ModelingToolDialog {

    private ContentPanel editorPanel;
    private ToolBar toolBar;

    private ListStore<Variable> variables = new ListStore<Variable>();
    private VariableEditorStoreListener listener = new VariableEditorStoreListener();
    private List<ColumnConfig> columns;
    private ColumnModel columnModel;
    private CellSelectionModel<Variable> csm;
    private EditorGrid<Variable> grid;

    public VariableEditorWindow() {
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
        editorPanel
                .setHeading(ModelingToolWidget.getMessages().editorHeading());
        editorPanel.setLayout(new FitLayout());

        /* Creating the columns and the Columns model */
        columns = new ArrayList<ColumnConfig>();
        NameColumn nameColumn = new NameColumn(Variable.LABEL,
                ModelingToolWidget.getMessages().nameColumn());
        columns.add(nameColumn);
        TypeColumn typeColumn = new TypeColumn(Variable.TYPE,
                ModelingToolWidget.getMessages().typeColumn());
        columns.add(typeColumn);
        ValueColumn valueColumn = new ValueColumn(Variable.VALUE,
                ModelingToolWidget.getMessages().valueColumn());
        columns.add(valueColumn);
        ConfigVarColumn configVarColumn = new ConfigVarColumn(
                Variable.CONFIGVAR, ModelingToolWidget.getMessages()
                        .configVarColumn());
        columns.add(configVarColumn);

        columnModel = new ColumnModel(columns);

        /* Create grid */
        grid = new EditorGrid<Variable>(variables, columnModel);
        csm = new CellSelectionModel<Variable>();
        grid.setSelectionModel(csm);
        grid.setAutoExpandColumn(Variable.VALUE);
        grid.addPlugin(configVarColumn);

        editorPanel.add(grid);
        createToolBar();
        this.add(editorPanel);
    }

    private void createToolBar() {
        toolBar = new ToolBar();

        TextToolItem newVar = new TextToolItem(ModelingToolWidget.getMessages()
                .newVariable()); //$NON-NLS-1$
        newVar.addSelectionListener(new SelectionListener<ToolBarEvent>() {
            @Override
            public void componentSelected(ToolBarEvent ce) {
                grid.stopEditing();
                NewVariableWindowInvoker.invoke(variables);
            }
        });
        toolBar.add(newVar);

        TextToolItem delVar = new TextToolItem(ModelingToolWidget.getMessages()
                .delVariable()); //$NON-NLS-1$
        delVar.addSelectionListener(new SelectionListener<ToolBarEvent>() {
            @Override
            public void componentSelected(ToolBarEvent ce) {
                grid.stopEditing();
                variables.remove(csm.getSelectCell().model);
            }
        });
        toolBar.add(delVar);

        TextToolItem editVar = new TextToolItem(ModelingToolWidget
                .getMessages().editVariable());
        editVar.addSelectionListener(new SelectionListener<ToolBarEvent>() {
            @Override
            public void componentSelected(ToolBarEvent ce) {
                grid.stopEditing();
                Variable variable = csm.getSelectCell().model;
                ValueEditorWindowInvoker.invoke(variable);
            }
        });
        toolBar.add(editVar);

        editorPanel.setBottomComponent(toolBar);
    }

    /**
     * Creates ok and cancel button
     */
    private void createButtons() {
        setButtonAlign(HorizontalAlignment.CENTER);
        addButton(new Button(ModelingToolWidget.getMessages().okButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        if (listener.hasDataChanged()) {
                            variables.commitChanges();
                            changeWorkflowModel();
                        }
                        DialogRegistry.getInstance().hideDialog(
                                VariableEditorWindow.class.getName());
                    }
                }));
        addButton(new Button(ModelingToolWidget.getMessages().cancelButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        variables.rejectChanges();
                        DialogRegistry.getInstance().hideDialog(
                                VariableEditorWindow.class.getName());
                    }
                }));
    }

    /**
     * Sets the list of variable to edit
     */
    public void setVariables(List<Variable> variablesModel) {
        for (Variable var : variablesModel) {
            variables.add(var.copy());
        }
    }

    private void changeWorkflowModel() {
        List<Variable> variablesModel = new ArrayList<Variable>();
        for (int i = 0; i < variables.getCount(); i++) {
            variablesModel.add(variables.getAt(i).copy());
        }
        CommandStack.getInstance().executeCommand(
                new ChangeVariablesCommand(variablesModel));
    }

    /**
     * Gets the list of variables that are currently edited.
     * 
     * @return the variables
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
    public Boolean initialize() {
        listener.setDataChanged(false);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.ui.dialogs.Dialog#reset()
     */
    @Override
    public void reset() {
        variables.removeAll();
    }

    /**
     *This method updates the view when a model change occurred
     */
    public void refresh() {
        if (grid != null && csm != null && csm.getSelectCell() != null
                && csm.getSelectCell().model != null) {
            grid.getStore().update(csm.getSelectCell().model);
            grid.getView().refresh(false);
        }
    }

}
