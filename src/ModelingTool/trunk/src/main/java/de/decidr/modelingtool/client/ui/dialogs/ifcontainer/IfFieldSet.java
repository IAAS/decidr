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
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class IfFieldSet {

    private Label label;
    private SimpleComboBox<String> typeSelector;
    private ComboBox<Variable> leftOperandField;
    private SimpleComboBox<String> operatorList;
    private ComboBox<Variable> rightOperandField;

    public IfFieldSet(Condition condition) {
        label = new Label(condition.getLabel());

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
                        // JS: rewrite this
                        leftOperandField.setEnabled(true);
                        leftOperandField.getStore().removeAll();
                        leftOperandField
                                .getStore()
                                .add(
                                        VariablesFilter
                                                .getVariablesOfType(
                                                        VariableType
                                                                .getTypeFromLocalName(typeSelector
                                                                        .getValue()
                                                                        .getValue()))
                                                .getModels());
                        rightOperandField.getStore().removeAll();
                        rightOperandField
                                .getStore()
                                .add(
                                        VariablesFilter
                                                .getVariablesOfType(
                                                        VariableType
                                                                .getTypeFromLocalName(typeSelector
                                                                        .getValue()
                                                                        .getValue()))
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
        leftOperandField
                .addSelectionChangedListener(new SelectionChangedListener<Variable>() {
                    @Override
                    public void selectionChanged(
                            SelectionChangedEvent<Variable> se) {
                        operatorList.getStore().removeAll();
                        for (Operator op : Operator
                                .getOperatorsForType(leftOperandField.getValue()
                                        .getType())) {
                            operatorList.add(op.getDisplayString());
                        }
                    }
                });

        operatorList = new SimpleComboBox<String>();
        // JS check this if condition
        if (condition.getLeftOperandId() != null) {
            for (Operator op : Operator.getOperatorsForType(VariablesFilter
                    .getVariableById(condition.getLeftOperandId()).getType())) {
                operatorList.add(op.getDisplayString());

            }
            operatorList.setSimpleValue(condition.getOperator()
                    .getDisplayString());
        }

        rightOperandField = new ComboBox<Variable>();
        rightOperandField.setDisplayField(Variable.LABEL);
        rightOperandField.setStore(VariablesFilter.getAllVariables());
        if (condition.getRightOperandId() != null) {
            rightOperandField.setValue(VariablesFilter.getVariableById(condition
                    .getRightOperandId()));
        }
        rightOperandField.setEditable(false);
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
}
