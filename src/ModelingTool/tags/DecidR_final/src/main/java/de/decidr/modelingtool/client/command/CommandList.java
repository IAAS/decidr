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

package de.decidr.modelingtool.client.command;

import java.util.List;
import java.util.Vector;

/**
 * Commands added to this command list can be executed / redone in one step. The
 * executing happens in add order, undoing in reverse add order.
 * 
 * @author Johannes Engelhardt
 */
public class CommandList implements UndoableCommand {

    /**
     * The list of the commands.
     * */
    private List<UndoableCommand> commands = new Vector<UndoableCommand>();

    /**
     * Adds a command to the list.
     * 
     * @param command
     *            The command to add.
     */
    public void addCommand(UndoableCommand command) {
        commands.add(command);
    }

    /**
     * Performs the execute method in every added command. This happens in the
     * order of added commands.
     */
    @Override
    public void execute() {
        for (int i = 0; i < commands.size(); i++) {
            commands.get(i).execute();
        }

    }

    /**
     * Performs the undo method in every added command. This happens in reverse
     * order of added commands.
     */
    @Override
    public void undo() {
        for (int i = commands.size() - 1; i >= 0; i--) {
            commands.get(i).undo();
        }
    }

}