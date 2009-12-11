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

package de.decidr.modelingtool.client.ui.dialogs.canvas;

import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;

/**
 * Invoker for the {@link CanvasSizeWindow}. The invoker first calls the
 * window to set the node and then calls the {@link DialogRegistry} to show the
 * window.
 *
 * @author Jonas Schlaak
 */
public class CanvasSizeWindowInvoker {

    public static void invoke(Workflow workflow) {
        /* Only invoke dialog if it is not visible */
        if (!DialogRegistry.getInstance().isDialogVisible(
                CanvasSizeWindow.class.getName())) {
            ((CanvasSizeWindow) DialogRegistry.getInstance().getDialog(
                    CanvasSizeWindow.class.getName())).setModel(workflow);
            DialogRegistry.getInstance().showDialog(
                    CanvasSizeWindow.class.getName());
        }
    }
    
}
