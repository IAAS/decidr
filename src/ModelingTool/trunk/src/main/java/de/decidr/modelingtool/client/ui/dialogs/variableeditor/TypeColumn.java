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

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;

import de.decidr.modelingtool.client.model.variable.Variable;

/**
 * Type column which displays the type of the variable.
 * 
 * @author Jonas Schlaak
 */
public class TypeColumn extends ColumnConfig {

    public TypeColumn(String columnId, String header) {
        this.setId(columnId);
        this.setHeader(header);
        this.setWidth(60);

        /* Renderer that displays the localized name of a variable type */
        GridCellRenderer<Variable> cellRenderer = new GridCellRenderer<Variable>() {
            @Override
            public String render(Variable model, String property,
                    ColumnData config, int rowIndex, int colIndex,
                    ListStore<Variable> store) {
                return model.getType().getLocalName();
            }
        };
        this.setRenderer(cellRenderer);
    }

}
