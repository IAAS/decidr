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

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbstractDropController;
import com.google.gwt.user.client.ui.Widget;

import de.decidr.modelingtool.client.ui.Port;
import de.decidr.modelingtool.client.ui.selection.ConnectionDragBox;

/**
 * This drop controller handles the drop operations of the connection drag boxes
 * on the ports. Every port has a port drop controller. A connection drag box is
 * only droppable on the port if the controller is registered to the drag
 * controller the connection drag box is dragged with.
 * 
 * @author Johannes Engelhardt
 */
public class PortDropController extends AbstractDropController {

    /**
     * The constructor.
     * 
     * @param dropTarget
     *            The port which is made to handle drop operations.
     */
    public PortDropController(Widget dropTarget) {
        super(dropTarget);
    }

    @Override
    public Widget getDropTarget() {
        return super.getDropTarget();
    }

    @Override
    public void onDrop(DragContext context) {
        super.onDrop(context);

        // dragged drag box
        ConnectionDragBox cdb = (ConnectionDragBox) context.draggable;
        // target port
        Port port = (Port) getDropTarget();
        // remove drag box from old port
        cdb.getGluedPort().remove(cdb);
        // add drag box to target port
        cdb.setGluedPort(port);

        port.addConnectionDragBox(cdb);

        // port.setWidgetPosition(cdb, 0, 0);

        // redraw connection
        cdb.getConnection().draw();
    }

    @Override
    public void onEnter(DragContext context) {
        super.onEnter(context);

        try {
            onPreviewDrop(context);
            // JE: andere Lösung!
            this.getDropTarget().setPixelSize(20, 20);
        } catch (VetoDragException e) {
            // drag item is not droppable on this port, perform no action
        }
    }

    @Override
    public void onLeave(DragContext context) {
        super.onLeave(context);

        this.getDropTarget().setPixelSize(8, 8);
    }

    @Override
    public void onMove(DragContext context) {
        super.onMove(context);
    }

    @Override
    public void onPreviewDrop(DragContext context) throws VetoDragException {
        super.onPreviewDrop(context);

        if (context.dropController.getDropTarget() instanceof Port) {
            Port port = (Port) context.dropController.getDropTarget();

            // cancel drop operation if there is already a connection connected
            // to the port and multiple connections are not allowed
            if (port != null && !port.isMultipleConnectionsAllowed()
                    && !port.getGluedDragBoxes().isEmpty()) {
                throw new VetoDragException();
            }

            // cancel drop operation if both ports belong to the same node
            if (context.draggable instanceof ConnectionDragBox) {
                ConnectionDragBox draggedDragBox = (ConnectionDragBox) context.draggable;

                // get the other drag box / other port
                ConnectionDragBox otherDragBox = draggedDragBox.getConnection()
                        .getOtherDragBox(draggedDragBox);
                Port otherPort = otherDragBox.getGluedPort();

                // check if both ports are on the same node
                if (port.getParentNode() == otherPort.getParentNode()) {
                    throw new VetoDragException();
                }
            }

            // cancel drop operation if the nodes of the ports are on different
            // panels
            if (context.draggable instanceof ConnectionDragBox) {
                ConnectionDragBox draggedDragBox = (ConnectionDragBox) context.draggable;

                // get the other drag box / other port
                ConnectionDragBox otherDragBox = draggedDragBox.getConnection()
                        .getOtherDragBox(draggedDragBox);
                Port otherPort = otherDragBox.getGluedPort();

                // get parentPanel of the port of the other drag box
                Widget otherParentPanel = (otherPort.isContainerPort()) ? otherPort
                        .getParent()
                        : otherPort.getParent().getParent();

                // get parentPanel of the drop port
                Widget parentPanel = (port.isContainerPort()) ? port
                        .getParent() : port.getParent().getParent();

                // check if parent panels are different
                if (parentPanel != otherParentPanel) {
                    throw new VetoDragException();
                }
            }

        }
    }
}
