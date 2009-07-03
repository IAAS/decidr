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
 * elements part of the {@link HumanTaskActivityWindowContentPanel}.
 * 
 * @author Jonas Schlaak
 */
public class FormElementFieldSet {

    private TextField<String> labelField;
    private ComboBox<Variable> variableComboBox;

    public FormElementFieldSet(TextField<String> labelField,
            ComboBox<Variable> variableComboxBox) {
        this.labelField = labelField;
        this.variableComboBox = variableComboxBox;
    }

    public TextField<String> getLabelField() {
        return labelField;
    }

    public ComboBox<Variable> getVariableComboBox() {
        return variableComboBox;
    }

}
