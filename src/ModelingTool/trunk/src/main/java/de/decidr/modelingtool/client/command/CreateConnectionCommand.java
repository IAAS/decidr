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

import de.decidr.modelingtool.client.exception.IncompleteModelDataException;
import de.decidr.modelingtool.client.model.ConnectionModel;
import de.decidr.modelingtool.client.ui.Connection;
import de.decidr.modelingtool.client.ui.Node;
import de.decidr.modelingtool.client.ui.OrthogonalConnection;
import de.decidr.modelingtool.client.ui.OutputPort;
import de.decidr.modelingtool.client.ui.Port;
import de.decidr.modelingtool.client.ui.dnd.DndRegistry;
import de.decidr.modelingtool.client.ui.selection.ConnectionDragBox;

/**
 * TODO: add comment
 * 
 * @author JE
 */
public class CreateConnectionCommand implements UndoableCommand {

    private Connection connection;
    private ConnectionModel model;

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
        model.setParentModel(connection.getParentPanel()
                .getHasChildModelsModel());

        // link connection model to node models
        if (startPort instanceof OutputPort) {
            model.setSource(startPort.getParentNode().getModel());
            model.setTarget(endPort.getParentNode().getModel());
        } else {
            model.setSource(endPort.getParentNode().getModel());
            model.setTarget(startPort.getParentNode().getModel());
        }
    }

    public CreateConnectionCommand(ConnectionModel model)
            throws IncompleteModelDataException {
        this.model = model;

        // check if model contains all needed information. if not, and
        // IncompleteModelDataException is thrown
        checkModelData();

        // get source port
        Port sourcePort = ((Node) model.getSource().getChangeListener())
                .getOutputPort();
        // get targetPort
        Port targetPort = ((Node) model.getTarget().getChangeListener())
                .getInputPort();

        // create connection
        connection = new OrthogonalConnection(model.getParentModel()
                .getHasChildrenChangeListener());

        // link connection and model
        connection.setModel(model);
        model.setChangeListener(connection);

        // set parent panel of connection
//        connection.setParentPanel(model.getParentModel()
//                .getHasChildrenChangeListener());

        // create and link start drag box
        ConnectionDragBox startDragBox = new ConnectionDragBox();
        startDragBox.setGluedPort(sourcePort);
        startDragBox.setConnection(connection);

        // make start drag box draggable
        DndRegistry.getInstance().getDragController("OutputPortDragController")
                .makeDraggable(startDragBox);

        // create and link end drag box
        ConnectionDragBox endDragBox = new ConnectionDragBox();
        endDragBox.setGluedPort(targetPort);
        endDragBox.setConnection(connection);

        // make end drag box draggable
        DndRegistry.getInstance().getDragController("InputPortDragController")
                .makeDraggable(endDragBox);

        // link connection
        connection.setStartDragBox(startDragBox);
        connection.setEndDragBox(endDragBox);
    }

    @Override
    public void undo() {
        // remove connection
        connection.getParentPanel().removeConnection(connection);

        // remove start drag box
        ConnectionDragBox startDragBox = connection.getStartDragBox();
        startDragBox.getGluedPort().remove(startDragBox);

        // remove end drag box
        ConnectionDragBox endDragBox = connection.getEndDragBox();
        endDragBox.getGluedPort().remove(endDragBox);

        // remove connection from parent panel
        connection.getParentPanel().removeConnection(connection);

        // unlink model
        model.getParentModel().removeModel(model);
        model.getSource().setOutput(null);
        model.getTarget().setInput(null);
    }

    @Override
    public void execute() {
        // add start drag box
        ConnectionDragBox startDragBox = connection.getStartDragBox();
        startDragBox.getGluedPort().add(startDragBox);

        // add end drag box
        ConnectionDragBox endDragBox = connection.getEndDragBox();
        endDragBox.getGluedPort().add(endDragBox);

        // add connection to parent panel
        connection.getParentPanel().addConnection(connection);
        connection.draw();

        // link model
        model.getParentModel().addModel(model);
        model.getSource().setOutput(model);
        model.getTarget().setInput(model);
    }

    /**
     * 
     * TODO: add comment
     * 
     * @return
     * @throws IncompleteModelDataException
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
