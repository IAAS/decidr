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

import java.util.Vector;

import com.allen_sauer.gwt.dnd.client.DragController;
import com.google.gwt.user.client.ui.AbsolutePanel;

import de.decidr.modelingtool.client.ui.dnd.WorkflowDragController;
import de.decidr.modelingtool.client.ui.selection.SelectionHandler;

/**
 * This is the basic workflow drawing canvas.
 * 
 * @author engelhjs
 */
public class Workflow extends AbsolutePanel implements ModelChangeListener {

    /**
     * The drag controller which makes the nodes in the workflow draggable.
     */
    private WorkflowDragController dragController;

    private SelectionHandler selectionHandler;

    

    // private WorkflowModel model;

    
    /**
     * All nodes in the workflow except for the nodes in a container.
     */
    private Vector<Node> nodes = new Vector<Node>();

    public SelectionHandler getSelectionHandler() {
        return selectionHandler;
    }

    /**
     * All connections in the workflow except for the nodes in a container.
     */
    private Vector<Connection> connections = new Vector<Connection>();

    /**
     * The constructor.
     */
    public Workflow() {
        super();

        // set workflow proerties
        this.addStyleName("workflow");
        this.setSize("600px", "400px");

        // create selection handler
        selectionHandler = new SelectionHandler(this);

        // create drag controller
        this.dragController = new WorkflowDragController(this);
    }

    public void add(Connection connection) {
        // add connection to the connections vector
        connections.add(connection);
        // add connection to workflow
        super.add(connection);
        // callback to connection
        connection.onWorkflowAdd();
    }

    /**
     * Adds a node to the workflow.
     * 
     * @param node
     */
    public void add(Node node) {
        // add node to workflow
        super.add(node);

        // do all the other stuff
        addNode(node);
    }

    /**
     * Adds a node to the workflow in the specified position.
     * 
     * @param node
     * @param x
     * @param y
     */
    public void add(Node node, int x, int y) {
        // add node to workflow
        super.add(node, x, y);
        
        // do all the other stuff
        addNode(node);
    }
    
    private void addNode(Node node) {
        // add node to the nodes vector
        nodes.add(node);

        // register the selection Handler
        // must happen before makeDraggable because of handler order!!!
        node.addMouseDownHandler(selectionHandler);

        // make node draggable
        if (node.isMoveable()) {
            this.dragController.makeDraggable(node);
        }

        // if node is a container, register the drop controller
        if (node instanceof Container) {
            dragController.registerDropController(((Container) node)
                    .getDropController());
        }

        // callback to node
        node.onWorkflowAdd();
    }

    public DragController getDragController() {
        return dragController;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.ui.ModelChangeListener#onModelChange()
     */
    @Override
    public void onModelChange() {
        // TODO Auto-generated method stub

    }

    /**
     * Removes a connection from the workflow.
     * 
     * @param connection
     */
    public void remove(Connection connection) {

    }

    /**
     * Removes a node from the workflow.
     * 
     * @param node
     */
    public void remove(Node node) {

    }

}
