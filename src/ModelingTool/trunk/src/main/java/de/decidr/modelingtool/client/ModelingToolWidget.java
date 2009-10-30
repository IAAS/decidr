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

package de.decidr.modelingtool.client;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import de.decidr.modelingtool.client.command.CreateWorkflowCommand;
import de.decidr.modelingtool.client.exception.IncompleteModelDataException;
import de.decidr.modelingtool.client.io.DWDLParser;
import de.decidr.modelingtool.client.io.DWDLParserImpl;
import de.decidr.modelingtool.client.menu.Menu;
import de.decidr.modelingtool.client.model.WorkflowModel;
import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.resources.Messages;

/**
 * The main class of the modeling tool.
 * 
 * @author Johannes Engelhardt, Jonas Schlaak
 */
public class ModelingToolWidget extends Composite implements
        HasAllMouseHandlers {

    private static Messages messages;

    private static HashMap<Long, String> users;

    private static ModelingToolWidget instance;

    // private static final String html = "<table align=\"center\">"
    // + "<tr><td id=\"menu\"></td></tr>"
    // + "<tr><td id=\"workflow\"></td></tr>" + "</table>";

    public static ModelingToolWidget getInstance() {
        if (instance == null) {
            instance = new ModelingToolWidget();
        }
        return instance;
    }

    public ModelingToolWidget() {
        super();

        VerticalPanel panel = new VerticalPanel();

        // create menu
        Menu menu = new Menu();
        panel.add(menu);

        // create workflow and add to the root panel.
        Workflow workflow = Workflow.getInstance();
        panel.add(workflow);

        initWidget(panel);

        /* Internationalization: "Instantiate" the Message interface class. */
        messages = GWT.create(Messages.class);

    }

    public static Messages getMessages() {
        if (messages == null) {
            messages = GWT.create(Messages.class);
        }
        return messages;
    }

    @Override
    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return addDomHandler(handler, MouseOutEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
        return addDomHandler(handler, MouseMoveEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        return addDomHandler(handler, MouseOverEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return addDomHandler(handler, MouseDownEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
        return addDomHandler(handler, MouseWheelEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
        return addDomHandler(handler, MouseUpEvent.getType());
    }

    @Deprecated
    public void init() {

    }

    /**
     * Sets the dwdl document for the modeling tool. The modeling tool parses
     * the dwdl and creates the (graphical) workflow.
     * 
     * @param dwdl
     *            the dwdl document
     */
    public void setDWDL(String dwdl) {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel workflowModel = parser.parse(dwdl);
        try {
            Command createWorkflowCmd = new CreateWorkflowCommand(workflowModel);
            createWorkflowCmd.execute();
        } catch (IncompleteModelDataException e) {
            Window.alert(e.getMessage());
        }
    }

    /**
     * Sets the userlist (all users of the tenant) for the modeling tool. The
     * userlist is a xml document, in which every user element has the user id
     * and a display name for user.
     * 
     * @param userxml
     *            the user list as xml
     */
    public void setUsers(String userxml) {
        users = new HashMap<Long, String>();

        Document doc = XMLParser.createDocument();
        doc = XMLParser.parse(userxml);
        NodeList list = doc.getElementsByTagName("user");

        for (int i = 0; i < list.getLength(); i++) {
            Element element = (Element) list.item(i);
            Long id = new Long(element.getAttribute("id"));
            String name = element.getAttribute("name");
            users.put(id, name);
        }
    }

    /**
     * Returns the tenant users as a hash map with user id and display username.
     * 
     * @return the user hash map
     */
    public HashMap<Long, String> getUsers() {
        return users;
    }

    public void sendDWDLtoServer(String dwdl) {
        /*
         * This method is intentionally left emtpy. The implementation is done
         * by the child class in the WebPortal.
         */
        GWT.log("DWDL output", null);
        GWT.log(dwdl, null);
    }

}
