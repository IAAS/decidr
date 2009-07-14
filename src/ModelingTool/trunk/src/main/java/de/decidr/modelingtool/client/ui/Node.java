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

import com.allen_sauer.gwt.dnd.client.DragController;
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

import de.decidr.modelingtool.client.command.CommandList;
import de.decidr.modelingtool.client.command.UndoableCommand;
import de.decidr.modelingtool.client.model.NodeModel;
import de.decidr.modelingtool.client.ui.dnd.DndRegistry;

/**
 * This class represents an abstract node in a workflow.
 * 
 * @author JE
 */
public abstract class Node extends AbsolutePanel implements
        ModelChangeListener, Selectable, HasMouseDownHandlers,
        HasMouseOutHandlers {

    protected final int BORDER_OFFSET = 5;

    protected FocusPanel graphic = null;

    private boolean selected = false;

    private boolean resizable = false;

    private boolean deletable = true;

    private boolean moveable = true;

    // private List<Port> ports = new Vector<Port>();

    private InputPort inputPort = null;

    private OutputPort outputPort = null;

    private HasChildren parentPanel = null;

    protected NodeModel model = null;

    public NodeModel getModel() {
        return model;
    }

    public void setModel(NodeModel model) {
        this.model = model;
    }

    public Node(HasChildren parentPanel) {
        super();
        this.parentPanel = parentPanel;
    }

    @Override
    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return addDomHandler(handler, MouseDownEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return addDomHandler(handler, MouseOutEvent.getType());
    }

    public void addSelectionHandler(MouseDownHandler selectionHandler) {
        graphic.addMouseDownHandler(selectionHandler);
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

    // public int getGraphicAbsoluteLeft() {
    // return graphic.getAbsoluteLeft()
    // - Workflow.getInstance().getAbsoluteLeft();
    // }

    public void setParentPanel(HasChildren parentPanel) {
        this.parentPanel = parentPanel;
    }

    // public int getGraphicAbsoluteTop() {
    // return graphic.getAbsoluteTop()
    // - Workflow.getInstance().getAbsoluteTop();
    // }

    public int getGraphicLeft() {
        if (graphic != null) {
            return getLeft() + getWidgetLeft(graphic);
        } else {
            return 0;
        }
    }

    public int getGraphicTop() {
        if (graphic != null) {
            return getTop() + getWidgetTop(graphic);
        } else {
            return 0;
        }
    }

    // protected void addPort(Port port) {
    // ports.add(port);
    // this.add(port);
    //
    // // set this node as parent
    // port.setParentNode(this);
    // }

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

    public InputPort getInputPort() {
        return inputPort;
    }

    public int getLeft() {
        if (getParent() instanceof AbsolutePanel) {
            return ((AbsolutePanel) this.getParent()).getWidgetLeft(this);
        } else {
            return 0;
        }
    }

    public OutputPort getOutputPort() {
        return outputPort;
    }

    public int getTop() {
        if (getParent() instanceof AbsolutePanel) {
            return ((AbsolutePanel) this.getParent()).getWidgetTop(this);
        } else {
            return 0;
        }
    }

    public void setPosition(int left, int top) {
        if (getParent() instanceof AbsolutePanel) {
            ((AbsolutePanel) getParent()).setWidgetPosition(this, left, top);
            refreshConnections();
            if (isSelected()) {
                Workflow.getInstance().getSelectionHandler().refreshSelection();
            }
        }
    }

    public boolean hasContainer() {
        return (getParent() instanceof Container);
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
     * Callback function for the parent panel, this function is called when the
     * node is added to a workflow or container.
     */
    public void onPanelAdd(HasChildren parentPanel) {
        this.parentPanel = parentPanel;

        // set pixel size, this can only be set after setting a graphic and
        // adding the node to a panel
        // if (graphic != null) {
        // this.setPixelSize(graphic.getOffsetWidth() + BORDER_OFFSET * 2,
        // graphic.getOffsetHeight() + BORDER_OFFSET * 2);
        // }

        // register port drop controllers
        if (inputPort != null && !inputPort.isDropControllerRegistered()) {
            inputPort.registerDropController();
        }
        if (outputPort != null && !outputPort.isDropControllerRegistered()) {
            outputPort.registerDropController();
        }

        // refresh the port positions
        // refreshPortPositions();

        refreshNodeSize();

        // Window.alert("onPaneladd");
    }

    public void setGraphicPixelSize(int width, int height) {
        if (graphic != null) {
            graphic.setPixelSize(width, height);
            refreshNodeSize();
        }
    }

    protected void refreshNodeSize() {
        // set pixel size, this can only be set after setting a graphic and
        // adding the node to a panel
        if (graphic != null) {
            this.setPixelSize(graphic.getOffsetWidth() + BORDER_OFFSET * 2,
                    graphic.getOffsetHeight() + BORDER_OFFSET * 2);
        }

        refreshPortPositions();
    }

    public void onPanelRemove() {
        // unregister port drop controllers
        if (inputPort != null && inputPort.isDropControllerRegistered()) {
            inputPort.unregisterDropController();
        }
        if (outputPort != null && outputPort.isDropControllerRegistered()) {
            outputPort.unregisterDropController();
        }
    }

    public HasChildren getParentPanel() {
        return parentPanel;
    }

    public void refreshConnections() {
        if (inputPort != null) {
            inputPort.refreshConnections();
        }
        if (outputPort != null) {
            outputPort.refreshConnections();
        }
    }

    protected void refreshPortPosition(Port port) {
        int portWidth = port.getOffsetWidth();
        int portHeight = port.getOffsetHeight();
        int xOffset = port.getXOffset();
        int yOffset = port.getYOffset();

        switch (port.getPosition()) {
        case TOP:
            this.setWidgetPosition(port, this.getOffsetWidth() / 2 - portWidth
                    / 2 + xOffset, BORDER_OFFSET - portHeight / 2 + yOffset);
            break;
        case LEFT:
            this.setWidgetPosition(port, BORDER_OFFSET - portWidth / 2
                    + xOffset, this.getOffsetHeight() / 2 - portHeight / 2
                    + yOffset);
            break;
        case RIGHT:
            this.setWidgetPosition(port, this.getOffsetWidth() - BORDER_OFFSET
                    - portWidth / 2 + xOffset, this.getOffsetHeight() / 2
                    - portHeight / 2 + yOffset);
            break;
        case BOTTOM:
            this.setWidgetPosition(port, this.getOffsetWidth() / 2 - portWidth
                    / 2 + xOffset, this.getOffsetHeight() - BORDER_OFFSET
                    - portHeight / 2 + yOffset);
            break;

        case ABSOLUTE:
            this.setWidgetPosition(port, xOffset, yOffset);
            break;
        }
    }

    /**
     * Sets the ports of the node to it's specified position. Useful if the node
     * is resized.
     */
    protected void refreshPortPositions() {
        if (inputPort != null) {
            refreshPortPosition(inputPort);
        }
        if (outputPort != null) {
            refreshPortPosition(outputPort);
        }

        // refresh connections
        refreshConnections();
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

        // bring ports to front, if present
        if (inputPort != null) {
            add(inputPort);
        }
        if (outputPort != null) {
            add(outputPort);
        }
    }

    protected void setInputPort(InputPort inputPort) {
        this.inputPort = inputPort;

        this.add(inputPort);
        inputPort.setParentNode(this);
    }

    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }

    protected void setOutputPort(OutputPort outputPort) {
        this.outputPort = outputPort;

        this.add(outputPort);
        outputPort.setParentNode(this);
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public UndoableCommand getRemoveConnectionsCommand() {
        CommandList cmdList = new CommandList();

        if (inputPort != null) {
            cmdList.addCommand(inputPort.getRemoveConnectionsCommand());
        }
        if (outputPort != null) {
            cmdList.addCommand(outputPort.getRemoveConnectionsCommand());
        }

        return cmdList;
    }

    public void makeDraggable() {
        DragController dc = DndRegistry.getInstance().getDragController(
                "WorkflowDragController");
        dc.makeDraggable(this, getGraphic());
    }

}
