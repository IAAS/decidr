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

import java.util.List;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * TODO: add comment
 * 
 * @author engelhjs
 */
public abstract class AbstractNode extends AbsolutePanel implements
        ModelChangeListener, Selectable {

    /**
     * TODO: add comment
     */
    private Widget graphic;

    /**
     * TODO: add comment
     */
    private boolean selected;

    /**
     * TODO: add comment
     */
    private boolean resizable;

    /**
     * TODO: add comment
     */
    private boolean deletable;

    /**
     * TODO: add comment
     */
    private boolean moveable;
    
    /**
     * TODO: add comment
     */
    private List<Port> ports;

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.ui.ModelChangeListener#onModelChange()
     */
    @Override
    public void onModelChange() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isSelected() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setSelected(boolean selected) {
        // TODO Auto-generated method stub
        
    }

    
    
}
