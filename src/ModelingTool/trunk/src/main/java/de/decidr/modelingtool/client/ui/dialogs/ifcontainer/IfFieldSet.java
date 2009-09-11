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

import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.google.gwt.user.client.ui.Label;

import de.decidr.modelingtool.client.model.ifcondition.Condition;
import de.decidr.modelingtool.client.model.ifcondition.Operator;
import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.model.variable.VariableType;
import de.decidr.modelingtool.client.model.variable.VariablesFilter;

/**
 * This container holds all references to the input fields that are necessary to
 * model a {@link Condition} in an {@link IfWindow}. The container consist of a
 * label (which displays the name of the condition), a type selector to select
 * the variable type, all necessary comboboxes to model a conditional expression
 * (see also {@link Operator}), and a combobox the set the execution order of
 * the conditions.
 * 
 * @author Jonas Schlaak
 */
public class IfFieldSet {

    private Label label;
    private SimpleComboBox<String> typeSelector;
    private ComboBox<Variable> leftOperandField;
    private SimpleComboBox<String> operatorList;
    private ComboBox<Variable> rightOperandField;
    private OrderComboBox orderField;

    private Condition condition;

    /**
     * Default constructor to create an IfFieldSet from a condition
     * 
     * @param condition
     *            the condition
     * @param count
     *            the execution order, 0 stands for default condition
     */
    public IfFieldSet(Condition condition, int count) {
        this.condition = condition;

        label = new Label(condition.getName());

        typeSelector = new SimpleComboBox<String>();
        for (VariableType t : VariableType.values()) {
            typeSelector.add(t.getLocalName());
        }
        typeSelector.setEditable(false);
        typeSelector
                .addSelectionChangedListener(new SelectionChangedListener<Variable>() {
                    @Override
                    public void selectionChanged(
                            SelectionChangedEvent<Variable> se) {
                        /*
                         * If type selector changes, get all variables with
                         * selected type from workflow
                         */
                        leftOperandField.setEnabled(true);
                        leftOperandField.getStore().removeAll();
                        leftOperandField.clearSelections();
                        leftOperandField
                                .getStore()
                                .add(
                                        VariablesFilter
                                                .getVariablesOfType(
                                                        VariableType
                                                                .getTypeFromLocalName(typeSelector
                                                                        .getSimpleValue()))
                                                .getModels());

                        operatorList.setEnabled(true);
                        updateOperatorListEntries();

                        /*
                         * If type selector changes, get all variables with
                         * selected type from workflow
                         */
                        rightOperandField.setEnabled(true);
                        rightOperandField.getStore().removeAll();
                        rightOperandField.clearSelections();
                        rightOperandField
                                .getStore()
                                .add(
                                        VariablesFilter
                                                .getVariablesOfType(
                                                        VariableType
                                                                .getTypeFromLocalName(typeSelector
                                                                        .getSimpleValue()))
                                                .getModels());
                    }
                });

        leftOperandField = new ComboBox<Variable>();
        leftOperandField.setDisplayField(Variable.LABEL);
        leftOperandField.setStore(VariablesFilter.getAllVariables());
        if (condition.getLeftOperandId() != null) {
            leftOperandField.setValue(VariablesFilter.getVariableById(condition
                    .getLeftOperandId()));
        }
        leftOperandField.setEditable(false);
        leftOperandField.setEnabled(false);

        operatorList = new SimpleComboBox<String>();
        /*
         * if the conditions already references the operands, update the
         * operator list entires according to the type of the operands.
         */
        if (condition.getLeftOperandId() != null
                && condition.getRightOperandId() != null) {
            updateOperatorListEntries();
        }
        if (condition.getOperator() != null) {
            operatorList.setSimpleValue(condition.getOperator()
                    .getDisplayString());
        }
        operatorList.setEditable(false);
        operatorList.setEnabled(false);

        rightOperandField = new ComboBox<Variable>();
        rightOperandField.setDisplayField(Variable.LABEL);
        rightOperandField.setStore(VariablesFilter.getAllVariables());
        if (condition.getRightOperandId() != null) {
            rightOperandField.setValue(VariablesFilter
                    .getVariableById(condition.getRightOperandId()));
        }
        rightOperandField.setEditable(false);
        rightOperandField.setEnabled(false);

        orderField = new OrderComboBox(count, condition);
    }

    private void updateOperatorListEntries() {
        operatorList.getStore().removeAll();
        operatorList.clearSelections();
        if (typeSelector != null && typeSelector.getValue() != null
                && typeSelector.getValue().getValue() != null) {
            for (Operator op : Operator.getOperatorsForType(VariableType
                    .getTypeFromLocalName(typeSelector.getValue().getValue()))) {
                operatorList.add(op.getDisplayString());
            }
        }
        if (condition.getOperator() != null) {
            operatorList.setSimpleValue(condition.getOperator()
                    .getDisplayString());
        }
    }

    /**
     * Checks whether the fields of the conditional expression (right and left
     * operand, operator) are empty, that means a value has not been selected.
     * 
     * @return true, if in all comboboxes a value has been selected
     */
    public Boolean areConditionFieldsOK() {
        /* all three fields must be filled with a value */
        return (leftOperandField.getValue() != null
                && operatorList.getSimpleValue() != null && rightOperandField
                .getValue() != null);
    }

    public Label getLabel() {
        return label;
    }

    public SimpleComboBox<String> getTypeSelector() {
        return typeSelector;
    }

    public ComboBox<Variable> getLeftOperandField() {
        return leftOperandField;
    }

    public SimpleComboBox<String> getOperatorList() {
        return operatorList;
    }

    public ComboBox<Variable> getRightOperandField() {
        return rightOperandField;
    }

    public OrderComboBox getOrderField() {
        return orderField;
    }

}
