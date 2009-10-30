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

package de.decidr.modelingtool.client.ui.dialogs.variableeditor;

import com.extjs.gxt.ui.client.store.ListStore;

import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;

/**
 * Invoker for the {@link NewVariableWindow}. The invoker first calls the window
 * to set the model and then calls the {@link DialogRegistry} to show the
 * window.
 * 
 * @author Jonas Schlaak
 */
public class NewVariableWindowInvoker {

    /**
     * Invokes the window.
     * 
     * @param variables
     *            the list of variable into which the new variable is to be
     *            inserted
     */
    public static void invoke(ListStore<Variable> variables) {
        ((NewVariableWindow) DialogRegistry.getInstance().getDialog(
                NewVariableWindow.class.getName())).setVariables(variables);
        DialogRegistry.getInstance().showDialog(
                NewVariableWindow.class.getName());
    }

}
