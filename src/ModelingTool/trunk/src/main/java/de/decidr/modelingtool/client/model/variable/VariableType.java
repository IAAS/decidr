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
import de.decidr.modelingtool.client.io.resources.DWDLNames;
import de.decidr.modelingtool.client.ui.resources.DateFormatter;

/**
 * This enumeration class holds all types that a {@link Variable} can have. It
 * also holds some additional informations such as the localized name of the
 * variable type or the default value
 * 
 * @author Jonas Schlaak
 */
public enum VariableType {

    STRING(DWDLNames.variableTypes.STRING, ModelingToolWidget.getMessages()
            .typeString(), ModelingToolWidget.getMessages().newStringValue()),
    INTEGER(DWDLNames.variableTypes.INTEGER, ModelingToolWidget.getMessages()
            .typeInteger(), "1"),
    FLOAT(DWDLNames.variableTypes.FLOAT, ModelingToolWidget.getMessages()
            .typeFloat(), "1.0"),
    BOOLEAN(DWDLNames.variableTypes.BOOLEAN, ModelingToolWidget.getMessages()
            .typeBoolean(), "false"),
    FILE(DWDLNames.variableTypes.FILE, ModelingToolWidget.getMessages()
            .typeFile(), ModelingToolWidget.getMessages().newStringValue()),
    DATE(DWDLNames.variableTypes.DATE, ModelingToolWidget.getMessages()
            .typeDate(), DateFormatter.getToday()),
    ROLE(DWDLNames.variableTypes.ROLE, ModelingToolWidget.getMessages()
            .typeRole(), ModelingToolWidget.getMessages().newStringValue()),
    FORM(DWDLNames.variableTypes.FORM, ModelingToolWidget.getMessages()
            .typeForm(), null);

    private final String dwdlName;
    private final String localName;
    private final String defaultValue;

    /**
     * Constructor of a variable type.
     * 
     * @param dwdlName
     *            the dwdl name of the variable type
     * @param localName
     *            the localized name of the variable type
     * @param defaultValue
     *            the default value each variable of the type has
     */
    private VariableType(String dwdlName, String localName, String defaultValue) {
        this.dwdlName = dwdlName;
        this.localName = localName;
        this.defaultValue = defaultValue;
    }

    /**
     * This method returns the dwdl name the variable type has according to the
     * dwdl structure document.
     * 
     * @return dwdl name
     */
    public String getDwdlName() {
        return dwdlName;
    }

    /**
     * This methods returns the localized name of the variable type (e.g.
     * "Datei" for "File") in a proper format (i.e. no caps).
     * 
     * @return local name
     */
    public String getLocalName() {
        return localName;
    }

    /**
     * This method return a reasonable (i.e. valid format) value for that type
     * of variable.
     * 
     * @return default value
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Returns the variable type to a given localized name of a variable type
     * 
     * @param localName
     *            the localized name
     * @return the variable type
     */
    public static VariableType getTypeFromLocalName(String localName) {
        VariableType result = null;
        for (VariableType type : VariableType.values()) {
            if (localName.equals(type.getLocalName()))
                result = type;
        }
        return result;
    }

    /**
     * Returns the variable type to a given dwdl name of a variable type
     * 
     * @param dwdlName
     *            the localized name
     * @return the variable type
     */
    public static VariableType getTypeFromDWDLName(String dwdlName) {
        VariableType result = null;
        for (VariableType type : VariableType.values()) {
            if (dwdlName.equals(type.getDwdlName()))
                result = type;
        }
        return result;
    }
}
