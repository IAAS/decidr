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

package main.java.de.decidr.modelingtool.client.ui.dnd;

import main.java.de.decidr.modelingtool.client.ui.Workflow;

import com.allen_sauer.gwt.dnd.client.PickupDragController;


/**
 * TODO: add comment
 * 
 * @author JE
 */
public class WorkflowDragController extends PickupDragController {

    private Workflow workflow;

    /**
     * TODO: add comment
     * 
     * @param boundaryPanel
     * @param allowDroppingOnBoundaryPanel
     */
    public WorkflowDragController(Workflow workflow) {
        super(workflow, true);

        this.workflow = workflow;
    }

    @Override
    public void dragEnd() {
        super.dragEnd();

        // remove the drag boxes assiged to the drag context.
        this.context.selectedWidgets.removeAll(workflow.getSelectionHandler()
                .getDragBoxes());
        
        // reset the drag boxes
        workflow.getSelectionHandler().update();
    }

    @Override
    public void dragMove() {
        super.dragMove();
    }

    @Override
    public void dragStart() {
        // add the drag boxes to the drag context to move them with the object
        this.context.selectedWidgets.addAll(workflow.getSelectionHandler()
                .getDragBoxes());
        
        super.dragStart();
    }

}
