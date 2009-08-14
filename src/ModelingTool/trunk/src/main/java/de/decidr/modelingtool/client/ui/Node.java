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

import org.jaxen.Navigator;

import com.allen_sauer.gwt.dnd.client.DragController;
import com.google.gwt.core.client.GWT;
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

import de.decidr.modelingtool.client.browserspecific.BrowserSpecificTools;
import de.decidr.modelingtool.client.command.CommandList;
import de.decidr.modelingtool.client.command.CommandStack;
import de.decidr.modelingtool.client.command.MoveNodeCommand;
import de.decidr.modelingtool.client.command.UndoableCommand;
import de.decidr.modelingtool.client.exception.ModelingToolException;
import de.decidr.modelingtool.client.model.NodeModel;
import de.decidr.modelingtool.client.ui.dnd.DndRegistry;
import de.decidr.modelingtool.client.ui.selection.SelectionHandler;

/**
 * This class represents an abstract node in a workflow.
 * 
 * @author Johannes Engelhardt
 */
public abstract class Node extends AbsolutePanel implements
        ModelChangeListener, Selectable, HasMouseDownHandlers,
        HasMouseOutHandlers {

    /** The width of the border that surrounds the graphic widget. */
    protected final int BORDER_OFFSET = 5;

    /** The graphic widget. */
    protected FocusPanel graphic = null;

    /** The selected flag. */
    private boolean selected = false;

    /** The resizable flag. Default is false. */
    private boolean resizable = false;

    /** The deletable flag. Default is true. */
    private boolean deletable = true;

    /** The movable flag. Default is true. */
    private boolean moveable = true;

    /** The input port. */
    private InputPort inputPort = null;

    /** The output port. */
    private OutputPort outputPort = null;

    /** The parent panel. */
    private HasChildren parentPanel = null;

    /** The model assigned to the node. */
    protected NodeModel model = null;

    /**
     * Constructor with the parent panel of the node.
     * 
     * @param parentPanel
     *            Parent panel the node is (later) assigned to.
     */
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

    /**
     * Adds a selection handler to the node.
     * 
     * @param selectionHandler
     *            The selection handler
     */
    public void addSelectionHandler(MouseDownHandler selectionHandler) {
        graphic.addMouseDownHandler(selectionHandler);
    }

    public Widget getGraphic() {
        return graphic;
    }

    /**
     * Returns the height of the graphic.
     * 
     * @return the height of the graphic in pixels.
     */
    public int getGraphicHeight() {
        // JE: check
        assert (graphic != null);

        if (graphic != null) {
            return graphic.getOffsetHeight();
        } else {
            // return 0 if no graphic set
            return 0;
        }
    }

    /**
     * Returns the x coordinate of the graphic relative to the parent element of
     * the node.
     * 
     * @return the x coordinate of the graphic in pixels.
     */
    public int getGraphicLeft() {
        // JE: check
        assert (graphic != null);

        if (graphic != null) {
            return getLeft() + getWidgetLeft(graphic);
        } else {
            return 0;
        }
    }

    /**
     * Returns the y coordinate of the graphic relative to the parent element of
     * the node.
     * 
     * @return the y coordinate of the graphic in pixels.
     */
    public int getGraphicTop() {
        // JE: check
        assert (graphic != null);

        if (graphic != null) {
            return getTop() + getWidgetTop(graphic);
        } else {
            return 0;
        }
    }

    /**
     * Returns the height of the graphic.
     * 
     * @return the height of the graphic in pixels.
     */
    public int getGraphicWidth() {
        // JE: check
        assert (graphic != null);

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

    /**
     * Returns the x coordinate of this node relative to its parent element.
     * 
     * @return The x coordinate in pixels.
     */
    public int getLeft() {
        // JE: check
        assert (getParent() instanceof AbsolutePanel);

        if (getParent() instanceof AbsolutePanel) {
            return ((AbsolutePanel) this.getParent()).getWidgetLeft(this);
        } else {
            return 0;
        }
    }

    public NodeModel getModel() {
        return model;
    }

    public OutputPort getOutputPort() {
        return outputPort;
    }

    public HasChildren getParentPanel() {
        return parentPanel;
    }

    /**
     * Creates and returns a command for deleteing all connections connected to
     * the node.
     * 
     * @return The command containing the connection delete commands.
     */
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

    /**
     * Returns the x coordinate of this node relative to its parent element.
     * 
     * @return The x coordinate in pixels.
     */
    public int getTop() {
        // JE: check
        assert (getParent() instanceof AbsolutePanel);

        if (getParent() instanceof AbsolutePanel) {
            return ((AbsolutePanel) this.getParent()).getWidgetTop(this);
        } else {
            return 0;
        }
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

    /**
     * Makes the node draggable.
     */
    public void makeDraggable() {
        DragController dc = DndRegistry.getInstance().getDragController(
                "WorkflowDragController");
        dc.makeDraggable(this, getGraphic());
    }

    @Override
    public void onModelChange() {
        int modelLeft = model.getChangeListenerLeft();
        int modelTop = model.getChangeListenerTop();
        int left = getLeft();
        int top = getTop();
        HasChildren parentPanel = getParentPanel();

        // if position parameters in model have changed
        if (modelLeft != left || modelTop != top) {
            // set node to new position
            setPosition(modelLeft, modelTop);
            // and new parent, if changed
            setParentPanel(model.getParentModel()
                    .getHasChildrenChangeListener());

            // create move command
            CommandStack.getInstance().executeCommand(
                    new MoveNodeCommand(this, parentPanel, left, top));
        }

    }

    /**
     * Callback function for the parent panel, this function is called when the
     * node is added to a workflow or container.
     */
    public void onPanelAdd(HasChildren parentPanel) {
        this.parentPanel = parentPanel;

        // register port drop controllers
        if (inputPort != null && !inputPort.isDropControllerRegistered()) {
            inputPort.registerDropController();
        }
        if (outputPort != null && !outputPort.isDropControllerRegistered()) {
            outputPort.registerDropController();
        }

        // refresh the size of the node.
        refreshNodeSize();
    }

    /**
     * Callback function for the parent panel, this function is called when the
     * node is removed from its parent element.
     */
    public void onPanelRemove() {
        // unregister port drop controllers
        if (inputPort != null && inputPort.isDropControllerRegistered()) {
            inputPort.unregisterDropController();
        }
        if (outputPort != null && outputPort.isDropControllerRegistered()) {
            outputPort.unregisterDropController();
        }
    }

    /**
     * Redraws all connected connections.
     */
    public void refreshConnections() {
        // refresh all connections
        if (inputPort != null) {
            inputPort.refreshConnections();
        }
        if (outputPort != null) {
            outputPort.refreshConnections();
        }
    }

    /**
     * Refreshes the node size and adapts the graphic.
     */
    protected void refreshNodeSize() {
        // set pixel size, this can only be set after setting a graphic and
        // adding the node to a panel
        // JE: check
        assert (graphic != null);

        if (graphic != null) {
            this.setPixelSize(graphic.getOffsetWidth() + BORDER_OFFSET * 2,
                    graphic.getOffsetHeight() + BORDER_OFFSET * 2);
        }

        refreshPortPositions();
    }

    /**
     * Refreshes the position of the given port.
     * 
     * @param port
     *            The port to refresh
     */
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
        
        port.refreshConnections();
    }

    /**
     * Sets the ports of the node to its specified position. Useful if the node
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

    /**
     * Sets the pixel size of the graphic.
     * 
     * @param width
     *            The desired width of the graphic
     * @param height
     *            The desired height of the graphic
     */
    public void setGraphicPixelSize(int width, int height) {
        if (graphic != null) {
            BrowserSpecificTools bsTools = GWT.create(BrowserSpecificTools.class);
            
            graphic.setPixelSize(bsTools.correctBorderOffset(width), bsTools.correctBorderOffset(height));
            refreshNodeSize();
        }
    }

    protected void setInputPort(InputPort inputPort) {
        this.inputPort = inputPort;

        this.add(inputPort);
        inputPort.setParentNode(this);
    }

    public void setModel(NodeModel model) {
        this.model = model;
        model.setNode(this);
    }

    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }

    protected void setOutputPort(OutputPort outputPort) {
        this.outputPort = outputPort;

        this.add(outputPort);
        outputPort.setParentNode(this);
    }

    public void setParentPanel(HasChildren parentPanel) {
        this.parentPanel = parentPanel;
    }

    /**
     * Sets the position of the node relative to its parent panel.
     * 
     * @param left
     *            The desired x coordinate of the node
     * @param top
     *            The desired y coordinate of the node
     */
    public void setPosition(int left, int top) {
        // JE: check
        assert (getParent() instanceof AbsolutePanel);

        if (getParent() instanceof AbsolutePanel) {
            ((AbsolutePanel) getParent()).setWidgetPosition(this, left, top);
            refreshConnections();
            if (isSelected()) {
                SelectionHandler.getInstance().refreshSelection();
            }
        }
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
