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
 * The start port of the container. All flows in the container has to start from
 * this port.
 * 
 * @author Johannes Engelhardt
 */
public class ContainerStartPort extends Port {

    /** The y coordinate offset of this port */
    private static final int Y_OFFSET = 15;

    public ContainerStartPort() {
        super(Port.Position.TOP, 0, Y_OFFSET);

        // set properties
        this.addStyleName("port-container-startport");
    }

    @Override
    public boolean isContainerPort() {
        return true;
    }

    @Override
    public boolean isInputPort() {
        return false;
    }

    @Override
    public boolean isOutputPort() {
        return true;
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

}
