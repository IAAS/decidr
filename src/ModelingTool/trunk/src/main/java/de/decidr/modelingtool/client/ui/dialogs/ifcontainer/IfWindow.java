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

package de.decidr.modelingtool.client.ui.dialogs.ifcontainer;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.command.ChangeNodePropertiesCommand;
import de.decidr.modelingtool.client.command.CommandStack;
import de.decidr.modelingtool.client.model.ifcondition.Condition;
import de.decidr.modelingtool.client.model.ifcondition.IfContainerModel;
import de.decidr.modelingtool.client.model.ifcondition.Operator;
import de.decidr.modelingtool.client.ui.IfContainer;
import de.decidr.modelingtool.client.ui.dialogs.Dialog;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class IfWindow extends Dialog {

    private IfContainer node;
    private IfContainerModel model;

    private ContentPanel contentPanel;
    private ScrollPanel scrollPanel;
    private FlexTable table;

    private List<IfFieldSet> fieldsets;

    public IfWindow() {
        super();
        this.setLayout(new FitLayout());
        this.setSize(400, 200);
        this.setResizable(true);
        createContentPanel();
        createButtons();

        fieldsets = new ArrayList<IfFieldSet>();
    }

    private void createContentPanel() {
        contentPanel = new ContentPanel();

        contentPanel.setHeading(ModelingToolWidget.messages.ifContainer());
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
                                IfWindow.class.getName());
                    }
                }));
        addButton(new Button(ModelingToolWidget.messages.cancelButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        DialogRegistry.getInstance().hideDialog(
                                IfWindow.class.getName());
                    }
                }));
    }

    public void setNode(IfContainer node) {
        this.node = node;
        this.model = (IfContainerModel) node.getModel();
    }

    private void changeWorkflowModel() {
        // JS make this nicer
        IfContainerModel newModel = new IfContainerModel();
        for (IfFieldSet fs : fieldsets) {
            String label = fs.getLabel().getText();
            Long operand1Id = fs.getOperand1Field().getValue().getId();
            Operator operator = Operator.getOperatorFromDisplayString(fs
                    .getOperatorList().getValue().getValue());
            Long operand2Id = fs.getOperand2Field().getValue().getId();
            System.out.println(operand1Id + " " + operand2Id);
            newModel.addCondition(new Condition(label, operand1Id, operator,
                    operand2Id));
        }
        CommandStack.getInstance().executeCommand(
                new ChangeNodePropertiesCommand<IfContainer>(node, newModel
                        .getProperties()));
    }

    private void createFields() {
        for (Condition con : model.getConditions()) {
            table.insertRow(table.getRowCount());

            IfFieldSet fieldset = new IfFieldSet(con);
            fieldsets.add(fieldset);
            table.setWidget(table.getRowCount() - 1, 0, fieldset.getLabel());
            table.setWidget(table.getRowCount() - 1, 1, fieldset
                    .getTypeSelector());
            table.setWidget(table.getRowCount() - 1, 2, fieldset
                    .getOperand1Field());
            table.setWidget(table.getRowCount() - 1, 3, fieldset
                    .getOperatorList());
            table.setWidget(table.getRowCount() - 1, 4, fieldset
                    .getOperand2Field());
        }
    }

    private void clearAllEntries() {
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
