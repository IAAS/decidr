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

package main.java.de.decidr.modelingtool.client.ui.dnd;

import main.java.de.decidr.modelingtool.client.ui.Workflow;
import main.java.de.decidr.modelingtool.client.ui.selection.DragBox;

import com.allen_sauer.gwt.dnd.client.PickupDragController;


/**
 * TODO: add comment
 *
 * @author JE
 */
public class ResizeDragController extends PickupDragController {

    /**
     * TODO: add comment
     *
     * @param boundaryPanel
     */
    public ResizeDragController(Workflow workflow) {
        super(workflow, true);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see com.allen_sauer.gwt.dnd.client.DragController#dragMove()
     */
    @Override
    public void dragMove() {
        if (this.context.draggable instanceof DragBox) {
            DragBox dragBox = (DragBox)this.context.draggable;
            
            switch (dragBox.getDirection()) {
            case NORTH:
                
                break;
            case NORTHEAST:
                
                break;
            case EAST:
                
                break;
            case SOUTHEAST:
                
                break;
            case SOUTH:
                
                break;
            case SOUTHWEST:
                
                break;
            case WEST:
                
                break;
            case NORTHWEST:
                
                break;
            }
        }
    }

}
