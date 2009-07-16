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

package de.decidr.modelingtool.client.ui;

import java.util.Collection;
import java.util.HashSet;

import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

import de.decidr.modelingtool.client.command.CommandList;
import de.decidr.modelingtool.client.command.RemoveConnectionCommand;
import de.decidr.modelingtool.client.command.UndoableCommand;
import de.decidr.modelingtool.client.ui.dnd.PortDropController;
import de.decidr.modelingtool.client.ui.selection.ConnectionDragBox;

/**
 * TODO: add comment
 * 
 * @author engelhjs
 */
public abstract class Port extends AbsolutePanel {

    public static enum Position {
        TOP, LEFT, RIGHT, BOTTOM, ABSOLUTE
    };

    private Position position;
    private int xOffset = 0;
    private int yOffset = 0;

    private boolean multipleConnectionsAllowed = false;

    private Node parentNode = null;

    // private List<Connection> connections = new Vector<Connection>();

    private DropController dropController = new PortDropController(this);

    private Collection<ConnectionDragBox> gluedDragBoxes = new HashSet<ConnectionDragBox>();

    protected boolean dropControllerRegistered;

    /** has to be made draggable by subclasses. **/
    protected ConnectionDragBox singleDragBox;

    public Port(Position position) {
        this.position = position;

        // create connection drag box
        createSingleDragBox();
    }

    public Port(Position position, int xOffset, int yOffset) {
        this.position = position;
        this.xOffset = xOffset;
        this.yOffset = yOffset;

        // create connection drag box
        createSingleDragBox();
    }

    public void addConnectionDragBox(ConnectionDragBox dragBox) {
        assert dragBox.getConnection() != null;

        if (!multipleConnectionsAllowed) {
            //assert gluedDragBoxes.isEmpty();
            removeSingleDragBox();
        }

        //gluedDragBoxes.add(dragBox);
        add(dragBox);
        //setWidgetPosition(dragBox, 0, 0);
    }
    
    @Override
    public void add(Widget w) {
        super.add(w);
        setWidgetPosition(w, 0, 0);
        
        if (w instanceof ConnectionDragBox && w != singleDragBox) {
            gluedDragBoxes.add((ConnectionDragBox) w);
        }
    }

    // public void removeConnectionDragBox(ConnectionDragBox dragBox) {
    // if (dragBox == singleDragBox) {
    // singleDragBox = null;
    // } else {
    // gluedDragBoxes.remove(dragBox);
    // }
    // remove(dragBox);
    //
    // if (multipleConnectionsAllowed) {
    //
    // }
    // }

    @Override
    public boolean remove(Widget w) {
        boolean value = super.remove(w);

        if (w instanceof ConnectionDragBox) {
            ConnectionDragBox dragBox = (ConnectionDragBox) w;
            
            if (dragBox == singleDragBox) {
                singleDragBox = null;
            } else {
                // remove from glued dag boxes, if present
                gluedDragBoxes.remove(dragBox);
            }

            if (multipleConnectionsAllowed) {
                if (singleDragBox == null) {
                    createSingleDragBox();
                }
            } else {
                if (singleDragBox == null && gluedDragBoxes.isEmpty()) {
                    createSingleDragBox();
                }
            }

        }
        //gluedDragBoxes.remove((ConnectionDragBox)w);

        return value;
    }

    protected void createSingleDragBox() {
        // create drag box if drag box is not connected to a connection or not
        // present
        if (!(singleDragBox != null && singleDragBox.getConnection() == null)) {
            singleDragBox = new ConnectionDragBox(this);

            add(singleDragBox);
            //setWidgetPosition(singleDragBox, 0, 0);

            singleDragBox.setVisibleStyle(false);
            singleDragBox.makeDraggable();
        }
    }

    protected void removeSingleDragBox() {
        if (singleDragBox != null && singleDragBox.getConnection() == null) {
            super.remove(singleDragBox);
            singleDragBox = null;
        }
    }

    public DropController getDropController() {
        return dropController;
    }

    // ConnectionDragBox(this);

    public Collection<ConnectionDragBox> getGluedDragBoxes() {
        return gluedDragBoxes;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public Position getPosition() {
        return position;
    }

    public UndoableCommand getRemoveConnectionsCommand() {
        CommandList cmdList = new CommandList();

        for (ConnectionDragBox dragBox : gluedDragBoxes) {
            // check if drag box is not the drag box with out connection
            if (dragBox.getConnection() != null) {
                cmdList.addCommand(new RemoveConnectionCommand(dragBox
                        .getConnection()));
            }
        }

        return cmdList;
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }

    // public List<Connection> getConnections() {
    // return connections;
    // }

    public abstract boolean isContainerPort();

    public boolean isDropControllerRegistered() {
        return dropControllerRegistered;
    }

    public boolean isMultipleConnectionsAllowed() {
        return multipleConnectionsAllowed;
    }

    public void refreshConnections() {
        for (ConnectionDragBox dragBox : gluedDragBoxes) {
            if (dragBox.getConnection() != null) {
                dragBox.getConnection().draw();
            }
        }
    }

    public abstract void registerDropController();

    public void setMultipleConnectionsAllowed(boolean mcAllowed) {
        this.multipleConnectionsAllowed = mcAllowed;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    public void setXOffset(int offset) {
        xOffset = offset;
    }

    public void setYOffset(int offset) {
        yOffset = offset;
    }

    public abstract void unregisterDropController();

}
