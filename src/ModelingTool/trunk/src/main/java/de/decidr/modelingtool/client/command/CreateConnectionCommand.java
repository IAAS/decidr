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

package de.decidr.modelingtool.client.command;

import com.google.gwt.user.client.Window;

import de.decidr.modelingtool.client.exception.IncompleteModelDataException;
import de.decidr.modelingtool.client.exception.InvalidTypeException;
import de.decidr.modelingtool.client.model.ConnectionModel;
import de.decidr.modelingtool.client.ui.Connection;
import de.decidr.modelingtool.client.ui.ContainerStartPort;
import de.decidr.modelingtool.client.ui.InputPort;
import de.decidr.modelingtool.client.ui.Node;
import de.decidr.modelingtool.client.ui.OrthogonalConnection;
import de.decidr.modelingtool.client.ui.OutputPort;
import de.decidr.modelingtool.client.ui.Port;
import de.decidr.modelingtool.client.ui.dnd.DndRegistry;
import de.decidr.modelingtool.client.ui.selection.ConnectionDragBox;

/**
 * This command adds a connection between to existing nodes to the workflow.
 * 
 * @author Johannes Engelhardt
 */
public class CreateConnectionCommand implements UndoableCommand {
    
    /** The connection to be added. */
    private Connection connection;

    /** The connection model of the connection to be added. */
    private ConnectionModel model;

    /**
     * Constructor for creating a connection model from an already drawn
     * connection. The model is created from the data of the drawn
     * connection.
     *
     * @param connection The connection which has been drawn
     */
    public CreateConnectionCommand(Connection connection) {
        this.connection = connection;

        // get ports
        Port startPort = connection.getStartDragBox().getGluedPort();
        Port endPort = connection.getEndDragBox().getGluedPort();

        // create connection model
        model = new ConnectionModel();

        // link connection and model
        connection.setModel(model);
        model.setChangeListener(connection);

        // set parent model
        try {
            model.setParentModel(connection.getParentPanel()
                    .getHasChildModelsModel());
        } catch (InvalidTypeException e) {
            Window.alert(e.getMessage());
            e.printStackTrace();
        }

        // link connection model to node models
        if (startPort instanceof OutputPort
                || startPort instanceof ContainerStartPort) {
            model.setSource(startPort.getParentNode().getModel());
            model.setTarget(endPort.getParentNode().getModel());
        } else {
            model.setSource(endPort.getParentNode().getModel());
            model.setTarget(startPort.getParentNode().getModel());
        }
    }
  
    /**
     * Contructor for creating a connection from an existing and linked
     * connection model. The connection is drawn from the data of the model.
     *
     * @param model The connection model from which the connection is drawn.
     */
    public CreateConnectionCommand(ConnectionModel model)
            throws IncompleteModelDataException {
        this.model = model;

        // check if model contains all needed information. if not, and
        // IncompleteModelDataException is thrown
        checkModelData();

        // get source port
        OutputPort sourcePort = ((Node) model.getSource().getChangeListener())
                .getOutputPort();
        // get targetPort
        InputPort targetPort = ((Node) model.getTarget().getChangeListener())
                .getInputPort();

        // create connection
        connection = new OrthogonalConnection(model.getParentModel()
                .getHasChildrenChangeListener());

        // link connection and model
        connection.setModel(model);
        model.setChangeListener(connection);

        // set parent panel of connection
        // connection.setParentPanel(model.getParentModel()
        // .getHasChildrenChangeListener());

        // create and link start drag box
        ConnectionDragBox startDragBox = new ConnectionDragBox();
        startDragBox.setGluedPort(sourcePort);
        startDragBox.setConnection(connection);

        // make start drag box draggable
        DndRegistry.getInstance().getDragController("InputPortDragController")
                .makeDraggable(startDragBox);

        // create and link end drag box
        ConnectionDragBox endDragBox = new ConnectionDragBox();
        endDragBox.setGluedPort(targetPort);
        endDragBox.setConnection(connection);

        // make end drag box draggable
        DndRegistry.getInstance().getDragController("OutputPortDragController")
                .makeDraggable(endDragBox);

        // link connection
        connection.setStartDragBox(startDragBox);
        connection.setEndDragBox(endDragBox);
    }

    @Override
    public void undo() {
        // remove start drag box
        ConnectionDragBox startDragBox = connection.getStartDragBox();
        startDragBox.getGluedPort().remove(startDragBox);

        // remove end drag box
        ConnectionDragBox endDragBox = connection.getEndDragBox();
        endDragBox.getGluedPort().remove(endDragBox);

        // remove connection from parent panel
        connection.getParentPanel().removeConnection(connection);

        // unlink model
        model.getParentModel().removeConnectionModel(model);
        model.getSource().setOutput(null);
        model.getTarget().setInput(null);
    }

    @Override
    public void execute() {
        // add start drag box
        ConnectionDragBox startDragBox = connection.getStartDragBox();
        startDragBox.getGluedPort().addConnectionDragBox(startDragBox);

        // add end drag box
        ConnectionDragBox endDragBox = connection.getEndDragBox();
        endDragBox.getGluedPort().addConnectionDragBox(endDragBox);

        // add connection to parent panel
        connection.getParentPanel().addConnection(connection);
        connection.draw();

        // link model
        model.getParentModel().addConnectionModel(model);
        model.getSource().setOutput(model);
        model.getTarget().setInput(model);

        // TODO: DEBUG
        // System.out.println(connection.getModel());
    }

    /**
     * Checks the connection model if it consists all required data for
     * drawing the connection: its parent model, target and source node.
     *
     * @return True, if all required data is not null.
     * @throws IncompleteModelDataException if any relevant data is null.
     */
    private boolean checkModelData() throws IncompleteModelDataException {
        if (model.getSource() == null) {
            throw new IncompleteModelDataException("model.source is null.");
        }

        if (model.getTarget() == null) {
            throw new IncompleteModelDataException("model.target is null.");
        }

        if (model.getParentModel() == null) {
            throw new IncompleteModelDataException("model.parentModel is null.");
        }

        return true;
    }

}