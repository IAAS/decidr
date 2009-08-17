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

import de.decidr.modelingtool.client.exception.OperationNotAllowedException;
import de.decidr.modelingtool.client.model.NodeModel;
import de.decidr.modelingtool.client.ui.Node;
import de.decidr.modelingtool.client.ui.selection.SelectionHandler;

/**
 * This command removes a node from the workflow and all connections connected
 * to it.
 *
 * @author Johannes Engelhardt
 */
public class RemoveNodeCommand implements UndoableCommand {

    /** The node to remove. */
    private Node node;

    /** The model of the node to remove. */
    private NodeModel model;

    /** The x coordinate of the node. */
    private int nodeLeft;
    /** The y coordinate of the node. */
    private int nodeTop;

    /** The selected state of the node. */
    private boolean selected;

    /** This Command removes any connections connected to the node. */
    private UndoableCommand removeConnectionsCmd;
    
    private UndoableCommand removeChildNodesCmd;

    /**
     * Constructor for removing the node.
     *
     * @param node The node to remove.
     */
    public RemoveNodeCommand(Node node) throws OperationNotAllowedException {
        // check if the node is deletable.
        if (!node.isDeletable()) {
            throw new OperationNotAllowedException("This Node can't be deleted.");
        }
        
        this.node = node;
        this.model = node.getModel();

        this.nodeLeft = node.getLeft();
        this.nodeTop = node.getTop();

        this.selected = node.isSelected();

        removeConnectionsCmd = node.getRemoveDependentItemsCommand();
    }

    @Override
    public void undo() {
        // add node to parent panel and set position
        node.getParentPanel().addNode(node);
        node.setPosition(nodeLeft, nodeTop);

        // link model
        model.getParentModel().addNodeModel(model);

        // select node if was selected before
        if (selected) {
            SelectionHandler.getInstance().select(node);
        }

        // add all connections removed before
        removeConnectionsCmd.undo();
    }

    @Override
    public void execute() {
        // remove all connections from the node
        removeConnectionsCmd.execute();

        // unselect node if selected
        if (selected) {
            SelectionHandler.getInstance().unselect();
        }

        // remove node from parent panel
        node.getParentPanel().removeNode(node);

        // unlink model
        model.getParentModel().removeNodeModel(model);
    }

}
