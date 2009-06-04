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

package main.java.de.decidr.modelingtool.client.ui.dialogs.variableeditor;

import java.util.ArrayList;
import java.util.List;

import main.java.de.decidr.modelingtool.client.model.Variable;
import main.java.de.decidr.modelingtool.client.model.VariableType;
import main.java.de.decidr.modelingtool.client.ui.dialogs.Dialog;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

/**
 * TODO: add comment
 * 
 * @author JS
 */
public class VariableEditor extends Dialog {

	private ContentPanel editorPanel = new ContentPanel();
	private List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
	private ColumnModel colModel;

	public VariableEditor() {
		super();
		this.setLayout(new BorderLayout());
		createEditorPanel();

	}

	private void createEditorPanel() {

		// TODO: Localization
		LabelColumn labelColumn = new LabelColumn("name", "Variablename");
		columns.add(labelColumn);
		
		TypeColumn typeColumn = new TypeColumn("type", "Variabletype");
		columns.add(typeColumn);
		
		ValueColumn valueColumn = new ValueColumn("value", "Value");
		columns.add(valueColumn);
		
		colModel = new ColumnModel(columns);
		
		final ListStore<Variable> variables = new ListStore<Variable>();
		variables.add(new Variable("Name", VariableType.STRING, "Hans Peter"));
		
		editorPanel.setHeading("Variable Editor");  
		editorPanel.setFrame(true);  
		editorPanel.setSize(600, 300);  
		editorPanel.setLayout(new FitLayout());
		
		final EditorGrid<Variable> grid = new EditorGrid<Variable>(variables, colModel);
		editorPanel.add(grid);
		this.add(editorPanel);
		
	}



}
