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

import java.util.HashMap;
import java.util.Map;

import com.allen_sauer.gwt.dnd.client.DragController;
import com.allen_sauer.gwt.dnd.client.PickupDragController;

/**
 * The registry of drag controllers. Any drag controller is registered here to
 * make it globally accessible. Every drag controller is associated with a name.
 * This is a singleton.
 * 
 * @author Johannes Engelhardt
 */
public class DndRegistry {

    /** The instance of the dnd registry. */
    private static DndRegistry instance = null;

    /** The map containing the drag controllers. */
    private Map<String, DragController> dragControllers = new HashMap<String, DragController>();

    /** Private contructor. */
    private DndRegistry() {}

    /**
     * Creates a new instance, if not done before, and returns it.
     *
     * @return The instance.
     */
    public static DndRegistry getInstance() {
        if (instance == null) {
            instance = new DndRegistry();
        }
        return instance;
    }

    /**
     * Registers a new drag controller-
     *
     * @param name The name of the drag controller
     * @param dragController The drag controller object
     */
    public void register(String name, DragController dragController) {
        dragControllers.put(name, dragController);
    }

    /**
     * Returns the drag controller associated with the given name as a
     * PickupDragController, if it is an instance of this class.
     *
     * @param name The name of the drag controller
     * @return The PickupDragController object if it is one, else null
     */
    public PickupDragController getPickupDragController(String name) {
        DragController dc = dragControllers.get(name);

        if (dc instanceof PickupDragController) {
            return (PickupDragController) dc;
        } else {
            return null;
        }
    }

    /**
     * Returns the drag controller associated with the given name.
     *
     * @param name The name of the drag controller
     * @return The crag controller object
     */
    public DragController getDragController(String name) {
        return dragControllers.get(name);
    }

}
