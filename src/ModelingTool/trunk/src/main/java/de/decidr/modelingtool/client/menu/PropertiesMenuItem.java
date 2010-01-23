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

package de.decidr.modelingtool.client.menu;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.exception.NoPropertyWindowException;
import de.decidr.modelingtool.client.ui.Node;
import de.decidr.modelingtool.client.ui.Selectable;
import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.selection.SelectionHandler;

/**
 * Menu item for editing the properties of a selected {@link Node}. If the
 * selected Node has no properties to be edited an error message is displayed.
 * If no Node was selected, the properties of the {@link Workflow} will be
 * displayed.
 * 
 * @author Jonas Schlaak
 */
public class PropertiesMenuItem implements Command {

    @Override
    public void execute() {
        // get selected item
        Selectable selectedItem = SelectionHandler.getInstance()
                .getSelectedItem();

        if (selectedItem != null) {
            try {
                selectedItem.showPropertyWindow();
            } catch (NoPropertyWindowException e) {
                // display error message
                Window.alert(ModelingToolWidget.getMessages()
                        .noPropertyWindowMessage());
            }
        } else {
            Workflow.getInstance().showPropertyWindow();
        }
    }
}
