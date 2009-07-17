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
import com.google.gwt.user.client.ui.ListBox;

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
public class FieldSet {

    private Label label;
    private SimpleComboBox<String> typeSelector;
    private ComboBox<Variable> operand1Field;
    private ListBox operatorList;
    private ComboBox<Variable> operand2Field;

    public FieldSet(Condition condition) {
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
                        // JS this is not finished
                    }
                });

        operand1Field = new ComboBox<Variable>();
        operand1Field.setDisplayField(Variable.NAME);
        operand1Field.setStore(VariablesFilter.getAllVariables());
        if (condition.getOperand1Id() != null) {
            operand1Field.setValue(VariablesFilter.getVariableById(condition
                    .getOperand1Id()));
        }
        operand1Field.setEditable(false);
        operand1Field
                .addSelectionChangedListener(new SelectionChangedListener<Variable>() {
                    @Override
                    public void selectionChanged(
                            SelectionChangedEvent<Variable> se) {
                        operatorList.clear();
                        for (Operator op : Operator
                                .getOperatorsForType(operand1Field.getValue()
                                        .getType())) {
                            operatorList.addItem(op.getDisplayString());
                        }
                    }
                });

        operatorList = new ListBox(false);
        if (condition.getOperand1Id() != null) {
            for (Operator op : Operator.getOperatorsForType(VariablesFilter
                    .getVariableById(condition.getOperand1Id()).getType())) {
                operatorList.addItem(op.getDisplayString());
            }
        }

        // operand2Field = new ComboBox<Variable>();
    }

    public Label getLabel() {
        return label;
    }

    public SimpleComboBox<String> getTypeSelector() {
        return typeSelector;
    }

    public ComboBox<Variable> getOperand1Field() {
        return operand1Field;
    }

    public ListBox getOperatorList() {
        return operatorList;
    }

    public ComboBox<Variable> getOperand2Field() {
        return operand2Field;
    }
}
