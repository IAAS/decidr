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

import java.util.HashMap;
import java.util.Map;

import de.decidr.modelingtool.client.ui.dialogs.email.EmailActivityWindow;
import de.decidr.modelingtool.client.ui.dialogs.foreachcontainer.ForEachWindow;
import de.decidr.modelingtool.client.ui.dialogs.humantask.HumanTaskActivityWindow;
import de.decidr.modelingtool.client.ui.dialogs.humantask.TaskItemWindow;
import de.decidr.modelingtool.client.ui.dialogs.ifcontainer.IfWindow;
import de.decidr.modelingtool.client.ui.dialogs.valueeditor.RoleEditor;
import de.decidr.modelingtool.client.ui.dialogs.valueeditor.ValueEditor;
import de.decidr.modelingtool.client.ui.dialogs.variableeditor.VariableEditor;
import de.decidr.modelingtool.client.ui.dialogs.workflow.WorkflowPropertyWindow;

/**
 * This class manages all instances of the activity and container property
 * windows. Only exactly one instance of each window is created. The instance
 * are stored in a map. The registry provides methods to show and hide the
 * windows as well as getting the window instances.
 * 
 * @author Jonas Schlaak
 */
public class DialogRegistry {

    private static DialogRegistry instance;

    private Map<String, ModelingToolDialog> dialogs;

    /**
     * The dialog registry is a singleton.
     * 
     * @return the instance of the dialog registry
     */
    public static DialogRegistry getInstance() {
        if (instance == null) {
            instance = new DialogRegistry();
        }
        return instance;
    }

    /**
     * Creates instances of all known dialogs and registers them.
     */
    private DialogRegistry() {
        register(new WorkflowPropertyWindow());
        register(new VariableEditor());
        register(new ValueEditor());
        register(new RoleEditor());
        register(new EmailActivityWindow());
        register(new HumanTaskActivityWindow());
        register(new TaskItemWindow());
        register(new ForEachWindow());
        register(new IfWindow());
    }

    /**
     * Registers the instance of a dialog.
     * 
     * @param dialog
     *            the dialog to register
     */
    private void register(ModelingToolDialog dialog) {
        getDialogs().put(dialog.getClass().getName(), dialog);
    }

    /**
     * Makes a dialog visible.
     * 
     * @param dialogName
     *            the class name of the dialog to be displayed
     */
    public void showDialog(String dialogName) {
        // JS modality
        ModelingToolDialog dialog = getDialog(dialogName);
        if (dialog.initialize()) {
            dialog.setModal(true);
            // ModelingToolWidget.view.mask();
            dialog.show();
        }
    }

    /**
     * Hides a dialog
     * 
     * @param dialogName
     *            the class name of the dialog to be hidden
     */
    public void hideDialog(String dialogName) {
        ModelingToolDialog dialog = getDialog(dialogName);
        dialog.hide();
        dialog.setModal(false);
        // ModelingToolWidget.view.unmask();
        dialog.reset();
    }

    /**
     * Return the instance of a dialog
     * 
     * @param dialogName
     *            the class name of the dialog
     * @return the instance of the dialog
     */
    public ModelingToolDialog getDialog(String dialogName) {
        return getDialogs().get(dialogName);
    }

    private Map<String, ModelingToolDialog> getDialogs() {
        if (dialogs == null) {
            dialogs = new HashMap<String, ModelingToolDialog>();
        }
        return dialogs;
    }
}
