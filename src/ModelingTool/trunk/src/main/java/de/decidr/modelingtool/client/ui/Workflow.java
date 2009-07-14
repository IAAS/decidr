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

import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

import de.decidr.modelingtool.client.exception.InvalidTypeException;
import de.decidr.modelingtool.client.model.HasChildModels;
import de.decidr.modelingtool.client.model.WorkflowModel;
import de.decidr.modelingtool.client.ui.dnd.ConnectionDragController;
import de.decidr.modelingtool.client.ui.dnd.DndRegistry;
import de.decidr.modelingtool.client.ui.dnd.ResizeDragController;
import de.decidr.modelingtool.client.ui.dnd.WorkflowDragController;
import de.decidr.modelingtool.client.ui.selection.SelectionHandler;

/**
 * This is the basic workflow drawing canvas.
 * 
 * @author engelhjs
 */
public class Workflow extends AbsolutePanel implements ModelChangeListener,
        HasMouseDownHandlers, HasChildren {

    private static Workflow instance;

    public static Workflow getInstance() {
        if (instance == null) {
            instance = new Workflow();
        }
        return instance;
    }

    /**
     * The drag controller which makes the nodes in the workflow draggable.
     */
    private WorkflowDragController dragController;

    private SelectionHandler selectionHandler;

    private WorkflowModel model = null;

    /**
     * All nodes in the workflow except for the nodes in a container.
     */
    private Collection<Node> childNodes = new HashSet<Node>();

    /**
     * All connections in the workflow except for the nodes in a container.
     */
    private Collection<Connection> childConnections = new HashSet<Connection>();

    /**
     * The constructor.
     */
    private Workflow() {
        super();

        // set workflow proerties
        this.addStyleName("workflow");
        this.setSize("600px", "400px");

        // create selection handler
        selectionHandler = new SelectionHandler(this);

        // register workflow to selection handler
        this.addMouseDownHandler(selectionHandler);

        // create drag controller
        dragController = new WorkflowDragController(this);

        // register drag controllers
        DndRegistry dndr = DndRegistry.getInstance();
        dndr.register("WorkflowDragController", dragController);
        dndr.register("InputPortDragController", new ConnectionDragController(
                this));
        dndr.register("OutputPortDragController", new ConnectionDragController(
                this));
        dndr.register("ResizeDragController", new ResizeDragController(this));
        
        // add empty workflow model
        WorkflowModel model = new WorkflowModel();
        model.setChangeListener(this);
        setModel(model);
    }

    public void addConnection(Connection connection) {
        // add connection to the connections vector
        childConnections.add(connection);
        // add connection to workflow
        // super.add(connection);
        // callback to connection
        connection.onPanelAdd(this);
    }

    @Override
    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return addDomHandler(handler, MouseDownEvent.getType());
    }

    public void addNode(Node node) {
        addNode(node, 0, 0);
    }

    /**
     * Adds a node to the workflow in the specified position.
     * 
     * @param node
     * @param x
     * @param y
     */
    public void addNode(Node node, int left, int top) {
        super.add(node, left, top);

        // add node to the nodes vector
        childNodes.add(node);

        // register the selection Handler
        // must happen before makeDraggable because of handler order!!!
        node.addSelectionHandler(selectionHandler);

        // make node draggable, only draggable at graphic widget of the node
        if (node.isMoveable()) {
            node.makeDraggable();
        }

        // if node is a container, register the drop controller
        if (node instanceof Container) {
            //((Container)node).registerDropController();
        }

        // callback to node after add
        node.onPanelAdd(this);
    }

    @Override
    public Collection<Connection> getConnections() {
        return childConnections;
    }

    @Override
    public HasChildModels getHasChildModelsModel() throws InvalidTypeException {
        if (model instanceof HasChildModels) {
            return (HasChildModels) model;
        } else {
            throw new InvalidTypeException("Model does not have children.");
        }
    }

    public WorkflowModel getModel() {
        return model;
    }

    /**
     * TODO: DEBUG
     */
    // public void add(Node node) {
    // System.out.println("DEBUG: workflow.add not supported for node!");
    // }
    @Override
    public Collection<Node> getNodes() {
        return childNodes;
    }

    public SelectionHandler getSelectionHandler() {
        return selectionHandler;
    }

    public Selectable getSelectedItem() {
        return selectionHandler.getSelectedItem();
    }

    @Override
    public void onModelChange() {
        // TODO Auto-generated method stub

    }

    /**
     * Removes a connection from the workflow.
     * 
     * @param connection
     */
    public void removeConnection(Connection connection) {
        childConnections.remove(connection);
        connection.remove();
    }

    /**
     * Removes a node from the workflow.
     * 
     * @param node
     */
    public void removeNode(Node node) {
        childNodes.remove(node);

        // callback to node before remove
        node.onPanelRemove();

        super.remove(node);
    }

    public void setModel(WorkflowModel model) {
        this.model = model;
    }

    @Override
    public void add(Widget w) {
        super.add(w);
        
        if (w instanceof Node) {
            Node node = (Node)w;
            // add node to the nodes vector
            childNodes.add(node);
    
            // callback to node after add
            node.onPanelAdd(this);
        }
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

    public int getLeft() {
        return 0;
    }
    
    public int getTop() {
        return 0;
    }
    
}
