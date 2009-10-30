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

package de.decidr.modelingtool.client.ui.dialogs.email;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.ui.dialogs.valueeditor.ValueEditorWindow;
import de.decidr.modelingtool.client.ui.dialogs.valueeditor.ValueEditorWindowInvoker;

/**
 * Listener class for the "change value" button in the
 * {@link EmailActivityWindow}. When the button is pressed, the
 * {@link ValueEditorWindow} is invoked.
 * 
 * @author Jonas Schlaak
 */
public class ChangeValueButtonListener extends SelectionListener<ButtonEvent> {

    private ComboBox<Variable> field;

    /**
     * Constructs a listener. Associates the listener with the combobox next to
     * the button so that the id of the variable whose values should be changed,
     * can be easily accessed.
     * 
     * @param field
     *            the combobox
     */
    public ChangeValueButtonListener(ComboBox<Variable> field) {
        super();
        this.field = field;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.extjs.gxt.ui.client.event.SelectionListener#componentSelected(com
     * .extjs.gxt.ui.client.event.ComponentEvent)
     */
    public void componentSelected(ButtonEvent ce) {
        ValueEditorWindowInvoker.invoke(field.getValue().getId());
    }
}
