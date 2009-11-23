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

import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.ToolBarEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.command.ChangeNodePropertiesCommand;
import de.decidr.modelingtool.client.command.CommandStack;
import de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel;
import de.decidr.modelingtool.client.model.humantask.TaskItem;
import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.model.variable.VariableType;
import de.decidr.modelingtool.client.ui.HumanTaskInvokeNode;
import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;
import de.decidr.modelingtool.client.ui.dialogs.ModelingToolDialog;

/**
 * The property window for a {@link HumanTaskInvokeNode}. It consists of
 * comboboxes for selecting the role, the task name and description, the form
 * container and a checkbox to determine whether the users should be notified
 * when they receive the work item or not. With the tool bar, the user can add
 * or remove a {@link TaskItemFieldSet}.
 * 
 * @author Jonas Schlaak
 */
public class HumanTaskActivityWindow extends ModelingToolDialog {

    private HumanTaskInvokeNode node;
    private HumanTaskInvokeNodeModel model;

    private ContentPanel contentPanel;
    private ScrollPanel scrollPanel;
    private FlexTable table;

    private ComboBox<Variable> userField;
    private ComboBox<Variable> nameField;
    private ComboBox<Variable> descriptionField;
    private ComboBox<Variable> formContainerField;
    private CheckBox notifyCheckBox;

    public HumanTaskActivityWindow() {
        super();
        this.setLayout(new FitLayout());
        this.setSize(400, 250);
        this.setResizable(true);
        createContentPanel();
        createButtons();
    }

    /**
     * Creates a {@link ContentPanel} which holds a {@link FlexTable} to which
     * the comboboxes are added.
     */
    private void createContentPanel() {
        contentPanel = new ContentPanel();

        contentPanel.setHeading(ModelingToolWidget.getMessages()
                .humanTaskActivity());
        contentPanel.setLayout(new FitLayout());

        table = new FlexTable();
        table.setBorderWidth(0);
        table.setWidth("100%");
        table.setCellPadding(2);
        table.setCellSpacing(2);
        scrollPanel = new ScrollPanel(table);
        contentPanel.add(scrollPanel);

        createToolBar();

        this.add(contentPanel);
    }

    private void createToolBar() {
        ToolBar toolBar = new ToolBar();

        /* tool bar button which displays the TaskItemWindow */
        TextToolItem editButton = new TextToolItem(ModelingToolWidget
                .getMessages().editTaskItems());
        editButton.addSelectionListener(new SelectionListener<ToolBarEvent>() {
            @Override
            public void componentSelected(ToolBarEvent ce) {
                TaskItemWindowInvoker.invoke(model);
            }
        });
        toolBar.add(editButton);

        this.setBottomComponent(toolBar);
    }

