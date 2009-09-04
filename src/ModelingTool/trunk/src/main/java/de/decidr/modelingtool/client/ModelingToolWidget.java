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

import de.decidr.modelingtool.client.command.CreateWorkflowCommand;
import de.decidr.modelingtool.client.exception.IncompleteModelDataException;
import de.decidr.modelingtool.client.io.DWDLParser;
import de.decidr.modelingtool.client.io.DWDLParserImpl;
import de.decidr.modelingtool.client.menu.Menu;
import de.decidr.modelingtool.client.model.WorkflowModel;
import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.resources.Messages;

/**
 * TODO: add comment
 * 
 * @author Johannes Engelhardt
 */
public class ModelingToolWidget extends Composite implements
        HasAllMouseHandlers {

    public static Messages messages;

    private static HashMap<Long, String> users;

    // private static final String html = "<table align=\"center\">"
    // + "<tr><td id=\"menu\"></td></tr>"
    // + "<tr><td id=\"workflow\"></td></tr>" + "</table>";

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

        /*
         * ButtonBar buttonBar = new ButtonBar(); buttonBar.add(new
         * Button("Variables", new SelectionListener<ButtonEvent>() {
         * 
         * @Override public void componentSelected(ButtonEvent ce) {
         * DialogRegistry.getInstance().showDialog(
         * VariableEditor.class.getName()); } })); buttonBar.add(new
         * Button("Email", new SelectionListener<ButtonEvent>() {
         * 
         * @Override public void componentSelected(ButtonEvent ce) {
         * DialogRegistry.getInstance().showDialog(
         * EmailActivityWindow.class.getName()); } })); buttonBar.add(new
         * Button("HT", new SelectionListener<ButtonEvent>() {
         * 
         * @Override public void componentSelected(ButtonEvent ce) {
         * DialogRegistry.getInstance().showDialog(
         * HumanTaskActivityWindow.class.getName()); } })); buttonBar.add(new
         * Button("ForEach", new SelectionListener<ButtonEvent>() {
         * 
         * @Override public void componentSelected(ButtonEvent ce) {
         * DialogRegistry.getInstance().showDialog(
         * ForEachWindow.class.getName()); } })); buttonBar.add(new
         * Button("Workflow", new SelectionListener<ButtonEvent>() {
         * 
         * @Override public void componentSelected(ButtonEvent ce) {
         * DialogRegistry.getInstance().showDialog(
         * WorkflowPropertyWindow.class.getName()); } }));
         * RootPanel.get().add(buttonBar);
         */
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

    public void init() {
        // Load Workflow Model
        // TODO: substitute stub by real implementation
        // This code maybe obsolete, except for testin purposes
//        WorkflowIO io = new WorkflowIOStub();
//        // WorkflowIO io = new WorkflowIOImpl(dataExchanger);
//        try {
//            WorkflowModel workflowModel = io.loadWorkflow();
//            Command createWorkflowCmd = new CreateWorkflowCommand(workflowModel);
//            createWorkflowCmd.execute();
//        } catch (LoadDWDLException e) {
//            Window.alert(e.getMessage());
//        } catch (IncompleteModelDataException e) {
//            Window.alert(e.getMessage());
//        }
    }

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

    public void setUsers(String userxml) {
        // JS: implement, first parse xml with gwt tools, then create user
        // objects as hash maps
        users = new HashMap<Long, String>();
        users.put(0L, "dsf");
        users.put(1L, "sdf");
        users.put(2L, "fgh");
    }

    public static HashMap<Long, String> getUsers() {
        return users;
    }

    public static void sendDWDLtoServer(String dwdl) {
        /*
         * This method is intentionally left emtpy. The implementation is done
         * by the child class in the WebPortal.
         */
    }

}
