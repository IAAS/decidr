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

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.user.client.ui.AbsolutePanel;

/**
 * TODO: add comment
 * 
 * @author JE
 */
public class ConnectionDragController extends PickupDragController {

    /**
     * TODO: add comment
     * 
     * @param boundaryPanel
     * @param allowDroppingOnBoundaryPanel
     */
    public ConnectionDragController(AbsolutePanel boundaryPanel,
            boolean allowDroppingOnBoundaryPanel) {
        super(boundaryPanel, allowDroppingOnBoundaryPanel);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void dragMove() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dragEnd() {
        // TODO Auto-generated method stub
        super.dragEnd();
    }

    @Override
    public void dragStart() {
        // TODO Auto-generated method stub
        super.dragStart();
    }

    @Override
    public void previewDragEnd() throws VetoDragException {
        // TODO Auto-generated method stub
        super.previewDragEnd();
    }

}
