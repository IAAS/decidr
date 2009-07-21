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

import com.allen_sauer.gwt.dnd.client.DragController;
import com.google.gwt.user.client.ui.AbsolutePanel;

import de.decidr.modelingtool.client.ui.Node;
import de.decidr.modelingtool.client.ui.dnd.DndRegistry;

/**
 * This class is responsible for the graphical representation of the selected
 * state of any node in the workflow. The selected node gets a 9-box assigned.
 * 
 * @author Johannes Engelhardt
 */
public class NodeSelectionBox {

    /** The List of the drag boxes. */
    private List<DragBox> dragBoxes = new Vector<DragBox>();

    /** The node currently selected. Null if none is selected. */
    private Node assignedNode = null;

    /**
     * The constructor.
     */
    public NodeSelectionBox() {
        // add drag boxes
        dragBoxes.add(new DragBox(DragBox.DragDirection.NORTH));
        dragBoxes.add(new DragBox(DragBox.DragDirection.NORTHEAST));
        dragBoxes.add(new DragBox(DragBox.DragDirection.EAST));
        dragBoxes.add(new DragBox(DragBox.DragDirection.SOUTHEAST));
        dragBoxes.add(new DragBox(DragBox.DragDirection.SOUTH));
        dragBoxes.add(new DragBox(DragBox.DragDirection.SOUTHWEST));
        dragBoxes.add(new DragBox(DragBox.DragDirection.WEST));
        dragBoxes.add(new DragBox(DragBox.DragDirection.NORTHWEST));
    }

    /**
     * Refreshes the position of the drag boxes. assignedNode must not be null.
     */
    public void refreshPosition() {
        // make sure that assignedNode is not null
        assert assignedNode != null;

        int nodeLeft = assignedNode.getGraphicLeft();
        int nodeTop = assignedNode.getGraphicTop();
        int nodeWidth = assignedNode.getGraphicWidth();
        int nodeHeight = assignedNode.getGraphicHeight();
        int width;
        int height;
        int left = 0;
        int top = 0;

        for (DragBox dragBox : dragBoxes) {
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

            AbsolutePanel parentPanel = (AbsolutePanel) assignedNode
                    .getParent();
            parentPanel.setWidgetPosition(dragBox, left, top);
        }
    }

    /**
     * Removes the drag boxes from the parent panel.
     */
    public void unassign() {
        for (DragBox dragBox : dragBoxes) {
            if (assignedNode != null) {
                // make box not draggable if it was before
                if (assignedNode.isResizable()) {
                    DragController rdc = DndRegistry.getInstance()
                            .getDragController("ResizeDragController");
                    rdc.makeNotDraggable(dragBox);
                }

                ((AbsolutePanel) dragBox.getParent()).remove(dragBox);
            }

        }

        assignedNode = null;
    }

    /**
     * 
     * Assigns the dragBoxes to the specified node on the workflow.
     * 
     * @param node
     *            The node which the drag boxes are assigned to.
     */
    public void assignTo(Node node) {
        unassign();
        this.assignedNode = node;

        for (DragBox dragBox : dragBoxes) {
            dragBox.setVisible(true);
            AbsolutePanel parentPanel = (AbsolutePanel) assignedNode
                    .getParent();
            parentPanel.add(dragBox);

            DragController rdc = DndRegistry.getInstance().getDragController(
                    "ResizeDragController");

            // dragBox.addStyleName("dragbox-node");

            if (node.isResizable()) {
                rdc.makeDraggable(dragBox);
            }

            dragBox.addStyleName("dragbox-node");
        }

        refreshPosition();

        // Window.alert(dragBoxes.get(0).getStyleName());
    }

    public List<DragBox> getDragBoxes() {
        return dragBoxes;
    }

}
