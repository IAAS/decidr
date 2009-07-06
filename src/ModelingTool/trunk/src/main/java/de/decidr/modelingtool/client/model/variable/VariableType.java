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

package de.decidr.modelingtool.client.model.variable;

import de.decidr.modelingtool.client.ModelingToolWidget;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public enum VariableType {

    STRING(ModelingToolWidget.messages.typeString()), INTEGER(ModelingToolWidget.messages
            .typeInteger()), FLOAT(ModelingToolWidget.messages.typeFloat()), BOOLEAN(
            ModelingToolWidget.messages.typeBoolean()), FILE(ModelingToolWidget.messages
            .typeFile()), DATE(ModelingToolWidget.messages.typeDate()), ROLE(
            ModelingToolWidget.messages.typeRole()), FORM(
            ModelingToolWidget.messages
            .typeForm());

    private final String localName;

    private VariableType(String localName) {
        this.localName = localName;
    }

    public String getLocalName() {
        return localName;
    }

    public static VariableType getTypeFromLocalName(String localName) {
        VariableType result = null;
        for (VariableType t : VariableType.values()) {
            if (localName == t.getLocalName())
                result = t;
        }
        return result;
    }
}
