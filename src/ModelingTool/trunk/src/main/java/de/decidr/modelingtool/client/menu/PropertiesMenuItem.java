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

import de.decidr.modelingtool.client.exception.NoPropertyWindowException;
import de.decidr.modelingtool.client.ui.Selectable;
import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.resources.Messages;
import de.decidr.modelingtool.client.ui.selection.SelectionHandler;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class PropertiesMenuItem implements Command {

    @Override
    public void execute() {
        // get selected item
        Selectable selectedItem = SelectionHandler.getInstance()
                .getSelectedItem();

        // create message resource
        Messages msgs = GWT.create(Messages.class);

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

    }

}
