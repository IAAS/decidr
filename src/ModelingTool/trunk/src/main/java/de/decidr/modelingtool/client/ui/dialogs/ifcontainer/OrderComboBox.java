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

import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.model.ifcondition.Condition;
import de.decidr.modelingtool.client.model.ifcondition.IfContainerModel;
import de.decidr.modelingtool.client.ui.resources.Messages;

/**
 * A combobox for selecting the execution order of a {@link Condition}.
 * Execution order means the order in which the conditional expressions are
 * checked if they are true.
 * 
 * @author Jonas Schlaak
 */
public class OrderComboBox extends SimpleComboBox<String> {

    /**
     * Constructs a combobox with the given parameters.
     * 
     * @param count
     *            the number of conditions the {@link IfContainerModel} has
     * @param condition
     *            the condition which this combobox is for
     */
    public OrderComboBox(int count, Condition condition) {
        super();

        /*
         * Add all possible order "ranks" a condition can have to the selection
         * list. There always has to be one default condition (which is executed
         * if no other condition is true). Therefore, there can be n-1 proper
         * order "ranks" for n conditions.
         */
        this.add(ModelingToolWidget.messages.defaultCondition());
        for (Integer i = 1; i < count; i++) {
            this.add(i.toString());
        }

        /* Set value */
        if (condition.getOperator() != null) {
            String order;
            if (condition.getOrder() == 0) {
                order = ModelingToolWidget.messages.defaultCondition();
            } else {
                order = condition.getOrder().toString();
            }
            this.setSimpleValue(order);
        }

        this.setEditable(false);
    }

    /**
     * Returns the selected order as integer value. If the entry for the default
     * condition was selected (see also {@link Messages#defaultCondition()}, the
     * returned value is 0.
     * 
     * @return the order
     */
    public Integer getOrder() {
        Integer result;
        if (this.getSimpleValue() == ModelingToolWidget.messages
                .defaultCondition()) {
            result = 0;
        } else {
            result = new Integer(this.getValue().getValue());
        }
        return result;
    }

}
