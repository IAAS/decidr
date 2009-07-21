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

import de.decidr.modelingtool.client.exception.InvalidTypeException;
import de.decidr.modelingtool.client.ui.HasChildren;
import de.decidr.modelingtool.client.ui.Node;
import de.decidr.modelingtool.client.ui.selection.SelectionHandler;

/**
 * This command moves a node to a specific position / container.
 * 
 * @author Johannes Engelhardt
 */
public class MoveNodeCommand implements UndoableCommand {

    /** The node to move. */
    private Node node;

    /** The parent panel of the node before moving. */
    private HasChildren oldParentPanel;
    /** The X coordinate of the node before moving. */
    private int oldNodeLeft;
    /** The Y coordinate of the node before moving. */
    private int oldNodeTop;

    /** The parent panel of the node after moving. */
    private HasChildren newParentPanel;
    /** The x coordinate of the node after moving. */
    private int newNodeLeft;
    /** The y coordiante of the node after moving. */
    private int newNodeTop;

    /**
     * This command removes any connections connected to the node, if the node
     * is moved to another panel and the connections get illegal.
     */
    private UndoableCommand removeConnectionsCmd;

    /**
     * 
     * Constructor for the already moved graphical node.
     *
     * @param node The graphical node (already moved to new position and panel)
     * @param oldParentPanel The parent panel of the node before moving
     * @param oldNodeLeft The x coordinate of the node before moving
     * @param oldNodeTop The y coordinate of the node before moving
     */
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
                SelectionHandler.getInstance().unselect();
            }

            try {
                node.getModel().setParentModel(
                        oldParentPanel.getHasChildModelsModel());
            } catch (InvalidTypeException e) {
                e.printStackTrace();
            }

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
            SelectionHandler.getInstance().select(node);
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
                SelectionHandler.getInstance().unselect();
            }

            // remove connections if parent panel has changed
            removeConnectionsCmd.execute();

            oldParentPanel.removeNode(node);
            newParentPanel.addNode(node);

            // set new parent model
            try {
                node.getModel().setParentModel(
                        newParentPanel.getHasChildModelsModel());
            } catch (InvalidTypeException e) {
                e.printStackTrace();
            }
        }

        // set node to new position
        node.setPosition(newNodeLeft, newNodeTop);
        // set position data in model
        node.getModel().setChangeListenerPosition(newNodeLeft, newNodeTop);

        // select node, if unselected and was selected before
        if (!node.isSelected() && selected) {
            SelectionHandler.getInstance().select(node);
        }
    }

}
