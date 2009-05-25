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

import java.util.Vector;

import javax.swing.plaf.basic.BasicSplitPaneDivider.DragController;

import com.google.gwt.user.client.ui.AbsolutePanel;

/**
 * TODO: add comment
 *
 * @author engelhjs
 */
public class Workflow extends AbsolutePanel implements ModelChangeListener {
    
    /**
     * TODO: add comment
     */
    private DragController dragController;
    
    /**
     * TODO: add comment
     */
    private Selectable selectedItem;
    
    /**
     * TODO: add comment
     */
    private WorkflowModel model;
    
    /**
     * TODO: add comment
     */
    private Vector<Node> nodes;
    
    /**
     * TODO: add comment
     *
     */
    public Workflow() {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see de.decidr.modelingtool.client.ui.ModelChangeListener#onModelChange()
     */
    @Override
    public void onModelChange() {
        // TODO Auto-generated method stub
        
    }

}
