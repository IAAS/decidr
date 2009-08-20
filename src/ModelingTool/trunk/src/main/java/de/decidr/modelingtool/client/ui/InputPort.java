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
 * Port holding the incoming connection(s) of the parent node.
 * 
 * @author Johannes Engelhardt
 */
public class InputPort extends Port {

    public InputPort() {
        super(Port.Position.TOP);

        // set properties
        this.addStyleName("port-inputport");
    }
    
    @Override
    public boolean isContainerPort() {
        return false;
    }

    @Override
    public boolean isInputPort() {
        return true;
    }

    @Override
    public boolean isOutputPort() {
        return false;
    }

    @Override
    public void registerDropController() {
        PickupDragController opdc = DndRegistry.getInstance()
                .getPickupDragController("OutputPortDragController");
        opdc.registerDropController(getDropController());
        dropControllerRegistered = true;
    }
    
    @Override
    public void unregisterDropController() {
        PickupDragController opdc = DndRegistry.getInstance()
                .getPickupDragController("OutputPortDragController");
        opdc.unregisterDropController(getDropController());
        dropControllerRegistered = false;
    }

}
