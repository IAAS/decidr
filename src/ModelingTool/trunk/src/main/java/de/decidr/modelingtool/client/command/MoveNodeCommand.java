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

import de.decidr.modelingtool.client.ui.Node;

/**
 * TODO: add comment
 *
 * @author JE
 */
public class MoveNodeCommand implements UndoableCommand {
    
    private Node node;
    private int oldNodeLeft;
    private int oldNodeTop;
    private int newNodeLeft;
    private int newNodeTop;

    public MoveNodeCommand(Node node, int oldNodeLeft, int oldNodeTop) {
        this.node = node;
        this.oldNodeLeft = oldNodeLeft;
        this.oldNodeTop = oldNodeTop;
        this.newNodeLeft = node.getLeft();
        this.newNodeTop = node.getTop();
    }
    
    @Override
    public void undo() {
        node.setPosition(oldNodeLeft, oldNodeTop);

    }

    @Override
    public void execute() {
        node.setPosition(newNodeLeft, newNodeTop);
    }

}