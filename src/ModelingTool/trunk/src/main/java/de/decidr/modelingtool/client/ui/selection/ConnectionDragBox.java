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

import com.allen_sauer.gwt.dnd.client.DragController;

import de.decidr.modelingtool.client.ui.Connection;
import de.decidr.modelingtool.client.ui.ContainerExitPort;
import de.decidr.modelingtool.client.ui.ContainerStartPort;
import de.decidr.modelingtool.client.ui.InputPort;
import de.decidr.modelingtool.client.ui.OutputPort;
import de.decidr.modelingtool.client.ui.Port;
import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.dnd.DndRegistry;

/**
 * This drag box is used to drag connections. Every connection has a connection
 * drag box at each end.
 *
 * @author Johannes Engelhardt
 */
public class ConnectionDragBox extends DragBox {

    /** The port, the drag box is glued to. Null, if not glued. */
    private Port gluedPort = null;

    /** The connection, the drag box is assigned to. */
    private Connection connection = null;

    /**
     * The default constructor.
     */
    public ConnectionDragBox() {
        this(null);
    }

    /**
     * Initializes the drag box an sets the glued port.
     *
     * @param gluedPort The glued port
     */
    public ConnectionDragBox(Port gluedPort) {
        super(DragDirection.ALL);
        this.gluedPort = gluedPort;
        
        SelectionHandler sh = SelectionHandler.getInstance();
        this.addMouseDownHandler(sh);
    }

    public Port getGluedPort() {
        return gluedPort;
    }

    public void setGluedPort(Port gluedPort) {
        // unglue if glued
        if (this.gluedPort != null) {
            this.gluedPort.remove(this);
        }

        this.gluedPort = gluedPort;
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * Returns the workflow relative x coordinate of the center of the drag box.
     * 
     * @return The x coordinate
     */
    public int getMiddleLeft() {
        return this.getAbsoluteLeft() + this.getOffsetWidth() / 2
                - Workflow.getInstance().getAbsoluteLeft();
    }

    /**
     * Returns the workflow relative y coordinate of the center of the drag box.
     * 
     * @return The y coordinate
     */
    public int getMiddleTop() {
        return this.getAbsoluteTop() + this.getOffsetHeight() / 2
                - Workflow.getInstance().getAbsoluteTop();
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Sets the style of the drag box to normal or invisible.
     *
     * @param visible The visibility state of the drag box.
     */
    public void setVisibleStyle(boolean visible) {
        if (visible) {
            this.setStyleName("dragbox-port");
        } else {
            this.setStyleName("dragbox-invisible");
        }
    }

    /**
     * Gets the right drag controller from the dnd registry and makes this
     * draggable, if gluedPort is not null.
     */
    public void makeDraggable() {
        Port port = getGluedPort();
        DragController dc;

        if (port instanceof InputPort || port instanceof ContainerExitPort) {
            dc = DndRegistry.getInstance().getDragController(
                    "InputPortDragController");
            dc.makeDraggable(this);
        } else if (port instanceof OutputPort
                || port instanceof ContainerStartPort) {
            dc = DndRegistry.getInstance().getDragController(
                    "OutputPortDragController");
            dc.makeDraggable(this);
        }
    }
    
}
