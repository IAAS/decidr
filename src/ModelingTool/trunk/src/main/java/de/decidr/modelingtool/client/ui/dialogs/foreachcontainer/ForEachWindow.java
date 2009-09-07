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

package de.decidr.modelingtool.client.ui.dialogs.foreachcontainer;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.command.ChangeNodePropertiesCommand;
import de.decidr.modelingtool.client.command.CommandStack;
import de.decidr.modelingtool.client.model.foreach.ForEachContainerModel;
import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.model.variable.VariablesFilter;
import de.decidr.modelingtool.client.ui.ForEachContainer;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;
import de.decidr.modelingtool.client.ui.dialogs.ModelingToolDialog;

/**
 * The property window for a {@link ForEachContainer}. It consists of one
 * combobox for selecting the iteration variable, a radiobutton group for
 * selecting the exit condition and a checkbox to determine whether the
 * activities in the container should be executed in a parallel way or not.
 * 
 * @author Jonas Schlaak
 */
public class ForEachWindow extends ModelingToolDialog {

    // JS todo: visibility of iteration variable
    private ForEachContainer node;
    private ForEachContainerModel model;

    private ContentPanel contentPanel;
    private ScrollPanel scrollPanel;
    private FlexTable table;

    private ComboBox<Variable> iterableField;
    private ForEachRadioGroup exitConditionGroup;
    private CheckBox parallelField;

    public ForEachWindow() {
        super();
        this.setLayout(new FitLayout());
        this.setSize(400, 200);
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

        contentPanel.setHeading(ModelingToolWidget.messages.forEachContainer());
        contentPanel.setLayout(new FitLayout());

        table = new FlexTable();
        table.setBorderWidth(0);
        table.setWidth("100%");
        table.setCellPadding(2);
        table.setCellSpacing(2);
        scrollPanel = new ScrollPanel(table);
        contentPanel.add(scrollPanel);

        this.add(contentPanel);
    }

    /**
     * Creates the ok and cancel button.
     */
    private void createButtons() {
        setButtonAlign(HorizontalAlignment.CENTER);
        addButton(new Button(ModelingToolWidget.messages.okButton(),
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
                                    ForEachWindow.class.getName());
                        } else {
                            MessageBox.alert(ModelingToolWidget.messages
                                    .warningTitle(),
                                    ModelingToolWidget.messages
                                            .flowContainerWarning(), null);
                        }
                    }
                }));
        addButton(new Button(ModelingToolWidget.messages.cancelButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        DialogRegistry.getInstance().hideDialog(
                                ForEachWindow.class.getName());
                    }
                }));
    }

    /**
     * Sets the {@link ForEachContainer} whose properties are to be modeled with
     * this window.
     * 
     * @param node
     *            the ForEachContainer
     */
    public void setNode(ForEachContainer node) {
        this.node = node;
        model = (ForEachContainerModel) node.getModel();
    }

    private boolean validateInputs() {
        Boolean result = false;
        if (iterableField.getValue() != null && exitConditionGroup.isSelected()) {
            result = true;
        }
        return result;
    }

    /**
     * Retrieves the values from all fields. Creates and executes a
     * {@link ChangeNodePropertiesCommand} with the new values.
     */
    private void changeWorkflowModel() {
        ForEachContainerModel newModel = new ForEachContainerModel(node
                .getModel().getParentModel());
        newModel.setIterationVariableId(iterableField.getValue().getId());
        newModel.setExitCondition(exitConditionGroup.getSelectedValue());
        newModel.setParallel(parallelField.getValue());
        CommandStack.getInstance()
                .executeCommand(
                        new ChangeNodePropertiesCommand(node, newModel
                                .getProperties()));
    }

    /**
     * Creates the combobox and other input elements and adds them to the
     * window's table.
     */
    private void createFields() {
        /* combobox for iteration variable */
        iterableField = new ComboBox<Variable>();
        iterableField.setDisplayField(Variable.LABEL);
        iterableField.setStore(VariablesFilter.getAllVariables());
        iterableField.setValue(VariablesFilter.getVariableById(model
                .getIterationVariableId()));
        iterableField.setTypeAhead(true);
        iterableField.setWidth("200px");
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, new Label(
                ModelingToolWidget.messages.iterationVarLabel()));
        table.setWidget(table.getRowCount() - 1, 1, iterableField);

        /* radio button group for exit condition */
        exitConditionGroup = new ForEachRadioGroup();
        exitConditionGroup.setSelectedValue(model.getExitCondition());
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, new Label(
                ModelingToolWidget.messages.exitConLabel()));
        table.setWidget(table.getRowCount() - 1, 1, exitConditionGroup);

        /* check box for execution */
        parallelField = new CheckBox();
        parallelField.setValue(model.isParallel());
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, new Label(
                ModelingToolWidget.messages.parallelLabel()));
        table.setWidget(table.getRowCount() - 1, 1, parallelField);
    }

    /**
     * Removes the combobox and other input elements from the table so that they
     * can be readded on the next call of the ForEachWindow.
     */
    private void clearAllEntries() {
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
    public void initialize() {
        createFields();
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
