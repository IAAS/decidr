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

import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class represents an abstract node in a workflow.
 * 
 * @author JE
 */
public abstract class Node extends AbsolutePanel implements
        ModelChangeListener, Selectable, HasMouseDownHandlers,
        HasMouseOutHandlers {

    private final int BORDER_OFFSET = 5;

    protected FocusPanel graphic = null;

    private boolean selected = false;

    private boolean resizable = false;

    private boolean deletable = true;

    private boolean moveable = true;

    private List<Port> ports = new Vector<Port>();
    
    public void refreshConnections() {
        for (Port port: ports) {
            port.refreshConnections();
        }
    }

    public Node() {
        super();

    }

    @Override
    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return addDomHandler(handler, MouseDownEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return addDomHandler(handler, MouseOutEvent.getType());
    }

    protected void addPort(Port port) {
        ports.add(port);
        this.add(port);

        // set this node as parent
        port.setParentNode(this);
    }

    public int getGraphicAbsoluteLeft() {
        int top = 0;

        if (this.hasContainer()) {
            top += this.getContainer().getLeft();
        }

        if (graphic != null) {
            top += this.getLeft() + this.getWidgetLeft(graphic);
        } 
        
        return top;
    }

    public int getGraphicAbsoluteTop() {
        int top = 0;

        if (this.hasContainer()) {
            top += this.getContainer().getTop();
        }

        if (graphic != null) {
            top += this.getTop() + this.getWidgetTop(graphic);
        } 
        
        return top;
    }

    public Container getContainer() {
        if (this.hasContainer()) {
            return (Container) this.getParent();
        } else {
            return null;
        }
    }

    public Widget getGraphic() {
        return graphic;
    }

    public int getGraphicHeight() {
        if (graphic != null) {
            return graphic.getOffsetHeight();
        } else {
            // return 0 if no graphic set
            return 0;
        }
    }

    public int getGraphicWidth() {
        if (graphic != null) {
            return graphic.getOffsetWidth();
        } else {
            // return 0 if no graphic set
            return 0;
        }
    }

    public int getLeft() {
        if (this.getParent() instanceof AbsolutePanel) {
            return ((AbsolutePanel) this.getParent()).getWidgetLeft(this);
        } else {
            return 0;
        }
    }

    public int getTop() {
        if (this.getParent() instanceof AbsolutePanel) {
            return ((AbsolutePanel) this.getParent()).getWidgetTop(this);
        } else {
            return 0;
        }
    }

    public boolean hasContainer() {
        return (this.getParent() instanceof Container);
    }

    public boolean isDeletable() {
        return deletable;
    }

    public boolean isMoveable() {
        return moveable;
    }

    public boolean isResizable() {
        return resizable;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void onModelChange() {
        // TODO Auto-generated method stub

    }

    /**
     * Callback function for the workflow, this function is called when the node
     * is added to a workflow.
     */
    public void onPanelAdd(AbsolutePanel parentPanel) {
        // set pixel size, this can only be set after setting a graphic and
        // adding the node to a workflow
        if (graphic != null) {
            this.setPixelSize(graphic.getOffsetWidth() + BORDER_OFFSET * 2,
                    graphic.getOffsetHeight() + BORDER_OFFSET * 2);
        }

        // refresh the port positions
        refreshPortPositions();
    }

    /**
     * Sets the ports of the node to it's specified position. Useful if the node
     * is resized.
     */
    protected void refreshPortPositions() {
        for (Port port : ports) {
            int portWidth = port.getOffsetWidth();
            int portHeight = port.getOffsetHeight();
            int xOffset = port.getXOffset();
            int yOffset = port.getYOffset();

            switch (port.getPosition()) {
            case TOP:
                this.setWidgetPosition(port, this.getOffsetWidth() / 2
                        - portWidth / 2 + xOffset, BORDER_OFFSET - portHeight
                        / 2 + yOffset);
                break;
            case LEFT:
                this.setWidgetPosition(port, BORDER_OFFSET - portWidth / 2
                        + xOffset, this.getOffsetHeight() / 2 - portHeight / 2
                        + yOffset);
                break;
            case RIGHT:
                this.setWidgetPosition(port, this.getOffsetWidth()
                        - BORDER_OFFSET - portWidth / 2 + xOffset, this
                        .getOffsetHeight()
                        / 2 - portHeight / 2 + yOffset);
                break;
            case BOTTOM:
                this.setWidgetPosition(port, this.getOffsetWidth() / 2
                        - portWidth / 2 + xOffset, this.getOffsetHeight()
                        - BORDER_OFFSET - portHeight / 2 + yOffset);
                break;

            case ABSOLUTE:
                this.setWidgetPosition(port, xOffset, yOffset);
                break;
            }
        }
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public void setGraphic(FocusPanel graphic) {
        this.graphic = graphic;

        // set standard graphic properties if graphic not set by subclass
        if (this.graphic == null) {
            this.graphic = new FocusPanel();
            this.graphic.addStyleName("node-graphic-std");
        }

        // add graphic to node
        this.add(this.graphic, BORDER_OFFSET, BORDER_OFFSET);
    }

    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    public void addSelectionHandler(MouseDownHandler selectionHandler) {
        graphic.addMouseDownHandler(selectionHandler);
    }

}
