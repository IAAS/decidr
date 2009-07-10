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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

import de.decidr.modelingtool.client.ui.Connection;
import de.decidr.modelingtool.client.ui.ConnectionLine;
import de.decidr.modelingtool.client.ui.Node;
import de.decidr.modelingtool.client.ui.Selectable;
import de.decidr.modelingtool.client.ui.Workflow;

/**
 * TODO: add comment
 * 
 * @author JE
 */
public class SelectionHandler implements MouseDownHandler {

    private Selectable selectedItem = null;

    public Selectable getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Selectable selectedItem) {
        this.selectedItem = selectedItem;
    }

    private Workflow parentWorkflow;

    private NodeSelectionBox nodeSelectionBox = null;

    public SelectionHandler(Workflow parentWorkflow) {
        this.parentWorkflow = parentWorkflow;

        nodeSelectionBox = new NodeSelectionBox();
    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
        // workaround for Firefox: prevent propagation of the event to
        // underlying objects (workflow or container), so that the selected item
        // stays selected!
        event.stopPropagation();
        
        Object source = event.getSource();
        // System.out.println (source);

        // select the clicked node or connection
        if (source instanceof Widget) {
            Widget w = (Widget)source;
            if (w.getParent() instanceof Node) {
                Node node = (Node) w.getParent();
                select(node);

            } else if (source instanceof ConnectionLine) {
                Connection connection = ((ConnectionLine) source).getConnection();
                select(connection);
                //Window.alert(connection.toString());
            } else {
                // unselect selected item
                
                unselect();
            }
        }
    }

    public void refreshSelection() {
        if (selectedItem instanceof Node) {
            nodeSelectionBox.refreshPosition();
        }
        // TODO: connection
    }

    public void select(Selectable selectedItem) {
        // unselect the selected item
        unselect();
        
        // assign node selection box, if selected item is a node
        if (selectedItem instanceof Node) {
            nodeSelectionBox.assignTo((Node)selectedItem);
        }
        
        this.selectedItem = selectedItem;
        selectedItem.setSelected(true);   
    }

    public void unselect() {
        // unselect currently selected item
        if (selectedItem != null) {
            selectedItem.setSelected(false);
            selectedItem = null;
        }
        nodeSelectionBox.unassign();
    }

    /**
     * 
     * adds the drag boxes to the list.
     * 
     * @param widgets
     */
    public List<DragBox> getDragBoxes() {
        if (selectedItem instanceof Node) {
            return nodeSelectionBox.getDragBoxes();
        } else {
            // TODO
            return new Vector<DragBox>();
        }
    }

}
