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

package de.decidr.modelingtool.client.ui.dialogs.valueeditor;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.model.variable.VariableType;

/**
 * A validator class that can check if the input of a {@link TextField} is of
 * the desired {@link VariableType}.
 * 
 * @author Jonas Schlaak
 */
public class FieldValidator implements Validator<String, TextField<String>> {

    private VariableType type = null;

    /**
     * Default constructor.
     * 
     * @param type
     *            the variable type to check against
     */
    public FieldValidator(VariableType type) {
        this.type = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.extjs.gxt.ui.client.widget.form.Validator#validate(com.extjs.gxt.
     * ui.client.widget.form.Field, java.lang.String)
     */
    @Override
    public String validate(TextField<String> field, String value) {
        List<String> values = new ArrayList<String>();
        values.add(value);
        ValueValidator validator = new ValueValidator(values, type);
        if (validator.validate(new ValueValidatorCallback())) {
            return null;
        } else {
            return ModelingToolWidget.getMessages().invalidFormat();
        }
    }

}
