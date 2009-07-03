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

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;

import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.model.variable.VariableType;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class TypeColumn extends ColumnConfig {

    private SimpleComboBox<String> typeComboBox = new SimpleComboBox<String>();
    private CellEditor comboBoxCellEditor;

    public TypeColumn(String columnId, String header) {
        this.setId(columnId);
        this.setHeader(header);
        this.setWidth(60);

        GridCellRenderer<Variable> cellRenderer = new GridCellRenderer<Variable>() {
            @Override
            public String render(Variable model, String property,
                    ColumnData config, int rowIndex, int colIndex,
                    ListStore<Variable> store) {
                return model.getType().getLocalName();
            }
        };
        this.setRenderer(cellRenderer);

        for (VariableType type : VariableType.values()) {
            typeComboBox.add(type.getLocalName());
        }
        typeComboBox.setEditable(false);

        comboBoxCellEditor = new CellEditor(typeComboBox) {
            /*
             * (non-Javadoc)
             * 
             * @see
             * com.extjs.gxt.ui.client.widget.Editor#preProcessValue(java.lang
             * .Object)
             */
            @Override
            public Object preProcessValue(Object value) {
                // TODO Auto-generated method stub
                return typeComboBox.findModel(value.toString());
            }

            /*
             * (non-Javadoc)
             * 
             * @see
             * com.extjs.gxt.ui.client.widget.Editor#postProcessValue(java.lang
             * .Object)
             */
            @Override
            public Object postProcessValue(Object value) {
                // TODO Auto-generated method stub
                Object result;
                if (value == null) {
                    result = VariableType.STRING;
                } else {
                    result = VariableType
                            .getTypeFromLocalName((String) ((ModelData) value)
                                    .get("value"));
                }
                return result;
            }
        };
        this.setEditor(comboBoxCellEditor);
    }

}
