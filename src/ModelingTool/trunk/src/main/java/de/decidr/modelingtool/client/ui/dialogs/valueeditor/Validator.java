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

import java.util.List;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.model.variable.VariableType;

/**
 * Provides methods that can determine whether the values (provided as a list of
 * strings) have have the correct format according a given type.
 * 
 * @author Jonas Schlaak
 */
public class Validator {

    private List<String> values;
    private VariableType type;

    /**
     * 
     * Default constructor. You have to give the values which are to be
     * validated and the type to which the values are to be validated.
     * 
     * @param values
     *            the values
     * @param type
     *            the type
     */
    public Validator(List<String> values, VariableType type) {
        this.values = values;
        this.type = type;
    }

    /**
     * 
     * Validates the values given in the constructor.
     * 
     * @param callback
     *            the callback
     * @return whether the values are valid or not
     */
    public Boolean validate(ValidatorCallback callback) {
        Boolean result = false;
        switch (type) {
        case STRING:
            /* In a string, everything is allowed */
            result = true;
            break;
        case INTEGER:
            result = checkInteger(callback);
            break;
        case FLOAT:
            result = checkFloat(callback);
            break;
        case BOOLEAN:
            break;
        case FILE:
            break;
        case DATE:
            break;
        case ROLE:
            break;
        case FORM:
            break;
        default:
            break;
        }
        return result;
    }

    private Boolean checkInteger(ValidatorCallback callback) {
        Boolean result = true;
        for (String value : values) {
            /*
             * check every character of the string. If it is not a digit, the
             * value is not valid.
             */
            for (int i = 0; i < value.length(); i++) {
                if (!Character.isDigit(value.charAt(i))) {
                    // JS: externalize
                    callback.addToMessage("Value "
                            + (values.indexOf(value) + 1)
                            + " has a wrong format.");
                    result = false;
                }
            }
        }
        return result;
    }

    private Boolean checkFloat(ValidatorCallback callback) {
        Boolean result = true;
        for (String value : values) {
            String[] parts = value.split(ModelingToolWidget.messages.decimalSeparator());
//            String test ="asd, ber, ceret";
//           parts=test.split(",");
            System.out.println(value);
            System.out.println(ModelingToolWidget.messages.decimalSeparator());
            System.out.println(parts.length);
            System.out.println(parts[0]);
            System.out.println(parts[1]);

            if (parts.length != 2) {
                result = false;
            }
            for (int i = 0; i < parts[0].length(); i++) {
                if (!Character.isDigit(parts[0].charAt(i))) {
                    // JS: externalize
                    callback.addToMessage("Value "
                            + (values.indexOf(value) + 1)
                            + " has a wrong format.");
                    result = false;
                }
            }
            for (int i = 0; i < parts[1].length(); i++) {
                if (!Character.isDigit(parts[1].charAt(i))) {
                    // JS: externalize
                    callback.addToMessage("Value "
                            + (values.indexOf(value) + 1)
                            + " has a wrong format.");
                    result = false;
                }
            }

        }
        return result;
    }

}
