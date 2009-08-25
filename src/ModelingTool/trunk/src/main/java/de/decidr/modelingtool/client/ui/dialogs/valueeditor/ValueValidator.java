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
import de.decidr.modelingtool.client.ui.resources.DateFormatter;

/**
 * Provides methods that can determine whether the values (provided as a list of
 * strings) have have the correct format according a given type.
 * 
 * @author Jonas Schlaak
 */
public class ValueValidator {

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
    public ValueValidator(List<String> values, VariableType type) {
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
    public Boolean validate(ValueValidatorCallback callback) {
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
                result = checkBoolean(callback);
                break;
            case FILE:
                break;
            case DATE:
                result = checkDate(callback);
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

    private Boolean checkInteger(ValueValidatorCallback callback) {
        Boolean result = true;
        for (int i = 0; i < values.size(); i++) {
            if (isInteger(values.get(i), true) == false) {
                result = false;
                /*
                 * "+1" is added in the following line because in the ui the
                 * indexes do not start with 0.
                 */
                callback.addValueToMessage(i + 1);
            }
        }
        return result;
    }

    private Boolean isInteger(String integer, Boolean signed) {
        /*
         * check every character of the string. If it is not a digit, the string
         * is not a valid integer.
         */
        Boolean result = true;
        int index = 0;
        /* a single '+' or '-' i not allowed */
        if (integer.length() == 1
                && (integer.charAt(0) == '-' || integer.charAt(0) == '+')) {
            result = false;
        }
        while (result == true && index < integer.length()) {
            /*
             * if the integer is supposed to be a signed integer, the first
             * index can be a '+', '-' or a digit.
             */
            if (index == 0 && signed) {
                if (!Character.isDigit(integer.charAt(index))
                        && !(integer.charAt(index) == '-')
                        && !(integer.charAt(index) == '+')) {
                    result = false;
                }
            } else if (Character.isDigit(integer.charAt(index)) == false) {
                result = false;
            }
            index = index + 1;
        }
        return result;
    }

    private Boolean checkFloat(ValueValidatorCallback callback) {
        Boolean result = true;
        for (int i = 0; i < values.size(); i++) {
            /*
             * Split every "float string" into two parts (devided by the decimal
             * separator), and check if both parts are an integer.
             */
            String[] parts = values.get(i).split(
                    ModelingToolWidget.messages.decimalSeparator());
            if (parts.length != 2) {
                callback.addValueToMessage(i + 1);
                result = false;
            }

            /* Validate the two parts using the methods to validate an integer */
            if (parts.length == 2 && isInteger(parts[0], true) == false) {
                callback.addValueToMessage(i + 1);
                result = false;
            }
            if (parts.length == 2 && isInteger(parts[1], false) == false) {
                callback.addValueToMessage(i + 1);
                result = false;
            }

        }
        return result;
    }

    private Boolean checkBoolean(ValueValidatorCallback callback) {
        Boolean result = true;
        for (int i = 0; i < values.size(); i++) {
            if (!(values.get(i).equalsIgnoreCase("false"))
                    && !(values.get(i).equalsIgnoreCase("true"))) {
                callback.addValueToMessage(i + 1);
                result = false;
            }
        }
        return result;
    }

    private Boolean checkDate(ValueValidatorCallback callback) {
        Boolean result = true;
        for (int i = 0; i < values.size(); i++) {
            if (DateFormatter.validate(values.get(i)) == false) {
                callback.addValueToMessage(i + 1);
                result = false;
            }
        }
        return result;
    }

}