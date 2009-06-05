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

import java.util.List;
import java.util.Vector;


import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.user.client.ui.FocusPanel;

import de.decidr.modelingtool.client.ui.dnd.PortDropController;


/**
 * TODO: add comment
 * 
 * @author engelhjs
 */
public class Port extends FocusPanel {
    
    public static enum Position {
        TOP, LEFT, RIGHT, BOTTOM, ABSOLUTE
    };
    
    private Position position;
    private int xOffset = 0;
    private int yOffset = 0;

    private boolean multipleConnectionsAllowed = false;

    private List<Connection> connections = new Vector<Connection>();

    private Node parentNode = null;

    private DropController dropController = new PortDropController(this);
    
    public Port(Position position) {
        this.position = position;
    }
    
    public Port(Position position, int xOffset, int yOffset) {
        this.position = position;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public DropController getDropController() {
        return dropController;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public Position getPosition() {
        return position;
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }

    public boolean isMultipleConnectionsAllowed() {
        return multipleConnectionsAllowed;
    }

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

}
