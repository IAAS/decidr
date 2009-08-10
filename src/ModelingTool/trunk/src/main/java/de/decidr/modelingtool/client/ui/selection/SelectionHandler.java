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

import java.util.List;
import java.util.Vector;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

import de.decidr.modelingtool.client.ui.Connection;
import de.decidr.modelingtool.client.ui.ConnectionLine;
import de.decidr.modelingtool.client.ui.Node;
import de.decidr.modelingtool.client.ui.Selectable;

/**
 * This class handles the selection of objects in the workflow, which implement
 * the Selectable interface. The selection handler has to be registered to each
 * element which is to be selectable. This is a singleton.
 * 
 * @author Johannes Engehlardt
 */
public class SelectionHandler implements MouseDownHandler {

    /** The currently selected item. Null, if none is selected. */
    private Selectable selectedItem = null;

    /** The node selection bo to select nodes. */
    private NodeSelectionBox nodeSelectionBox = null;

    /** The instance of the selection handler. */
    private static SelectionHandler instance;

    /**
     * Private constructor.
     */
    private SelectionHandler() {
        nodeSelectionBox = new NodeSelectionBox();
    }

    /**
     * Creates a new instance, if not done before, and returns it.
     * 
     * @return The instance.
     */
    public static SelectionHandler getInstance() {
        if (instance == null) {
            instance = new SelectionHandler();
        }
        return instance;
    }

    /**
     * Returns a list of the drag boxes of the currently selected item, if it is
     * a node. Else it returns an empty list.
     */
    public List<DragBox> getDragBoxes() {
        if (selectedItem instanceof Node) {
            return nodeSelectionBox.getDragBoxes();
        } else {
            return new Vector<DragBox>();
        }
    }

    public Selectable getSelectedItem() {
        return selectedItem;
    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
        // workaround for Firefox: prevent propagation of the event to
        // underlying objects (workflow or container), so that the selected item
        // stays selected!
        event.stopPropagation();

        Object source = event.getSource();
        //System.out.println(source.getClass());

        // select the clicked node or connection
        Widget w = (Widget) source;
        if (source instanceof FocusPanel && w.getParent() instanceof Node) {
            Node node = (Node) w.getParent();
            select(node);

        } else if (source instanceof ConnectionLine) {
            Connection connection = ((ConnectionLine) source).getConnection();
            select(connection);
            
        } else if (source instanceof ConnectionDragBox) {
            Connection connection = ((ConnectionDragBox) source)
                    .getConnection();
            if (connection != null) {
                select(connection);
            }
        } else {
            // unselect selected item
            unselect();
        }
    }

    /**
     * Refreshes the selection representation of the currently selected item.
     */
    public void refreshSelection() {
        if (selectedItem instanceof Node) {
            nodeSelectionBox.refreshPosition();
        }
    }

    /**
     * Selects the given selectable item.
     * 
     * @param selectedItem
     *            The item to select.
     */
    public void select(Selectable selectedItem) {
        // unselect the selected item
        unselect();

        // assign node selection box, if selected item is a node
        if (selectedItem instanceof Node) {
            nodeSelectionBox.assignTo((Node) selectedItem);
        }

        this.selectedItem = selectedItem;
        selectedItem.setSelected(true);
    }

    public void setSelectedItem(Selectable selectedItem) {
        this.selectedItem = selectedItem;
    }

    /**
     * Removes the selection of the durrently selected item, if one is selected.
     */
    public void unselect() {
        // unselect currently selected item
        if (selectedItem != null) {
            selectedItem.setSelected(false);
            selectedItem = null;
        }
        nodeSelectionBox.unassign();
    }

}
