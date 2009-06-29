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

package de.decidr.modelingtool.client.ui.dialogs;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

import de.decidr.modelingtool.client.model.Variable;
import de.decidr.modelingtool.client.ui.dialogs.variableeditor.ValueEditor;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class SelectionBoxListener extends SelectionListener<ButtonEvent> {

    private ComboBox<Variable> field;

    public SelectionBoxListener(ComboBox<Variable> field) {
        super();
        this.field = field;
    }

    @Override
    public void componentSelected(ButtonEvent ce) {
        VEInvokeCommand.invoke(field.getValue().getId());
        DialogRegistry.getInstance().showDialog(ValueEditor.class.getName());
    }
}
