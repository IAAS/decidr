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
 * Port holding the outgoing connection(s) of the parent node.
 * 
 * @author Johannes Engelhardt
 */
public class OutputPort extends Port {

    public OutputPort() {
        super(Port.Position.BOTTOM);

        // set properties
        this.addStyleName("port-outputport");
    }

    @Override
    public void registerDropController() {
        PickupDragController ipdc = DndRegistry.getInstance()
                .getPickupDragController("InputPortDragController");
        ipdc.registerDropController(getDropController());
        dropControllerRegistered = true;
    }

    @Override
    public void unregisterDropController() {
        PickupDragController ipdc = DndRegistry.getInstance()
                .getPickupDragController("InputPortDragController");
        ipdc.unregisterDropController(getDropController());
        dropControllerRegistered = false;
    }
    
    @Override
    public boolean isContainerPort() {
        return false;
    }

}
