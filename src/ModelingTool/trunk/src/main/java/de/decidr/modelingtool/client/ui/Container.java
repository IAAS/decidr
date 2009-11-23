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

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.command.CommandList;
import de.decidr.modelingtool.client.command.RemoveNodeCommand;
import de.decidr.modelingtool.client.command.UndoableCommand;
import de.decidr.modelingtool.client.exception.InvalidTypeException;
import de.decidr.modelingtool.client.exception.OperationNotAllowedException;
import de.decidr.modelingtool.client.model.HasChildModels;
import de.decidr.modelingtool.client.ui.dnd.ContainerDropController;
import de.decidr.modelingtool.client.ui.dnd.DndRegistry;
import de.decidr.modelingtool.client.ui.selection.SelectionHandler;

/**
 * The basic class of every container. Provides attributes to manage children.
 * 
 * @author Johannes Engelhardt
 */
public abstract class Container extends Node implements HasChildren {

    /** The container start port. */
    private ContainerStartPort containerStartPort = null;

    /** The container exit port. */
    private ContainerExitPort containerExitPort = null;

    /** Collection of the child nodes. */
    private Collection<Node> childNodes = new HashSet<Node>();

    /** Collection of the child connections. */
    private Collection<Connection> childConnections = new HashSet<Connection>();

    /** The drop controller of the container. */
    private DropController dropController = new ContainerDropController(this);

    /**
     * Indicates if the drop controller of the container is registered to the
     * drag controller.
     */
    private boolean dropControllerRegistered = false;

    public Container(HasChildren parentPanel) {
        super(parentPanel);

        // set container graphic properties
        FocusPanel graphic = new FocusPanel();
        graphic.addStyleName("container-std");
        setGraphic(graphic);

        setInputPort(new InputPort());
        setOutputPort(new OutputPort());
        setContainerStartPort(new ContainerStartPort());
        setContainerExitPort(new ContainerExitPort());
    }

    @Override
    public void add(Widget w) {
        super.add(w);

        if (w instanceof Node) {
            Node node = (Node) w;
            // add node to the nodes collection
            childNodes.add(node);

            // callback to node after add
            node.onPanelAdd(this);
        }
    }

    @Override
    public void addConnection(Connection connection) {
        // add connection to the connections vector
        childConnections.add(connection);
        // callback to connection
        connection.onPanelAdd(this);

    }

    @Override
    public void addNode(Node node) {
        addNode(node, 0, 0);
    }

    @Override
    public void addNode(Node node, int left, int top) {
        super.add(node, left, top);

        // add node to the nodes collection
        childNodes.add(node);

        // register the selection Handler
        // must happen before makeDraggable because of handler order!!!
        node.addSelectionHandler(SelectionHandler.getInstance());

        // make node draggable, only draggable at graphic widget of the node
        if (node.isMoveable()) {
            node.makeDraggable();
        }

        // callback to node after add
        node.onPanelAdd(this);
    }

    @Override
    public Collection<Connection> getConnections() {
        return childConnections;
    }

    public ContainerExitPort getContainerExitPort() {
        return containerExitPort;
    }

    public ContainerStartPort getContainerStartPort() {
        return containerStartPort;
    }

    public DropController getDropController() {
        return dropController;
    }

    /**
     * Recursively gets the parent's offset (and all parent's parents' offset)
     * in x direction. Needed for placing child nodes correctly.
     * 
     * @return the offset value
     */
    public int getParentXOffset() {
        int result = this.getLeft();
        if (this.getParentPanel() instanceof Container) {
            result = result
                    + ((Container) this.getParentPanel()).getParentXOffset();
        }
        return result;
    }

    /**
     * Recursively gets the parent's offset (and all parent's parents' offset)
     * in y direction. Needed for placing child nodes correctly.
     * 
     * @return the offset value
     */
    public int getParentYOffset() {
        int result = this.getTop();
        if (this.getParentPanel() instanceof Container) {
            result = result
                    + ((Container) this.getParentPanel()).getParentYOffset();
        }
        return result;
    }

