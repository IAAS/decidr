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

import de.decidr.modelingtool.client.ui.HasChildren;
import de.decidr.modelingtool.client.ui.Node;
import de.decidr.modelingtool.client.ui.Workflow;

/**
 * TODO: add comment
 * 
 * @author JE
 */
public class MoveNodeCommand implements UndoableCommand {

    private Node node;

    private HasChildren oldParentPanel;
    private int oldNodeLeft;
    private int oldNodeTop;

    private HasChildren newParentPanel;
    private int newNodeLeft;
    private int newNodeTop;

    private UndoableCommand removeConnectionsCmd;

    public MoveNodeCommand(Node node, HasChildren oldParentPanel,
            int oldNodeLeft, int oldNodeTop) {
        this.node = node;

        this.oldParentPanel = oldParentPanel;
        this.oldNodeLeft = oldNodeLeft;
        this.oldNodeTop = oldNodeTop;

        this.newParentPanel = node.getParentPanel();
        this.newNodeLeft = node.getLeft();
        this.newNodeTop = node.getTop();

        removeConnectionsCmd = node.getRemoveConnectionsCommand();
    }

    @Override
    public void undo() {
        boolean selected = false;

        if (oldParentPanel != newParentPanel) {
            // get selected state
            selected = node.isSelected();

            // unselect node, if selected (nessecary if parent panel of node is
            // changed.
            if (selected) {
                Workflow.getInstance().getSelectionHandler().unselect();
            }

            node.getModel().setParentModel(
                    oldParentPanel.getHasChildModelsModel());

            newParentPanel.removeNode(node);
            oldParentPanel.addNode(node);

            removeConnectionsCmd.undo();
        }

        // set node to former position
        node.setPosition(oldNodeLeft, oldNodeTop);
        // set position data in model
        node.getModel().setChangeListenerPosition(oldNodeLeft, oldNodeTop);

        // select node, if unselected and was selected before
        if (!node.isSelected() && selected) {
            Workflow.getInstance().getSelectionHandler().select(node);
        }
    }

    @Override
    public void execute() {
        boolean selected = false;

        if (oldParentPanel != newParentPanel) {
            // get selected state
            selected = node.isSelected();

            // unselect node, if selected (nessecary if parent panel of node is
            // changed.
            if (selected) {
                Workflow.getInstance().getSelectionHandler().unselect();
            }

            // remove connections if parent panel has changed
            removeConnectionsCmd.execute();

            oldParentPanel.removeNode(node);
            newParentPanel.addNode(node);

            // set new parent model
            node.getModel().setParentModel(
                    newParentPanel.getHasChildModelsModel());
        }

        // set node to new position
        node.setPosition(newNodeLeft, newNodeTop);
        // set position data in model
        node.getModel().setChangeListenerPosition(newNodeLeft, newNodeTop);

        // select node, if unselected and was selected before
        if (!node.isSelected() && selected) {
            Workflow.getInstance().getSelectionHandler().select(node);
        }
    }

}
