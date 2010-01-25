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
import java.util.HashMap;
import java.util.HashSet;

import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

import de.decidr.modelingtool.client.exception.InvalidTypeException;
import de.decidr.modelingtool.client.model.container.HasChildModels;
import de.decidr.modelingtool.client.model.workflow.WorkflowModel;
import de.decidr.modelingtool.client.ui.dialogs.workflow.WorkflowPropertyWindowInvoker;
import de.decidr.modelingtool.client.ui.dnd.ConnectionDragController;
import de.decidr.modelingtool.client.ui.dnd.DndRegistry;
import de.decidr.modelingtool.client.ui.dnd.ResizeDragController;
import de.decidr.modelingtool.client.ui.dnd.WorkflowDragController;
import de.decidr.modelingtool.client.ui.selection.KeyboardHandler;
import de.decidr.modelingtool.client.ui.selection.SelectionHandler;

/**
 * This is the basic workflow drawing canvas.
 * 
 * @authors Johannes Engelhardt, Jonas Schlak
 */
public class Workflow extends AbsolutePanel implements ModelChangeListener,
        HasMouseDownHandlers, HasKeyDownHandlers, HasChildren {

    /** The instance of the workflow. */
    private static Workflow instance;

    public static Workflow getInstance() {
        if (instance == null) {
            instance = new Workflow();
        }
        return instance;
    }

    /** The workflow model */
    private WorkflowModel model = null;

    private HashMap<Long, String> users = null;

    /** All nodes in the workflow except for the nodes in a container. */
    private Collection<Node> childNodes = new HashSet<Node>();

    /** All connections in the workflow except for the nodes in a container. */
    private Collection<Connection> childConnections = new HashSet<Connection>();

    /*
     * These two variables are needed to store the size values, because there
     * are no methods to get width and height form the UIObject. Setting these
     * variables has no effect.
     */
    private int width = 800;
    private int height = 600;

    public final static int MAX_SIZE = 10000;

    /**
     * Private constructor.
     */
    private Workflow() {
        super();

        // set workflow properties
        this.addStyleName("workflow");
        // set size of workflow
        this.setSize(width, height);

        // register workflow to selection handler
        this.addMouseDownHandler(SelectionHandler.getInstance());

        // register workflow to keyboard handler
        this.addKeyDownHandler(KeyboardHandler.getInstance());

        // register drag controllers
        DndRegistry dndr = DndRegistry.getInstance();
        dndr.register("WorkflowDragController",
                new WorkflowDragController(this));
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

    @Override
    public void add(Widget w) {
        super.add(w);

        if (w instanceof Node) {
            Node node = (Node) w;
            // add node to the nodes vector
            childNodes.add(node);

            // callback to node after add
            node.onPanelAdd(this);
        }
    }

    @Override
    public void addConnection(Connection connection) {
        // add connection to the connections vector
        childConnections.add(connection);
        // add connection to workflow
        // super.add(connection);
        // callback to connection
        connection.onPanelAdd(this);
    }

    @Override
    public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
        return addDomHandler(handler, KeyDownEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return addDomHandler(handler, MouseDownEvent.getType());
    }

    @Override
    public void addNode(Node node) {
        addNode(node, 0, 0);
    }

    @Override
    public void addNode(Node node, int left, int top) {
        super.add(node, left, top);

        // add node to the nodes vector
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

    @Override
    public HasChildModels getHasChildModelsModel() throws InvalidTypeException {
        if (model.getChildNodeModels().isEmpty() == false) {
            return model;
        } else {
            throw new InvalidTypeException("Model does not have children.");
        }
    }

    public int getHeight() {
        return this.height;
    }

    @Override
    public int getLeft() {
        return 0;
    }

    public WorkflowModel getModel() {
        return model;
    }

    @Override
    public Collection<Node> getNodes() {
        return childNodes;
    }

    @Override
    public int getTop() {
        return 0;
    }

    /**
     * Returns the tenant users (of the tenant admin who is currently modeling
     * this workflow) as a hash map with user id and display username.
     * 
     * @return the user hash map
     */
    public HashMap<Long, String> getUsers() {
        return users;
    }

    public int getWidth() {
        return this.width;
    }

    @Override
    public void onModelChange() {
        // do nothing
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

    public void setModel(WorkflowModel model) {
        this.model = model;
    }

    /**
     * Sets the size of the workflow canvas
     * 
     * @param width
     *            the width in pixels
     * @param height
     *            the height in pixels
     */
    public void setSize(int width, int height) {
        super.setSize(width + "px", height + "px");
        this.width = width;
        this.height = height;
    }

    /**
     * DO NOT USE THIS METHOD. USE setSize(int, int) INSTEAD.
     */
    @Override
    public void setSize(String width, String height) {
        super.setSize(width, height);
    }

    /**
     *Sets the List of tenant users.
     * 
     * @param users
     *            the user hash map
     */
    public void setUsers(HashMap<Long, String> users) {
        this.users = users;

    }

    public void showPropertyWindow() {
        WorkflowPropertyWindowInvoker.invoke(Workflow.getInstance().getModel());
    }
}