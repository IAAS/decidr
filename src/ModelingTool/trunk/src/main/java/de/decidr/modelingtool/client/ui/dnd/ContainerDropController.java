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

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.google.gwt.user.client.ui.AbsolutePanel;

import de.decidr.modelingtool.client.ui.EndNode;
import de.decidr.modelingtool.client.ui.StartNode;

/**
 * TODO: add comment
 * 
 * @author JE
 */
public class ContainerDropController extends AbsolutePositionDropController {

    /**
     * TODO: add comment
     * 
     * @param dropTarget
     */
    public ContainerDropController(AbsolutePanel dropTarget) {
        super(dropTarget);
    }

    @Override
    public void onPreviewDrop(DragContext context) throws VetoDragException {
        super.onPreviewDrop(context);

        if (context.draggable instanceof StartNode
                || context.draggable instanceof EndNode) {
            throw new VetoDragException();
        }
    }

}
