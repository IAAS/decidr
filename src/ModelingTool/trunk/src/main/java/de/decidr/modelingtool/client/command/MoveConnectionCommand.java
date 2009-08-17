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

import de.decidr.modelingtool.client.model.ConnectionModel;
import de.decidr.modelingtool.client.ui.Connection;
import de.decidr.modelingtool.client.ui.Port;
import de.decidr.modelingtool.client.ui.selection.ConnectionDragBox;

/**
 * TODO: add comment
 * 
 * @author JE
 */
public class MoveConnectionCommand implements UndoableCommand {

    /** The connection to remove. */
    private Connection connection;

    /** The model of the connection to remove. */
    private ConnectionModel model;

    private Port oldStartPort;
    private Port oldEndPort;

    private Port newStartPort;
    private Port newEndPort;

    /**
     * Constructor for moving the connection.
     * 
     * @param connection
     *            The connection to remove
     */
    public MoveConnectionCommand(Connection connection, Port oldStartPort,
            Port oldEndPort) {
        this.connection = connection;
        this.model = connection.getModel();

        this.oldStartPort = oldStartPort;
        this.oldEndPort = oldEndPort;

        this.newStartPort = connection.getStartDragBox().getGluedPort();
        this.newEndPort = connection.getEndDragBox().getGluedPort();
    }

    @Override
    public void undo() {
        //JE: geht nicht richtig
        
        // move start drag box
        ConnectionDragBox startDragBox = connection.getStartDragBox();
        startDragBox.getGluedPort().removeConnectionDragBox(startDragBox);
        
        oldStartPort.addConnectionDragBox(startDragBox);
        startDragBox.setGluedPort(oldStartPort);

        // move end drag box
        ConnectionDragBox endDragBox = connection.getEndDragBox();
        endDragBox.getGluedPort().removeConnectionDragBox(endDragBox);
        
        oldEndPort.addConnectionDragBox(endDragBox);
        endDragBox.setGluedPort(oldEndPort);

        // draw connection
        connection.getParentPanel().addConnection(connection);
        connection.draw();

        // link model
        model.getSource().setOutput(null);
        model.getTarget().setInput(null);
        
        model.setSource(oldStartPort.getParentNode().getModel());
        model.setTarget(oldEndPort.getParentNode().getModel());
        
        model.getSource().setOutput(model);
        model.getTarget().setInput(model);
    }

    @Override
    public void execute() {
        // move start drag box
        ConnectionDragBox startDragBox = connection.getStartDragBox();
        startDragBox.getGluedPort().removeConnectionDragBox(startDragBox);
        
        startDragBox.setGluedPort(newStartPort);
        newStartPort.addConnectionDragBox(startDragBox);
        
        // move end drag box
        ConnectionDragBox endDragBox = connection.getEndDragBox();
        endDragBox.getGluedPort().removeConnectionDragBox(endDragBox);
        
        endDragBox.setGluedPort(newEndPort);
        newEndPort.addConnectionDragBox(endDragBox);
        

        // draw connection
        connection.getParentPanel().addConnection(connection);
        connection.draw();

        // link model
        model.getSource().setOutput(null);
        model.getTarget().setInput(null);
        
        model.setSource(newStartPort.getParentNode().getModel());
        model.setTarget(newEndPort.getParentNode().getModel());
        
        model.getSource().setOutput(model);
        model.getTarget().setInput(model);
    }

}
