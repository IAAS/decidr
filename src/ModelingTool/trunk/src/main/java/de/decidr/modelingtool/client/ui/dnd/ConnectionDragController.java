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
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.user.client.ui.AbsolutePanel;

import de.decidr.modelingtool.client.ui.Connection;
import de.decidr.modelingtool.client.ui.OrthogonalConnection;
import de.decidr.modelingtool.client.ui.selection.ConnectionDragBox;

/**
 * TODO: add comment
 * 
 * @author JE
 */
public class ConnectionDragController extends PickupDragController {

    /**
     * The connection currently be dragged
     */
    Connection connection = null;

    /**
     * On drag start a connectionless dragbox is added to the port to be able to
     * drag new connections from that port at later time (if allowed).
     */
    ConnectionDragBox newDragBox = null;

    /**
     * TODO: add comment
     * 
     * @param boundaryPanel
     * @param allowDroppingOnBoundaryPanel
     */
    public ConnectionDragController(AbsolutePanel boundaryPanel) {
        super(boundaryPanel, false);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void dragMove() {
        // TODO Auto-generated method stub
        super.dragMove();

        if (connection != null) {
            connection.draw();
        }
    }

    @Override
    public void dragEnd() {
        super.dragEnd();

        if (context.draggable instanceof ConnectionDragBox) {
            ConnectionDragBox draggedDragBox = (ConnectionDragBox) context.draggable;
            draggedDragBox.setVisibleStyle(false);

            // if drag box is assigned to a connection
            if (connection != null) {
                // get other drag box
                ConnectionDragBox otherDragBox;
                if (connection.getStartDragBox() != draggedDragBox) {
                    otherDragBox = connection.getStartDragBox();
                } else {
                    otherDragBox = connection.getEndDragBox();
                }

                // if box is dropped on an assigned port
                if (context.finalDropController != null) {

                    // TODO: select connection

                } else {
                    // remove recently added new dag box because the dragged
                    // drag box returns to its port connectionless
                    // newDragBox.getGluedPort().remove(newDragBox);

                    // unglue and delete connection with drag boxes
                    // delete other drag box
                    otherDragBox.getGluedPort().remove(otherDragBox);
                    otherDragBox.setGluedPort(null);
                    otherDragBox.setConnection(null);

                    // delete dragged drag box
                    draggedDragBox.getGluedPort().remove(draggedDragBox);
                    draggedDragBox.setGluedPort(null);
                    draggedDragBox.setConnection(null);

                    // delete connection
                    connection.delete();
                    connection = null;
                }

            }
        }
    }

    @Override
    public void dragStart() {
        if (context.draggable instanceof ConnectionDragBox) {
            ConnectionDragBox draggedDragBox = (ConnectionDragBox) context.draggable;
            draggedDragBox.setVisibleStyle(true);

            if (draggedDragBox.getConnection() == null) {
                // create new connection
                connection = new OrthogonalConnection(draggedDragBox
                        .getGluedPort().getParentNode().getParentPanel());

                // set parent panel of connection to parent panel of involved
                // node
//                connection.setParentPanel(draggedDragBox.getGluedPort()
//                        .getParentNode().getParentPanel());

                // create start drag box
                ConnectionDragBox startDragBox = new ConnectionDragBox();
                // glue to port
                startDragBox.setGluedPort(draggedDragBox.getGluedPort());
                startDragBox.setVisibleStyle(true);
                // add to glued Port
                startDragBox.getGluedPort().add(startDragBox);
                // make dragbox draggable
                makeDraggable(startDragBox);

                // set drag boxes and add connection to workflow
                connection.setStartDragBox(startDragBox);
                startDragBox.setConnection(connection);
                connection.setEndDragBox(draggedDragBox);
                draggedDragBox.setConnection(connection);
                connection.getParentPanel().addConnection(connection);

                // create new drag box and add to the port from which the
                // dragged drag box is dragged from
                newDragBox = new ConnectionDragBox();
                newDragBox.setGluedPort(draggedDragBox.getGluedPort());
                newDragBox.getGluedPort().add(newDragBox);
                makeDraggable(newDragBox);

                // DEBUG: newDragBox.setStyleName("dragbox-debug");
            }

        }

        // create drag box and add to workflow
        // ConnectionDragBox startDragBox = new ConnectionDragBox();
        // Workflow.getInstance().add(startDragBox);

        super.dragStart();
    }

    @Override
    public void previewDragEnd() throws VetoDragException {
        // TODO Auto-generated method stub
        super.previewDragEnd();
    }

}
