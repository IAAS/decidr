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

package de.decidr.modelingtool.client.ui.dialogs.humantask;

import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;

import de.decidr.modelingtool.client.model.variable.Variable;

/**
 * This container holds the references to all fields of one row of the form
 * elements part of the {@link HumanTaskActivityWindowContentPanel}. When the
 * workflow is being executed a human task appears as work item to the user.
 * This container then represents an input field of a specific type.
 * 
 * @author Jonas Schlaak
 */
public class HumanTaskFieldSet {

    private TextField<String> labelField;
    private ComboBox<Variable> variableField;

    /**
     * Constructs a field set with the given parameters.
     * 
     * @param labelField
     *            this field is for the name of the input field which later
     *            appears to the user
     * @param variableField
     *            from this field the variable can be selected into which the
     *            input from the user shall be written
     */
    public HumanTaskFieldSet(TextField<String> labelField,
            ComboBox<Variable> variableField) {
        this.labelField = labelField;
        this.variableField = variableField;
    }

    public TextField<String> getLabelField() {
        return labelField;
    }

    public ComboBox<Variable> getVariableField() {
        return variableField;
    }

}
