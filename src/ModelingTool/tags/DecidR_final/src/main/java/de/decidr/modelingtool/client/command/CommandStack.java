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

import java.util.Stack;

/**
 * Every user performed command is added to the command stack. Commands added
 * here can be undone and redone. The command stack is a singleton.
 * 
 * @author Johannes Engelhardt
 */
public class CommandStack {

    /**
     * The command stack instance.
     */
    private static CommandStack instance = null;

    /**
     * Returns the command stack instance.
     * 
     * @return The command stack instance
     */
    public static CommandStack getInstance() {
        if (instance == null) {
            instance = new CommandStack();
        }
        return instance;
    }

    /**
     * All user performed operations are pushed on this stack that they can be
     * made undone at later time.
     */
    private Stack<UndoableCommand> undoStack = new Stack<UndoableCommand>();

    /**
     * Every undone user operation is pushed on this stack that it can be
     * executed again. If the user performes a new action, the redo stack is
     * flushed.
     */
    private Stack<UndoableCommand> redoStack = new Stack<UndoableCommand>();

    private CommandStack() {
        // private constructor to prevent non-singleton instantiation
    }

    /**
     * Adds a command to the undo stack. The redo stack is flushed.
     * 
     * @param command
     *            The command to add.
     */
    public void addCommand(UndoableCommand command) {
        redoStack.clear();
        undoStack.push(command);
    }

    /**
     * Adds a command to the undo stack and exectues it. The redo stack is
     * flushed.
     * 
     * @param command
     *            The command to add/execute.
     */
    public void executeCommand(UndoableCommand command) {
        addCommand(command);
        command.execute();
    }

    /**
     * Flushes the command stack.
     */
    public void flush() {
        undoStack.clear();
        redoStack.clear();
    }

    /**
     * Performs the execute method of the top command on the redo stack, if
     * present. The redone command is pushed on the undo stack.
     */
    public void redo() {
        if (!redoStack.isEmpty()) {
            UndoableCommand command = redoStack.pop();
            undoStack.push(command);
            command.execute();
        }
    }

    /**
     * Performs the undo method of the top command on the undo stack, if
     * present. The undone command is pushed on the redo stack.
     */
    public void undo() {
        if (!undoStack.isEmpty()) {
            UndoableCommand command = undoStack.pop();
            redoStack.push(command);
            command.undo();
        }
    }
}