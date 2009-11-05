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

package de.decidr.modelingtool.client.ui.selection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window;

import de.decidr.modelingtool.client.command.CommandStack;
import de.decidr.modelingtool.client.command.RemoveConnectionCommand;
import de.decidr.modelingtool.client.command.RemoveNodeCommand;
import de.decidr.modelingtool.client.command.UndoableCommand;
import de.decidr.modelingtool.client.exception.NoPropertyWindowException;
import de.decidr.modelingtool.client.exception.NodeNotDeletableException;
import de.decidr.modelingtool.client.ui.Connection;
import de.decidr.modelingtool.client.ui.Node;
import de.decidr.modelingtool.client.ui.Selectable;
import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.resources.Messages;

/**
 * This class handles keyboard inputs from the user. This is a singleton.
 * 
 * @author Johannes Engelhardt
 */
public class KeyboardHandler implements KeyDownHandler {

    private static KeyboardHandler instance;

    private KeyboardHandler() {
        super();
    }

    public static KeyboardHandler getInstance() {
        if (instance == null) {
            instance = new KeyboardHandler();
        }
        return instance;
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        // stop propagation of the event
        event.stopPropagation();

        // get selected item
        Selectable selectedItem = SelectionHandler.getInstance()
                .getSelectedItem();

        // create message resource
        Messages msgs = GWT.create(Messages.class);

        // switch key codes
        switch (event.getNativeKeyCode()) {

            // DELETE key: delete selected item
            case (KeyCodes.KEY_DELETE):
                UndoableCommand removeCmd = null;

                if (selectedItem != null) {
                    try {
                        // create command according to item type
                        if (selectedItem instanceof Node) {
                            removeCmd = new RemoveNodeCommand(
                                    (Node) selectedItem);
                        } else if (selectedItem instanceof Connection) {
                            removeCmd = new RemoveConnectionCommand(
                                    (Connection) selectedItem);
                        }

                        CommandStack.getInstance().executeCommand(removeCmd);

                    } catch (NodeNotDeletableException e) {
                        Window.alert(msgs.notDeletableMessage());
                    }
                }
                break;

            // ENTER key: show property window
            case (KeyCodes.KEY_ENTER):
                if (selectedItem != null) {
                    try {
                        selectedItem.showPropertyWindow();
                    } catch (NoPropertyWindowException e) {
                        // display error message
                        Window.alert(msgs.noPropertyWindowMessage());
                    }
                } else {
                    Workflow.getInstance().showPropertyWindow();
                }
                break;

        }
    }

}
