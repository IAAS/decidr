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
 * This command moves and/or resizes a node to a specific position / container.
 * 
 * @author Johannes Engelhardt
 */
public class MoveResizeNodeCommand implements UndoableCommand {

    /** The node to move. */
    private Node node;

    /** The parent panel of the node before moving. */
    private HasChildren oldParentPanel;
    /** The X coordinate of the node before moving. */
    private int oldNodeLeft;
    /** The Y coordinate of the node before moving. */
    private int oldNodeTop;
    /** The width of the node graphic before resizing. */
    private int oldGraphicWidth;
    /** The height of the node graphic before resizing. */
    private int oldGraphicHeight;

    /** The parent panel of the node after moving. */
    private HasChildren newParentPanel;
    /** The x coordinate of the node after moving. */
    private int newNodeLeft;
    /** The y coordinate of the node after moving. */
    private int newNodeTop;
    /** The width of the node graphic before resizing. */
    private int newGraphicWidth;
    /** The height of the node graphic before resizing. */
    private int newGraphicHeight;

    /**
     * This command removes any connections connected to the node, if the node
     * is moved to another panel and the connections get illegal.
     */
    private UndoableCommand removeConnectionsCmd;

    /**
     * Constructor for the already moved and resized graphical node.
     * 
     * @param node
     *            The graphical node (already moved to new position and panel)
     * @param oldParentPanel
     *            The parent panel of the node before moving
     * @param oldNodeLeft
     *            The x coordinate of the node before moving
     * @param oldNodeTop
     *            The y coordinate of the node before moving
     * @param oldGraphicWidth
     *            The width of the node graphic before resizing
     * @param oldGraphicHeight
     *            The height of the node graphic before resizing
     */
    public MoveResizeNodeCommand(Node node, HasChildren oldParentPanel,
            int oldNodeLeft, int oldNodeTop, int oldGraphicWidth,
            int oldGraphicHeight) {
        this.node = node;

        this.oldParentPanel = oldParentPanel;
        this.oldNodeLeft = oldNodeLeft;
        this.oldNodeTop = oldNodeTop;
        this.oldGraphicWidth = oldGraphicWidth;
        this.oldGraphicHeight = oldGraphicHeight;

        this.newParentPanel = node.getParentPanel();
        this.newNodeLeft = node.getLeft();
        this.newNodeTop = node.getTop();
        this.newGraphicWidth = node.getGraphicWidth();
        this.newGraphicHeight = node.getGraphicHeight();

        removeConnectionsCmd = node.getRemoveDependentItemsCommand();
    }

    /**
     * Constructor for the already moved graphical node (without resizing).
     * 
     * @param node
     *            The graphical node (already moved to new position and panel)
     * @param oldParentPanel
     *            The parent panel of the node before moving
     * @param oldNodeLeft
     *            The x coordinate of the node before moving
     * @param oldNodeTop
     *            The y coordinate of the node before moving
     */
    public MoveResizeNodeCommand(Node node, HasChildren oldParentPanel,
            int oldNodeLeft, int oldNodeTop) {
        this(node, oldParentPanel, oldNodeLeft, oldNodeTop, 0, 0);
    }

    @Override
    public void undo() {
        // get selected state
        boolean selected = node.isSelected();

        if (oldParentPanel != newParentPanel) {
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

        // check if model have been resized
        if (node.isResizable() && oldGraphicWidth != 0 && oldGraphicHeight != 0) {
            // set node to new size
            node.setGraphicPixelSize(oldGraphicWidth, oldGraphicHeight);
            // set size in data model
            node.getModel().setChangeListenerSize(oldGraphicWidth,
                    oldGraphicHeight);
        }

        // select node if was selected before
        if (selected) {
            SelectionHandler.getInstance().select(node);
        }
    }

    @Override
    public void execute() {
        // get selected state
        boolean selected = node.isSelected();

        if (oldParentPanel != newParentPanel) {
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
                oldParentPanel.getHasChildModelsModel().removeNodeModel(
                        node.getModel());
                newParentPanel.getHasChildModelsModel().addNodeModel(
                        node.getModel());
            } catch (InvalidTypeException e) {
                e.printStackTrace();
            }
        }

        // set node to new position
        node.setPosition(newNodeLeft, newNodeTop);
        // set position data in model
        node.getModel().setChangeListenerPosition(newNodeLeft, newNodeTop);

        // check if model have been resized
        if (node.isResizable() && oldGraphicWidth != 0 && oldGraphicHeight != 0) {
            // set node to new size
            node.setGraphicPixelSize(newGraphicWidth, newGraphicHeight);
            // set size in data model
            node.getModel().setChangeListenerSize(newGraphicWidth,
                    newGraphicHeight);
        }

        // select node if it was selected before
        if (selected) {
            SelectionHandler.getInstance().select(node);
            // SelectionHandler.getInstance().refreshSelection();
        }

    }
}
