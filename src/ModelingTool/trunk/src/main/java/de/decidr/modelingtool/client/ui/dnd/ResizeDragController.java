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

import com.allen_sauer.gwt.dnd.client.AbstractDragController;

import de.decidr.modelingtool.client.command.CommandStack;
import de.decidr.modelingtool.client.command.MoveResizeNodeCommand;
import de.decidr.modelingtool.client.ui.HasChildren;
import de.decidr.modelingtool.client.ui.Node;
import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.selection.DragBox;
import de.decidr.modelingtool.client.ui.selection.SelectionHandler;

/**
 * The drag controller for resizing containers.
 * 
 * @author Johannes Engelhardt
 */
public class ResizeDragController extends AbstractDragController {

    private Node node = null;
    private HasChildren oldParentPanel;
    private int oldLeft;
    private int oldTop;
    private int oldWidth;
    private int oldHeight;

    public ResizeDragController(Workflow workflow) {
        super(workflow);
    }

    @Override
    public void dragEnd() {
        // create command
        CommandStack.getInstance().addCommand(
                new MoveResizeNodeCommand(node, oldParentPanel, oldLeft,
                        oldTop, oldWidth, oldHeight));

        super.dragEnd();
    }

    @Override
    public void dragStart() {
        // make sure that selected item is a node
        assert SelectionHandler.getInstance().getSelectedItem() instanceof Node;
        node = (Node) SelectionHandler.getInstance().getSelectedItem();

        // get parent panel of node
        oldParentPanel = node.getParentPanel();
        // get graphic size of the node
        oldWidth = node.getGraphicWidth();
        oldHeight = node.getGraphicHeight();
        // get node position
        oldLeft = node.getLeft();
        oldTop = node.getTop();

        super.dragStart();
    }

    @Override
    public void dragMove() {
        if (context.draggable instanceof DragBox) {
            DragBox dragBox = (DragBox) context.draggable;

            // make sure that selected item is a node
            // assert SelectionHandler.getInstance().getSelectedItem()
            // instanceof Node;
            // Node node = (Node)
            // SelectionHandler.getInstance().getSelectedItem();

            // get graphic size of the node
            int width = node.getGraphicWidth();
            int height = node.getGraphicHeight();
            // get node position
            int left = node.getLeft();
            int top = node.getTop();

            // get drag box position
            int boxtop = dragBox.getAbsoluteTop();
            int boxleft = dragBox.getAbsoluteLeft();

            // get dragging delta
            int deltaX = context.desiredDraggableX - boxleft;
            int deltaY = context.desiredDraggableY - boxtop;

            // move and resize node
            switch (dragBox.getDirection()) {
            case NORTH:
                if (deltaY != 0) {
                    node.setPosition(left, top + deltaY);
                    node.setGraphicPixelSize(width, height - deltaY);
                }
                break;

            case NORTHEAST:
                if (deltaX != 0 || deltaY != 0) {
                    node.setPosition(left, top + deltaY);
                    node.setGraphicPixelSize(width + deltaX, height - deltaY);
                }
                break;

            case EAST:
                if (deltaX != 0) {
                    node.setGraphicPixelSize(width + deltaX, height);
                }
                break;

            case SOUTHEAST:
                if (deltaX != 0 || deltaY != 0) {
                    node.setGraphicPixelSize(width + deltaX, height + deltaY);
                }
                break;

            case SOUTH:
                if (deltaY != 0) {
                    node.setGraphicPixelSize(width, height + deltaY);
                }
                break;

            case SOUTHWEST:
                if (deltaX != 0 || deltaY != 0) {
                    // node.setPosition(left + deltaX, top);
                    node.setPosition(left + deltaX, top);
                    node.setGraphicPixelSize(width - deltaX, height + deltaY);
                }
                break;

            case WEST:
                if (deltaX != 0) {
                    node.setPosition(left + deltaX, top);
                    node.setGraphicPixelSize(width - deltaX, height);
                }
                break;

            case NORTHWEST:
                if (deltaX != 0 || deltaY != 0) {
                    node.setPosition(left + deltaX, top + deltaY);
                    node.setGraphicPixelSize(width - deltaX, height - deltaY);
                }
                break;
            }

            // refresh selection
            SelectionHandler.getInstance().refreshSelection();
        }
    }

}
