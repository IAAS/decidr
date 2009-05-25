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

import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.user.client.ui.HTML;

/**
 * TODO: add comment
 *
 * @author engelhjs
 */
public abstract class AbstractPort extends HTML implements Port {
    
    /**
     * TODO: add comment
     */
    private boolean multipleConnectionsAllowed;
    
    /**
     * TODO: add comment
     */
    private List<Connection> connections;
    
    /**
     * TODO: add comment
     */
    private Node parent;
    
    /**
     * TODO: add comment
     */
    private DropController dropController;

    /* (non-Javadoc)
     * @see de.decidr.modelingtool.client.ui.Port#getConnections()
     */
    @Override
    public List<Connection> getConnections() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.decidr.modelingtool.client.ui.Port#getDropController()
     */
    @Override
    public DropController getDropController() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.decidr.modelingtool.client.ui.Port#getParent()
     */
    @Override
    public Node getParentNode() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.decidr.modelingtool.client.ui.Port#isMultipleConnectionsAllowed()
     */
    @Override
    public boolean isMultipleConnectionsAllowed() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see de.decidr.modelingtool.client.ui.Port#setMultipleConnectionsAllowed(boolean)
     */
    @Override
    public void setMultipleConnectionsAllowed(boolean mcAllowed) {
        // TODO Auto-generated method stub

    }

}
