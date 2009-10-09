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

package de.decidr.modelingtool.client.io;

import de.decidr.modelingtool.client.io.resources.DWDLNames;

/**
 * This class creates an XSD:NCName from a variable id and vice versa. This is
 * necessary because the dwdl schema requires variable names to be unique
 * ncnames. However, in the Modeling Tool, the variables have Long variables.
 * 
 * @author Jonas Schlaaj
 */
public class VariableNameFactory {

    public static String createNCNameFromId(Long variableId) {
        return DWDLNames.variableNCnamePrefix + variableId.toString();
    }

    public static Long createIdFromNCName(String ncname) {
        /*
         * It can occur, that an ncname is "Lnull". This happens if there was no
         * variable assigned. If that is the case, we do not want to return a
         * Long number, but a null object.
         */
        String text = ncname.substring(DWDLNames.variableNCnamePrefix.length());
        if (text.equals("null")) {
            return null;
        } else {
            return new Long(text);
        }

    }
}
