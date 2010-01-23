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

package de.decidr.modelingtool.client.ui.dnd;

import com.allen_sauer.gwt.dnd.client.PickupDragController;

import de.decidr.modelingtool.client.command.CommandStack;
import de.decidr.modelingtool.client.command.MoveResizeNodeCommand;
import de.decidr.modelingtool.client.ui.HasChildren;
import de.decidr.modelingtool.client.ui.Node;
import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.selection.SelectionHandler;

/**
 * This drag controller is responsible for making all nodes in the workflow
 * draggable.
 * 
 * @author Johannes Engelhardt
 */
public class WorkflowDragController extends PickupDragController {

    /** The node to be dragged. */
    private Node node;

    /** The parent panel of the node before dragging. */
    private HasChildren oldParentPanel;
    /** The x coordinate of the node before dragging. */
    private int oldNodeLeft;
    /** The y coordinate of the node before dragging. */
    private int oldNodeTop;

    /**
     * The constructor.
     * 
     * @param workflow
     *            The workflow object
     */
    public WorkflowDragController(Workflow workflow) {
        super(workflow, true);
        this.setBehaviorDragStartSensitivity(1);
        this.setBehaviorMultipleSelection(false);
    }

    @Override
    public void dragEnd() {
        super.dragEnd();

        // if node is dropped on a legal position
        if (context.finalDropController != null) {
            // create move command
            if (node != null) {
                CommandStack.getInstance().executeCommand(
                        new MoveResizeNodeCommand(node, oldParentPanel,
                                oldNodeLeft, oldNodeTop));
            }

            // remove the drag boxes assiged to the drag context.
            this.context.selectedWidgets.removeAll(SelectionHandler
                    .getInstance().getDragBoxes());

            // reset the drag boxes
            SelectionHandler.getInstance().refreshSelection();
        }
    }

    @Override
    public void dragMove() {
        super.dragMove();

        // refresh all connection connected to the dragged node
        if (node != null) {
            node.refreshConnections();
        }
    }

    @Override
    public void dragStart() {
        // get draggable position of node before dragging
        if (context.draggable instanceof Node) {
            node = (Node) context.draggable;

            oldParentPanel = node.getParentPanel();
            oldNodeLeft = node.getLeft();
            oldNodeTop = node.getTop();
        }

        // add the drag boxes to the drag context to move them with the object
        this.context.selectedWidgets.addAll(SelectionHandler.getInstance()
                .getDragBoxes());

        super.dragStart();
    }

}
