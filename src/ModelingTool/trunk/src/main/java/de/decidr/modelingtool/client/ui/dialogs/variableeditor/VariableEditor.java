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
import de.decidr.modelingtool.client.model.Variable;
import de.decidr.modelingtool.client.model.VariableType;
import de.decidr.modelingtool.client.model.WorkflowModel;
import de.decidr.modelingtool.client.ui.dialogs.Dialog;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;

/**
 * TODO: add comment
 * 
 * @author JS
 */
public class VariableEditor extends Dialog {

    private ContentPanel editorPanel = new ContentPanel();

    private ToolBar toolBar = new ToolBar();

    private ListStore<Variable> variables = new ListStore<Variable>();
    private List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
    private ColumnModel colModel;
    private CellSelectionModel<Variable> csm = new CellSelectionModel<Variable>();

    public VariableEditor() {
        super();
        this.setLayout(new FitLayout());
        this.setSize(500, 500);
        this.setResizable(true);

        getVariablesfromModel();
        createEditorPanel();
        createButtons();

        this.add(editorPanel);
    }

    private void getVariablesfromModel() {
        List<Variable> variablesModel = WorkflowModel.getInstance()
                .getVariables();
        for (Variable v : variablesModel) {
            variables.add(new Variable(v.getName(), v.getType(), v.getValues()
                    .get(0)));
        }
    }

    private void createEditorPanel() {
        /* Do the layout of the panel which holds the grid */
        editorPanel.setHeading(ModelingTool.messages.editorHeading());
        editorPanel.setLayout(new FitLayout());

        /* Creating the columns and the Columns model */
        NameColumn labelColumn = new NameColumn(Variable.NAME,
                ModelingTool.messages.nameColumn());
        columns.add(labelColumn);
        TypeColumn typeColumn = new TypeColumn(Variable.TYPE,
                ModelingTool.messages.typeColumn());
        columns.add(typeColumn);
        ValueColumn valueColumn = new ValueColumn(Variable.VALUE,
                ModelingTool.messages.valueColumn());
        columns.add(valueColumn);
        ArrayVarColumn arrayVarColumn = new ArrayVarColumn(Variable.ARRAYVAR
                .toString(), ModelingTool.messages.arrayVarColumn());
        columns.add(arrayVarColumn);
        ConfigVarColumn configVarColumn = new ConfigVarColumn(
                Variable.CONFIGVAR.toString(), ModelingTool.messages
                        .configVarColumn());
        columns.add(configVarColumn);

        colModel = new ColumnModel(columns);

        /* Create grid */
        EditorGrid<Variable> grid = new EditorGrid<Variable>(variables,
                colModel);
        grid.setSelectionModel(csm);
        grid.setAutoExpandColumn(Variable.VALUE);
        grid.addPlugin(arrayVarColumn);
        grid.addPlugin(configVarColumn);

        // TODO: Remove pöhse Testdaten
        for (int i = 0; i <= 20; i++) {
            Variable var = new Variable();
            var.set("name", "Zeile " + new Integer(i).toString());
            variables.add(var);
        }
        variables.add(new Variable("Datum", VariableType.DATE, "3458398475"));

        editorPanel.add(grid);
        createToolBar(grid);

    }

    private void createToolBar(final EditorGrid<Variable> grid) {
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

        TextToolItem editValue = new TextToolItem(ModelingTool.messages
                .editValue());
        editValue.addSelectionListener(new SelectionListener<ToolBarEvent>() {
            @Override
            public void componentSelected(ToolBarEvent ce) {
                grid.stopEditing();
                ((ValueEditor) DialogRegistry.getInstance().getDialog(
                        ValueEditor.class.getName())).setContent(csm
                        .getSelectCell().model);
                DialogRegistry.getInstance().getDialog(
                        ValueEditor.class.getName()).setVisible(true);
            }
        });
        toolBar.add(editValue);

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
                        editorPanel.getParent().setVisible(false);
                    }
                }));
        addButton(new Button(ModelingTool.messages.cancelButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        variables.rejectChanges();
                        editorPanel.getParent().setVisible(false);
                    }
                }));
        addButton(new Button(ModelingTool.messages.applyButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        variables.commitChanges();
                    }
                }));
    }

    public ListStore<Variable> getVariables() {
        return variables;
    }
}
