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
import com.extjs.gxt.ui.client.widget.MessageBox;
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
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;
import de.decidr.modelingtool.client.ui.dialogs.ModelingToolDialog;

/**
 * The property window for an {@link IfContainer}. It consists of one
 * {@link IfFieldSet} for each {@link Condition} that is in the IfContainer.
 * 
 * @author Jonas Schlaak
 */
public class IfWindow extends ModelingToolDialog {

    private IfContainer node;
    private IfContainerModel model;

    private ContentPanel contentPanel;
    private ScrollPanel scrollPanel;
    private FlexTable table;

    private List<IfFieldSet> fieldsets;

    public IfWindow() {
        super();
        this.setLayout(new FitLayout());
        this.setSize(1000, 300);
        this.setResizable(true);
        createContentPanel();
        createButtons();

        fieldsets = new ArrayList<IfFieldSet>();
    }

    /**
     * Creates a {@link ContentPanel} which holds a {@link FlexTable} to which
     * the comboboxes are added.
     */
    private void createContentPanel() {
        contentPanel = new ContentPanel();

        contentPanel.setHeading(ModelingToolWidget.getMessages().ifContainer());
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
        addButton(new Button(ModelingToolWidget.getMessages().okButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        /*
                         * check if the inputs are valid. If not, display a
                         * warning message, else change the workflow model.
                         */
                        if (validateConditions() == false) {
                            MessageBox.alert(ModelingToolWidget.getMessages()
                                    .warningTitle(), ModelingToolWidget
                                    .getMessages().conditionWarning(), null);
                        } else if (validateOrder() == false) {
                            MessageBox.alert(ModelingToolWidget.getMessages()
                                    .warningTitle(), ModelingToolWidget
                                    .getMessages().conditionOrderWarning(),
                                    null);
                        } else {
                            changeWorkflowModel();
                            DialogRegistry.getInstance().hideDialog(
                                    IfWindow.class.getName());
                        }
                    }
                }));
        addButton(new Button(ModelingToolWidget.getMessages().cancelButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        DialogRegistry.getInstance().hideDialog(
                                IfWindow.class.getName());
                    }
                }));
    }

    /**
     * Sets the {@link IfContainer} whose properties are to be modeled with this
     * window.
     * 
     * @param node
     *            the IfContainer
     */
    public void setNode(IfContainer node) {
        this.node = node;
        this.model = (IfContainerModel) node.getModel();
    }

    private boolean validateConditions() {
        boolean result = true;
        for (IfFieldSet fieldset : fieldsets) {
            if (fieldset.areConditionFieldsOK() == false) {
                result = false;
            }
        }
        return result;
    }

    private boolean validateOrder() {
        Boolean result = false;
        /*
         * There has to be a default condition. Every index must not be used
         * more than once
         */
        for (int order = 0; order < fieldsets.size(); order++) {
            Boolean indexFound = false;
            for (IfFieldSet fs : fieldsets) {
                if (fs.getOrderField() != null
                        && fs.getOrderField().getOrder() == order) {
                    indexFound = true;
                }
            }
            /* If the index was not found, that means the input is not valid. */
            result = indexFound;
        }
        return result;
    }

    private void changeWorkflowModel() {
        IfContainerModel newModel = new IfContainerModel(node.getModel()
                .getParentModel());
        for (IfFieldSet fs : fieldsets) {
            String label = fs.getLabel().getText();
            Long operand1Id = fs.getLeftOperandField().getValue().getId();
            Operator operator = Operator.getOperatorFromDisplayString(fs
                    .getOperatorList().getSimpleValue());
            Long operand2Id = fs.getRightOperandField().getValue().getId();
            Integer order = fs.getOrderField().getOrder();
            newModel.addCondition(new Condition(label, operand1Id, operator,
                    operand2Id, order));
        }
        /* only push to command stack if changes where made */
        if (newModel.getProperties().equals(model.getProperties()) == false) {
            CommandStack.getInstance().executeCommand(
                    new ChangeNodePropertiesCommand(node, newModel
                            .getProperties()));
        }
    }

    private void createFields() {
        for (Condition con : model.getConditions()) {
            table.insertRow(table.getRowCount());

            IfFieldSet fieldset = new IfFieldSet(con, model.getConditions()
                    .size());
            fieldsets.add(fieldset);
            table.setWidget(table.getRowCount() - 1, 0, fieldset.getLabel());
            table.setWidget(table.getRowCount() - 1, 1, fieldset
                    .getTypeSelector());
            table.setWidget(table.getRowCount() - 1, 2, fieldset
                    .getLeftOperandField());
            table.setWidget(table.getRowCount() - 1, 3, fieldset
                    .getOperatorList());
            table.setWidget(table.getRowCount() - 1, 4, fieldset
                    .getRightOperandField());
            table.setWidget(table.getRowCount() - 1, 5, fieldset
                    .getOrderField());
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
