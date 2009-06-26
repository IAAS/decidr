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
import de.decidr.modelingtool.client.command.MoveNodeCommand;
import de.decidr.modelingtool.client.ui.Node;
import de.decidr.modelingtool.client.ui.Workflow;

/**
 * TODO: add comment
 * 
 * @author JE
 */
public class WorkflowDragController extends PickupDragController {

    private Workflow workflow;

    private Node node;

    private int oldNodeLeft;
    private int oldNodeTop;

    /**
     * TODO: add comment
     * 
     * @param boundaryPanel
     * @param allowDroppingOnBoundaryPanel
     */
    public WorkflowDragController(Workflow workflow) {
        super(workflow, true);

        this.workflow = workflow;
    }

    @Override
    public void dragEnd() {
        super.dragEnd();

        // create move command
        if (node != null) {
            CommandStack.getInstance().executeCommand(
                    new MoveNodeCommand(node, oldNodeLeft, oldNodeTop));
        }

        // remove the drag boxes assiged to the drag context.
        this.context.selectedWidgets.removeAll(workflow.getSelectionHandler()
                .getDragBoxes());

        // reset the drag boxes
        workflow.getSelectionHandler().refreshSelection();
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
            oldNodeLeft = node.getLeft();
            oldNodeTop = node.getTop();
        }

        // add the drag boxes to the drag context to move them with the object
        this.context.selectedWidgets.addAll(workflow.getSelectionHandler()
                .getDragBoxes());

        super.dragStart();
    }

}
