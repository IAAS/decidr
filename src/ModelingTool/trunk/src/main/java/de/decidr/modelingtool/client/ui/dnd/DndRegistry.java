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
 * TODO: add comment
 * 
 * @author JE
 */
public class DndRegistry {

    private static DndRegistry instance = null;

    private Map<String, DragController> dragControllers = new HashMap<String, DragController>();

    private DndRegistry() {

    }

    public static DndRegistry getInstance() {
        if (instance == null) {
            instance = new DndRegistry();
        }
        return instance;
    }

    public void register(String name, DragController dragController) {
        dragControllers.put(name, dragController);
    }

    public PickupDragController getPickupDragController(String name) {
        DragController dc = dragControllers.get(name);

        if (dc instanceof PickupDragController) {
            return (PickupDragController) dc;
        } else {
            return null;
        }
    }

    public DragController getDragController(String name) {
        return dragControllers.get(name);
    }

}
