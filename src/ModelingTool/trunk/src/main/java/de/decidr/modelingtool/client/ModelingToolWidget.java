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

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;

import de.decidr.modelingtool.client.command.CreateWorkflowCommand;
import de.decidr.modelingtool.client.exception.IncompleteModelDataException;
import de.decidr.modelingtool.client.exception.LoadDWDLException;
import de.decidr.modelingtool.client.io.WorkflowIO;
import de.decidr.modelingtool.client.io.WorkflowIOStub;
import de.decidr.modelingtool.client.menu.Menu;
import de.decidr.modelingtool.client.model.WorkflowModel;
import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;
import de.decidr.modelingtool.client.ui.dialogs.WorkflowPropertyWindow;
import de.decidr.modelingtool.client.ui.dialogs.email.EmailActivityWindow;
import de.decidr.modelingtool.client.ui.dialogs.foreachcontainer.ForEachWindow;
import de.decidr.modelingtool.client.ui.dialogs.humantask.HumanTaskActivityWindow;
import de.decidr.modelingtool.client.ui.dialogs.variableeditor.VariableEditor;
import de.decidr.modelingtool.client.ui.resources.Messages;

/**
 * TODO: add comment
 * 
 * @author JE
 */
public class ModelingToolWidget extends HTMLPanel {

    public static Messages messages;

    private static final String html = "<table align=\"center\">"
            + "<tr><td id=\"menu\"></td></tr>"
            + "<tr><td id=\"workflow\"></td></tr>" + "</table>";

    public ModelingToolWidget() {
        super(html);

        /* Internationalization: "Instantiate" the Message interface class. */
        messages = GWT.create(Messages.class);
    }

    public void init(long workflowModelId) {
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.add(new Button("Variables",
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        DialogRegistry.getInstance().showDialog(
                                VariableEditor.class.getName());
                    }
                }));
        buttonBar.add(new Button("Email", new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                DialogRegistry.getInstance().showDialog(
                        EmailActivityWindow.class.getName());
            }
        }));
        buttonBar.add(new Button("HT", new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                DialogRegistry.getInstance().showDialog(
                        HumanTaskActivityWindow.class.getName());
            }
        }));
        buttonBar.add(new Button("ForEach",
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        DialogRegistry.getInstance().showDialog(
                                ForEachWindow.class.getName());
                    }
                }));
        buttonBar.add(new Button("Workflow",
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        DialogRegistry.getInstance().showDialog(
                                WorkflowPropertyWindow.class.getName());
                    }
                }));
        RootPanel.get().add(buttonBar);

        // create menu
        Menu menu = new Menu();
        this.add(menu, "menu");

        // create workflow and add to the root panel.
        Workflow workflow = Workflow.getInstance();
        this.add(workflow, "workflow");
        
        // Load Workflow Model
        
        // TODO: substitude stub by real implementation
        WorkflowIO io = new WorkflowIOStub();

        try {
            WorkflowModel workflowModel = io.loadWorkflow(workflowModelId);
            Command createWorkflowCmd = new CreateWorkflowCommand(workflowModel);
            createWorkflowCmd.execute();

        } catch (LoadDWDLException e) {
            Window.alert(e.getMessage());
        } catch (IncompleteModelDataException e) {
            Window.alert(e.getMessage());
        }
    }

}
