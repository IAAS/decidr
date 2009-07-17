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
    EQUAL("==", VariableType.STRING, VariableType.INTEGER, VariableType.BOOLEAN), NOTEQUAL(
            "!=", VariableType.STRING, VariableType.INTEGER,
            VariableType.BOOLEAN), SMALLER("<", VariableType.INTEGER), SMALLEREQUALS(
            "<="), GREATER(">"), GREATEREQUALS(">="), CONTAINS("?=",
            VariableType.STRING);

    private String displayString;
    private VariableType[] types;

    private Operator(String displayString, VariableType... types) {
        this.displayString = displayString;
        this.types = types;
    }

    public String getDisplayString() {
        return displayString;
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
