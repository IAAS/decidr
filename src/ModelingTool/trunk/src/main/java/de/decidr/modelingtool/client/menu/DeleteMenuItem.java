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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;

import de.decidr.modelingtool.client.command.CommandStack;
import de.decidr.modelingtool.client.command.RemoveConnectionCommand;
import de.decidr.modelingtool.client.command.RemoveNodeCommand;
import de.decidr.modelingtool.client.command.UndoableCommand;
import de.decidr.modelingtool.client.exception.NodeNotDeletableException;
import de.decidr.modelingtool.client.ui.Connection;
import de.decidr.modelingtool.client.ui.Node;
import de.decidr.modelingtool.client.ui.Selectable;
import de.decidr.modelingtool.client.ui.resources.Messages;
import de.decidr.modelingtool.client.ui.selection.SelectionHandler;

/**
 * Menu item command for deleting the selected item.
 * 
 * @author Johannes Engelhardt
 */
public class DeleteMenuItem implements Command {

    @Override
    public void execute() {
        // get selected item
        Selectable selectedItem = SelectionHandler.getInstance()
                .getSelectedItem();
        UndoableCommand removeCmd = null;

        Messages msgs = GWT.create(Messages.class);

        if (selectedItem != null) {
            try {
                // create command according to item type
                if (selectedItem instanceof Node) {
                    removeCmd = new RemoveNodeCommand((Node) selectedItem);
                } else if (selectedItem instanceof Connection) {
                    removeCmd = new RemoveConnectionCommand(
                            (Connection) selectedItem);
                }

                CommandStack.getInstance().executeCommand(removeCmd);

            } catch (NodeNotDeletableException e) {
                Window.alert(msgs.notDeletableMessage());
            }
        }
    }

}
