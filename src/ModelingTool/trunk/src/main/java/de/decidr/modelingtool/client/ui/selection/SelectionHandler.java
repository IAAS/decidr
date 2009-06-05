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

import de.decidr.modelingtool.client.ui.Connection;
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

    private Workflow parentWorkflow = null;

    private NodeSelectionBox nodeSelectionBox = null;

    public SelectionHandler(Workflow parentWorkflow) {
        this.parentWorkflow = parentWorkflow;

        nodeSelectionBox = new NodeSelectionBox(parentWorkflow);
    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
        Object source = event.getSource();
        //System.out.println (source);

        // select the clicked node or connection
        if (source instanceof Node) {
            Node node = (Node) source;
            select(node);
        } else if (source instanceof Connection) {
            Connection connection = (Connection) source;
            select(connection);
        }
    }
    
    public void update() {
        if (selectedItem instanceof Node) {
            nodeSelectionBox.assignTo((Node)selectedItem);
        }
        //TODO
    }

    public void select(Node node) {
        // unselect the selected item
        unselect();
        selectedItem = node;
        node.setSelected(true);
        nodeSelectionBox.assignTo(node);
    }

    public void select(Connection connection) {

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
            //TODO
            return new Vector<DragBox>();
        }
    }

}
