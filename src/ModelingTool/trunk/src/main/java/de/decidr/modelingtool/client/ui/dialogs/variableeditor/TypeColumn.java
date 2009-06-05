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
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;

import de.decidr.modelingtool.client.model.VariableType;

/**
 * TODO: add comment
 * 
 * @author JS
 */
public class TypeColumn extends ColumnConfig {

    private SimpleComboBox<String> selection = new SimpleComboBox<String>();
    private CellEditor comboBoxCellEditor;

    public TypeColumn(String columnId, String header) {
        this.setId(columnId);
        this.setHeader(header);
        this.setWidth(100);

        selection.setTriggerAction(TriggerAction.ALL);
        for (VariableType type : VariableType.values()) {
            selection.add(type.getName());
        }

        comboBoxCellEditor = new CellEditor(selection) {
            @Override
            public Object preProcessValue(Object value) {
                if (value == null) {
                    return value;
                }
                return selection.findModel(value.toString());
            }

            @Override
            public Object postProcessValue(Object value) {
                if (value == null) {
                    return value;
                }
                return ((ModelData) value).get("value");
            }
        };
        this.setEditor(comboBoxCellEditor);
    }

}
