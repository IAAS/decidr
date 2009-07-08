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

import de.decidr.modelingtool.client.model.NodeModel;
import de.decidr.modelingtool.client.ui.Node;
import de.decidr.modelingtool.client.ui.Workflow;

/**
 * TODO: add comment
 * 
 * @author JE
 */
public class RemoveNodeCommand implements UndoableCommand {

    private Node node;

    private NodeModel model;

    private int nodeLeft;
    private int nodeTop;

    private boolean selected;

    private UndoableCommand removeConnectionsCmd;

    public RemoveNodeCommand(Node node) {
        this.node = node;
        this.model = node.getModel();

        this.nodeLeft = node.getLeft();
        this.nodeTop = node.getTop();

        this.selected = node.isSelected();

        removeConnectionsCmd = node.getRemoveConnectionsCommand();
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
            Workflow.getInstance().getSelectionHandler().select(node);
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
            Workflow.getInstance().getSelectionHandler().unselect();
        }

        // remove node from parent panel
        node.getParentPanel().removeNode(node);

        // unlink model
        model.getParentModel().removeNodeModel(model);
    }

}
