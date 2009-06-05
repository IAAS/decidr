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
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.ToolBarEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

import de.decidr.modelingtool.client.model.Variable;
import de.decidr.modelingtool.client.model.VariableType;
import de.decidr.modelingtool.client.model.WorkflowModel;
import de.decidr.modelingtool.client.ui.dialogs.Dialog;

/**
 * TODO: add comment
 * 
 * @author JS
 */
public class VariableEditor extends Dialog {

    private ListStore<Variable> variables = new ListStore<Variable>();
    private ToolBar toolBar = new ToolBar();
    private ContentPanel editorPanel = new ContentPanel();
    private List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
    private ColumnModel colModel;
    private EditorGrid<Variable> grid;

    public VariableEditor() {
        super();
        this.setLayout(new BorderLayout());
        getVariablesfromModel();

        createEditorPanel();
        createToolBar();
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

    private void createToolBar() {
        TextToolItem addVar = new TextToolItem("Add Variable");
        addVar.addSelectionListener(new SelectionListener<ToolBarEvent>() {
            @Override
            public void componentSelected(ToolBarEvent ce) {
                Variable var = new Variable();
            }
        });
        toolBar.add(addVar);

        TextToolItem delVar = new TextToolItem("Del Variable");
        delVar.addSelectionListener(new SelectionListener<ToolBarEvent>() {
            @Override
            public void componentSelected(ToolBarEvent ce) {
                // TODO Auto-generated method stub
            }
        });
        toolBar.add(delVar);

        editorPanel.setBottomComponent(toolBar);
    }

    private void createEditorPanel() {

        // TODO: Localization
        /* First step: Creating the columns and the Columns model */
        LabelColumn labelColumn = new LabelColumn("name", "Variablename");
        columns.add(labelColumn);

        TypeColumn typeColumn = new TypeColumn("type", "Variabletype");
        columns.add(typeColumn);

        ValueColumn valueColumn = new ValueColumn("value", "Value");
        columns.add(valueColumn);

        colModel = new ColumnModel(columns);

        editorPanel.setHeading("Variable Editor");
        editorPanel.setFrame(true);
        editorPanel.setSize(600, 300);
        editorPanel.setLayout(new FitLayout());

        editorPanel.add(grid);
        grid = new EditorGrid<Variable>(variables, colModel);
    }

}
