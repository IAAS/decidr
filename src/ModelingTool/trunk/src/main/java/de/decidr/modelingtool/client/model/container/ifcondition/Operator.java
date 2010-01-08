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

package de.decidr.modelingtool.client.model.container.ifcondition;

import java.util.ArrayList;
import java.util.List;

import de.decidr.modelingtool.client.model.variable.VariableType;
import de.decidr.modelingtool.client.ui.dialogs.ifcontainer.IfWindow;

/**
 * This enumeration type holds all operator types and their properties.
 * Operators are used in {@link Condition} as part of the condition statement.
 * This class also holds some additional information about the operator, for
 * example, to which variable types the operator can be applied to.
 * 
 * @author Jonas Schlaak
 */
public enum Operator {

    EQUAL("==", VariableType.STRING, VariableType.INTEGER, VariableType.FLOAT,
            VariableType.DATE, VariableType.BOOLEAN, VariableType.DATE,
            VariableType.ROLE),
    NOTEQUAL("!=", VariableType.STRING, VariableType.INTEGER,
            VariableType.FLOAT, VariableType.DATE, VariableType.BOOLEAN,
            VariableType.DATE, VariableType.ROLE),
    SMALLER("<", VariableType.INTEGER, VariableType.FLOAT, VariableType.DATE),
    SMALLEREQUALS("<=", VariableType.INTEGER, VariableType.FLOAT),
    GREATER(">", VariableType.INTEGER, VariableType.FLOAT, VariableType.DATE),
    GREATEREQUALS(">=", VariableType.INTEGER, VariableType.FLOAT),
    CONTAINS("?=", VariableType.STRING);

    private String displayString;
    private VariableType[] types;

    /**
     * Default constructor of an operator
     * 
     * @param displayString
     *            the string representation of the operator which is displayed
     *            in a combobox of {@link IfWindow}.
     * @param types
     *            the variable types to which the operator can be applied to
     */
    private Operator(String displayString, VariableType... types) {
        this.displayString = displayString;
        this.types = types;
    }

    /**
     * Returns a ("mathematical") string representation of the operator. Useful
     * for identifying an operator in a combobox.
     * 
     * @return the display string
     */
    public String getDisplayString() {
        return displayString;
    }

    /**
     * Gets the right enum type {@link Operator} which correlates to the given
     * display string. Display string are, for example, "==", "!="...
     * 
     * @param displayString
     *            the display string
     * @return the operator belonging to the display string
     */
    public static Operator getOperatorFromDisplayString(String displayString) {
        Operator result = null;
        for (Operator op : Operator.values()) {
            if (op.getDisplayString().equals(displayString)) {
                result = op;
            }
        }
        return result;
    }

    /**
     * Returns an array of variables type to which this operator can be applied.
     * 
     * @return the variable types
     */
    public VariableType[] getTypes() {
        return types;
    }

    /**
     * Returns the list of {@link Operator}s which can be applied to a given
     * {@link VariableType}.
     * 
     * @param type
     *            the VariableType
     * @return the list of Operators
     */
    public static List<Operator> getOperatorsForType(VariableType type) {
        List<Operator> list = new ArrayList<Operator>();
        for (Operator op : Operator.values()) {
            if (op.isApplicableToType(type)) {
                list.add(op);
            }
        }
        return list;
    }

    /**
     * Checks whether this operator can be applied to a given variable type.
     * 
     * @param type
     *            the type to be checked against
     * @return the result
     */
    private boolean isApplicableToType(VariableType type) {
        boolean result = false;
        for (VariableType t : types) {
            if (t == type) {
                result = true;
            }
        }
        return result;
    }

}
