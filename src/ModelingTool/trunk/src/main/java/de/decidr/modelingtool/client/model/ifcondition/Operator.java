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

package de.decidr.modelingtool.client.model.ifcondition;

import java.util.ArrayList;
import java.util.List;

import de.decidr.modelingtool.client.model.variable.VariableType;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public enum Operator {
    // JS finish
    EQUAL("==", VariableType.STRING, VariableType.INTEGER, VariableType.FLOAT, VariableType.DATE, VariableType.BOOLEAN, VariableType.DATE, VariableType.ROLE),
    NOTEQUAL("!=", VariableType.STRING, VariableType.INTEGER, VariableType.FLOAT, VariableType.DATE, VariableType.BOOLEAN, VariableType.DATE, VariableType.ROLE),
    SMALLER("<", VariableType.INTEGER, VariableType.FLOAT, VariableType.DATE),
    SMALLEREQUALS("<=", VariableType.INTEGER, VariableType.FLOAT),
    GREATER(">", VariableType.INTEGER, VariableType.FLOAT, VariableType.DATE),
    GREATEREQUALS(">=", VariableType.INTEGER, VariableType.FLOAT),
    CONTAINS("?=", VariableType.STRING);

    private String displayString;
    private VariableType[] types;

    private Operator(String displayString, VariableType... types) {
        this.displayString = displayString;
        this.types = types;
    }

    public String getDisplayString() {
        return displayString;
    }

    public static Operator getOperatorFromDisplayString(String displayString) {
        Operator result = null;
        for (Operator op : Operator.values()) {
            if (op.getDisplayString().equals(displayString)) {
                result = op;
            }
        }
        return result;
    }

    public VariableType[] getTypes() {
        return types;
    }

    public static List<Operator> getOperatorsForType(VariableType type) {
        List<Operator> list = new ArrayList<Operator>();
        for (Operator op : Operator.values()) {
            if (op.hasType(type)) {
                list.add(op);
            }
        }
        return list;
    }

    // JS rename this method
    private boolean hasType(VariableType type) {
        boolean result = false;
        for (VariableType t : types) {
            if (t == type) {
                result = true;
            }
        }
        return result;
    }

}
