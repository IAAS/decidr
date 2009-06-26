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

import com.allen_sauer.gwt.dnd.client.PickupDragController;

import de.decidr.modelingtool.client.ui.dnd.DndRegistry;

/**
 * TODO: add comment
 * 
 * @author engelhjs
 */
public class InputPort extends Port {

    /**
     * TODO: add comment
     * 
     * @param parent
     * @param position
     */
    public InputPort() {
        super(Port.Position.TOP);

        // set properties
        this.addStyleName("port-inputport");

        // register drop controller to drag controller
        //registerDropController();

        // make connection drag box draggable
        PickupDragController ipdc = DndRegistry.getInstance()
                .getDragController("InputPortDragController");
        ipdc.makeDraggable(connectionDragBox);
    }

    public void registerDropController() {
        PickupDragController opdc = DndRegistry.getInstance()
                .getDragController("OutputPortDragController");
        opdc.registerDropController(getDropController());
    }

    public void unregisterDropController() {
        PickupDragController opdc = DndRegistry.getInstance()
                .getDragController("OutputPortDragController");
        opdc.unregisterDropController(getDropController());
    }

}
