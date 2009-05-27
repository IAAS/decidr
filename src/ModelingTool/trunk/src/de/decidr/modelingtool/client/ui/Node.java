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
import com.google.gwt.user.client.ui.Widget;

/**
 * TODO: add comment
 * 
 * @author engelhjs
 */
public abstract class Node extends AbsolutePanel implements
        ModelChangeListener, Selectable, HasMouseDownHandlers,
        HasMouseOutHandlers {

    private final int BORDER_OFFSET = 5;

    private Widget graphic;// = new AbsolutePanel();

    private boolean selected = false;

    private boolean resizable = false;

    private boolean deletable = true;

    private boolean moveable = true;

    private List<Port> ports = new Vector<Port>();

    public Node() {
        super();

        // set graphic properties
        graphic = new AbsolutePanel();
        graphic.addStyleName("node-graphic-std");

        // add graphic to node
        this.add(graphic, BORDER_OFFSET, BORDER_OFFSET);
    }

    /**
     * Callback function for the workflow, this function is called when the node
     * is added to a workflow.
     */
    public void onWorkflowAdd() {
        // set pixel size, this can only be set after adding the node to a
        // workflow
        this.setPixelSize(graphic.getOffsetWidth() + BORDER_OFFSET * 2, graphic
                .getOffsetHeight()
                + BORDER_OFFSET * 2);
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public boolean isMoveable() {
        return moveable;
    }

    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }

    public boolean isResizable() {
        return resizable;
    }

    @Override
    public void onModelChange() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return addDomHandler(handler, MouseOutEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return addDomHandler(handler, MouseDownEvent.getType());
    }

    public void addPort(Port port) {
        //ports.add(port);
        //this.add(port);
        refreshPortPositions();
    }

    protected void refreshPortPositions() {
        for (Port port : ports) {
            int width = port.getOffsetWidth();
            int height = port.getOffsetHeight();

            switch (port.getPosition()) {
            case TOP:
                this.setWidgetPosition(port, this.getOffsetWidth() / 2 + width
                        / 2, BORDER_OFFSET - height / 2);
                break;
            case LEFT:
                break;
                // TODO
            case RIGHT:
                break;

            case BOTTOM:
                break;

            case ABSOLUTE:
                break;
            }
        }
    }

}
