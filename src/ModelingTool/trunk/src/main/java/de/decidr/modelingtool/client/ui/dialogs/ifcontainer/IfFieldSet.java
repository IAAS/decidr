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

import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.google.gwt.user.client.ui.Label;

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
     * Default constructor to create an IfFieldSet from a condition
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
        typeSelector.addSelectionChangedListener(new TypeSelectorListener(this,
                typeSelector));

        leftOperandField = new ComboBox<Variable>();
        leftOperandField.setDisplayField(Variable.LABEL);
        leftOperandField.setEditable(false);
        leftOperandField.setEnabled(false);
        leftOperandField.setStore(VariablesFilter.getAllVariablesAsStore());

        operatorList = new SimpleComboBox<String>();
        operatorList.setEditable(false);
        operatorList.setEnabled(false);

        rightOperandField = new ComboBox<Variable>();
        rightOperandField.setDisplayField(Variable.LABEL);
        rightOperandField.setStore(VariablesFilter.getAllVariablesAsStore());
       

        orderField = new OrderComboBox(numberOfConditions, condition);

        /* If condition is complete, set the value of the input fields */
        setValues();
        
        rightOperandField.setEditable(false);
        rightOperandField.setEnabled(false);
    }
    
    public void setValues(){
     // JS remove
        System.out.println(condition.isComplete());
        if (condition.isComplete()) {
            /*
             * Find out which type the operand variables of the condition have.
             * (use only left operand, type of left and right operand are the
             * same anyway). Set the type selector to the type
             */
            Variable leftVariable = Workflow.getInstance().getModel()
                    .getVariable(condition.getLeftOperandId());
            Variable rightVariable = Workflow.getInstance().getModel()
                    .getVariable(condition.getRightOperandId());
            typeSelector.setSimpleValue(leftVariable.getType().getLocalName());

            /* Set the values */
            // JS remove
            System.out.println("render" + leftOperandField.isRendered());
            leftOperandField.setRawValue(leftVariable.getLabel());
            operatorList.setSimpleValue(condition.getOperator()
                    .getDisplayString());
            rightOperandField.setValue(rightVariable);
            rightOperandField.render(rightOperandField.getElement());
            orderField.setOrder(condition.getOrder());

            /* Update stores according to the type */
            // updateAllStores(leftVariable.getType());

        }
    }

    /**
     * This methods updates the stores of the left and right operand field and
     * the operator field. The stores of these input fields are specific to the
     * variable type.
     * 
     * @param type
     *            the {@link VariableType} to set the stores to
     */
    public void updateAllStores(VariableType type) {
        leftOperandField.clearSelections();
        leftOperandField.getStore().removeAll();
        leftOperandField.getStore().add(
                VariablesFilter.getVariablesOfTypeAsStore(type).getModels());
        leftOperandField.setEnabled(true);

        operatorList.clearSelections();
        operatorList.removeAll();
        for (Operator op : Operator.getOperatorsForType(type)) {
            operatorList.add(op.getDisplayString());
        }
        operatorList.setEnabled(true);

        rightOperandField.clearSelections();
        rightOperandField.getStore().removeAll();
        rightOperandField.getStore().add(
                VariablesFilter.getVariablesOfTypeAsStore(type).getModels());
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
