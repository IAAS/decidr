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

package de.decidr.modelingtool.client.model.container.ifcondition;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.model.connections.ContainerStartConnectionModel;
import de.decidr.modelingtool.client.model.variable.VariableType;
import de.decidr.modelingtool.client.ui.Workflow;

/**
 * This class is the model for a condition of an {@link IfContainerModel}. A
 * conditions basically consist of an left operand, an operator, a right operand
 * and the execution order.
 * 
 * @author Jonas Schlaak
 */
public class Condition extends ContainerStartConnectionModel {

    private Long leftOperandId;
    private Operator operator;
    private Long rightOperandId;
    private Integer order;

    private static Integer counter;

    /**
     * Creates an blank condition, no operands etc. are set.
     */
    public Condition() {
        super();
        if (counter == null) {
            counter = new Integer(1);
        } else {
            counter = counter + 1;
        }
        this.setName(ModelingToolWidget.getMessages().condition() + " "
                + counter.toString());
    }

    /**
     * Creates a condition with defined values.
     * 
     * @param name
     *            the name of the condition
     * @param leftOperandId
     *            the left operand
     * @param operator
     *            the operator
     * @param rightOperandId
     *            the right operand
     * @param order
     *            the execution order of the condition
     */
    public Condition(String name, Long leftOperandId, Operator operator,
            Long rightOperandId, Integer order) {
        super();
        if (counter == null) {
            counter = new Integer(1);
        } else {
            counter = counter + 1;
        }
        this.setName(name);
        this.leftOperandId = leftOperandId;
        this.operator = operator;
        this.rightOperandId = rightOperandId;
        this.order = order;
    }

    public Long getLeftOperandId() {
        return leftOperandId;
    }

    public Operator getOperator() {
        return operator;
    }

    public Long getRightOperandId() {
        return rightOperandId;
    }

    public Integer getOrder() {
        return order;
    }

    public void setLeftOperandId(Long leftOperandId) {
        this.leftOperandId = leftOperandId;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public void setRightOperandId(Long rightOperand2Id) {
        this.rightOperandId = rightOperand2Id;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    /**
     * Returns the {@link VariableType} of the operands of this condition.
     * Actually it returns the type of the left operand, but is is assumed that
     * the types of the operands are always the same.
     * 
     * @return the type
     */
    public VariableType getType() {
        return Workflow.getInstance().getModel().getVariable(leftOperandId)
                .getType();
    }

    /**
     * Returns whether this condition is complete or not. Complete means that
     * the condition has a left and right operand, an operator and an order.
     * 
     * @return the result
     */
    public boolean isComplete() {
        return (leftOperandId != null && operator != null
                && rightOperandId != null && order != null);
    }

}
