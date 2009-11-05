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

import de.decidr.modelingtool.client.command.CommandStack;

/**
 * Command for redoing the last action made undone. If the last user performed
 * action was not a undo action, the redo stack is emtpy and no redo action is
 * performed.
 * 
 * @author Johannes Engelhardt
 */
public class RedoMenuItem implements Command {

    @Override
    public void execute() {
        CommandStack.getInstance().redo();
    }

}
