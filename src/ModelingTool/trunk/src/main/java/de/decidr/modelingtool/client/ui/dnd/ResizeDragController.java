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


import com.allen_sauer.gwt.dnd.client.AbstractDragController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.selection.DragBox;


/**
 * TODO: add comment
 *
 * @author JE
 */
public class ResizeDragController extends AbstractDragController {

    /**
     * TODO: add comment
     *
     * @param boundaryPanel
     */
    public ResizeDragController(Workflow workflow) {
        super(workflow);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void dragMove() {  
        if (context.draggable instanceof DragBox) {
            DragBox dragBox = (DragBox)context.draggable;
            
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
                int delta = dragBox.getAbsoluteTop() - context.desiredDraggableY;
                if (delta != 0) {
                    Widget w = (Widget)Workflow.getInstance().getSelectedItem();
                    // FIXME: selectedItem = null
                    Window.alert(w.toString());
                    w.setPixelSize(w.getOffsetWidth(), w.getOffsetHeight() + delta);
                }
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
