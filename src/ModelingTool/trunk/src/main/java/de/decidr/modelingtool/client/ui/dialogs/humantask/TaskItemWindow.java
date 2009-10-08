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
import com.extjs.gxt.ui.client.event.ToolBarEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel;
import de.decidr.modelingtool.client.model.humantask.TaskItem;
import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.model.variable.VariablesFilter;
import de.decidr.modelingtool.client.ui.HumanTaskInvokeNode;
import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;
import de.decidr.modelingtool.client.ui.dialogs.ModelingToolDialog;

/**
 * This window is for editing the task items of a {@link HumanTaskInvokeNode}.
 * 
 * @author Jonas Schlaak
 */
public class TaskItemWindow extends ModelingToolDialog {

    private ContentPanel contentPanel;
    private FlexTable table;
    private ScrollPanel scrollPanel;

    private List<TaskItem> taskItems;
    private List<TaskItemFieldSet> fieldsets = new ArrayList<TaskItemFieldSet>();

    /**
     * Default constructor that sets the layout.
     */
    public TaskItemWindow() {
        super();
        this.setLayout(new FitLayout());
        this.setSize(800, 300);
        this.setResizable(true);
        createContentPanel();
        createButtons();
    }

    /**
     * Creates the content panel for the dialog. The content panel holds the
     * text fields for the values and a toolbar.
     */
    private void createContentPanel() {
        contentPanel = new ContentPanel();
        contentPanel
                .setHeading(ModelingToolWidget.getMessages().editVariable());
        contentPanel.setLayout(new FitLayout());

        table = new FlexTable();
        table.setBorderWidth(0);
        table.setWidth("100%");
        scrollPanel = new ScrollPanel(table);
        contentPanel.add(scrollPanel);

        createToolBar();
        this.add(contentPanel);
    }

    /**
     * Creates a toolbar that has two buttons to add or delete a value.
     */
    private void createToolBar() {
        ToolBar toolBar = new ToolBar();

        TextToolItem addTaskItem = new TextToolItem(ModelingToolWidget
                .getMessages().addTaskItem()); //$NON-NLS-1$
        addTaskItem.addSelectionListener(new SelectionListener<ToolBarEvent>() {
            @Override
            public void componentSelected(ToolBarEvent ce) {
                addEntry(new TaskItem(ModelingToolWidget.getMessages()
                        .taskItemLabel(), ModelingToolWidget.getMessages()
                        .taskItemHint(), null));
            }
        });
        toolBar.add(addTaskItem);

        contentPanel.setBottomComponent(toolBar);
    }

    /**
     * Creates ok and cancel button
     */
    private void createButtons() {
        setButtonAlign(HorizontalAlignment.CENTER);
        addButton(new Button(ModelingToolWidget.getMessages().okButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        if (validateInputs()) {
                            changeWorkflowModel();
                            DialogRegistry.getInstance().hideDialog(
                                    TaskItemWindow.class.getName());
                        } else {
                            MessageBox.alert(ModelingToolWidget.getMessages()
                                    .warningTitle(), ModelingToolWidget
                                    .getMessages().taskItemWarning(), null);
                        }
                    }

                }));
        addButton(new Button(ModelingToolWidget.getMessages().cancelButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        DialogRegistry.getInstance().hideDialog(
                                TaskItemWindow.class.getName());
                    }
                }));
    }

    private boolean validateInputs() {
        boolean result = true;
        for (TaskItemFieldSet fieldset : fieldsets) {
            if (fieldset.getVariableField().getValue() == null) {
                result = false;
            }
        }
        return result;
    }

    private void changeWorkflowModel() {
        List<TaskItem> newTaskItems = new ArrayList<TaskItem>();
        for (TaskItemFieldSet fieldset : fieldsets) {
            TaskItem newTaskItem = new TaskItem(fieldset.getLabelField()
                    .getValue(), fieldset.getHintField().getValue(), fieldset
                    .getVariableField().getValue().getId());
            newTaskItems.add(newTaskItem);
        }
        taskItems = newTaskItems;
    }

    /**
     * Adds a human task field set
     */
    private void addEntry(TaskItem ti) {
        /* label */
        TextField<String> labelField = new TextField<String>();
        labelField.setValue(ti.getLabel());
        labelField.setAutoWidth(true);

        /* hint */
        TextField<String> hintField = new TextField<String>();
        hintField.setValue(ti.getHint());
        hintField.setAutoWidth(true);

        /* combobox */
        ComboBox<Variable> variableField = new ComboBox<Variable>();
        variableField.setDisplayField(Variable.LABEL);
        variableField.setStore(VariablesFilter.getAllVariables());
        if (ti.getVariableId() != null) {
            variableField.setValue(Workflow.getInstance().getModel()
                    .getVariable(ti.getVariableId()));
        }
        variableField.setTypeAhead(true);

        table.insertRow(table.getRowCount());
        fieldsets.add(new TaskItemFieldSet(labelField, hintField,
                variableField, table, fieldsets));
        table.setWidget(table.getRowCount() - 1, 0, fieldsets.get(fieldsets
                .size() - 1));
    }

    public void clearAllEntries() {
        if (table.getRowCount() > 0) {
            int start = table.getRowCount();
            for (int i = start; i > 0; i--) {
                table.removeRow(i - 1);
            }
        }
        if (fieldsets.size() > 0) {
            fieldsets.clear();
        }
    }

    /**
     * Sets the model for this window.
     * 
     * @param model
     *            the model
     */
    public void setModel(HumanTaskInvokeNodeModel model) {
        this.taskItems = model.getTaskItems();
    }

    public List<TaskItem> getTaskItems() {
        return taskItems;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.modelingtool.client.ui.dialogs.ModelingToolDialog#initialize()
     */
    @Override
    public Boolean initialize() {
        if (taskItems != null) {
            for (TaskItem ti : taskItems) {
                addEntry(ti);
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.ui.dialogs.ModelingToolDialog#reset()
     */
    @Override
    public void reset() {
        if (table.getRowCount() > 0) {
            int start = table.getRowCount();
            for (int i = start; i > 0; i--) {
                table.removeRow(i - 1);
            }
        }
        if (fieldsets.size() > 0) {
            fieldsets.clear();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.modelingtool.client.ui.dialogs.ModelingToolDialog#refresh()
     */
    @Override
    public void refresh() {

    }

}
