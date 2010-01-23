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

import de.decidr.modelingtool.client.model.connections.ConnectionModel;
import de.decidr.modelingtool.client.ui.Connection;
import de.decidr.modelingtool.client.ui.selection.ConnectionDragBox;

/**
 * This command removes a connection from the workflow.
 * 
 * @author Johannes Engelhardt
 */
public class RemoveConnectionCommand implements UndoableCommand {

    /** The connection to remove. */
    private Connection connection;

    /** The model of the connection to remove. */
    private ConnectionModel model;

    /**
     * Constructor for removing the connection.
     * 
     * @param connection
     *            The connection to remove
     */
    public RemoveConnectionCommand(Connection connection) {
        this.connection = connection;
        this.model = connection.getModel();
    }

    @Override
    public void execute() {
        // remove start drag box
        ConnectionDragBox startDragBox = connection.getStartDragBox();
        startDragBox.getGluedPort().removeConnectionDragBox(startDragBox, true);

        // remove end drag box
        ConnectionDragBox endDragBox = connection.getEndDragBox();
        endDragBox.getGluedPort().removeConnectionDragBox(endDragBox, true);

        // remove connection from parent panel
        connection.getParentPanel().removeConnection(connection);
        connection.remove();

        // unlink model
        model.getParentModel().removeConnectionModel(model);
        model.getSource().setOutput(null);
        model.getTarget().setInput(null);
    }

    @Override
    public void undo() {
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
    }
}
