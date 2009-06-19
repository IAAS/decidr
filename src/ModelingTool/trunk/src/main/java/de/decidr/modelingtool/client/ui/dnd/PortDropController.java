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

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbstractDropController;
import com.google.gwt.user.client.ui.Widget;

import de.decidr.modelingtool.client.ui.Port;
import de.decidr.modelingtool.client.ui.selection.ConnectionDragBox;

/**
 * TODO: add comment
 *
 * @author JE
 */
public class PortDropController extends AbstractDropController {

    /**
     * TODO: add comment
     *
     * @param dropTarget
     */
    public PortDropController(Widget dropTarget) {
        super(dropTarget);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Widget getDropTarget() {
        // TODO Auto-generated method stub
        return super.getDropTarget();
    }

    @Override
    public void onDrop(DragContext context) {
        // TODO Auto-generated method stub
        super.onDrop(context);
        
        // dragged drag box
        ConnectionDragBox cdb = (ConnectionDragBox)context.draggable;
        // target port
        Port port = (Port)getDropTarget();
        // remove drag box from old port
        cdb.getGluedPort().remove(cdb);
        // add drag box to target port
        cdb.setGluedPort(port);
        port.add(cdb);
        port.setWidgetPosition(cdb, 0, 0);
        
        // redraw connection
        cdb.getConnection().draw();
    }

    @Override
    public void onEnter(DragContext context) {
        // TODO Auto-generated method stub
        super.onEnter(context);
    }

    @Override
    public void onLeave(DragContext context) {
        // TODO Auto-generated method stub
        super.onLeave(context);
    }

    @Override
    public void onMove(DragContext context) {
        // TODO Auto-generated method stub
        super.onMove(context);
    }

    @Override
    public void onPreviewDrop(DragContext context) throws VetoDragException {
        // TODO Auto-generated method stub
        super.onPreviewDrop(context);
    }

    
}
