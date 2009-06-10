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

package de.decidr.modelingtool.client.ui.dnd;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.user.client.ui.AbsolutePanel;

import de.decidr.modelingtool.client.ui.Connection;
import de.decidr.modelingtool.client.ui.OrthogonalConnection;
import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.selection.ConnectionDragBox;

/**
 * TODO: add comment
 * 
 * @author JE
 */
public class ConnectionDragController extends PickupDragController {
    
    /**
     * The connection currently be dragged
     */
    Connection connection = null;
    

    /**
     * TODO: add comment
     * 
     * @param boundaryPanel
     * @param allowDroppingOnBoundaryPanel
     */
    public ConnectionDragController(AbsolutePanel boundaryPanel) {
        super(boundaryPanel, false);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void dragMove() {
        // TODO Auto-generated method stub
        super.dragMove();
        
        if (connection != null) {
            connection.draw();
        }
    }

    @Override
    public void dragEnd() {
        // TODO Auto-generated method stub
        super.dragEnd();
        
        if (context.draggable instanceof ConnectionDragBox) {
            ((ConnectionDragBox)context.draggable).setVisibleStyle(false);
        }
        
        connection = null;
    }

    @Override
    public void dragStart() {
        if (context.draggable instanceof ConnectionDragBox) {
            ConnectionDragBox dragBox = (ConnectionDragBox)context.draggable;
            dragBox.setVisibleStyle(true);
            
            if (dragBox.getConnection() == null) {
                // create new connection
                connection = new OrthogonalConnection();
                
                // create start drag box
                ConnectionDragBox startDragBox = new ConnectionDragBox();
                startDragBox.setVisibleStyle(true);
                Workflow.getInstance().add(startDragBox);
                
                // set drag boxes and add connection to workflow
                connection.setStartDragBox(startDragBox);
                connection.setEndDragBox(dragBox);
                Workflow.getInstance().add(connection);
            }
            
        }
        
        
        ConnectionDragBox startDragBox = new ConnectionDragBox();
        Workflow.getInstance().add(startDragBox);
        
        super.dragStart();
    }

    @Override
    public void previewDragEnd() throws VetoDragException {
        // TODO Auto-generated method stub
        super.previewDragEnd();
    }

}
