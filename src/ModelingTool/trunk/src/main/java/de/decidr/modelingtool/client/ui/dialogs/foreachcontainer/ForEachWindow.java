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
import com.extjs.gxt.ui.client.widget.button.Button;
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
import de.decidr.modelingtool.client.ui.dialogs.Dialog;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class ForEachWindow extends Dialog {

    // JS todo: visibility of iteration variable and execution mehod(parallel)
    private ForEachContainer node;
    private ForEachContainerModel model;

    private ContentPanel contentPanel;
    private ScrollPanel scrollPanel;
    private FlexTable table;

    private ComboBox<Variable> iterableField;
    private ForEachRadioGroup exitConditionGroup;

    public ForEachWindow() {
        super();
        this.setLayout(new FitLayout());
        this.setSize(400, 200);
        this.setResizable(true);
        createContentPanel();
        createButtons();
    }

    private void createContentPanel() {
        contentPanel = new ContentPanel();

        contentPanel.setHeading(ModelingToolWidget.messages.forEachContainer());
        contentPanel.setLayout(new FitLayout());

        // TODO: fix layout
        table = new FlexTable();
        table.setBorderWidth(0);
        table.setWidth("100%");
        table.setCellPadding(2);
        table.setCellSpacing(2);
        scrollPanel = new ScrollPanel(table);
        contentPanel.add(scrollPanel);

        this.add(contentPanel);
    }

    private void createButtons() {
        setButtonAlign(HorizontalAlignment.CENTER);
        addButton(new Button(ModelingToolWidget.messages.okButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        changeWorkflowModel();
                        DialogRegistry.getInstance().hideDialog(
                                ForEachWindow.class.getName());
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

    public void setNode(ForEachContainer node) {
        this.node = node;
        model = (ForEachContainerModel) node.getModel();
    }

    protected void changeWorkflowModel() {
        // JS make this nicer
        ForEachContainerModel newModel = new ForEachContainerModel();
        newModel.setIterationVariableId(iterableField.getValue().getId());
        newModel.setExitCondition(exitConditionGroup.getSelectedValue());
        CommandStack.getInstance().executeCommand(
                new ChangeNodePropertiesCommand<ForEachContainer>(node,
                        newModel.getProperties()));
    }

    private void createFields() {
        iterableField = new ComboBox<Variable>();
        iterableField.setDisplayField(Variable.NAME);
        iterableField.setStore(VariablesFilter.getAllVariables());
        iterableField.setValue(VariablesFilter.getVariableById(model
                .getIterationVariableId()));
        iterableField.setTypeAhead(true);
        iterableField.setWidth("200px");
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, new Label(
                ModelingToolWidget.messages.iterationVarLabel()));
        table.setWidget(table.getRowCount() - 1, 1, iterableField);

        exitConditionGroup = new ForEachRadioGroup();
        exitConditionGroup.setSelectedValue(model.getExitCondition());
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, new Label(
                ModelingToolWidget.messages.exitConLabel()));
        table.setWidget(table.getRowCount() - 1, 1, exitConditionGroup);
    }

    private void clearAllEntries() {
        if (table.getRowCount() > 0) {
            int start = table.getRowCount();
            for (int i = start; i > 0; i--) {
                table.removeRow(i - 1);
            }
        }
    }

    @Override
    public void initialize() {
        createFields();
    }

    @Override
    public void reset() {
        clearAllEntries();
    }

    @Override
    public void refresh() {
        // TODO Auto-generated method stub

    }

}
