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

package de.decidr.modelingtool.client.ui.dialogs.ifcontainer;

import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;

import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.model.variable.VariableType;

/**
 * This class is a listener for any changes made in the variable type selection
 * combobox of the {@link IfFieldSet}.
 * 
 * @author Jonas Schlaak
 */
public class TypeSelectorListener extends SelectionChangedListener<Variable> {

    private IfFieldSet fieldset;
    private SimpleComboBox<String> typeSelector;

    /**
     * Default constructor.
     * 
     * @param fieldset
     *            the {@link IfFieldSet} which contains the combobox
     * @param typeSelector
     *            the combobox for which the listener is for
     */
    public TypeSelectorListener(IfFieldSet fieldset,
            SimpleComboBox<String> typeSelector) {
        this.fieldset = fieldset;
        this.typeSelector = typeSelector;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.extjs.gxt.ui.client.event.SelectionChangedListener#selectionChanged
     * (com.extjs.gxt.ui.client.event.SelectionChangedEvent)
     */
    @Override
    public void selectionChanged(SelectionChangedEvent<Variable> se) {
        VariableType type = VariableType.getTypeFromLocalName(typeSelector
                .getSimpleValue());
        fieldset.updateAllStores(type);
    }
}
