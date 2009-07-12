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

package de.decidr.ui.gwt.client.ui;

import com.vaadin.terminal.Paintable;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

import de.decidr.modelingtool.client.ModelingToolWidget;

/**
 * TODO: add comment
 *
 * @author AT
 */
public class VModelingTool extends ModelingToolWidget implements com.vaadin.terminal.gwt.client.Paintable {
    
    private String uidlId;
    
    private ApplicationConnection client;
    
    private final String CLASSNAME = "modelingtool";
    
    /**
     * TODO: add comment
     *
     */
    public VModelingTool() {
        super();
        setStyleName(CLASSNAME);
    }

    /* (non-Javadoc)
     * @see com.vaadin.terminal.gwt.client.Paintable#updateFromUIDL(com.vaadin.terminal.gwt.client.UIDL, com.vaadin.terminal.gwt.client.ApplicationConnection)
     */
    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (client.updateComponent(this, uidl, true))
            return;
        
        this.client = client;
        
        uidlId = uidl.getId();
        
    }
    
   

}
