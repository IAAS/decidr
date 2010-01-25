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
import de.decidr.modelingtool.client.model.variable.VariableType;
import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;

/**
 * Invoker for the {@link ValueEditorWindow}. The invoker first calls the window
 * to set the node and then calls the {@link DialogRegistry} to show the window.
 * 
 * @author Jonas Schlaak
 */
public class ValueEditorWindowInvoker {

    /**
     * Invokes with a variable that is in the model and can be identified by its
     * id. For example, this happens when the value editor is invoked by a
     * "change values" button within the email activity window.
     * 
     * @param id
     *            the id of the variable the variable whose values are to be
     *            edited
     */
    public static void invoke(Long id) {
        Variable variable = Workflow.getInstance().getModel().getVariable(id);
        if (variable.getType() == VariableType.ROLE) {
            /* Only invoke dialog if it is not visible */
            if (!DialogRegistry.getInstance().isDialogVisible(
                    RoleEditor.class.getName())) {
                ((RoleEditor) DialogRegistry.getInstance().getDialog(
                        RoleEditor.class.getName())).setVariable(variable);
                DialogRegistry.getInstance().showDialog(
                        RoleEditor.class.getName());
            }
        } else {
            /* Only invoke dialog if it is not visible */
            if (!DialogRegistry.getInstance().isDialogVisible(
                    ValueEditorWindow.class.getName())) {
                ((ValueEditorWindow) DialogRegistry.getInstance().getDialog(
                        ValueEditorWindow.class.getName()))
                        .setVariable(variable);
                DialogRegistry.getInstance().showDialog(
                        ValueEditorWindow.class.getName());
            }
        }
    }

    /**
     * Invokes the value editor with with a variable directly that is not in the
     * workflow model. The variable editor uses this method because the variable
     * editor uses its own model, not the variable in the workflow model.
     * 
     * @param variable
     *            the variable whose values are to be edited
     */
    public static void invoke(Variable variable) {
        if (variable.getType() == VariableType.ROLE) {
            /* Only invoke dialog if it is not visible */
            if (!DialogRegistry.getInstance().isDialogVisible(
                    RoleEditor.class.getName())) {
                ((RoleEditor) DialogRegistry.getInstance().getDialog(
                        RoleEditor.class.getName())).setVariable(variable);
                DialogRegistry.getInstance().showDialog(
                        RoleEditor.class.getName());
            }
        } else {
            /* Only invoke dialog if it is not visible */
            if (!DialogRegistry.getInstance().isDialogVisible(
                    ValueEditorWindow.class.getName())) {
                ((ValueEditorWindow) DialogRegistry.getInstance().getDialog(
                        ValueEditorWindow.class.getName()))
                        .setVariable(variable);
                DialogRegistry.getInstance().showDialog(
                        ValueEditorWindow.class.getName());
            }
        }
    }

}