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

package de.decidr.modelingtool.client.ui.dialogs.humantask;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.command.ChangeNodePropertiesCommand;
import de.decidr.modelingtool.client.command.CommandStack;
import de.decidr.modelingtool.client.model.humantask.TaskItem;
import de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel;
import de.decidr.modelingtool.client.ui.HumanTaskInvokeNode;
import de.decidr.modelingtool.client.ui.dialogs.Dialog;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class HumanTaskActivityWindow extends Dialog {

    private HumanTaskInvokeNode node;
    private HumanTaskInvokeNodeModel model;

    private HumanTaskActivityWindowContentPanel taskPanel;

    public HumanTaskActivityWindow() {
        // TODO: fix layout
        super();
        this.setLayout(new FitLayout());
        this.setSize(600, 400);
        this.setResizable(true);
        createContentPanel();
        createButtons();
    }

    private void createContentPanel() {
        // JS rewrite, form panel is obsolete
        /*
         * There is no ContentPanel here. A taskPanel and a formPanel form the
         * "ContentPanel".
         */
        taskPanel = new HumanTaskActivityWindowContentPanel();
        this.add(taskPanel);
    }

    private void createButtons() {
        setButtonAlign(HorizontalAlignment.CENTER);
        addButton(new Button(ModelingToolWidget.messages.okButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        // JS implement change listener
                        changeWorkflowModel();
                        DialogRegistry.getInstance().hideDialog(
                                HumanTaskActivityWindow.class.getName());
                    }
                }));
        addButton(new Button(ModelingToolWidget.messages.cancelButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        DialogRegistry.getInstance().hideDialog(
                                HumanTaskActivityWindow.class.getName());
                    }
                }));
    }

    public void setNode(HumanTaskInvokeNode node) {
        this.node = node;
        model = (HumanTaskInvokeNodeModel) node.getModel();
    }

    private void changeWorkflowModel() {
        HumanTaskInvokeNodeModel newModel = new HumanTaskInvokeNodeModel(node
                .getModel().getParentModel());
        newModel.setUserVariableId(taskPanel.getUserField().getValue().getId());
        newModel.setFormVariableId(taskPanel.getFormContainerField().getValue()
                .getId());
        newModel.setNotifyVariableId(taskPanel.getNotifyCheckBox().getValue());
        List<TaskItem> formElements = new ArrayList<TaskItem>();
        for (HTFieldSet fields : taskPanel.getFormElementFields()) {
            TaskItem element = new TaskItem(fields.getLabelField()
                    .getValue(), fields.getVariableField().getValue().getId());
            formElements.add(element);
        }
        newModel.setFormElements(formElements);
        // JS check if changed
        CommandStack.getInstance().executeCommand(
                new ChangeNodePropertiesCommand<HumanTaskInvokeNode>(node,
                        newModel.getProperties()));
    }

    @Override
    public void initialize() {
        // TODO Auto-generated method stub
        taskPanel.createFields(model);
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
        taskPanel.clearAllEntries();
    }

    @Override
    public void refresh() {
        // TODO Auto-generated method stub

    }

}
