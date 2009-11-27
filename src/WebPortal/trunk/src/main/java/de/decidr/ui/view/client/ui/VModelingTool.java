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

package de.decidr.ui.view.client.ui;

import org.apache.log4j.Logger;

import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

import de.decidr.model.logging.DefaultLogger;

/**
 * TODO: add comment
 * 
 * @author TODO
 */
public class VModelingTool extends
        de.decidr.modelingtool.client.ModelingToolWidget implements Paintable {

    private Logger logger = DefaultLogger.getLogger(VModelingTool.class);

    /** Set the tagname used to statically resolve widget from UIDL. */
    public static final String TAGNAME = "modelingtool";

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-" + TAGNAME;

    /** Component identifier in UIDL communications. */
    String uidlId;

    /** Reference to the server connection object. */
    ApplicationConnection client;

    /**
     * The constructor should first call super() to initialize the component and
     * then handle any initialization relevant to Vaadin.
     */
    public VModelingTool() {
        super();
        // This method call of the Paintable interface sets the component
        // style name in DOM tree
        setStyleName(CLASSNAME);
    }

    @Override
    public void sendDWDLtoServer(String dwdl) {
        logger.debug("Trying to send the DWDL document to the server...");

        // Updating the state to the server can not be done
        // before the server connection is known, i.e., before
        // updateFromUIDL() has been called.
        if ((uidlId == null) || (client == null)) {
            logger.debug("Failed. UidlId or client are null");
            return;
        }

        // Communicate the user interaction parameters to server.
        // This call will initiate an AJAX request to the server.
        client.updateVariable(uidlId, "dwdl", dwdl, false);

        logger.debug("Sending DWDL document was successful.");
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        // This call should be made first. Ensure correct implementation,
        // and let the containing layout manage caption, etc.
        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        // Save reference to server connection object to be able to send
        // user interaction later
        this.client = client;

        // Save the UIDL identifier for the component
        uidlId = uidl.getId();

        // Set the DWDL document received from server
        setDWDL(uidl.getStringVariable("dwdl"));
        
        //Set the user list received from server
        setUsers(uidl.getStringVariable("users"));
    }
}
