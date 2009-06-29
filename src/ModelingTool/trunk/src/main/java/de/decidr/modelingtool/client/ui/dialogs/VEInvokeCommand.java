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

package de.decidr.modelingtool.client.ui.dialogs;

import de.decidr.modelingtool.client.model.VariablesFilter;
import de.decidr.modelingtool.client.ui.dialogs.variableeditor.ValueEditor;
import de.decidr.modelingtool.client.ui.dialogs.variableeditor.VariableEditor;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class VEInvokeCommand {

    public static void invoke() {
        ((ValueEditor) DialogRegistry.getInstance().getDialog(
                ValueEditor.class.getName()))
                .setModel(((VariableEditor) DialogRegistry.getInstance()
                        .getDialog(VariableEditor.class.getName()))
                        .getSelectedVariable());

    }

    public static void invoke(Long id) {
        ((ValueEditor) DialogRegistry.getInstance().getDialog(
                ValueEditor.class.getName())).setModel(VariablesFilter
                .getVariableById(id));
    }

}
