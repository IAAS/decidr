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

import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.model.variable.VariablesFilter;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class ValueEditorInvoker {

    /**
     * 
     * TODO: add comment (with a variable directly that is not in the model yet)
     * 
     * @param variable
     */
    public static void invoke(Variable variable) {
        ((ValueEditor) DialogRegistry.getInstance().getDialog(
                ValueEditor.class.getName())).setVariable(variable);
    }

    /**
     * 
     * TODO: add comment (with a variable that is already in the model and can
     * be identified by its id
     * 
     * @param id
     */
    public static void invoke(Long id) {
        ((ValueEditor) DialogRegistry.getInstance().getDialog(
                ValueEditor.class.getName())).setVariable(VariablesFilter
                .getVariableById(id));
    }

}
