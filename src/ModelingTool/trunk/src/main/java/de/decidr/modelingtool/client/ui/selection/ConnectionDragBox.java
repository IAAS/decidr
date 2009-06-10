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

package de.decidr.modelingtool.client.ui.selection;

import de.decidr.modelingtool.client.ui.Connection;
import de.decidr.modelingtool.client.ui.Workflow;

/**
 * TODO: add comment
 * 
 * @author JE
 */
public class ConnectionDragBox extends DragBox {

    private Connection connection = null;
    
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * TODO: add comment
     * 
     * @param direction
     */
    public ConnectionDragBox() {
        super(DragDirection.ALL);
        // TODO Auto-generated constructor stub
    }
    
    public void setVisibleStyle(boolean visible) {
        if (visible) {
            this.setStyleName("dragbox-port");
        } else {
            this.setStyleName("dragbox-invisible");
        }
    }

    /**
     * 
     * Returns the workflow relative x coordinate of the center of the drag box.
     * 
     * @return
     */
    public int getMiddleLeft() {
        return this.getAbsoluteLeft() + this.getOffsetWidth()
                - Workflow.getInstance().getAbsoluteLeft();
    }

    /**
     * 
     * Returns the workflow relative y coordinate of the center of the drag box.
     * 
     * @return
     */
    public int getMiddleTop() {
        return this.getAbsoluteTop() + this.getOffsetHeight()
                - Workflow.getInstance().getAbsoluteTop();
    }
}
