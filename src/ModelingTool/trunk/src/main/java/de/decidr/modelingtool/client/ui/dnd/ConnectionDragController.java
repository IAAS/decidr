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

import de.decidr.modelingtool.client.command.CommandStack;
import de.decidr.modelingtool.client.command.CreateConnectionCommand;
import de.decidr.modelingtool.client.command.MoveConnectionCommand;
import de.decidr.modelingtool.client.command.RemoveConnectionCommand;
import de.decidr.modelingtool.client.ui.Connection;
import de.decidr.modelingtool.client.ui.HasChildren;
import de.decidr.modelingtool.client.ui.OrthogonalConnection;
import de.decidr.modelingtool.client.ui.Port;
import de.decidr.modelingtool.client.ui.selection.ConnectionDragBox;
import de.decidr.modelingtool.client.ui.selection.SelectionHandler;

/**
 * This drag controller is used to drag the connections. All connection drag
 * boxes are made draggable by this class.
 * 
 * @author Johannes Engelhardt
 */
public class ConnectionDragController extends PickupDragController {

    /** The connection currently being dragged */
    private Connection connection = null;

    /**
     * Indicates if the connection being dragged is a new or an existing
     * connection.
     */
    private boolean existingConnection;

    private Port oldStartPort = null;
    private Port oldEndPort = null;

    /**
     * On drag start a connectionless dragbox is added to the port to be able to
     * drag new connections from that port at later time (if allowed).
     */
    // private ConnectionDragBox newDragBox = null;
    /**
     * The constructor.
     * 
     * @param boundaryPanel
     *            The boundary panel, should be the workflow itself.
     */
    public ConnectionDragController(AbsolutePanel boundaryPanel) {
        super(boundaryPanel, false);
        // setBehaviorDragStartSensitivity(1);
    }

    @Override
    public void dragMove() {
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
                    // check if connection has only been moved and was not
                    // created
                    if (existingConnection) {
                        assert oldStartPort != null;
                        assert oldEndPort != null;
                        CommandStack.getInstance().executeCommand(
                                new MoveConnectionCommand(connection,
                                        oldStartPort, oldEndPort));
                    } else {
                        // execute create connection command
                        CommandStack.getInstance().executeCommand(
                                new CreateConnectionCommand(connection));
                    }

                    // select connection
                    SelectionHandler.getInstance().select(connection);

                } else {
                    if (existingConnection) {
                        // execute remove connection command
                        CommandStack.getInstance().executeCommand(
                                new RemoveConnectionCommand(connection));
                    } else {
                        // unglue and delete connection with drag boxes
                        // delete other drag box
                        otherDragBox.getGluedPort().removeConnectionDragBox(
                                otherDragBox, true);
                        otherDragBox.setGluedPort(null);
                        otherDragBox.setConnection(null);

                        // delete dragged drag box
                        draggedDragBox.getGluedPort().removeConnectionDragBox(
                                draggedDragBox, true);
                        draggedDragBox.setGluedPort(null);
                        draggedDragBox.setConnection(null);

                        // delete connection
                        connection.remove();
                    }

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

            connection = draggedDragBox.getConnection();

            if (connection == null) {
                // connection is a new connection
                existingConnection = false;

                // get port of dragged drag box
                Port draggedPort = draggedDragBox.getGluedPort();

                // check if connection is within an inner container connection
                if (draggedPort.isContainerPort()
                        && draggedPort.getParentNode() instanceof HasChildren) {
                    // create connection on container
                    connection = new OrthogonalConnection(
                            (HasChildren) draggedPort.getParentNode());
                } else {
                    // create new connection
                    connection = new OrthogonalConnection(draggedPort
                            .getParentNode().getParentPanel());
                }

                // create other drag box
                ConnectionDragBox otherDragBox = new ConnectionDragBox();
                // glue to port
                otherDragBox.setGluedPort(draggedPort);
                otherDragBox.setVisibleStyle(true);
                // add to glued Port
                otherDragBox.getGluedPort().add(otherDragBox);

                // make dragbox draggable
                otherDragBox.makeDraggable(true);

                // set drag boxes and add connection to workflow
                otherDragBox.setConnection(connection);
                draggedDragBox.setConnection(connection);

                if (otherDragBox.getGluedPort().isOutputPort()) {
                    connection.setStartDragBox(otherDragBox);
                    connection.setEndDragBox(draggedDragBox);
                } else {
                    connection.setStartDragBox(draggedDragBox);
                    connection.setEndDragBox(otherDragBox);
                }

                // add connection to workflow
                connection.getParentPanel().addConnection(connection);

            } else {
                // connection is an existing connection
                existingConnection = true;
                oldStartPort = connection.getStartDragBox().getGluedPort();
                oldEndPort = connection.getEndDragBox().getGluedPort();
            }

            // remove dragged drag box from port
            draggedDragBox.getGluedPort().removeConnectionDragBox(
                    draggedDragBox, false);
        }

        super.dragStart();
    }

    @Override
    public void previewDragEnd() throws VetoDragException {
        super.previewDragEnd();
    }

}
