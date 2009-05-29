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

package de.decidr.modelingtool.client.ui.selection;

import java.util.List;
import java.util.Vector;

import de.decidr.modelingtool.client.ui.Node;
import de.decidr.modelingtool.client.ui.Workflow;

/**
 * TODO: add comment
 * 
 * @author JE
 */
public class NodeSelectionBox {

    private Workflow parentWorkflow;

    private List<DragBox> dragBoxes = new Vector<DragBox>();
    
    private Node assignedNode = null;

    public NodeSelectionBox(Workflow parentWorkflow) {
        this.parentWorkflow = parentWorkflow;

        dragBoxes.add(new DragBox(DragBox.ResizeDirection.NORTH));
        dragBoxes.add(new DragBox(DragBox.ResizeDirection.NORTHEAST));
        dragBoxes.add(new DragBox(DragBox.ResizeDirection.EAST));
        dragBoxes.add(new DragBox(DragBox.ResizeDirection.SOUTHEAST));
        dragBoxes.add(new DragBox(DragBox.ResizeDirection.SOUTH));
        dragBoxes.add(new DragBox(DragBox.ResizeDirection.SOUTHWEST));
        dragBoxes.add(new DragBox(DragBox.ResizeDirection.WEST));
        dragBoxes.add(new DragBox(DragBox.ResizeDirection.NORTHWEST));
    }

    public void update() {
        assignTo(assignedNode);
    }
    
    /**
     * Removes the drag boxes from the workflow.
     */
    public void unassign() {

    }

    /**
     * 
     * Assigns the dragBoxes to the specified node on the workflow.
     *
     * @param node The node which the drag boxes are assigned to.
     */
    public void assignTo(Node node) {
        this.assignedNode = node;
        
        int nodeTop = node.getGraphicTop();
        int nodeLeft = node.getGraphicLeft();
        int nodeWidth = node.getGraphicWidth();
        int nodeHeight = node.getGraphicHeight();
        int width;
        int height;
        int left = 0;
        int top = 0;

        for (DragBox dragBox : dragBoxes) {
            dragBox.setVisible(true);
            parentWorkflow.add(dragBox);

            width = dragBox.getOffsetWidth();
            height = dragBox.getOffsetHeight();

            switch (dragBox.getDirection()) {
            case NORTH:
                top = nodeTop - height;
                left = nodeLeft + nodeWidth / 2 - width / 2;
                break;
            case NORTHEAST:
                top = nodeTop - height;
                left = nodeLeft + nodeWidth;
                break;
            case EAST:
                top = nodeTop + nodeHeight / 2 - height / 2;
                left = nodeLeft + nodeWidth;
                break;
            case SOUTHEAST:
                top = nodeTop + nodeHeight;
                left = nodeLeft + nodeWidth;
                break;
            case SOUTH:
                top = nodeTop + nodeHeight;
                left = nodeLeft + nodeWidth / 2 - width / 2;
                break;
            case SOUTHWEST:
                top = nodeTop + nodeHeight;
                left = nodeLeft - width;
                break;
            case WEST:
                top = nodeTop + nodeHeight / 2 - height / 2;
                left = nodeLeft - width;
                break;
            case NORTHWEST:
                top = nodeTop - height;
                left = nodeLeft - width;
                break;
            }

            parentWorkflow.setWidgetPosition(dragBox, left, top);

        }
    }

    public List<DragBox> getDragBoxes() {
        return dragBoxes;
    }

}
