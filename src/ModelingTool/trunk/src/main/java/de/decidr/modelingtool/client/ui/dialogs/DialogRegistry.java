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

import de.decidr.modelingtool.client.ui.dialogs.activitywindows.EmailActivityWindow;
import de.decidr.modelingtool.client.ui.dialogs.activitywindows.HumanTaskActivityWindow;
import de.decidr.modelingtool.client.ui.dialogs.containerwindows.ForEachWindow;
import de.decidr.modelingtool.client.ui.dialogs.variableeditor.ValueEditor;
import de.decidr.modelingtool.client.ui.dialogs.variableeditor.VariableEditor;

/**
 * TODO: add comment
 * 
 * @author JS
 */
public class DialogRegistry {

    private static DialogRegistry instance;

    private Map<String, Dialog> dialogs;

    public static DialogRegistry getInstance() {
        if (instance == null) {
            instance = new DialogRegistry();
        }
        return instance;
    }

    private DialogRegistry() {
        register(new VariableEditor());
        register(new ValueEditor());
        register(new EmailActivityWindow());
        register(new HumanTaskActivityWindow());
        register(new ForEachWindow());        
    }

    public void register(Dialog dialog) {
        getDialogs().put(dialog.getClass().getName(), dialog);
    }

    public void showDialog(String dialogName) {
        Dialog dialog = getDialog(dialogName);
        dialog.initialize();
        dialog.setVisible(true);
    }

    public void hideDialog(String dialogName) {
        Dialog dialog = getDialog(dialogName);
        dialog.setVisible(false);
        dialog.reset();
    }

    public Dialog getDialog(String dialogName) {
        return getDialogs().get(dialogName);
    }

    private Map<String, Dialog> getDialogs() {
        if (dialogs == null) {
            dialogs = new HashMap<String, Dialog>();
        }
        return dialogs;
    }
}