    @Override
    public HasChildModels getHasChildModelsModel() throws InvalidTypeException {
        if (model instanceof HasChildModels) {
            return (HasChildModels) model;
        } else {
            throw new InvalidTypeException("Model does not have children.");
        }
    }

    @Override
    public Collection<Node> getNodes() {
        return childNodes;
    }

    @Override
    public UndoableCommand getRemoveDependentItemsCommand() {
        CommandList cmdList = new CommandList();

        for (Node node : childNodes) {
            try {
                cmdList.addCommand(new RemoveNodeCommand(node));
            } catch (OperationNotAllowedException e) {
                Window.alert(ModelingToolWidget.getMessages()
                        .operationNotAllowedMessage());
            }
        }

        cmdList.addCommand(super.getRemoveDependentItemsCommand());
        return cmdList;
    }

    public boolean isDropControllerRegistered() {
        return dropControllerRegistered;
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public void onPanelAdd(HasChildren parentPanel) {
        // register port drop controllers
        if (containerStartPort != null
                && !containerStartPort.isDropControllerRegistered()) {
            containerStartPort.registerDropController();
        }
        if (containerExitPort != null
                && !containerExitPort.isDropControllerRegistered()) {
            containerExitPort.registerDropController();
        }

        // register drop controller
        if (!isDropControllerRegistered()) {
            registerDropController();
        }

        super.onPanelAdd(parentPanel);
    }

    @Override
    public void onPanelRemove() {
        // unregister port drop controllers
        if (containerStartPort != null
                && containerStartPort.isDropControllerRegistered()) {
            containerStartPort.unregisterDropController();
        }
        if (containerExitPort != null
                && containerExitPort.isDropControllerRegistered()) {
            containerExitPort.unregisterDropController();
        }

        // unregister drop controller
        if (isDropControllerRegistered()) {
            unregisterDropController();
        }

        super.onPanelRemove();
    }

    @Override
    protected void refreshNodeSize() {
        super.refreshNodeSize();
        refreshPortPositions();
    }

    @Override
    protected void refreshPortPositions() {
        // set position of container ports
        if (containerStartPort != null) {
            refreshPortPosition(containerStartPort);
        }
        if (containerExitPort != null) {
            refreshPortPosition(containerExitPort);
        }

        // set position of node ports
        super.refreshPortPositions();
    }

    /**
     * Registers the container drop controller to the workflow drag controller.
     */
    public void registerDropController() {
        PickupDragController dc = DndRegistry.getInstance()
                .getPickupDragController("WorkflowDragController");
        dc.registerDropController(getDropController());
        dropControllerRegistered = true;
    }

    @Override
    public boolean remove(Widget w) {
        if (w instanceof Node) {
            Node node = (Node) w;
            // remove node from the nodes collection
            childNodes.remove(node);

            // callback to node before remove
            node.onPanelRemove();
        }

        return super.remove(w);
    }

    @Override
    public void removeConnection(Connection connection) {
        childConnections.remove(connection);
        connection.remove();
    }

    @Override
    public void removeNode(Node node) {
        childNodes.remove(node);

        // callback to node before remove
        node.onPanelRemove();

        super.remove(node);
    }

    public void setContainerExitPort(ContainerExitPort containerExitPort) {
        this.containerExitPort = containerExitPort;

        this.add(containerExitPort);
        containerExitPort.setParentNode(this);
    }

    public void setContainerStartPort(ContainerStartPort containerStartPort) {
        this.containerStartPort = containerStartPort;

        this.add(containerStartPort);
        containerStartPort.setParentNode(this);
    }

    /**
     * Unregisters the container drop controller from the workflow drag
     * controller.
     */
    public void unregisterDropController() {
        PickupDragController dc = DndRegistry.getInstance()
                .getPickupDragController("WorkflowDragController");
        dc.unregisterDropController(getDropController());
        dropControllerRegistered = false;
    }

}