    /**
     * Creates the ok and cancel button.
     */
    private void createButtons() {
        setButtonAlign(HorizontalAlignment.CENTER);
        addButton(new Button(ModelingToolWidget.getMessages().okButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        /*
                         * check if the inputs are valid. If not, display a
                         * warning message, else change the workflow model.
                         */
                        if (validateInputs()) {
                            changeWorkflowModel();
                            DialogRegistry.getInstance().hideDialog(
                                    HumanTaskActivityWindow.class.getName());
                        } else {
                            MessageBox.alert(ModelingToolWidget.getMessages()
                                    .warningTitle(), ModelingToolWidget
                                    .getMessages().humanTaskActivityWarning(),
                                    null);
                        }
                    }
                }));
        addButton(new Button(ModelingToolWidget.getMessages().cancelButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        DialogRegistry.getInstance().hideDialog(
                                HumanTaskActivityWindow.class.getName());
                    }
                }));
    }

    private boolean validateInputs() {
        boolean result = false;
        if (userField.getValue() != null && nameField.getValue() != null
                && descriptionField.getValue() != null
                && formContainerField.getValue() != null) {
            result = true;
        }
        return result;
    }

    /**
     * Sets the {@link HumanTaskInvokeNode} whose properties are to be modeled
     * with this window.
     * 
     * @param node
     *            the HumanTaskInvokeNode
     */
    public void setNode(HumanTaskInvokeNode node) {
        this.node = node;
        model = (HumanTaskInvokeNodeModel) node.getModel();
    }

    /**
     * Retrieves the values from all fields. Creates and executes a
     * {@link ChangeNodePropertiesCommand} with the new values.
     */
    private void changeWorkflowModel() {
        HumanTaskInvokeNodeModel newModel = new HumanTaskInvokeNodeModel(node
                .getModel().getParentModel());
        newModel.setUserVariableId(userField.getValue().getId());
        newModel.setWorkItemNameVariableId(nameField.getValue().getId());
        newModel.setWorkItemDescriptionVariableId(descriptionField.getValue()
                .getId());
        newModel.setFormVariableId(formContainerField.getValue().getId());
        newModel.setNotifyActor(notifyCheckBox.getValue());
        List<TaskItem> newTaskItems = ((TaskItemWindow) DialogRegistry
                .getInstance().getDialog(TaskItemWindow.class.getName()))
                .getTaskItems();
        /*
         * if the newTaskItems variable is null, that means the task items were
         * not edited by the task item window. Therefore, get the task items
         * from the oldmodel. If not null, get the new task items from the task
         * item window.
         */
        if (newTaskItems == null) {
            newModel.setTaskItems(model.getTaskItems());
        } else {
            newModel.setTaskItems(newTaskItems);
        }
        /* only push to command stack if changes where made */
        if (newModel.getProperties().equals(model.getProperties()) == false) {
            CommandStack.getInstance().executeCommand(
                    new ChangeNodePropertiesCommand(node, newModel
                            .getProperties()));
        }
    }

    private void createFields() {
        /* combobox for role variable */
        userField = new ComboBox<Variable>();
        userField.setDisplayField(Variable.LABEL);
        userField.setStore(Workflow.getInstance().getModel()
                .getVariablesOfTypeAsStore(VariableType.ROLE));
        userField.setValue(Workflow.getInstance().getModel().getVariable(
                model.getUserVariableId()));
        userField.setEditable(false);
        userField.setWidth("200px");
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, new Label(
                ModelingToolWidget.getMessages().userLabel()));
        table.setWidget(table.getRowCount() - 1, 1, userField);

        /* combobox for task name */
        nameField = new ComboBox<Variable>();
        nameField.setDisplayField(Variable.LABEL);
        nameField.setStore(Workflow.getInstance().getModel()
                .getVariablesOfTypeAsStore(VariableType.STRING));
        nameField.setValue(Workflow.getInstance().getModel().getVariable(
                model.getWorkItemNameVariableId()));
        nameField.setEditable(false);
        nameField.setWidth("200px");
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, new Label(
                ModelingToolWidget.getMessages().taskNameLabel()));
        table.setWidget(table.getRowCount() - 1, 1, nameField);

        /* combobox for task description */
        descriptionField = new ComboBox<Variable>();
        descriptionField.setDisplayField(Variable.LABEL);
        descriptionField.setStore(Workflow.getInstance().getModel()
                .getVariablesOfTypeAsStore(VariableType.STRING));
        descriptionField.setValue(Workflow.getInstance().getModel()
                .getVariable(model.getWorkItemNameVariableId()));
        descriptionField.setEditable(false);
        descriptionField.setWidth("200px");
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, new Label(
                ModelingToolWidget.getMessages().descriptionLabel()));
        table.setWidget(table.getRowCount() - 1, 1, descriptionField);

        /* combobox for form container */
        formContainerField = new ComboBox<Variable>();
        formContainerField.setDisplayField(Variable.LABEL);
        formContainerField.setStore(Workflow.getInstance().getModel()
                .getVariablesOfTypeAsStore(VariableType.FORM));
        formContainerField.setValue(Workflow.getInstance().getModel()
                .getVariable(model.getFormVariableId()));
        formContainerField.setEditable(false);
        formContainerField.setWidth("200px");
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, new Label(
                ModelingToolWidget.getMessages().formLabel()));
        table.setWidget(table.getRowCount() - 1, 1, formContainerField);

        /* checkbox for notification */
        notifyCheckBox = new CheckBox();
        notifyCheckBox.setValue(model.getNotifyActor());
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, new Label(
                ModelingToolWidget.getMessages().notifyLabel()));
        table.setWidget(table.getRowCount() - 1, 1, notifyCheckBox);
    }

    /**
     * Removes the combobox and other input elements from the table so that they
     * can be readded on the next call of the ForEachWindow.
     */
    public void clearAllEntries() {
        if (table.getRowCount() > 0) {
            int start = table.getRowCount();
            for (int i = start; i > 0; i--) {
                table.removeRow(i - 1);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.modelingtool.client.ui.dialogs.ModelingToolDialog#initialize()
     */
    @Override
    public Boolean initialize() {
        createFields();
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.ui.dialogs.ModelingToolDialog#reset()
     */
    @Override
    public void reset() {
        clearAllEntries();
    }

}
