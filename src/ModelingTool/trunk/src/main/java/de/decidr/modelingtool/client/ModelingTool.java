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
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

import de.decidr.modelingtool.client.command.CommandStack;
import de.decidr.modelingtool.client.command.CreateConnectionCommand;
import de.decidr.modelingtool.client.command.CreateInvokeNodeCommand;
import de.decidr.modelingtool.client.command.CreateContainerCommand;
import de.decidr.modelingtool.client.exception.IncompleteModelDataException;
import de.decidr.modelingtool.client.menu.Menu;
import de.decidr.modelingtool.client.model.ConnectionModel;
import de.decidr.modelingtool.client.model.EmailInvokeNodeModel;
import de.decidr.modelingtool.client.model.FlowContainerModel;
import de.decidr.modelingtool.client.model.WorkflowModel;
import de.decidr.modelingtool.client.model.foreach.ForEachContainerModel;
import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;
import de.decidr.modelingtool.client.ui.dialogs.WorkflowPropertyWindow;
import de.decidr.modelingtool.client.ui.dialogs.containerwindows.ForEachWindow;
import de.decidr.modelingtool.client.ui.dialogs.email.EmailActivityWindow;
import de.decidr.modelingtool.client.ui.dialogs.humantask.HumanTaskActivityWindow;
import de.decidr.modelingtool.client.ui.dialogs.variableeditor.VariableEditor;
import de.decidr.modelingtool.client.ui.resources.Messages;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ModelingTool implements EntryPoint {

    public static Messages messages;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        /* Internationalization: "Instantiate" the Message interface class. */
        messages = GWT.create(Messages.class);

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
        final Menu menu = new Menu();
        RootPanel.get("menu").add(menu);

        // create workflow and add to the root panel.
        final Workflow workflow = Workflow.getInstance();
        RootPanel.get("workflow").add(workflow);

        // create workflow model
        WorkflowModel workflowModel = new WorkflowModel();
        workflow.setModel(workflowModel);
        workflowModel.setChangeListener(workflow);

        // create test elements
        try {
            EmailInvokeNodeModel model1 = new EmailInvokeNodeModel(
                    workflowModel);
            CommandStack.getInstance().executeCommand(
                    new CreateInvokeNodeCommand(model1, 50, 50));

            EmailInvokeNodeModel model2 = new EmailInvokeNodeModel(
                    workflowModel);
            CommandStack.getInstance().executeCommand(
                    new CreateInvokeNodeCommand(model2, 150, 150));

            ConnectionModel conModel = new ConnectionModel();
            conModel.setSource(model1);
            conModel.setTarget(model2);
            conModel.setParentModel(workflowModel);
            CommandStack.getInstance().executeCommand(
                    new CreateConnectionCommand(conModel));

            FlowContainerModel flowModel = new FlowContainerModel(workflowModel);
            CommandStack.getInstance().executeCommand(
                    new CreateContainerCommand(flowModel, 250, 50, 300, 200));

            ForEachContainerModel forEachModel = new ForEachContainerModel(
                    workflowModel);
            CommandStack.getInstance()
                    .executeCommand(
                            new CreateContainerCommand(forEachModel, 50, 250,
                                    150, 150));
        } catch (IncompleteModelDataException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
