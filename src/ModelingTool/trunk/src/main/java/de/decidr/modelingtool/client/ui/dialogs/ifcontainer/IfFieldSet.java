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

import java.util.List;

import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.google.gwt.user.client.ui.Label;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.model.ifcondition.Condition;
import de.decidr.modelingtool.client.model.ifcondition.IfContainerModel;
import de.decidr.modelingtool.client.model.ifcondition.Operator;
import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.model.variable.VariableType;
import de.decidr.modelingtool.client.model.variable.VariablesFilter;
import de.decidr.modelingtool.client.ui.Workflow;

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

    // JS finish

    /**
     * Default constructor to create an IfFieldSet from a condition.
     * 
     * @param condition
     *            the condition
     * @param numberOfConditions
     *            the number of conditions the {@link IfContainerModel} has
     */
    public IfFieldSet(Condition condition, int numberOfConditions) {
        this.condition = condition;

        label = new Label(condition.getName());

        typeSelector = new SimpleComboBox<String>();
        for (VariableType type : VariableType.values()) {
            typeSelector.add(type.getLocalName());
        }
        typeSelector.setEditable(false);
        if (condition.isComplete()) {
            typeSelector.setSimpleValue(condition.getType().getLocalName());
        }
        typeSelector.addSelectionChangedListener(new TypeSelectorListener(this,
                typeSelector));

        leftOperandField = new ComboBox<Variable>();
        leftOperandField.setDisplayField(Variable.LABEL);
        leftOperandField.setStore(VariablesFilter.getAllVariablesAsStore());
        leftOperandField.setEnabled(false);

        operatorList = new SimpleComboBox<String>();
        for (Operator operator : Operator.values()) {
            operatorList.add(operator.getDisplayString());
        }
        operatorList.setEnabled(false);

        rightOperandField = new ComboBox<Variable>();
        rightOperandField.setDisplayField(Variable.LABEL);
        rightOperandField.setStore(VariablesFilter.getAllVariablesAsStore());
        rightOperandField.setEnabled(false);
        rightOperandField.setTypeAhead(true);

        orderField = new OrderComboBox(numberOfConditions, condition);

        /* If condition is complete, set the value of the input fields */
        if (condition.isComplete()) {
            Variable leftVariable = Workflow.getInstance().getModel()
                    .getVariable(condition.getLeftOperandId());
            leftOperandField.setValue(leftVariable);

            operatorList.setSimpleValue(condition.getOperator()
                    .getDisplayString());

            Variable rightVariable = Workflow.getInstance().getModel()
                    .getVariable(condition.getRightOperandId());
            rightOperandField.setValue(rightVariable);

            orderField.setOrder(condition.getOrder());
        }
    }

    /**
     * This methods updates the stores (that means the set of values that is
     * available for selection in the combxbox) of the left and right operand
     * field and the operator field. The stores of these input fields depends on
     * the variable type.
     * 
     * @param type
     *            the {@link VariableType} to set the stores to
     */
    public void updateAllStores(VariableType type) {
        leftOperandField.getStore().removeAll();
        for (Variable variable : Workflow.getInstance().getModel()
                .getVariablesOfType(type)) {
            leftOperandField.getStore().add(variable);
        }
        leftOperandField.setEnabled(true);

        operatorList.getStore().removeAll();
        for (Operator operator : Operator.getOperatorsForType(type)) {
            operatorList.add(operator.getDisplayString());
        }
        operatorList.setEnabled(true);

        rightOperandField.getStore().removeAll();
        for (Variable variable : Workflow.getInstance().getModel()
                .getVariablesOfType(type)) {
            rightOperandField.getStore().add(variable);
        }
        rightOperandField.setEnabled(true);
    }

    /**
     * Returns the id of the {@link Condition} that is modeled by this
     * IfFieldSet.
     * 
     * @return the id
     */
    public Long getConditionId() {
        return this.condition.getId();
    }

    /**
     * Checks whether the inputs of the fields of the {@link IfFieldSet} (right
     * and left operand, operator) form a valid condition expression. The input
     * fields may not be empty. Left and right operand have to be of the same
     * type. The operator must be available for the type.
     * 
     * @param callback
     *            Callback string for error messages
     * 
     * @return true, if valid
     */
    public Boolean isConditionValid(String callback) {
        Boolean validationResult = true;

        /* Check if fields are empty */
        if (leftOperandField.getValue() == null
                || operatorList.getSimpleValue() == null
                || rightOperandField.getValue() == null) {
            validationResult = false;
            callback = callback + condition.getName() + ": "
                    + ModelingToolWidget.getMessages().conditionFieldsEmpty()
                    + "\n";
        }

        /*
         * Check if left and right operand are of the same type, only if
         * previous check ran through
         */
        if (validationResult == true) {
            VariableType leftType = leftOperandField.getValue().getType();
            VariableType rightType = rightOperandField.getValue().getType();
            if (leftType != rightType) {
                validationResult = false;
                callback = callback
                        + condition.getName()
                        + ": "
                        + ModelingToolWidget.getMessages()
                                .conditionTypeMismatch() + "\n";
            }
        }

        /*
         * Check if the operator is applicable to the selected type, only if
         * previous check ran through
         */
        if (validationResult == true) {
            VariableType type = leftOperandField.getValue().getType();
            List<Operator> applicableOperators = Operator
                    .getOperatorsForType(type);
            Operator operator = Operator
                    .getOperatorFromDisplayString(operatorList.getSimpleValue());
            if (applicableOperators.contains(operator) == false) {
                validationResult = false;
                callback = callback
                        + condition.getName()
                        + ": "
                        + ModelingToolWidget.getMessages()
                                .conditionWrongOperator() + "\n";
            }
        }
        return validationResult;
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
