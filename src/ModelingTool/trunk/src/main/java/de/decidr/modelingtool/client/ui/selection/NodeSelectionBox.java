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
 * TODO: add comment
 * 
 * @author JE
 */
public class NodeSelectionBox {

    // private AbsolutePanel parentPanel;

    private List<DragBox> dragBoxes = new Vector<DragBox>();

    private Node assignedNode = null;

    public NodeSelectionBox() {
        // this.parentPanel = parentPanel;

        dragBoxes.add(new DragBox(DragBox.DragDirection.NORTH));
        dragBoxes.add(new DragBox(DragBox.DragDirection.NORTHEAST));
        dragBoxes.add(new DragBox(DragBox.DragDirection.EAST));
        dragBoxes.add(new DragBox(DragBox.DragDirection.SOUTHEAST));
        dragBoxes.add(new DragBox(DragBox.DragDirection.SOUTH));
        dragBoxes.add(new DragBox(DragBox.DragDirection.SOUTHWEST));
        dragBoxes.add(new DragBox(DragBox.DragDirection.WEST));
        dragBoxes.add(new DragBox(DragBox.DragDirection.NORTHWEST));

        for (DragBox dragBox : dragBoxes) {
            //dragBox.addStyleName("dragbox-node");
        }
    }

    // public void refreshPosition() {
    // int nodeTop = assignedNode.getGraphicAbsoluteTop();
    // int nodeLeft = assignedNode.getGraphicAbsoluteLeft();
    // refreshPosition(nodeTop, nodeLeft);
    // }

    // parameters are a workaround for dragging, cause position of the node
    // is always 0 during dragging -- UPDATE: removed parameters
    public void refreshPosition() throws NullPointerException {
        if (assignedNode != null) {
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
        } else {
            throw new NullPointerException(
                    "Cannot refresh position, assignedNode is null.");
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
        
        //Window.alert(dragBoxes.get(0).getStyleName());
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

            DragController rdc = DndRegistry.getInstance()
                    .getDragController("ResizeDragController");
            
            //dragBox.addStyleName("dragbox-node");

            if (node.isResizable()) {
                rdc.makeDraggable(dragBox);
            }
            
            dragBox.addStyleName("dragbox-node");
        }

        refreshPosition();
        
        //Window.alert(dragBoxes.get(0).getStyleName());
    }

    public List<DragBox> getDragBoxes() {
        return dragBoxes;
    }

}
