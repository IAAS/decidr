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

    private final int OFFSET_PIXEL = 10;

    /**
     * TODO: add comment
     */
    private Widget graphic;// = new AbsolutePanel();

    /**
     * TODO: add comment
     */
    private boolean selected = false;

    /**
     * TODO: add comment
     */
    private boolean resizable = false;

    /**
     * TODO: add comment
     */
    private boolean deletable = true;

    /**
     * TODO: add comment
     */
    private boolean moveable = true;

    public Node() {
        super();

        // set graphic properties
        graphic = new AbsolutePanel();
        graphic.addStyleName("node-stdgraphic");

        // add graphic to node
        this.add(graphic, OFFSET_PIXEL, OFFSET_PIXEL);
    }

    /**
     * Callback function for the workflow, this function is called when the node
     * is added to a workflow.
     */
    public void onWorkflowAdd() {
        this.setPixelSize(graphic.getOffsetWidth() + OFFSET_PIXEL * 2, graphic
                .getOffsetHeight()
                + OFFSET_PIXEL * 2);
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

    /**
     * TODO: add comment
     */
    private List<Port> ports;

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.ui.ModelChangeListener#onModelChange()
     */
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

}
